package com.gem.pivot.wtk;

import org.apache.pivot.wtk.TextInput;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 18.6.15<br/>
 * Time: 14:35<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class TextCalendarMapping implements TextInput.TextBindMapping {

	private final String datePattern;
	private final SimpleDateFormat sdf;


	public TextCalendarMapping(String datePattern) {
		this.datePattern = datePattern;
		sdf = new SimpleDateFormat(datePattern);
	}

	@Override
	public String toString(Object value) {
		if (value != null) {
			if (Calendar.class.isAssignableFrom(value.getClass())) {
				return sdf.format(((Calendar) value).getTime());
			} else {
				System.out.println("Unsuported object " + value + " in "+getClass().getSimpleName());
			}
		}
		return "";
	}

	@Override
	public Object valueOf(String text) {
		if (text != null) {
			try {
				final Date parse = sdf.parse(text);
				final Calendar instance = Calendar.getInstance();
				instance.setTime(parse);
				return instance;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
