package pivot_contrib.util.query;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Border;

import pivot_contrib.di.Inject;
import pivot_contrib.util.launcher.Launcher;


public class QueryTableSample extends Border implements Bindable{

	@Inject
	private Query query;
	
	@BXML
	private QueryTable queryTable;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestingDatabaseBuilder.buildDatabase();
		Launcher.launch("/pivot_contrib/util/query/QueryTableSample.bxml");
	}

	private void loadCustomers() {
		query.loadTemplateFromResource("SampleQuery.sql");
		java.util.Map<String,Object>[] r=query.executeQuery().rows;
		queryTable.setTableData(r);
		
	}
	public void initialize(Map<String, Object> namespace, URL location,
			Resources resources) {
		loadCustomers();
		
	}

}
