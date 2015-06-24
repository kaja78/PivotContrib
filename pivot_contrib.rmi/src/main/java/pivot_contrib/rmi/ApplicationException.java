package pivot_contrib.rmi;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 6631260162655173915L;
	
	private final boolean doRollback;	

	public ApplicationException(String message) {
		super(message);
		doRollback=false;		
	}
	
	public ApplicationException(String message,boolean doRollback) {
		super(message);
		this.doRollback=doRollback;		
	}
	
	public boolean isDoRollback() {
		return doRollback;
	}
	


}
