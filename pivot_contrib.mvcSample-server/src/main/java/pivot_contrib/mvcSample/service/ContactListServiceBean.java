package pivot_contrib.mvcSample.service;

import pivot_contrib.di.Inject;
import pivot_contrib.mvcSample.dao.contact.ContactDAO;
import pivot_contrib.mvcSample.vo.Contact;

public class ContactListServiceBean implements ContactListService {

	@Inject
	private ContactDAO contactDao;
	
	public void generateSampleData() {
		contactDao.generateSampleData();
	}

	public void merge(Contact contact) {
		contactDao.merge(contact);
	}

	public void delete(Contact contact) {
		contactDao.delete(contact);
	}

	public Contact[] findAll() {
		return contactDao.findAll();
	}

}
