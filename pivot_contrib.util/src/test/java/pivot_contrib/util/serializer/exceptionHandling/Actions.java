package pivot_contrib.util.serializer.exceptionHandling;

import pivot_contrib.rmi.ApplicationException;

public class Actions {
	public void throwApplicationException() {
		throw new ApplicationException("Aplikační vyjímka.");
		
	}
	
public void throwRuntimeException() {
		throw new RuntimeException("Neošetřená vyjímka.");
	}
}
