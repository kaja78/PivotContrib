package pivot_contrib.util.query;

import java.util.Comparator;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.wtk.SortDirection;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TableViewSortListener;
import org.apache.pivot.wtk.content.TableViewRowComparator;

public class QueryTable extends TableView {

	public QueryTable() {
		this.getTableViewSortListeners().add(new SortListener());
	}

	public void setTableData(Object[] rows) {
		List<?> list;
		list=new ArrayList<Object>(rows);
		this.setTableData(list);
	}

	protected class SortListener implements TableViewSortListener {
		public void sortAdded(TableView tableView, String columnName) {
		}

		public void sortUpdated(TableView tableView, String columnName,
				SortDirection previousSortDirection) {
		}

		public void sortRemoved(TableView tableView, String columnName,
				SortDirection sortDirection) {
		}


		@SuppressWarnings("unchecked")
		public void sortChanged(TableView tableView) {
			List<?> tableData = getTableData();
			@SuppressWarnings("rawtypes")
			Comparator comparator=new TableViewRowComparator(
					QueryTable.this); 
			tableData.setComparator(comparator);
		}
	}

}
