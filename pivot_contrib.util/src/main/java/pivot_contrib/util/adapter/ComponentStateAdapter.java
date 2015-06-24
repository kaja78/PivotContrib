/**
 * 
 */
package pivot_contrib.util.adapter;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentStateListener;

public class ComponentStateAdapter implements ComponentStateListener {
	public void enabledChanged(Component component) {		
	}

	public void focusedChanged(Component component, Component obverseComponent) {
		if (component.isFocused()) {
			focusGot(component, obverseComponent);
		} else {
			focusLost(component, obverseComponent);
		}
	}
	
	protected void focusGot(Component component, Component obverseComponent){
		
	}
	
	protected void focusLost(Component component, Component obverseComponent){
	}

}
