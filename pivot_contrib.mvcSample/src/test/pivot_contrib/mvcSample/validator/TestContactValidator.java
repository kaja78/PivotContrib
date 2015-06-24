package pivot_contrib.mvcSample.validator;

import junit.framework.TestCase;
import pivot_contrib.mvcSample.validator.ContactValidator;
import pivot_contrib.mvcSample.vo.Contact;
import pivot_contrib.util.validator.BeanValidationException;

public class TestContactValidator extends TestCase {
	
	private ContactValidator contactValidator=new ContactValidator();
	
	public void testValidateProblems() {		
		assertEquals(2,countValidationProblems(new Contact()));
	}
	
	public void testValidateOK() throws BeanValidationException {
		assertEquals(0,countValidationProblems(new Contact(0,"Joe","999")));
	}
	
	private int countValidationProblems(Contact contact) {
		int problemCount=0;
		try {
			contactValidator.validate(contact);
		} catch (BeanValidationException e) {
			problemCount=e.getValidationProblems().getLength();
		}
		return problemCount;
	}
}
