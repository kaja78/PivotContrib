package pivot_contrib.rmi;

/**
 * Transfer result of sucessfull RMI from server to client.
 * */
public class RMIResultResponse implements RMIResponse {
	private static final long serialVersionUID = 1L;
	
	private Object result;
	
	public RMIResultResponse(Object result) {
		this.result=result;		
	}
	
	
	public Object getResult() {
		return result;
	}	
}
