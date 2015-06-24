package pivot_contrib.util.launcher;

import pivot_contrib.di.AbstractBeanFactory;
import pivot_contrib.di.BeanFactoryBuilder;
import pivot_contrib.di.ServiceFactory;

public class LaunchMocking extends Launch {
	
	protected void config() {
		BeanFactoryBuilder.registerBeanFactory(ServiceFactory.class,
				new AbstractBeanFactory.MockFactory());

	}
	
	public static void main(String[] args) {
		String bxmlPath=args[0];
		new LaunchMocking().launch(bxmlPath);
	}
}
