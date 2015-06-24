package pivot_contrib.util.serializer;

import java.lang.reflect.Method;

import org.apache.pivot.wtk.ApplicationContext;

public class ApplicationContextHelper {
	
	public static void handleUncaughtException(Exception exception) {
		try {
			//TODO: replace with ApplicationContext.handleUncaughtException when scope changes to public
			invokeHandleUncaughtException(exception);
		} catch (Exception e) {
			exception.printStackTrace();
			e.printStackTrace();
		}		
	}
	
	/**
	 * Invoke ApplicationContext.handleUncaughtException using reflection, while the method is protected.
	 * */
	private static void invokeHandleUncaughtException(Exception exception) throws Exception {
		Method handleUncaughtExceptionMethod=ApplicationContext.class.getDeclaredMethod("handleUncaughtException", new Class[]{Exception.class});
		handleUncaughtExceptionMethod.setAccessible(true);
		handleUncaughtExceptionMethod.invoke(null, exception);
	}


}
