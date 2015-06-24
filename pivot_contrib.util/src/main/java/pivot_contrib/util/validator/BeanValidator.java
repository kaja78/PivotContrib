package pivot_contrib.util.validator;

public interface BeanValidator {
	public void validate(Object bean) throws BeanValidationException;
}
