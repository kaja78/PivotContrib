package pivot_contrib.util.validator;

import org.apache.pivot.collections.List;

public class BeanValidationException extends RuntimeException {

	private static final long serialVersionUID = 7115633569739190332L;

	private final List<ValidationProblem> validationProblems;
	
	public BeanValidationException(List<ValidationProblem> validationProblems) {
		super();
		this.validationProblems=validationProblems;
	}

	public List<ValidationProblem> getValidationProblems() {
		return validationProblems;
	}
		
}
