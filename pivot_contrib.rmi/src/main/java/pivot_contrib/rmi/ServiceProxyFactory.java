package pivot_contrib.rmi;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import pivot_contrib.di.BeanFactory;
import pivot_contrib.di.BeanFactoryBuilder;
import pivot_contrib.di.ServiceFactory;

public abstract class ServiceProxyFactory implements BeanFactory {

	@SuppressWarnings("unchecked")
	public <C> C createInstance(Class<C> remoteInterfaceClass) {
		checkRemoteInterfaceClass(remoteInterfaceClass);
		InvocationHandler invocationHandler = getInvocationHandler(remoteInterfaceClass
				.getName());

		Object proxy = Proxy.newProxyInstance(
				ServiceProxyFactory.class.getClassLoader(),
				new Class[] { remoteInterfaceClass }, invocationHandler);

		return (C) proxy;
	}
	
	public Object createInstance(Field field) {
		return createInstance(field.getType());
	}

	protected abstract InvocationHandler getInvocationHandler(
			String remoteInterfaceName);

	private void checkRemoteInterfaceClass(Class<?> remoteInterfaceClass) {
		if (!remoteInterfaceClass.isInterface()) {
			throw new IllegalArgumentException(remoteInterfaceClass.getName()
					+ " is not interface.");
		}
	}
	
	public void register() {
		BeanFactoryBuilder.registerBeanFactory(ServiceFactory.class, this);
	}

}
