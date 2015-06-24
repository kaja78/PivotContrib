package pivot_contrib.mvcSample.vo;

import java.io.Serializable;

public class Contact implements Serializable {
	private static final long serialVersionUID = 1L;

	public long id;
	public String name;
	public String phoneNumber;

	public Contact() {
	}

	public Contact(long id, String name, String phoneNumber) {
		super();
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

}
