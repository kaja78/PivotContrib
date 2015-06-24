package pivot_contrib.rmi;

/**
 * RemoteException is use to transfer Exception information thrown on server by
 * remote method invocation. Some server exception may not be available on
 * client classpath. Such exceptions are undeserialisable on client. The
 * RemoteException must be available on both server and client classpath, which
 * makes it secure to transfer original exception message and stack trace from
 * server to client.
 * */
public class RemoteException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private RemoteException(Throwable t) {
		super(t.toString());
		setStackTrace(t.getStackTrace());
	}

	private RemoteException(Throwable t, Throwable cause) {
		super(t.toString(), RemoteException.getRemoteException(cause));
		setStackTrace(t.getStackTrace());
	}

	public static RemoteException getRemoteException(Throwable throwable) {
		if (throwable.getCause() == null) {
			return new RemoteException(throwable);
		} else {
			return new RemoteException(throwable, throwable.getCause());
		}
	}

}