package pivot_contrib.rmi;

public class RMIExceptionResponse implements RMIResponse {

	private static final long serialVersionUID = 1L;
	
	private RemoteException remoteException;

	public RMIExceptionResponse(Throwable throwable) {
		this.remoteException=RemoteException.getRemoteException(throwable);
	}

	public Object getResult() {
		throw new RMIException(remoteException);
	}

}
