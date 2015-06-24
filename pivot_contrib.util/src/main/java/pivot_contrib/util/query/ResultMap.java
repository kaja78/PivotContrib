package pivot_contrib.util.query;

import java.util.LinkedHashMap;

public class ResultMap extends LinkedHashMap<String, Object>{

	private static final long serialVersionUID = 1L;
	
	public ResultMap(int capacity) {
		super(capacity);
	}
	
	/**
	 * Returns value of specified column. The index of first column is 1.
	 * */
	public Object getColumnValue(int index) {
		String key=this.keySet().toArray()[index-1].toString();
		return this.get(key);
	}
	
	public Object[] toArray() {
		if (this.values()==null) {
			return null;
		}
		return this.values().toArray();
	}
	
	
	


}
