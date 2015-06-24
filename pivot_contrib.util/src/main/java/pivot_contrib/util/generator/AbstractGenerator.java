package pivot_contrib.util.generator;

import org.apache.pivot.collections.ArrayList;

public abstract class AbstractGenerator<Type> implements Generator<Type> {

	
	public abstract Type generateValue(int index);

	
	public ArrayList<Type> generateList(int size) {
		ArrayList<Type> list=new ArrayList<Type>(size);
		for (int i=0;i<size;i++) {
			list.add(generateValue(i));
		}
		return list;
	}
	
	
	public ArrayList<Type> generateList(int startIndex, int endIndex) {
		ArrayList<Type> list=generateList(endIndex+1);
		list.remove(0, startIndex);
		return list;
	}
	
	
	public Type generateValue() {
		return generateValue(0);
	}

}
