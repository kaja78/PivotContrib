package pivot_contrib.di.logInjector;

public class BeanWithLogger {
	
	@InjectLogger
	protected java.util.logging.Logger  l;
	
	public void sayHello() {
		l.info("Hello");
	}

}
