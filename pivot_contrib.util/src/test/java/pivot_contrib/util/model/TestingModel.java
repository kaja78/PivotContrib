package pivot_contrib.util.model;

import org.apache.pivot.collections.Map;
import org.apache.pivot.util.ListenerList;


@Model
public interface TestingModel extends Map<String, Object>{
	public ListenerList<TestingModelListener> getTestingModelListeners();
	public abstract void setMessage(String message);
	public abstract String getMessage();
	
}