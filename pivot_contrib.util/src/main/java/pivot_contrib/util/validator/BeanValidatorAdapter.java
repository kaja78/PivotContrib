package pivot_contrib.util.validator;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.wtk.MessageType;

public abstract class BeanValidatorAdapter<Bean> implements BeanValidator {

	private List<ValidationProblem> validationProblems=new ArrayList<ValidationProblem>();
	
	@SuppressWarnings("unchecked")
	
	public final void validate(Object bean) throws BeanValidationException {
		validationProblems.clear();
		validateBean((Bean)bean);
		checkProblems();
	}

	protected abstract void validateBean(Bean bean);
	
	protected void checkProblems() throws BeanValidationException{
		if (!validationProblems.isEmpty()) {
			throw new BeanValidationException(validationProblems);
		}		
	}
	
	protected void assertProblem(String textKey, MessageType messageType,String message,boolean isProblem) {
		if (isProblem) {
			validationProblems.add(new ValidationProblem(textKey, message, messageType));
		}
	}

	

	



}
