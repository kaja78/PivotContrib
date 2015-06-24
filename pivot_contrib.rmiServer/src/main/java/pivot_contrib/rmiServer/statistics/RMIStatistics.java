package pivot_contrib.rmiServer.statistics;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;



public class RMIStatistics {
	private final String key;
	private AtomicLong totalExecutionTime = new AtomicLong();
	private AtomicLong maxExecutionTime = new AtomicLong();
	private AtomicInteger totalExecutionCount = new AtomicInteger();
	private AtomicInteger totalExceptionCount = new AtomicInteger();
	private AtomicLong requestTotalBytes = new AtomicLong();
	private AtomicLong responseTotalBytes = new AtomicLong();
	
	public RMIStatistics(String key) {
		this.key=key;
	}
	
	public void update(RMIStatisticsRecord record) {
		totalExecutionTime.getAndAdd(record.executionTime);
		totalExecutionCount.incrementAndGet();
		requestTotalBytes.getAndAdd(record.requestSize);
		responseTotalBytes.getAndAdd(record.responseSize);
		while (maxExecutionTime.get() < record.executionTime) {
			maxExecutionTime.set(record.executionTime);
		}
		if (record.exceptionResult) {
			totalExceptionCount.incrementAndGet();
		}
	}

	public double getAverageExecutionTime() {
		if (totalExecutionCount.get() == 0) {
			return 0;
		}
		return totalExecutionTime.doubleValue() / totalExecutionCount.get();
	}

	public int getTotalExecutionCount() {
		return totalExecutionCount.get();
	}

	public int getTotalExceptionCount() {
		return totalExceptionCount.get();
	}

	public long getMaxExecutionTime() {
		return maxExecutionTime.get();
	}

	public String toString() {
		return "Executions: " + getTotalExecutionCount() + ",Exceptions:"
				+ getTotalExceptionCount() + ",Total execution time:"
				+ getTotalExecutionTime() + " ms,Average executions time:"
				+ getAverageExecutionTime() + " ms,Max execution time:"
				+ getMaxExecutionTime() + " ms, requests total bytes:" 
				+ getRequestTotalBytes()+ " response total bytes:"
				+ getResponseTotalBytes();
	}

	public long getResponseTotalBytes() {
		return responseTotalBytes.get();
	}

	public long getRequestTotalBytes() {
		return requestTotalBytes.get();
	}

	private long getTotalExecutionTime() {
		return totalExecutionTime.get();
	}
	
	public String getKey() {
		return key;
	}
	
	public RMIStatisticsVO toValueObject() {
		RMIStatisticsVO vo=new RMIStatisticsVO();
		vo.key=this.getKey();
		vo.totalExceptionCount=this.getTotalExceptionCount();
		vo.totalExecutionCount=this.getTotalExecutionCount();
		vo.maxExecutionTime=this.getMaxExecutionTime();
		vo.averageExecutionTime=this.getAverageExecutionTime();
		vo.requestTotalBytes=this.getRequestTotalBytes();
		vo.responseTotalBytes=this.getResponseTotalBytes();
		return vo;
	}

}
