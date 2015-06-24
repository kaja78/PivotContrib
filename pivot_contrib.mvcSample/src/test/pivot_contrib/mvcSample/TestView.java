package pivot_contrib.mvcSample;

import junit.framework.TestCase;

import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Form;
import org.apache.pivot.wtk.TextInput;

import pivot_contrib.di.AbstractBeanFactory;
import pivot_contrib.di.BeanFactoryBuilder;
import pivot_contrib.di.ServiceFactory;
import pivot_contrib.mvcSample.vo.Contact;
import pivot_contrib.util.serializer.InjectingSerializer;

public class TestView extends TestCase {
	private Map<String, Object> namespace;
		
	@Override
	protected void setUp() throws Exception {
		BeanFactoryBuilder.registerBeanFactory(ServiceFactory.class, new AbstractBeanFactory.MockFactory());
		InjectingSerializer serializer=new InjectingSerializer();
		serializer.readObject(this.getClass(),"/pivot_contrib/mvcSample/SampleView.bxml");
		namespace=serializer.getNamespace();
	}
	
	public void testFormBinding() {
		TextInput nameInput=(TextInput)namespace.get("nameInput");		
		Form form=(Form)namespace.get("form");
				
		Contact contact=new Contact();
		contact.name="John";
		
		form.load(contact);
		assertEquals("John", nameInput.getText());
	}

}
