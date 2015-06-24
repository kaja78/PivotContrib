/**
 * 
 */
package pivot_contrib.util.adapter;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.DragSource;
import org.apache.pivot.wtk.DropAction;
import org.apache.pivot.wtk.LocalManifest;
import org.apache.pivot.wtk.Point;
import org.apache.pivot.wtk.Visual;

/**
 * @author khubl
 *
 */
public abstract class DragSourceAdapter implements DragSource {
	protected LocalManifest content;
	protected int supportedDropActions;
	
	public DragSourceAdapter() {
		supportedDropActions=DropAction.COPY.getMask();
	}
	
	public DragSourceAdapter(int supportedDropActions) {
		this.supportedDropActions=supportedDropActions;
	}
	
	public abstract boolean beginDrag(Component component, int x, int y);

	public void endDrag(Component component, DropAction dropAction) {
		content=null;
	}

	public boolean isNative() {
		return false;
	}

	public LocalManifest getContent() {
		return content;
	}

	public Visual getRepresentation() {
		return null;
	}

	public Point getOffset() {
		return null;
	}

	public int getSupportedDropActions() {
		return supportedDropActions;
	}

}
