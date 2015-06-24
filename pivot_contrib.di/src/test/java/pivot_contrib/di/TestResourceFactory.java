package pivot_contrib.di;


import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

public class TestResourceFactory extends TestCase {

	private Object defaultInstance=new Object();
	
	public TestResourceFactory() throws NamingException {
		ResourceFactory f = (ResourceFactory) BeanFactoryBuilder
				.getBeanFactory(ResourceFactory.class);
		f.registerDefaultResource(String.class, "defaultValue");
		f.registerDefaultResourceName(Object.class, "testingInstance");
		f.registerDefaultResource(Object.class, defaultInstance);
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, TestingInitialContextFactory.class.getName());
		BeanInjector.getBeanInjector().injectDependencies(this);
		
	}


	@Resource
	private String injectedString;
	
	@Resource("testingInstance")
	private Object injectedInstance;
	
	@Resource
	private Object injectedInstance2;
	
	@Resource("testingInstance3")
	private Object injectedInstance3;
	

	public void testInjectString() {
		assertEquals("defaultValue", injectedString);
	}
	
	public void testInjectByName() {		
		assertEquals(TestingInitialContextFactory.testingInstance, injectedInstance);
		assertEquals(defaultInstance, injectedInstance2);		
		assertEquals(TestingInitialContextFactory.testingInstance3,injectedInstance3);
		assertNotSame(TestingInitialContextFactory.testingInstance,injectedInstance3);
	
	}


}
