package pivot_contrib.rmi;



public class RMIApplicationExceptionResponse implements RMIResponse {
	
	
	private static final long serialVersionUID = 1L;
	
	private ApplicationException applicationException;
	
	public RMIApplicationExceptionResponse(
			ApplicationException applicationException) {
		super();
		this.applicationException = applicationException;
	}

	public Object getResult() {
		throw applicationException;
	}

}
