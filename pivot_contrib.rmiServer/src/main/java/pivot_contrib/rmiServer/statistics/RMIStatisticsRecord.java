package pivot_contrib.rmiServer.statistics;

public class RMIStatisticsRecord {
	public long startTime;
	public long executionTime;
	public String serviceName;
	public String method;	
	public boolean exceptionResult;
	public int responseSize;
	public long requestSize;
	
	public String toString() {
		return serviceName+"."+method+", execution time "+executionTime+", request size "+requestSize+", respoinse size "+responseSize;
	}
}
