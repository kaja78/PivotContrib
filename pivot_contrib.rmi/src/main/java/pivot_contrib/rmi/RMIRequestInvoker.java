package pivot_contrib.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import pivot_contrib.di.BeanFactory;
import pivot_contrib.di.BeanFactoryBuilder;
import pivot_contrib.di.ServiceFactory;

/**
 * Invokes RMIRequest using service obtained by ServiceFactoryBuilder.
 * */
public class RMIRequestInvoker {

	private BeanFactory serviceFactory = BeanFactoryBuilder.getBeanFactory(ServiceFactory.class);

	public Object invoke(RMIRequest request) {
		Class<?> remoteInterface = getRemoteInterfaceClass(request
				.getRemoteInterfaceName());
		Object service = serviceFactory.createInstance(remoteInterface);
		Throwable t = null;
		try {
			Method serviceMethod = service.getClass().getMethod(
					request.getMethodName(),
					request.getParameterTypes());
			return serviceMethod.invoke(service, request.getParamaters());
		} 
		catch (NoSuchMethodException e) {
			t = e;
		} catch (InvocationTargetException e) {
			t = e.getCause();
			if (t instanceof ApplicationException) {
				throw (ApplicationException)t;
			}
		} catch (IllegalAccessException e) {
			t = e;
		}
		throw new RuntimeException("Exception invoking RMIRequest ("
				+ request.getRemoteInterfaceName() + ","
				+ request.getMethodName() + ")", t);
	}

	private Class<?> getRemoteInterfaceClass(String remoteInterfaceName) {
		try {
			return Class.forName(remoteInterfaceName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Remote interface "
					+ remoteInterfaceName + " not found.");
		}
	}



}
