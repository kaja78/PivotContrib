package com.gem.pivot.wtk.validators;

import com.gem.pivot.wtk.annotations.Nullable;
import org.apache.commons.lang.StringUtils;
import org.apache.pivot.wtk.validation.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 17.6.15<br/>
 * Time: 14:17<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class GeneralValidator implements Validator {

	private final Object o;
	private final Field field;
	protected  boolean nullable = true;


	public GeneralValidator(Object o, Field field) {
		this.o = o;
		this.field = field;
	}

	@Override
	public boolean isValid(String text) {
		final Annotation[] annotations = field.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAssignableFrom(Nullable.class)) {
				nullable = ((Nullable) annotation).value();
			}
		}
		if (StringUtils.isEmpty(text) && !nullable) return false; else return true;
	}

}
