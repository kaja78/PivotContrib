package pivot_contrib.util.validator;

public interface ValidatingFormListener {
	public void contextChanged(Object context);
	public void	contextStoredAndValidated(Object context);
	public void contextValidationError(BeanValidationException e);
}
