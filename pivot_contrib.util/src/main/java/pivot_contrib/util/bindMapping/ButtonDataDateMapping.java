package pivot_contrib.util.bindMapping;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.pivot.util.CalendarDate;
import org.apache.pivot.wtk.Calendar.SelectedDateBindMapping;

public class ButtonDataDateMapping implements SelectedDateBindMapping {

	
	public CalendarDate toDate(Object value) {
		if (value instanceof Date) {
			return getButtonData((Date) value);
		}
		throw new IllegalArgumentException(
				"Value must be instanceof java.util.Date");
	}

	
	public Object valueOf(CalendarDate calendarDate) {
		return calendarDate.toCalendar().getTime();
	}

	private CalendarDate getButtonData(Date value) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(value);
		return new CalendarDate(calendar);
	}

}
