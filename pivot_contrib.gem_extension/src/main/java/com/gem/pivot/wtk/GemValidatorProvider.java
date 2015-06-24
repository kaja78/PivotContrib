package com.gem.pivot.wtk;

import com.gem.pivot.wtk.annotations.DatePicker;
import com.gem.pivot.wtk.validators.CalendarValidator;
import com.gem.pivot.wtk.validators.EmptyValidator;
import com.gem.pivot.wtk.validators.IntegerValidator;
import com.gem.pivot.wtk.validators.StringValidator;
import org.apache.pivot.wtk.validation.Validator;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 17.6.15<br/>
 * Time: 11:03<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class GemValidatorProvider {

	public static Validator getValidatorForField(Object o, Field field) {
		final Class<?> type = field.getType();

		if (type.isAssignableFrom(Integer.class)) {
			return new IntegerValidator(o, field);
		}

		if (type.isAssignableFrom(Calendar.class)) {
			final DatePicker datePicker = field.getAnnotation(DatePicker.class);
			final String pattern = datePicker != null ? datePicker.pattern() : "dd.MM.yyyy";
			return new CalendarValidator(o, field, pattern);
		}

		if (type.isAssignableFrom(String.class)) {
			return new StringValidator(o, field);
		}


		return new EmptyValidator();
	}

}
