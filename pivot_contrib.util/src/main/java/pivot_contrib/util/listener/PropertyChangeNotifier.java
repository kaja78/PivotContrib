/**
 * 
 */
package pivot_contrib.util.listener;

import org.apache.pivot.util.ListenerList;

public interface PropertyChangeNotifier<Listener> {
	public ListenerList<Listener> getPropertyChangeListenerList();
}
