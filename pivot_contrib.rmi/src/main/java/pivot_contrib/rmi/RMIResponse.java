package pivot_contrib.rmi;

import java.io.Serializable;
/**
 * Transfers result of RMI from server to client;
 * */
public interface RMIResponse extends Serializable{
	public static final String CONTENT_TYPE="application/RMIResponse";
	
	public abstract Object getResult();

}