/**
 * 
 */
package pivot_contrib.util.adapter;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.Mouse.Button;

/**
 * @author khubl
 *
 */
public class ComponentMouseButtonAdapter implements
		ComponentMouseButtonListener {

	private static final int SINGLE_CLICK=1;
	private static final int DOUBLE_CLICK=2;

	
	public boolean mouseDown(Component component, Button button, int x, int y) {
		return false;
	}

	
	public boolean mouseUp(Component component, Button button, int x, int y) {
		return false;
	}

	
	public boolean mouseClick(Component component, Button button, int x, int y,
			int count) {
		switch (count) {
		case SINGLE_CLICK:
			return mouseSingleClick();
		case DOUBLE_CLICK:
			return mouseDoubleClick();
		default:
			return false;
		}
	}
	
	/**
	 * Single click handler.
	 * */
	public boolean mouseSingleClick() {
		return false;
	}
	
	/**
	 * Double click handler.
	 * */
	public boolean mouseDoubleClick() {
		return false;
	}

}
