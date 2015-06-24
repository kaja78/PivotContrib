package pivot_contrib.util.query;

import java.lang.reflect.Field;

import pivot_contrib.di.BeanFactory;
import pivot_contrib.di.BeanInjector;

public class QueryFactory implements BeanFactory {

	
	public <C> C createInstance(Class<C> type) {
		throw new UnsupportedOperationException();
	}

	
	public Object createInstance(Field field) {
		String resourceName = field.getAnnotation(SQL.class).value();
		Query query = new Query();
		BeanInjector.getBeanInjector().injectDependencies(query);
		query.loadTemplateFromResource(resourceName, field.getDeclaringClass());
		return query;
	}

}
