package pivot_contrib.mda;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.dirigent.metafacade.builder.classloader.Stereotype;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Stereotype(pattern="valueObject/ValueObject.pattern.xml")

public @interface ValueObject {

}
