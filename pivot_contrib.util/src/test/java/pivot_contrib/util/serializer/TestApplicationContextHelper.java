package pivot_contrib.util.serializer;

import org.junit.Test;

public class TestApplicationContextHelper {
	
	@Test
	public void testInvokeHandleUncaughtException() throws Exception {		
		ApplicationContextHelper.handleUncaughtException(new RuntimeException());//must not fail
	}

}
