package pivot_contrib.rmi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import pivot_contrib.rmi.RMIException;
import pivot_contrib.rmi.RMIExceptionResponse;
import pivot_contrib.rmi.RMIRequest;
import pivot_contrib.rmi.RMIResponse;
import pivot_contrib.rmi.RMIResultResponse;
import pivot_contrib.rmi.RMISerializer;


import junit.framework.TestCase;

public class TestRMIRequestSerializer extends TestCase {
	
	RMISerializer serializer = new RMISerializer();
	RMIRequest request = new RMIRequest(String.class.getName(), "dummy",new Class<?>[]{String.class},
			new Object[] { "value" });
	
	public void testDeserializeRequest() throws Exception {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in;
		RMIRequest deserializedRequest;
		

		serializer.writeRequest(request, out);
		in = new ByteArrayInputStream(out.toByteArray());
		deserializedRequest = serializer.readRequest(in);

		assertNotNull(deserializedRequest);
		assertEquals(request.getRemoteInterfaceName(),
				deserializedRequest.getRemoteInterfaceName());
		assertEquals(request.getMethodName(),
				deserializedRequest.getMethodName());
		assertEquals(request.getParamaters()[0],
				deserializedRequest.getParamaters()[0]);
	}
	
	

	public void testDeserializeExceptionResponse() {
		Exception rootCause = new Exception("Message");
		Exception exception = new Exception(rootCause);
		RMIResponse response = new RMIExceptionResponse(exception);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in;
		RMIResponse deserializedResponse;
		RMISerializer serializer = new RMISerializer();

		serializer.writeResponse(response, out);
		in = new ByteArrayInputStream(out.toByteArray());
		deserializedResponse = serializer.readResponse(in);

		RMIException rmiException = null;
		try {
			deserializedResponse.getResult();
		} catch (RMIException e) {
			rmiException = e;
			e.printStackTrace();
		}

		assertNotNull(rmiException);
		assertEquals(
				"Root cause: pivot_contrib.rmi.RemoteException: "+rootCause.toString(),
				rmiException.getMessage());
		assertEquals("pivot_contrib.rmi.RemoteException: "+rootCause.toString(), rmiException.getCause().getCause().toString());
		assertEquals(exception.getStackTrace().length, rmiException.getCause().getStackTrace().length);
		for (int i = 0; i < exception.getStackTrace().length; i++) {
			assertEquals(exception.getStackTrace()[i],
					rmiException.getCause().getStackTrace()[i]);
		}
	}

	public void testDeserializeResultResponse() {
		String result = "result";
		RMIResultResponse response = new RMIResultResponse(result);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in;
		RMIResponse deserializedResponse;
		RMISerializer serializer = new RMISerializer();

		serializer.writeResponse(response, out);
		in = new ByteArrayInputStream(out.toByteArray());
		deserializedResponse = serializer.readResponse(in);

		assertNotNull(result, deserializedResponse.getResult());
	}

}
