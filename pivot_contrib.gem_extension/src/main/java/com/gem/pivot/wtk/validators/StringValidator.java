package com.gem.pivot.wtk.validators;

import com.gem.pivot.wtk.annotations.Empty;
import org.apache.commons.lang.StringUtils;
import org.apache.pivot.wtk.validation.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 17.6.15<br/>
 * Time: 11:17<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class StringValidator extends GeneralValidator implements Validator {
	private boolean empty=false;

	public StringValidator(Object o, Field field) {
		super(o, field);

		final Annotation[] annotations = field.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAssignableFrom(Empty.class)) {
				empty = ((Empty) annotation).value();
			}
		}
	}

	@Override
	public boolean isValid(String text) {
		return !(super.isValid(text) && StringUtils.isEmpty(text) && !empty);
	}
}
