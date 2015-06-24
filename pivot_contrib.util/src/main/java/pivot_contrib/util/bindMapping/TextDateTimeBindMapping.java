package pivot_contrib.util.bindMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.pivot.wtk.TextInput.TextBindMapping;

public class TextDateTimeBindMapping implements TextBindMapping,org.apache.pivot.wtk.TextArea.TextBindMapping,org.apache.pivot.wtk.Label.TextBindMapping {
	public static final String PATTERN = "dd.MM.yyyy HH:mm";
	SimpleDateFormat format = new SimpleDateFormat(PATTERN);

	
	public String toString(Object obj) {
		return format.format((Date)obj);
	}

	
	public Object valueOf(String text) {
		try {
			return format.parse(text);			
		} catch (ParseException e) {
			throw new RuntimeException("Invalid date format.["+PATTERN+"]");
		}
	}

}
