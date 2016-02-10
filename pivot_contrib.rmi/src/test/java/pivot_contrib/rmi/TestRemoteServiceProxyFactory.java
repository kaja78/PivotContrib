package pivot_contrib.rmi;

import junit.framework.TestCase;
import pivot_contrib.di.BeanInjector;
import pivot_contrib.di.Inject;


/*
 * Requires testing user tomcat with password tomcat.
 * */
public class TestRemoteServiceProxyFactory extends TestCase {
	
	private static final String TOMCAT_BASIC_AUTHENTICATION="Basic dG9tY2F0OnRvbWNhdA==";//username=tomcat password=tomcat
	
	@Inject
	private TestingService service;
	
	public TestRemoteServiceProxyFactory() {
		
		new RemoteServiceProxyFactory("http://localhost:8080/rmiServer/secured/rmi",TOMCAT_BASIC_AUTHENTICATION).register();
		BeanInjector.getBeanInjector().injectDependencies(this);
	}



	public void testGetService() {
		assertEquals("Hello !", service.getMessage());
	}

	public void testThrowException() {
		RMIException rmiException = null;
		try {
			service.throwException();
		} catch (RMIException e) {
			rmiException = e;
		}
		assertNotNull(rmiException);
		assertEquals(
				"pivot_contrib.rmi.RMIException: Root cause: pivot_contrib.rmi.RemoteException: java.lang.RuntimeException: Sample exception.",
				rmiException.toString());
		assertEquals("pivot_contrib.rmi.RemoteException: java.lang.RuntimeException: Exception invoking RMIRequest (pivot_contrib.rmi.TestingService,throwException)",
				rmiException.getCause().toString());
		assertEquals("pivot_contrib.rmi.RemoteException: java.lang.RuntimeException: Sample exception.",
				rmiException.getCause().getCause().toString());
	}
	
	public void testUnauthorizedMethod() {
		RMIException rmiException = null;
		try {
			service.unauthorizedMethod();
		} catch (RMIException e) {
			rmiException=e;
		}
		assertNotNull(rmiException);
		assertEquals("pivot_contrib.rmi.RMIException: Root cause: pivot_contrib.rmi.RemoteException: java.lang.SecurityException: User tomcat is not authorised to invokepivot_contrib.rmi.TestingService.unauthorizedMethod().", rmiException.toString());
	}

}
