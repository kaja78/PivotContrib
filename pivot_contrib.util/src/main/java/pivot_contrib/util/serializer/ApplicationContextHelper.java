package pivot_contrib.util.serializer;

import org.apache.pivot.wtk.ApplicationContext;

public class ApplicationContextHelper {
	
	public static void handleUncaughtException(Exception exception) {
		try {
			ApplicationContext.handleUncaughtException(exception);
		} catch (Exception e) {
			exception.printStackTrace();
			e.printStackTrace();
		}		
	}
}
