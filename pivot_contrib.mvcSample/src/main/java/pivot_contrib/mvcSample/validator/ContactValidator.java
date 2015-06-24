package pivot_contrib.mvcSample.validator;

import org.apache.pivot.wtk.MessageType;

import pivot_contrib.mvcSample.vo.Contact;
import pivot_contrib.util.validator.BeanValidatorAdapter;

public class ContactValidator extends BeanValidatorAdapter<Contact> {

	@Override
	public void validateBean(Contact contact) {
		assertProblem("name", MessageType.ERROR, "Name must containg at least 3 characters.", contact.name == null || contact.name.length()<3);
		assertProblem("phoneNumber", MessageType.ERROR, "Phone number is required.", contact.phoneNumber == null || contact.phoneNumber.equals(""));
	}

}
