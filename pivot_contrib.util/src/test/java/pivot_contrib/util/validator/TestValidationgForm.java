package pivot_contrib.util.validator;

import java.util.List;

import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Form.Flag;
import org.apache.pivot.wtk.Form.Section;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.TextInput;
import org.junit.Assert;
import org.junit.Test;

public class TestValidationgForm {

	private ValidatingForm form=new ValidatingForm();
	
	@Test
	public void testGetComponentKeys() throws Exception {
		TextInput textInput=new TextInput();
		textInput.setTextKey("textKey");
		List<String> keys=form.getComponentKeys(textInput);
		Assert.assertEquals(1, keys.size());
		Assert.assertEquals("textKey",keys.get(0));
		
		ListButton listButton=new ListButton();
		listButton.setSelectedItemKey("selectedItemKey");
		listButton.setListDataKey("listDataKey");
		keys=form.getComponentKeys(listButton);
		Assert.assertEquals(2, keys.size());
		Assert.assertTrue(keys.contains("selectedItemKey"));
		Assert.assertTrue(keys.contains("listDataKey"));
	}
	
	@Test
	public void testValidateBean() {
		Section section=new Section();
		TextInput textInput=new TextInput();
		textInput.setTextKey("value");
		section.add(textInput);
		BoxPane boxPane=new BoxPane();
		TextInput innerTextInput=new TextInput();
		innerTextInput.setTextKey("value");
		boxPane.add(innerTextInput);
		section.add(boxPane);
		form.getSections().add(section);
		form.setBeanValidator(new FailingValidator());		
		form.setContext(new Context());
		form.storeAndValidateContext();
		
		
		Flag flag=org.apache.pivot.wtk.Form.getFlag(textInput);
		Assert.assertNotNull(flag);
		Assert.assertEquals("Invalid value",flag.getMessage());
		
		flag=org.apache.pivot.wtk.Form.getFlag(boxPane);
		Assert.assertNotNull(flag);
		Assert.assertEquals("Invalid value",flag.getMessage());
	}
	
	@Test
	public void testFormChanged() {
		Section section=new Section();
		TextInput textInput=new TextInput();
		textInput.setTextKey("value");
		section.add(textInput);
		form.getSections().add(section);
		
		form.setContext(new Context());
		Assert.assertFalse(form.isFormModified());
		textInput.setText("newValue");
		Assert.assertTrue(form.isFormModified());
		
		
	}
	public static class Context implements Cloneable{
		public String value="";

		
		public Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
		
		
		public boolean equals(Object obj) {
			return value.equals(((Context)obj).value);
		}
	}
	
	private static class FailingValidator extends BeanValidatorAdapter<Context> {

		
		protected void validateBean(Context bean) {
			assertProblem("value", MessageType.ERROR, "Invalid value", true);
			
		}

	
		
	}

}
