package pivot_contrib.di.logInjector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pivot_contrib.di.Provider;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Provider(beanFactory=LoggerFactory.class)
public @interface InjectLogger {

}
