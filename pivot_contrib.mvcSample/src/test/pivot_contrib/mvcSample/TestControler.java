package pivot_contrib.mvcSample;

import junit.framework.TestCase;
import pivot_contrib.di.AbstractBeanFactory;
import pivot_contrib.di.BeanFactoryBuilder;
import pivot_contrib.di.BeanInjector;
import pivot_contrib.di.Inject;
import pivot_contrib.di.ServiceFactory;
import pivot_contrib.mvcSample.service.ContactListServiceMock;
import pivot_contrib.mvcSample.vo.Contact;

public class TestControler extends TestCase {
	
	@Inject
	private SampleController controller;
	
	@Inject
	private SampleModel model;

	public TestControler() {
		BeanFactoryBuilder.registerBeanFactory(ServiceFactory.class, new AbstractBeanFactory.MockFactory());
		BeanInjector.getBeanInjector().injectDependencies(this);
	}
	
	@Override
	protected void setUp() throws Exception {		
		super.setUp();
		ContactListServiceMock.generateData();
	}

	public void testCreateContact() {
		controller.createContact();
		assertNotNull(model.getEditedContact());
	}
	
	public void testDeleteContact() {
		controller.loadContacts();
		model.setEditedContact(model.getContacts().get(0));
		controller.deleteContact();
		assertEquals(1, model.getContacts().getLength());
	}
	
	public void testGenerateSampleData() {
		controller.generateSampleData();
		assertEquals(2, model.getContacts().getLength());
	}
	
	public void testLoadContacts() {		
		controller.loadContacts();
		assertEquals(2, model.getContacts().getLength());
	}
	
	public void testSaveContact() {
		model.setEditedContact(new Contact());
		controller.saveContact();
	}

}
