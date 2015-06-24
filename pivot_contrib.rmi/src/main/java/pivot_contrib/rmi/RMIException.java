package pivot_contrib.rmi;

/**
 * Thrown when RMI fails on server.
 * */
public class RMIException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RMIException(RemoteException e) {
		super("Root cause: " + getRootCause(e).toString(), e);
	}

	private static Throwable getRootCause(RemoteException e) {
		Throwable cause = e;
		while (cause.getCause() != null) {
			cause = cause.getCause();
		}
		return cause;
	}

}
