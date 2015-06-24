package pivot_contrib.util.generator;

public class EmailGenerator extends NameGenerator{
	
	public String generateValue(int i) {
		return super.generateValue(i).toLowerCase()+"@gmail.com";
	}
}
