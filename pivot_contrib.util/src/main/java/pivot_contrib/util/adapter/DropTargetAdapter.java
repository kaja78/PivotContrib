/**
 * 
 */
package pivot_contrib.util.adapter;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.DropAction;
import org.apache.pivot.wtk.DropTarget;
import org.apache.pivot.wtk.Manifest;

/**
 * @author khubl
 * 
 */
public abstract class DropTargetAdapter implements DropTarget {
	protected DropAction defaultDropAction;

	public DropTargetAdapter() {
		defaultDropAction = DropAction.COPY;
	}

	public DropTargetAdapter(DropAction defaultDropAction) {
		this.defaultDropAction = defaultDropAction;
	}

	protected abstract boolean hasContent(Manifest dragContent);

	public abstract DropAction drop(Component component, Manifest dragContent,
			int supportedDropActions, int x, int y, DropAction userDropAction);

	public DropAction dragEnter(Component component, Manifest dragContent,
			int supportedDropActions, DropAction userDropAction) {
		if (hasContent(dragContent)) {
			return this.defaultDropAction;
		}
		return null;
	}

	public void dragExit(Component component) {
	}

	public DropAction dragMove(Component component, Manifest dragContent,
			int supportedDropActions, int x, int y, DropAction userDropAction) {
		if (hasContent(dragContent)) {
			return this.defaultDropAction;
		}
		return null;
	}

	public DropAction userDropActionChange(Component component,
			Manifest dragContent, int supportedDropActions, int x, int y,
			DropAction userDropAction) {
		return this.defaultDropAction;
	}

}
