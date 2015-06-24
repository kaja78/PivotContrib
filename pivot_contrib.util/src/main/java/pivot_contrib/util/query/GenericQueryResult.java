package pivot_contrib.util.query;

import java.sql.ResultSet;

public class GenericQueryResult extends QueryResult<ResultMap> {

	private static final long serialVersionUID = -1093113521486617299L;

	public GenericQueryResult(ResultSet res) {
		super(res, ResultMap.class);
	}
	

	public Object[] getSingleResultArray() {
		ResultMap res=getSingleResult();
		if (res==null) {
			return null;
		}
		return res.toArray();
	}
	
	public Object[][] toObjectArray() {
		Object[][] res = new Object[rows.length][this.columnCount];
		for (int i = 0; i < res.length; i++) {
			res[i] = rows[i].toArray();
		}
		return res;
	}
	
}
