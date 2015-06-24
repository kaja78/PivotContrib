package pivot_contrib.rmiServer.auditlog;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import pivot_contrib.rmiServer.statistics.RMIStatisticsRecord;

public class AuditLogHandler extends Handler {

	public void publish(LogRecord record) {
		RMIStatisticsRecord rmiRecord = getRMIStatisticsRecord(record);
		if (rmiRecord != null) {
			System.out.println("TODO: publish record.");
		}
	}

	private RMIStatisticsRecord getRMIStatisticsRecord(LogRecord record) {
		if (record.getParameters() != null
				&& record.getParameters().length == 1
				&& record.getParameters()[0] instanceof RMIStatisticsRecord) {
			return (RMIStatisticsRecord) record.getParameters()[0];
		}
		return null;
	}

	public void flush() {
	}

	public void close() throws SecurityException {
	}

}
