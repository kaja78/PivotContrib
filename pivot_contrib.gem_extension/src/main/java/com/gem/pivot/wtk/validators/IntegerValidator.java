package com.gem.pivot.wtk.validators;

import com.gem.pivot.wtk.annotations.Max;
import com.gem.pivot.wtk.annotations.Min;
import org.apache.commons.lang.StringUtils;
import org.apache.pivot.wtk.validation.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 17.6.15<br/>
 * Time: 11:02<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class IntegerValidator extends GeneralValidator implements Validator {
	private Integer min = null;
	private Long max = null;


	public IntegerValidator(Object o, Field field) {
		super(o, field);

		final Annotation[] annotations = field.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAssignableFrom(Min.class)) {
				min = ((Min) annotation).value();
				continue;
			}
			if (annotation.annotationType().isAssignableFrom(Max.class)) {
				max = ((Max) annotation).value();
			}
		}
	}

	@Override
	public boolean isValid(String text) {
		boolean res = super.isValid(text);
		if (res) {
			if (nullable && StringUtils.isEmpty(text)) {
				res = true;
			} else {
				try {
					final int i = Integer.parseInt(text);
					if (min != null) {
						res = res && (i >= min);
					}
					if (max != null) {
						res = res && (i <= max);
					}
				} catch (NumberFormatException e) {
					res = false;
				}
			}
		}
		return res;
	}
}
