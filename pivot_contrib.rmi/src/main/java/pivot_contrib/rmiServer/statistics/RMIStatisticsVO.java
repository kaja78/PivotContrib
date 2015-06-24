package pivot_contrib.rmiServer.statistics;

import java.io.Serializable;



public class RMIStatisticsVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String key;
	public long maxExecutionTime;
	public long totalExecutionCount;
	public long totalExceptionCount;
	public double averageExecutionTime;
	public long requestTotalBytes;
	public long responseTotalBytes;
}
