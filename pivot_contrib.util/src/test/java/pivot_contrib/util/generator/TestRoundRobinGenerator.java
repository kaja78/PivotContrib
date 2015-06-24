package pivot_contrib.util.generator;

import org.apache.pivot.collections.List;
import org.junit.Test;

import junit.framework.Assert;


public class TestRoundRobinGenerator {
	private RoundRobinGenerator<String> generator=new RoundRobinGenerator<String>(){

		
		protected String[] generateValues() {
			return new String[]{"1","2"};
		}};
	
	@Test
	public void testGenerateValue() {
		Assert.assertEquals("1",generator.generateValue());		
	}
	
	@Test
	public void testGenerateList() {
		List<String> values=generator.generateList(4);
		Assert.assertEquals(4, values.getLength());
		Assert.assertEquals("1",values.get(0));
		Assert.assertEquals("2",values.get(1));
		Assert.assertEquals("1",values.get(2));
		Assert.assertEquals("2",values.get(3));
	}
			
}
