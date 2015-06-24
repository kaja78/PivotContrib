package pivot_contrib.util.query;

import junit.framework.TestCase;
import pivot_contrib.di.BeanInjector;
import pivot_contrib.di.Inject;

public class TestQuery extends TestCase {
	public TestQuery() throws Exception{		
		BeanInjector.getBeanInjector().injectDependencies(this);
	}
	
	
	protected void setUp() throws Exception {
		super.setUp();
		TestingDatabaseBuilder.buildDatabase();
	}
	
	
	protected void tearDown() throws Exception {
		super.tearDown();
		TestingDatabaseBuilder.dropDatabase();
	}
	
	@Inject
	private Query query;
	
	
	public void testLoadTemplateFromResource() {
		query.loadTemplateFromResource("SampleQuery.sql");
		assertEquals("select * from CUSTOMER", query.getTemplate());
	}
	
	public void testExcuteQuery() {
		query.setTemplate("select * from CUSTOMER");
		GenericQueryResult r=query.executeQuery();
		assertNotNull(r);
		assertNotNull(r.toObjectArray());
		assertEquals(2, r.rows.length);
	}
	
	public void	 testBindingDisabledQuery() {
		query.setTemplate("select * from ? where 1=?");
		query.setParameterBindingEnabled(false);
		query.setParameters("CUSTOMER","1");
		GenericQueryResult r=query.executeQuery();
		assertNotNull(r);
		assertNotNull(r.toObjectArray());
		assertEquals(2, r.rows.length);

	}
	//HSQLDB does not support out params
	/*public void testCallQueryFunction() {
		query.setTemplate("{ ? = call findAllCustomers()}");
		QueryResult<Object[]> r=query.callQueryFunction(Object[].class);
		assertNotNull(r);
		assertEquals(2, r.rows.length);
	}*/
	
	public void testMaxRows() {
		query.setTemplate("select * from CUSTOMER").setMaxRows(1);
		QueryResult<ResultMap> r=query.executeQuery();
		assertNotNull(r);
		assertEquals(1, r.rows.length);
	}
	
	public void testExcuteValueObjectQuery() {
		query.setTemplate("select NAME as \"name\",LOCATION as \"location\" from CUSTOMER order by \"name\" desc");
		TestingVO[] r=query.executeQuery(TestingVO.class).rows;
		assertNotNull(r);
		assertEquals(2, r.length);
		assertEquals("Joe",r[0].name);
		assertEquals("Prague",r[0].location);
	}
	
	public void testExcuteParamQuery() {
		query.setTemplate("select * from CUSTOMER where NAME=?");
		QueryResult<ResultMap> r=query.setParameters("Joe").executeQuery();
		assertEquals(1, r.rows.length);
		assertEquals("Joe", r.rows[0].get("NAME"));
	}
	
	public void testExecuteUpdate() {
		query.setTemplate("update CUSTOMER set LOCATION='Vienna' where NAME='Joe'");
		assertEquals(1, query.executeUpdate());
	}
	
	public static class TestingVO {
		private String name;
		public String location;		
	}

}
