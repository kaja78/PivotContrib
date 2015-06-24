package pivot_contrib.rmiServer;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestHelper {
	/**
	 * Returns ServletContext URL using HttpServletRequest.getRequestURL().
	 */
	public static String getServletContextURL(HttpServletRequest request) {
		try {
			URL url=new URL(new URL(request.getRequestURL().toString()),request.getContextPath());
			return url.toString();
		} catch (Exception e) {
			throw new RuntimeException("Unable to get ServletContextURL",e);
		}
	}
	
	/**
	 * Returns ServletContext URL. The host is derived from host HTTP request header.
	 * */
	public static String getServletContextClientURL(HttpServletRequest request) {
		try {
			URL requestUrl=new URL(request.getRequestURL().toString());
			String host=request.getHeader("host");//includes port
			String contextPath=request.getContextPath();
			return requestUrl.getProtocol()+"://"+host+contextPath;
		} catch (Exception e) {
			throw new RuntimeException("Unable to get ServletContextURL",e);
		}
	}
}
