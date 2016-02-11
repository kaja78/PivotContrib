package pivot_contrib.rmiServer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import junit.framework.TestCase;
import pivot_contrib.rmi.RMIException;
import pivot_contrib.rmi.RMIExceptionResponse;
import pivot_contrib.rmi.RMISerializer;



public class TestRMISerializerFilter extends TestCase {
	private static final String TEST_URL="http://localhost:8080/rmiServer/rmi";
	
	private HttpURLConnection createConnection() throws Exception {
		return (HttpURLConnection)new URL(TEST_URL).openConnection();
	}
	
	public void testHead() throws Exception{
		HttpURLConnection c=createConnection();
		c.setRequestMethod("HEAD");
		assertEquals(HttpURLConnection.HTTP_BAD_METHOD, c.getResponseCode());
		
	}
	
	public void testGet() throws Exception{
		HttpURLConnection c=createConnection();
		c.setRequestMethod("GET");
		assertEquals(HttpURLConnection.HTTP_BAD_METHOD, c.getResponseCode());
	}
	
	public void testBadPost() throws Exception{
		HttpURLConnection c=createConnection();
		c.setRequestMethod("POST");
		assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, c.getResponseCode());		
	}

}
