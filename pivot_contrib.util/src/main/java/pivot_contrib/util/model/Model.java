package pivot_contrib.util.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pivot_contrib.di.Provider;
/**
 * Marks field for model injection.
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
@Provider(beanFactory=ModelFactory.class)
public @interface Model {

}
