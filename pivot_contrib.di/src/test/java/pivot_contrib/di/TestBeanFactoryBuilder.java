package pivot_contrib.di;

import junit.framework.TestCase;
import pivot_contrib.di.TestBeanFactory.SampleService;

public class TestBeanFactoryBuilder extends TestCase {
	
	public void testGetInstanceRuntimeScopedClass() {
		SampleService instance1 = BeanFactoryBuilder.getBeanInstance(TestBeanFactory.SampleRuntimeService.class);
		SampleService instance2 = BeanFactoryBuilder.getBeanInstance(TestBeanFactory.SampleRuntimeService.class);
		assertNotNull(instance1);
		assertEquals(TestBeanFactory.SampleRuntimeServiceBean.class.getName(),
				instance1.getClass().getName());
		assertTrue(instance1 == instance2);
	}
}
