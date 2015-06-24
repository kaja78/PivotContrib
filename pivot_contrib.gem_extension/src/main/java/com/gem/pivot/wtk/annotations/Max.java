package com.gem.pivot.wtk.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 17.6.15<br/>
 * Time: 10:11<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Max {
	long value();
}
