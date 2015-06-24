package pivot_contrib.mvcSample;

import org.apache.pivot.collections.List;
import org.apache.pivot.util.ListenerList;

import pivot_contrib.di.RuntimeScoped;
import pivot_contrib.mvcSample.vo.Contact;
import pivot_contrib.util.model.Model;

@Model /*Comment out this line to use test/pivot_contrib/mvcSample/SampleModelBean instead of dynamic proxy created by ModelFactory.*/
@RuntimeScoped
public interface SampleModel {
	public ListenerList<SampleModelListener> getSampleModelListeners();
	
	public List<Contact> getContacts();
	public void setContacts(List<Contact> contacts);
	public Contact getEditedContact();
	public void setEditedContact(Contact editedContact);
}
