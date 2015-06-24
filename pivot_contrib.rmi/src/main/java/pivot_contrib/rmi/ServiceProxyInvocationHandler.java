package pivot_contrib.rmi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;



public abstract class ServiceProxyInvocationHandler implements InvocationHandler {

	private String remoteInterfaceName;
	
	public ServiceProxyInvocationHandler(String remoteInterfaceName) {
		this.remoteInterfaceName = remoteInterfaceName;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object[] arguments=args;
		if (arguments==null) {
			arguments=new Object[]{};
		}
		RMIRequest request=new RMIRequest(remoteInterfaceName, method.getName(),method.getParameterTypes(),args);
		return invoke(request);
		
	}
	protected abstract Object invoke(RMIRequest request);

}
