package pivot_contrib.di.logInjector;

import org.junit.Test;

import pivot_contrib.di.DITestCase;
import pivot_contrib.di.Inject;

public class TestBeanWithLogger extends DITestCase {
	@Inject
	private BeanWithLogger bean;
	
	@Test
	public void testSayHello() {
		bean.sayHello();
	}
}
