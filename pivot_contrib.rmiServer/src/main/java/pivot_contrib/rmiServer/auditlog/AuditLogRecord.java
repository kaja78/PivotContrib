package pivot_contrib.rmiServer.auditlog;

public class AuditLogRecord {

	public long startTime;
	public long executionTime;
	public String serviceName;
	public String method;	
	public Object[] parameters;
	public boolean exceptionResult;
	public int responseSize;
	public long requestSize;
	public String userName;
	public String sessionId;

}
