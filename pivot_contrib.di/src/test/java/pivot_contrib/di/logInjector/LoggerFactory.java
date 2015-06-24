package pivot_contrib.di.logInjector;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import pivot_contrib.di.AbstractBeanFactory;

public class LoggerFactory extends AbstractBeanFactory {

	public Object createInstance(Field field) {
		return Logger.getLogger(field.getDeclaringClass().getName());
	}
	
	public <C> C createInstance(Class<C> remoteInterface) {
		throw new UnsupportedOperationException();
	}

	protected String getClassNameForInterface(Class<?> interfaceClass) {
		throw new UnsupportedOperationException();
	}

}
