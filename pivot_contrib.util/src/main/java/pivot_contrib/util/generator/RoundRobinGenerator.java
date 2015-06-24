package pivot_contrib.util.generator;

public abstract class RoundRobinGenerator<Type> extends AbstractGenerator<Type> {

	private Type[] values;
	
	public RoundRobinGenerator() {
		this.values=generateValues();
	}
	
	protected abstract Type[] generateValues();
	
	
	public Type generateValue(int index) {
		int valueIndex=(index)%values.length;
		return values[valueIndex];
	}
	
	

}
