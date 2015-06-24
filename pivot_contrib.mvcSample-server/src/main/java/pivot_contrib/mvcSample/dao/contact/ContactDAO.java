package pivot_contrib.mvcSample.dao.contact;

import pivot_contrib.mvcSample.vo.Contact;
import pivot_contrib.util.query.Query;
import pivot_contrib.util.query.SQL;

public class ContactDAO {

	@SQL("findAll.sql")
	private Query findAll;

	public Contact[] findAll() {
		return findAll.executeQuery(Contact.class).rows;
	}

	public void merge(Contact contact) {
		if (contact.id == 0) {
			insert(contact);
		} else {
			update(contact);
		}
	}

	private void insert(Contact contact) {
		Query.create(
				"INSERT INTO CONTACT values (NEXT VALUE FOR CONTACT_SEQ,?,?)")
				.setParameters(contact.name, contact.phoneNumber)
				.executeUpdate();
	}

	private void update(Contact contact) {
		Query.create("UPDATE CONTACT SET name=?,phoneNumber=? where id=?")
				.setParameters(contact.name, contact.phoneNumber, contact.id)
				.executeUpdate();

	}

	public void delete(Contact contact) {
		Query.create("DELETE FROM CONTACT where id=?")
				.setParameters(contact.id).executeUpdate();
	}

	public void generateSampleData() {
		createTable();
		insert(new Contact(0, "John", "+420 602 125 325"));
		insert(new Contact(0, "Joe", "+420 602 125 326"));
		insert(new Contact(0, "Jack", "+420 602 125 327"));
	}

	private void createTable() {
		try {
			Query.create("DROP TABLE CONTACT").executeUpdate();
		} catch (Exception e) {
		}
		try {
			Query.create("DROP SEQUENCE CONTACT_SEQ").executeUpdate();
		} catch (Exception e) {
		}
		Query.create("CREATE SEQUENCE CONTACT_SEQ START WITH 1")
				.executeUpdate();
		Query.create(
				"CREATE TABLE CONTACT(id INTEGER,name VARCHAR(255),phoneNumber VARCHAR(255))")
				.executeUpdate();
	}
}
