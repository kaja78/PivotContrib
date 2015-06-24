package pivot_contrib.di;

import junit.framework.TestCase;

public abstract class DITestCase extends TestCase{

	public DITestCase() {
		injectDependencies();
	}

	protected final void injectDependencies() {
		BeanInjector.getBeanInjector().injectDependencies(this);
	}

}
