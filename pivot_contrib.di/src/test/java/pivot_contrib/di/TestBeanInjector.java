/**
 * 
 */
package pivot_contrib.di;

import javax.annotation.PostConstruct;

import junit.framework.TestCase;

/**
 * @author khubl
 * 
 */
public class TestBeanInjector extends TestCase {
	public void testInjectProperties() {
		ContainerBean container = new ContainerBean();
		BeanInjector.getBeanInjector().injectDependencies(container);
		assertNotNull(container.containee);
		assertTrue(container.postConstructDone);
		
	}

	public void testSetInjector() {
		BeanInjector.setBeanInjector(null);
		assertEquals(BeanInjector.FieldInjector.class.getName(), BeanInjector
				.getBeanInjector().getClass().getName());
	}

	public void testInvalidPostContructMethod() {
		try {
			BeanInjector.getBeanInjector().injectDependencies(
					new InvalidPostConstruct());
		} catch (RuntimeException e) {
			return;
		}
		fail();
	}

	public static class ContainerBean {
		@Inject
		protected ContaineeBean containee;
		
		protected boolean postConstructDone = false;

		@PostConstruct
		protected void postConstruct() {
			postConstructDone = true;
		}
	}

	public static class ContaineeBean {
	}

	public static class InvalidPostConstruct {
		@PostConstruct
		protected void postConstruct() {
			throw new RuntimeException();
		}
	}


}
