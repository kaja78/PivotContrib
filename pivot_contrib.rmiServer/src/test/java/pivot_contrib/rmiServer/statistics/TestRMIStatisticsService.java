package pivot_contrib.rmiServer.statistics;

import junit.framework.TestCase;

import org.junit.Test;

import pivot_contrib.di.BeanInjector;
import pivot_contrib.di.Service;

public class TestRMIStatisticsService extends TestCase {

	public TestRMIStatisticsService() {
		injectDependencies();
	}

	protected final void injectDependencies() {
		BeanInjector.getBeanInjector().injectDependencies(this);
	}

	@Service
	private RMIStatisticsService service;

	@Test
	public void testGetGlobalStatistics() {
		RMIStatisticsVO[] statistics = service.getRMIStatistics();
		assertNotNull(statistics);
	}

}
