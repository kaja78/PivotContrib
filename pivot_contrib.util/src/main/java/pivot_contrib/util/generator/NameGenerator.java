package pivot_contrib.util.generator;

public class NameGenerator extends RoundRobinGenerator<String>{

	
	protected String[] generateValues() {
		return new String[]{"John","Joe","John","Jack","James","Rodger","Ralph","Paul","Peter","Hugo"};
	}



}
