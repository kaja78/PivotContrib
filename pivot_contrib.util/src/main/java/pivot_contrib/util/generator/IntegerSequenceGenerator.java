package pivot_contrib.util.generator;

public class IntegerSequenceGenerator extends AbstractGenerator<Integer>{

	
	public Integer generateValue(int index) {
		return new Integer(index);
	}

}
