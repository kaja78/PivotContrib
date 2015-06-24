package pivot_contrib.util.generator;

import junit.framework.Assert;

import org.apache.pivot.collections.List;
import org.junit.Test;

public class TestEmailGenerator {
	private EmailGenerator generator=new EmailGenerator();
	
	@Test
	public void testGenerateList(){
		List<String> values=generator.generateList(25);
		Assert.assertEquals(25, values.getLength());
		Assert.assertEquals("john@gmail.com", values.get(0));
		Assert.assertEquals("james@gmail.com", values.get(24));
	}
}
