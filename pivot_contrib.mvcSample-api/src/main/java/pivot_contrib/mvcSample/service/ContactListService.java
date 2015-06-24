package pivot_contrib.mvcSample.service;

import pivot_contrib.di.Service;
import pivot_contrib.mvcSample.vo.Contact;

@Service
public interface ContactListService {

	void generateSampleData();

	Contact[] findAll();

	void merge(Contact contact);

	void delete(Contact contact);

}
