package pivot_contrib.rmi;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
/**
 * Handles serialisation of RMI requests and response on both client and server.
 * */
public class RMISerializer {	
	
	public void writeRequest(RMIRequest request, OutputStream out) {
		try {
			writeObject(request, out);
		} catch (IOException e) {
			throw new RuntimeException(
					"Exception serializing RMIRequest " + request+".", e);
		}

	}

	private void writeObject(Object object, OutputStream out)
			throws IOException {
		ObjectOutputStream objectOut = new ObjectOutputStream(out);
		objectOut.writeObject(object);
		objectOut.close();
	}

	public RMIRequest readRequest(InputStream in) throws RuntimeException {
		try {
			RMIRequest request = (RMIRequest) readObject(in);
			return request;
		} catch (Exception e) {
			throw new RuntimeException("Exception deserializing RMIRequest.",e);
		}		
	}

	private Object readObject(InputStream inputStream) throws ClassNotFoundException, IOException {
		ObjectInputStream objectIn = new ObjectInputStream(inputStream);
		Object object = objectIn.readObject();
		return object;
	}
	
	public RMIResponse readResponse(InputStream in) {
		try {
			RMIResponse response = (RMIResponse) readObject(in);
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Exception deserializing RMIResponse.",e);
		}		
	}
	
	public void writeResponse(RMIResponse response, OutputStream out) {
		try {
			writeObject(response, out);
		} catch (IOException e) {
			throw new RuntimeException(
					"Exception serializing RMIResponse " + response+".", e);
		}

	}
	
	

}
