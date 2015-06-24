package pivot_contrib.rmiServer.statistics;

import java.util.ArrayList;


public class RMIStatisticsServiceBean implements RMIStatisticsService {

	public RMIStatisticsVO[] getRMIStatistics() {
		RMIStatistics[] rmiStatistics=RMIStatisticsProvider.getRMIStatistics();
		ArrayList<RMIStatisticsVO> res=new ArrayList<RMIStatisticsVO>(rmiStatistics.length+1);
		res.add(RMIStatisticsProvider.getGlobalStatistics().toValueObject());
		for (RMIStatistics item : rmiStatistics) {
			res.add(item.toValueObject());
		}
		return res.toArray(new RMIStatisticsVO[res.size()]);
	}



}
