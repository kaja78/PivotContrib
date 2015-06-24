package pivot_contrib.rmiServer;

import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pivot_contrib.rmi.RMIRequest;
import pivot_contrib.rmi.RMIResponse;

public final class RMIRequestContext {
	private static ThreadLocal<RMIRequestContext> requestContext = new ThreadLocal<RMIRequestContext>();

	private RMIRequest rmiRequest;
	private RMIResponse rmiResponse;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String uuid;

	public static RMIRequestContext getRMIRequestContext() {
		return requestContext.get();
	}

	protected static void remove() {
		requestContext.remove();
	}

	public static void init(RMIRequest rmiRequest, ServletRequest request,
			ServletResponse response) {
		requestContext.set(new RMIRequestContext(rmiRequest,
				(HttpServletRequest) request, (HttpServletResponse) response));
	}
	
	public static String getRemoteUser() {
		if (getRMIRequestContext()==null || getRMIRequestContext().getRequest()==null) {
			return null;			
		}
		if (getRMIRequestContext().getRequest().getRemoteUser()==null) {
			return "annonymous";			
		}
		return getRMIRequestContext().getRequest().getRemoteUser();
	}
	
	public static boolean isUserInRole(String roleName) {
		return getRMIRequestContext().getRequest().isUserInRole(roleName);
	}

	private RMIRequestContext(RMIRequest rmiRequest,
			HttpServletRequest request, HttpServletResponse response) {
		this.rmiRequest = rmiRequest;
		this.request = request;
		this.response = response;
		this.uuid = UUID.randomUUID().toString();
	}

	public RMIResponse getRmiResponse() {
		return rmiResponse;
	}

	protected void setRmiResponse(RMIResponse rmiResponse) {
		this.rmiResponse = rmiResponse;
	}

	public RMIRequest getRmiRequest() {
		return rmiRequest;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getUUID() {
		return uuid;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

}
