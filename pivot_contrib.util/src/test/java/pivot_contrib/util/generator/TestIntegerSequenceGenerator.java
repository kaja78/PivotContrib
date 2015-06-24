package pivot_contrib.util.generator;

import junit.framework.Assert;

import org.apache.pivot.collections.List;
import org.junit.Test;

public class TestIntegerSequenceGenerator {
	private IntegerSequenceGenerator generator=new IntegerSequenceGenerator();
	
	@Test
	public void testGenerateList() {
		List<Integer> values=generator.generateList(4);
		Assert.assertEquals(4, values.getLength());
		Assert.assertEquals(new Integer(0),values.get(0));
		Assert.assertEquals(new Integer(1),values.get(1));
		Assert.assertEquals(new Integer(2),values.get(2));
		Assert.assertEquals(new Integer(3),values.get(3));
		
		values=generator.generateList(1,2);
		Assert.assertEquals(2, values.getLength());
		Assert.assertEquals(new Integer(1),values.get(0));
		Assert.assertEquals(new Integer(2),values.get(1));
	}
}
