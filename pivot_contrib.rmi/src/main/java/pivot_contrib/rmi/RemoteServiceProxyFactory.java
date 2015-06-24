package pivot_contrib.rmi;

import java.lang.reflect.InvocationHandler;

import pivot_contrib.di.BeanFactoryBuilder;
import pivot_contrib.di.ProxyFactory;
import pivot_contrib.di.ServiceFactory;

public class RemoteServiceProxyFactory extends ProxyFactory {

	private String endpointUrl;
	private String basicAuthorisation;
	
	public RemoteServiceProxyFactory(String endpointUrl) {
		super();
		this.endpointUrl = endpointUrl;
	}
	
	public RemoteServiceProxyFactory(String endpointUrl,String basicAuthorisation) {
		super();
		this.endpointUrl = endpointUrl;
		this.basicAuthorisation=basicAuthorisation;
	}


	protected InvocationHandler getInvocationHandler(Class<?> remoteInterfaceType) {
		return new RemoteServiceProxyInvocationHandler(remoteInterfaceType.getName(),getEndpointUrl(),getBasicAuthorisation());
	}

	private String getBasicAuthorisation() {
		return basicAuthorisation;
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}
	
	public void register() {
		BeanFactoryBuilder.registerBeanFactory(ServiceFactory.class, this);
	}

}
