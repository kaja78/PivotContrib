package com.gem.pivot.wtk.validators;

import org.apache.commons.lang.StringUtils;
import org.apache.pivot.wtk.validation.Validator;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 17.6.15<br/>
 * Time: 11:18<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class CalendarValidator extends GeneralValidator implements Validator {

	private final SimpleDateFormat sdf;

	public CalendarValidator(Object o, Field field, String pattern) {
		super(o, field);
		sdf = new SimpleDateFormat(pattern);
	}

	@Override
	public boolean isValid(String text) {
		if (super.isValid(text)) {
			try {
				if (nullable && StringUtils.isEmpty(text)) return true;
				final Date parse = sdf.parse(text);
				final String pS = sdf.format(parse);
				return pS.equals(text);
			} catch (ParseException e) {
				return false;
			}
		} else {
			return false;
		}
	}
}
