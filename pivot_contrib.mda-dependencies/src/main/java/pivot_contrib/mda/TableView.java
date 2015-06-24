/**
 * 
 */
package pivot_contrib.mda;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.dirigent.metafacade.builder.classloader.Stereotype;
//TODO: Vytvorit sablonu a unit testy
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Stereotype(pattern="tableView/TableView.pattern.xml")
public @interface TableView {

}
