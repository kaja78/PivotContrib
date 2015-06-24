package pivot_contrib.util.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pivot_contrib.di.Provider;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Provider(beanFactory = QueryFactory.class)
public @interface SQL {
	/**
	 * Resource name of file with SQL template for query.
	 * */
	String value();
}
