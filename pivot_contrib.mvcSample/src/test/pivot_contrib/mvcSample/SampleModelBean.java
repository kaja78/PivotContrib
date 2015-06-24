package pivot_contrib.mvcSample;

import org.apache.pivot.collections.List;
import org.apache.pivot.util.ListenerList;

import pivot_contrib.di.RuntimeScoped;
import pivot_contrib.mvcSample.vo.Contact;
import pivot_contrib.util.listener.NotifyingProxySupport;

@RuntimeScoped
public class SampleModelBean implements SampleModel {
	private List<Contact> contacts;
	private Contact editedContact;
	private NotifyingProxySupport<SampleModelListener> proxySupport = new NotifyingProxySupport<SampleModelListener>(
			this, SampleModelListener.class);

	public SampleModelBean() {
		System.out.println("New instance of SampleModelBean created.");
	}
	
	public ListenerList<SampleModelListener> getSampleModelListeners() {
		return proxySupport.getListenerList();
	}
	
	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
		proxySupport.firePropertyChange("contacts");
	}

	public Contact getEditedContact() {
		return editedContact;
	}

	public void setEditedContact(Contact editedContact) {
		this.editedContact = editedContact;
		proxySupport.firePropertyChange("editedContact");
	}

}
