package pivot_contrib.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Provider marks another annotation types as bean providers. Bean providers can
 * be used to mark fields and types.
 * </p>
 * <p>
 * Fields marked with Provider annotation are subject of dependency injection.
 * Specified bean factory type will be used to obtain instance to inject. See
 * {@link BeanInjector}.
 * </p>
 * <p>
 * Provider annotation on type (typically interface) defines which bean factory
 * type will be used to create new instances. See {@link BeanFactoryBuilder}
 * </p>
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Provider {
	Class<? extends BeanFactory> beanFactory();
}
