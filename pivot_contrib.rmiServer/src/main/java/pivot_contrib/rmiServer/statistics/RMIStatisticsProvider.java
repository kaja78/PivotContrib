package pivot_contrib.rmiServer.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import pivot_contrib.rmi.RMIExceptionResponse;
import pivot_contrib.rmi.RMIRequest;
import pivot_contrib.rmi.RMIResponse;
import pivot_contrib.rmiServer.RMIRequestContext;

public class RMIStatisticsProvider {

	private static RMIStatistics globalStatistics;
	private static Map<String, RMIStatistics> serviceStatistics;

	private static Logger l = Logger.getLogger(RMIStatisticsProvider.class
			.getName());

	static {
		initStatistics();
	}

	private static RMIStatisticsRecord createRecord(long startTime,
			int responseSize) {
		RMIRequestContext rmiRequestContext = RMIRequestContext
				.getRMIRequestContext();
		RMIResponse rmiResponse = rmiRequestContext.getRmiResponse();
		RMIRequest rmiRequest = rmiRequestContext.getRmiRequest();
		HttpServletRequest request = rmiRequestContext.getRequest();
		RMIStatisticsRecord r = new RMIStatisticsRecord();
		r.executionTime = System.currentTimeMillis() - startTime;
		r.exceptionResult = (rmiResponse instanceof RMIExceptionResponse);
		r.method = rmiRequest.getMethodName();
		r.serviceName = rmiRequest.getRemoteInterfaceName();
		r.startTime = startTime;
		r.requestSize = request.getContentLength();
		r.responseSize = responseSize;
		return r;
	}

	private synchronized static RMIStatistics createServiceStatistics(String key) {
		RMIStatistics statistics = RMIStatisticsProvider.serviceStatistics
				.get(key);
		if (statistics == null) {
			statistics = new RMIStatistics(key);
			RMIStatisticsProvider.serviceStatistics.put(key, statistics);
		}
		return statistics;
	}

	public synchronized static RMIStatistics[] getRMIStatistics() {
		return serviceStatistics.values().toArray(
				new RMIStatistics[serviceStatistics.size()]);
	}

	public static RMIStatistics getGlobalStatistics() {
		return globalStatistics;
	}

	private static RMIStatistics getServiceStatistics(String serviceName,
			String methodName) {
		String key = serviceName + "." + methodName;
		RMIStatistics statistics = RMIStatisticsProvider.serviceStatistics
				.get(key);
		if (statistics == null) {
			statistics = createServiceStatistics(key);
		}
		return statistics;
	}

	public synchronized static void initStatistics() {
		globalStatistics = new RMIStatistics("GLOBAL");
		serviceStatistics = new HashMap<String, RMIStatistics>();
	}

	public static void updateStatistics(long startTime, int responseSize) {
		if (RMIRequestContext.getRMIRequestContext().getRmiRequest() == null) {
			return;
		}
		RMIStatisticsRecord record = createRecord(startTime, responseSize);
		RMIStatisticsProvider.updateStatistics(record);
	}

	private static void updateStatistics(RMIStatisticsRecord record) {
		RMIStatisticsProvider.globalStatistics.update(record);
		RMIStatisticsProvider.getServiceStatistics(record.serviceName,
				record.method).update(record);
		l.info(record.toString());
	}

}
