package pivot_contrib.rmiServer.statistics;

import org.junit.Test;

import pivot_contrib.di.DITestCase;
import pivot_contrib.di.Service;

public class TestRMIStatisticsService extends DITestCase {

	@Service
	private RMIStatisticsService service;
	
	@Test
	public void testGetGlobalStatistics() {
		RMIStatisticsVO[] statistics=service.getRMIStatistics();
		assertNotNull(statistics);
	}

}
