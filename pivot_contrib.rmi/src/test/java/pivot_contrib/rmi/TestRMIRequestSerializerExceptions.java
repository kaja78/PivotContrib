package pivot_contrib.rmi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import pivot_contrib.rmi.RMIRequest;
import pivot_contrib.rmi.RMIResultResponse;
import pivot_contrib.rmi.RMISerializer;


public class TestRMIRequestSerializerExceptions {
	RMISerializer serializer = new RMISerializer();
	RMIRequest request = new RMIRequest(String.class.getName(), "dummy",new Class<?>[]{String.class},
			new Object[] { "value" });
	String result = "result";
	RMIResultResponse response = new RMIResultResponse(result);

	OutputStream failingOut = new OutputStream() {

		public void write(int b) throws IOException {
			throw new IOException();
		}
	};

	InputStream failingInputStream = new InputStream() {

		public int read() throws IOException {
			throw new IOException();
		}

	};

	@Test(expected = RuntimeException.class)
	public void testWriteRequest() throws RuntimeException {
		serializer.writeRequest(request, failingOut);
	}
	
	@Test(expected = RuntimeException.class)
	public void testWriteResponse() throws RuntimeException {
		serializer.writeResponse(response, failingOut);
	}
	
	@Test(expected = RuntimeException.class)
	public void testReadRequest() throws RuntimeException {
		serializer.readRequest(failingInputStream);
	}
	
	@Test(expected = RuntimeException.class)
	public void testReadResponse() throws RuntimeException {
		serializer.readResponse(failingInputStream);
	}
	
	

}
