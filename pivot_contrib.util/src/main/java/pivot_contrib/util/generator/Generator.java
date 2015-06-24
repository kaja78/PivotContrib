package pivot_contrib.util.generator;

import org.apache.pivot.collections.ArrayList;

public interface Generator<Type> {
	public ArrayList<Type> generateList(int size);
	public ArrayList<Type> generateList(int startIndex,int endIndex);
	public Type generateValue();
	public Type generateValue(int index);
}
