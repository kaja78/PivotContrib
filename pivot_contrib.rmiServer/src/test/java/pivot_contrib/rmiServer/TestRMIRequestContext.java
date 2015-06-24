package pivot_contrib.rmiServer;

import org.junit.Assert;
import org.junit.Test;

public class TestRMIRequestContext {
	
	@Test
	public void testGetRemoteUser() {
		Assert.assertNull(RMIRequestContext.getRemoteUser());
	}

}
