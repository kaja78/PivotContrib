package pivot_contrib.util.query;

import pivot_contrib.di.BeanInjector;
import pivot_contrib.util.query.Query;
import pivot_contrib.util.query.SQL;
import junit.framework.TestCase;

public class TestQueryFactory extends TestCase {
	
	@SQL("SampleQuery.sql")
	private Query query;

	public TestQueryFactory() {
		BeanInjector.getBeanInjector().injectDependencies(this);
	}

	public void testSqlInject() {
		assertEquals("select * from CUSTOMER", query.getTemplate());
	}
}
