package pivot_contrib.rmi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Invokes RMIRequest on HTTP endpoint.
 * */
public class HttpRMIRequestInvoker {
	private String endpointURL;
	private String basicAuthorisation;
	private RMIRequest request;
	private RMISerializer serializer;
	private HttpURLConnection con;
	
	
	
	public HttpRMIRequestInvoker(String endpointURL, String basicAuthorisation, RMIRequest request) {
		super();
		this.endpointURL = endpointURL;
		this.request = request;
		this.serializer=new RMISerializer();
		this.basicAuthorisation=basicAuthorisation;
	}

	public Object getResult() {
		OutputStream out = getOutputStream();
		serializer.writeRequest(request, out);
		checkResponseCode();			
		RMIResponse response=serializer.readResponse(getInputStream());
		return response.getResult();			
	}

	private InputStream getInputStream() {
		try {
			return con.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException("Unable to get response from endpoint "+endpointURL,e);
		}
	}

	private void checkResponseCode() {
		try {
			int responseCode=con.getResponseCode();
			if (responseCode!=HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Invalid response code "+responseCode+" while sending request to "+endpointURL);
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to get HTTP response code.",e);
		}
	}

	private OutputStream getOutputStream()  {
		try {
			con= (HttpURLConnection)new URL(endpointURL).openConnection();
			con.setRequestMethod("POST");
			if (basicAuthorisation!=null) {
				con.setRequestProperty("Authorization", basicAuthorisation);
			}
			con.setDoOutput(true);
			OutputStream out=con.getOutputStream();
			return out;
		} catch (Exception e) {
			throw new RuntimeException("Unable to connect to remote endpoint "+endpointURL,e);
		}
	}
}