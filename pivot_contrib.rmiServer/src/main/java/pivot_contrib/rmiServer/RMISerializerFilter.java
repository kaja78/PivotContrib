package pivot_contrib.rmiServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pivot_contrib.rmi.ApplicationException;
import pivot_contrib.rmi.RMIApplicationExceptionResponse;
import pivot_contrib.rmi.RMIExceptionResponse;
import pivot_contrib.rmi.RMIRequest;
import pivot_contrib.rmi.RMIResponse;
import pivot_contrib.rmi.RMISerializer;
import pivot_contrib.rmi.RolesAllowed;
import pivot_contrib.rmiServer.statistics.RMIStatisticsProvider;

public class RMISerializerFilter implements Filter {

	public static final String REQUEST_TIMESTAMP = "pivot_contrib.rmiServer.RMISerializerFilter.REQUEST_TIMESTAMP";
	public static final String RESPONSE_SIZE = "pivot_contrib.rmiServer.RMISerializerFilter.RESPONSE_SIZE";
	
	private RMISerializer serializer = new RMISerializer();

	protected Logger l = Logger.getLogger(RMISerializerFilter.class.getName());

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		request.setAttribute(REQUEST_TIMESTAMP, System.currentTimeMillis());
		if ("POST".equals(((HttpServletRequest) request).getMethod())) {
			try {
				handleRequest(request, response, filterChain);
			} finally {
				RMIRequestContext.remove();
			}
		} else {
			((HttpServletResponse) response)
					.setStatus(HttpURLConnection.HTTP_BAD_METHOD);
		}
	}

	protected void handleRequest(ServletRequest request,
			ServletResponse response, FilterChain filterChain)
			throws IOException {
		new RequestHandler((HttpServletRequest) request,
				(HttpServletResponse) response, filterChain)
				.handleRequest();
	}

	public void destroy() {
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

	protected class RequestHandler {
		
		private HttpServletRequest request;
		private HttpServletResponse response;
		private FilterChain filterChain;
		private final long startTime = System.currentTimeMillis();

		protected RequestHandler(HttpServletRequest request,
				HttpServletResponse response, FilterChain filterChain) {
			this.request = request;
			this.response = response;
			this.filterChain = filterChain;
		}

		protected void handleRequest() throws IOException {
			try {
				initRMIRequestContext();
				doFilter();
			} catch (Exception e) {
				handleException(e);
			}
			writeRmiResponse();

		}

		private void doFilter() {
			try {
				filterChain.doFilter(request, response);
			} catch (Throwable t) {
				handleException(t);
			}
		}

		private void handleException(Throwable t) {
			RMIResponse rmiResponse;
			if (t instanceof ApplicationException) {
				rmiResponse=new RMIApplicationExceptionResponse((ApplicationException)t);
			} else {
				logException(t);
				rmiResponse = new RMIExceptionResponse(t);				
			}
			RMIRequestContext.getRMIRequestContext()
					.setRmiResponse(rmiResponse);

		}

		private void logException(Throwable t) {
			l.log(Level.SEVERE,
					"Unexpected exception while handling RMIRequest.", t);
		}

		private void initRMIRequestContext() throws Exception {
			RMIRequest rmiRequest = null;
			try {
				rmiRequest = getRmiRequest();
				new AccessChecker(rmiRequest).checkAccess();
			} finally {
				RMIRequestContext.init(rmiRequest, request, response);
			}
		}

		private RMIRequest getRmiRequest() throws Exception {
			try {
				InputStream in = request.getInputStream();
				return serializer.readRequest(in);
			} catch (Exception e) {
				throw new Exception("Invalid request.", e);
			}
		}

		private void writeRmiResponse() throws IOException {
			RMIResponse rmiResponse = RMIRequestContext.getRMIRequestContext()
					.getRmiResponse();
			DataOutputStream out = new DataOutputStream(
					response.getOutputStream());
			response.setContentType(RMIResponse.CONTENT_TYPE);
			serializer.writeResponse(rmiResponse, out);
			((HttpServletResponse) response)
					.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
			int responseSize = out.size();
			RMIRequestContext.getRMIRequestContext().getRequest().setAttribute(RESPONSE_SIZE, responseSize);
			
			RMIStatisticsProvider.updateStatistics(startTime, responseSize);
			
		}

		protected class AccessChecker {
			RMIRequest rmiRequest;

			public AccessChecker(RMIRequest rmiRequest) {
				this.rmiRequest = rmiRequest;
			}

			protected void checkAccess() throws SecurityException {
				Method targetMethod;
				try {
					Class<?> targetType = Class.forName(rmiRequest
							.getRemoteInterfaceName());
					targetMethod = targetType.getMethod(
							rmiRequest.getMethodName(),
							rmiRequest.getParameterTypes());
				} catch (Exception e) {
					throw new SecurityException(
							"Exception checking method access priviliges ("
									+ rmiRequest + ").", e);
				}
				checkAccess(targetMethod);

			}

			private void checkAccess(Method targetMethod) {
				RolesAllowed rolesAllowed = targetMethod
						.getAnnotation(RolesAllowed.class);
				if (rolesAllowed != null) {
					checkAccess(rolesAllowed);
					return;
				} else {
					rolesAllowed = targetMethod.getDeclaringClass()
							.getAnnotation(RolesAllowed.class);
					if (rolesAllowed != null) {
						checkAccess(rolesAllowed);
					}
				}
			}

			private void checkAccess(RolesAllowed annotation) {
				for (String roleName : annotation.value()) {
					if (request.isUserInRole(roleName)) {
						return;
					}
				}				
				throw new SecurityException("User "+RMIRequestContext.getRemoteUser()+" is not authorised to invoke"
						+ rmiRequest + ".");
			}

		}
	}

}
