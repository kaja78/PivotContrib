package pivot_contrib.mvcSample.launcher;

import pivot_contrib.di.AbstractBeanFactory;
import pivot_contrib.di.BeanFactoryBuilder;
import pivot_contrib.di.ServiceFactory;
import pivot_contrib.util.launcher.Launcher;

public class MockingLauncher {

	public static void main(String[] args) {
		BeanFactoryBuilder.registerBeanFactory(ServiceFactory.class,
				new AbstractBeanFactory.MockFactory());
		Launcher.launch("/pivot_contrib/mvcSample/SampleView.bxml");
	}

}
