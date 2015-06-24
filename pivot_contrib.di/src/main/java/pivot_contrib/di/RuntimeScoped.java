/**
 * 
 */
package pivot_contrib.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks class as RuntimeScoped. The BeanFactory will create only one instance
 * of this class during run time.
 * 
 * @author Karel Hübl
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RuntimeScoped {

}
