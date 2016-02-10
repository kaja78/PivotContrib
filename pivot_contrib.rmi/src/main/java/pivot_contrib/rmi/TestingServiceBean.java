package pivot_contrib.rmi;

public class TestingServiceBean implements TestingService {
	public static int counter = 0;

	public String getMessage() {
		return "Hello !";
	}

	public String getMessage(String message) {
		return "Message: " + message;
	}

	public void increaseCounter() {
		TestingServiceBean.counter = TestingServiceBean.counter + 1;
	}

	public void throwException() {
		throw new RuntimeException("Sample exception.");
	}

	public void unauthorizedMethod() {
	}

}
