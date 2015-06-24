/**
 * 
 */
package pivot_contrib.util.serializer;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;

import pivot_contrib.di.Inject;

/**
 * @author khubl
 *
 */
public class BXMLBean implements Bindable {
	@BXML
	@Inject
	protected Containee containee;
	
	@BXML(id="anotherContaineeNamespaceName")
	@Inject
	protected Containee anotherContainee;
	
	protected Map<String,Object> namespace;
	
	public static class Containee{}

	public void initialize(Map<String, Object> namespace, URL location,
			Resources resources) {
		this.namespace=namespace;		
	}
}
