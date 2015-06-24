package pivot_contrib.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import pivot_contrib.di.Inject.DefinedByFieldType;

/**
 * Marks field for dependency injection. The bean provider is defined by field type.
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Provider(beanFactory = DefinedByFieldType.class)
public @interface Inject {

	public class DefinedByFieldType implements BeanFactory {

		
		public <C> C createInstance(Class<C> type) {
			throw new UnsupportedOperationException();
		}

		
		public Object createInstance(Field field) {
			return BeanFactoryBuilder.getBeanInstance(field.getType());
		}

	}
}
