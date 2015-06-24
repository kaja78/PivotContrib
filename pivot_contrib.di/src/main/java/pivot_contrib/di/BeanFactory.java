package pivot_contrib.di;

import java.lang.reflect.Field;

public interface BeanFactory {

	/**
	 * Provide instance of bean.
	 * */
	public <C> C createInstance(Class<C> type);
	
	/**
	 * Provide instance of bean for dependency injection.
	 * */
	public abstract  Object createInstance(Field field);

}