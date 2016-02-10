package pivot_contrib.rmiServer;

import junit.framework.TestCase;
import pivot_contrib.rmi.RMIRequest;
import pivot_contrib.rmi.RMIRequestInvoker;
import pivot_contrib.rmi.TestingService;
import pivot_contrib.rmi.TestingServiceBean;
public class TestRMIRequestInvoker extends TestCase {

	public void testInvokeWithNoParameter() {
		RMIRequestInvoker invoker = new RMIRequestInvoker();
		RMIRequest request = new RMIRequest(TestingService.class.getName(),
				"getMessage");
		assertEquals("Hello !", invoker.invoke(request));
	}

	public void testInvokeWithParameter() {
		RMIRequestInvoker invoker = new RMIRequestInvoker();
		RMIRequest request = new RMIRequest(TestingService.class.getName(),
				"getMessage", new Class[] { String.class },
				new Object[] { "Hello" });
		assertEquals("Message: Hello", invoker.invoke(request));
	}

	public void testInvokeVoid() {
		int originalCounter = TestingServiceBean.counter;
		RMIRequestInvoker invoker = new RMIRequestInvoker();
		RMIRequest request = new RMIRequest(TestingService.class.getName(),
				"increaseCounter");
		assertNull(invoker.invoke(request));
		assertEquals(originalCounter + 1, TestingServiceBean.counter);

	}
}
