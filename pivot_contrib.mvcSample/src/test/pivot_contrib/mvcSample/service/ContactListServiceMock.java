package pivot_contrib.mvcSample.service;

import org.apache.pivot.collections.ArrayList;

import pivot_contrib.mvcSample.vo.Contact;

public class ContactListServiceMock implements ContactListService {

	private static ArrayList<Contact> l;

	static {
		generateData();
	}

	public static void generateData() {
		l = new ArrayList<Contact>();
		l.add(new Contact(1, "John", "+420 122 456 789"));
		l.add(new Contact(2, "Joe", "+421 123 456 444"));
	}

	public void generateSampleData() {
		generateData();
	}

	public Contact[] findAll() {
		return l.toArray(Contact[].class);
	}

	public void merge(Contact contact) {
		if (l.indexOf(contact) == -1) {
			l.add(contact);
		}
	}

	public void delete(Contact contact) {
		l.remove(contact);
	}

}
