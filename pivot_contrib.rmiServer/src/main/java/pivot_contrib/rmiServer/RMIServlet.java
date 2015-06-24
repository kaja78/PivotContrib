package pivot_contrib.rmiServer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pivot_contrib.rmi.RMIRequest;
import pivot_contrib.rmi.RMIRequestInvoker;
import pivot_contrib.rmi.RMIResponse;
import pivot_contrib.rmi.RMIResultResponse;

public class RMIServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private RMIRequestInvoker invoker = new RMIRequestInvoker();

	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		RMIRequest rmiRequest = RMIRequestContext.getRMIRequestContext().getRmiRequest();
		RMIResponse rmiResponse = createRMIResponse(rmiRequest);
		RMIRequestContext.getRMIRequestContext().setRmiResponse(rmiResponse);
	}

	protected RMIResponse createRMIResponse(RMIRequest rmiRequest) {
		Object result = invoker.invoke(rmiRequest);
		RMIResponse rmiResponse = new RMIResultResponse(result);
		return rmiResponse;
	}

}
