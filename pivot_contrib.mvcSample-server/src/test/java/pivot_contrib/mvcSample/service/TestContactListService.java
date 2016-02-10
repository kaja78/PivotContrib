package pivot_contrib.mvcSample.service;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.hsqldb.jdbc.JDBCDataSource;

import pivot_contrib.di.BeanFactoryBuilder;
import pivot_contrib.di.BeanInjector;
import pivot_contrib.di.ResourceFactory;
import pivot_contrib.di.Service;
import pivot_contrib.mvcSample.vo.Contact;

public class TestContactListService extends TestCase {
	
	@Service
	private ContactListService service;
	
	public TestContactListService() {
		configLocal();
		BeanInjector.getBeanInjector().injectDependencies(this);
	}

	private void configLocal(){
		JDBCDataSource ds=new JDBCDataSource();
		ds.setDatabase("jdbc:hsqldb:mem:mvcSampleDB");
		ds.setUser("SA");
		ResourceFactory rf=(ResourceFactory)BeanFactoryBuilder.getBeanFactory(ResourceFactory.class);
		rf.registerDefaultResource(DataSource.class, ds);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		service.generateSampleData();
	}
	
	public void testFindAll(){
		assertEquals(3, service.findAll().length);
	}
	
	public void testMergeUpdate() {
		Contact[] contacts=service.findAll();
		contacts[0].phoneNumber="+99 999 999";
		service.merge(contacts[0]);
		contacts=service.findAll();
		assertEquals(3, contacts.length);
		assertEquals("+99 999 999", contacts[0].phoneNumber);
	}
	
	public void testMergeInsert() {
		service.merge(new Contact());
		Contact[] contacts=service.findAll();
		assertEquals(4, contacts.length);
	}
	
	public void testDelete() {
		Contact[] contacts=service.findAll();
		assertEquals(3, contacts.length);
		service.delete(contacts[0]);
		contacts=service.findAll();
		assertEquals(2, contacts.length);
		
	}
	
	
}
