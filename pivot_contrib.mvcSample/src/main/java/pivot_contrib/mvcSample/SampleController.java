package pivot_contrib.mvcSample;

import javax.annotation.PostConstruct;

import org.apache.pivot.collections.ArrayList;

import pivot_contrib.di.Inject;
import pivot_contrib.di.RuntimeScoped;
import pivot_contrib.mvcSample.service.ContactListService;
import pivot_contrib.mvcSample.vo.Contact;

@RuntimeScoped
public class SampleController {

	@Inject
	private ContactListService service;

	@Inject
	private SampleModel model;

	@PostConstruct
	private void init() {
		generateSampleData();
	}

	public void generateSampleData() {
		service.generateSampleData();
		loadContacts();
	}

	public void createContact() {
		model.setEditedContact(new Contact());
	}

	public void loadContacts() {
		model.setContacts(new ArrayList<Contact>(service.findAll()));
	}

	public void deleteContact() {
		if (model.getEditedContact() != null) {
			service.delete(model.getEditedContact());
			loadContacts();
		}
	}

	public void saveContact() {
		if (model.getEditedContact() != null) {
			service.merge(model.getEditedContact());
			loadContacts();
		}
	}
}
