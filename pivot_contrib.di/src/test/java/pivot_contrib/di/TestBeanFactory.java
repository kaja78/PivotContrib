/**
 * 
 */
package pivot_contrib.di;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author khubl
 * 
 */
public class TestBeanFactory extends TestCase {

	public void testGetInstanceClass() {
		SampleService instance1 = (SampleService) BeanFactoryBuilder.getDefaultBeanFactory()
				.createInstance(TestBeanFactory.SampleServiceBean.class);
		SampleService instance2 = (SampleService) BeanFactoryBuilder.getDefaultBeanFactory()
				.createInstance(TestBeanFactory.SampleServiceBean.class);
		assertNotNull(instance1);
		assertEquals(TestBeanFactory.SampleServiceBean.class.getName(),
				instance1.getClass().getName());
		assertTrue(instance1 != instance2);
	}



	public void testGetInstanceInterfaceNamingConvention() {
		SampleService instance1 = (SampleService) BeanFactoryBuilder.getDefaultBeanFactory()
				.createInstance(SampleService.class);
		assertNotNull(instance1);
		assertEquals(TestBeanFactory.SampleServiceBean.class.getName(),
				instance1.getClass().getName());
	}

	public void testGetInstanceInterfaceAnnotation() {
		SampleService instance1 = (SampleService) BeanFactoryBuilder.getDefaultBeanFactory()
				.createInstance(SampleRuntimeService.class);
		assertNotNull(instance1);
		assertEquals(TestBeanFactory.SampleRuntimeServiceBean.class.getName(),
				instance1.getClass().getName());
	}

	@Test(expected = RuntimeException.class)
	public void testGetInstanceInterfaceMissingBean() {
		try {
			BeanFactoryBuilder
					.getDefaultBeanFactory().createInstance(MissingService.class);
		} catch (RuntimeException e) {
			return;
		}
		fail();
	}

	@Test(expected = ClassNotFoundException.class)
	public void testGetInstanceBadConstructorBean() {
		try {
			BeanFactoryBuilder
					.getDefaultBeanFactory().createInstance(BadConstructorBean.class);
		} catch (RuntimeException e) {
			return;
		}
		fail();
	}

	public void testGetMockInstance() {
		BeanFactoryBuilder.setDefaultBeanFactory(new AbstractBeanFactory.MockFactory.MockFactory());
		SampleService instance1 = (SampleService) BeanFactoryBuilder.getDefaultBeanFactory()
				.createInstance(TestBeanFactory.SampleService.class);
		assertNotNull(instance1);
		assertEquals(TestBeanFactory.SampleServiceMock.class.getName(),
				instance1.getClass().getName());
		BeanFactoryBuilder.setDefaultBeanFactory(null);
	}

	public static interface SampleService {
	}

	@RuntimeScoped
	public static interface SampleRuntimeService extends SampleService {
	}

	public static class SampleServiceBean implements SampleService {
	}

	public static class SampleServiceMock implements SampleService {
	}

	
	public static class SampleRuntimeServiceBean implements SampleService,
			SampleRuntimeService {
	}

	public static interface MissingService {
	};

	public static class BadConstructorBean {
		public BadConstructorBean(int dummy) {
		}
	}

}
