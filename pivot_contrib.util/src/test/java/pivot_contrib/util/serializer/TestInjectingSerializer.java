/**
 * 
 */
package pivot_contrib.util.serializer;

import junit.framework.TestCase;


/**
 * @author khubl
 *
 */
public class TestInjectingSerializer extends TestCase {
	public void testReadObject() throws Exception {
		BXMLBean bxmlBean=(BXMLBean)new InjectingSerializer().readObject(this.getClass().getResourceAsStream("BXMLBean.bxml"));
		assertNotNull(bxmlBean.containee);
		assertNotNull(bxmlBean.anotherContainee);
		assertNotNull(bxmlBean.namespace.get("containee"));
		assertNotNull(bxmlBean.namespace.get("anotherContaineeNamespaceName"));
		assertTrue(bxmlBean.containee==bxmlBean.namespace.get("containee"));
		assertTrue(bxmlBean.anotherContainee==bxmlBean.namespace.get("anotherContaineeNamespaceName"));
		assertTrue(bxmlBean.containee!=bxmlBean.anotherContainee);
	}

}
