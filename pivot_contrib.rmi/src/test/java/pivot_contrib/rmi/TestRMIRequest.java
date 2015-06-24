package pivot_contrib.rmi;

import pivot_contrib.rmi.RMIRequest;

import junit.framework.TestCase;

public class TestRMIRequest extends TestCase {
	public void testRMIRequest() {
		RMIRequest r=new RMIRequest("interfaceName", "methodName");
		assertEquals("interfaceName", r.getRemoteInterfaceName());
		assertEquals("methodName", r.getMethodName());
		assertEquals(0, r.getParamaters().length);
	}
	
	public void testRMIRequest2() {
		RMIRequest r=new RMIRequest("interfaceName", "methodName",new Class[]{String.class},new Object[]{null});
		assertEquals("interfaceName", r.getRemoteInterfaceName());
		assertEquals("methodName", r.getMethodName());
		assertEquals(1, r.getParamaters().length);
		assertEquals(String.class, r.getParameterTypes()[0]);
		assertNull(r.getParamaters()[0]);
	}
}
