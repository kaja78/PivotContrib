package pivot_contrib.util.validator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.util.ListenerList;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Container;
import org.apache.pivot.wtk.Form;
import org.apache.pivot.wtk.MessageType;

public class ValidatingForm extends Form {

	private BeanValidator beanValidator;
	private Object context;
	boolean clearFormOnStore = true;
	boolean loadInProgress = false;
	private ValidatingFormListenerList validatingFormListeners = new ValidatingFormListenerList();

	public ListenerList<ValidatingFormListener> getValidatingFormListeners() {
		return validatingFormListeners;
	}

	public boolean isClearFormOnStore() {
		return clearFormOnStore;
	}

	public void setClearFormOnStore(boolean clearFormOnStore) {
		this.clearFormOnStore = clearFormOnStore;
	}

	/**
	 * Tests if load operation is in progress on this form. Intent of this
	 * method is to enable differentiate handling of form control's events.
	 * */
	public boolean isLoadInProgress() {
		return loadInProgress;
	}

	protected void setLoadInProgress(boolean loadInProgress) {
		this.loadInProgress = loadInProgress;
	}

	public Object getContext() {
		return context;
	}

	public void setContext(Object context) {
		this.context = context;
		loadContext();
		if (context == null) {
			setEnabled(false);
			clearFields();
		} else {
			setEnabled(true);
			requestFocus();
		}
		validatingFormListeners.contextChanged(context);
	}

	
	public void load(Object context) {
		setLoadInProgress(true);
		try {
			super.load(context);
		} finally {
			setLoadInProgress(false);
		}
	}

	private void loadContext() {
		clearFlags();
		if (context != null) {
			load(context);
		}
	}

	/**
	 * The method returns true if the content of the form has changed. The
	 * intent is to test if user had edited the form and the context object
	 * needs to be updated. Form is stored into clone of actual context. Then
	 * clone is compared with actual context. If the store operation fails, true
	 * is returned. The context of the form must be set to Cloneable object with
	 * public clone() method and correctly implemented equals method.
	 * */
	public boolean isFormModified() {
		Object contextClone = cloneContext();
		try {
			store(contextClone);
		} catch (Exception e) {
			return true;
		}
		return !context.equals(contextClone);
	}

	/**
	 * Clone context using reflection.
	 * */
	private Object cloneContext() {
		if (context == null || !(context instanceof Cloneable)) {
			throw new IllegalStateException(
					"Form context must be set to Clonable object.");
		}
		try {
			return context.getClass().getMethod("clone", new Class<?>[] {})
					.invoke(context, new Object[] {});
		} catch (Exception e) {
			throw new RuntimeException("Unable to clone form context.", e);
		}
	}

	public BeanValidator getBeanValidator() {
		return beanValidator;
	}

	public void setBeanValidator(BeanValidator beanValidator) {
		this.beanValidator = beanValidator;
	}

	public void storeAndValidate(Object bean) throws BeanValidationException {
		clearFlags();
		store(bean);
		validateBean(bean);
	}

	
	public void store(Object context) throws BeanValidationException {
		new FormStoreHandler(context).store();
	}

	protected class FormStoreHandler {
		private Object context;
		private List<ValidationProblem> problems = new ArrayList<ValidationProblem>();

		protected FormStoreHandler(Object context) {
			this.context = context;
		}

		public void store() {
			storeContainer(ValidatingForm.this);
			if (problems.getLength() > 0) {
				throw new BeanValidationException(problems);
			}
		}

		private void storeContainer(Container container) {
			for (Component component : container) {
				// TODO: Create JIRA ISSUE
				// Wanted to flag the most inner component if store fails.
				// It seems this does not work for now.
				// So we need flag the container even if the problem is bound to
				// inner component.
				/*
				 * if (component instanceof Container) {
				 * storeContainer((Container)component); } else {
				 * storeComponent(component); }
				 */
				storeComponent(component);
			}

		}

		private void storeComponent(Component component) {
			try {
				component.store(context);
			} catch (RuntimeException e) {
				Form.setFlag(component,
						new Flag(MessageType.ERROR, e.getMessage()));
				ValidationProblem problem = new ValidationProblem(
						component.getName(), e.getMessage(), MessageType.ERROR);
				problems.add(problem);
			}
		}

	}

	public void storeAndValidateContext() {
		try {
			storeAndValidate(getContext());
			if (clearFormOnStore) {
				clearFields();
				setEnabled(false);
			}
			validatingFormListeners.contextStoredAndValidated(getContext());
		} catch (BeanValidationException e) {
			validatingFormListeners.contextValidationError(e);
		}
	}

	private void clearFields() {
		for (Section section : getSections()) {
			for (Component field : section) {
				field.clear();
			}
		}

	}

	public void validateBean(Object bean) throws BeanValidationException {
		try {
			if (beanValidator != null) {
				beanValidator.validate(bean);
			}
		} catch (BeanValidationException e) {
			setFlags(e);
			throw e;
		}
	}

	/**
	 * Set's flags based on validation problems on this form's components. The
	 * whole form compoennt hierarchy is searched. For each validation problem,
	 * flag is set on all components whose key or name equals to
	 * ValidationProblem key.
	 * */
	private void setFlags(BeanValidationException e) {
		for (ValidationProblem problem : e.getValidationProblems()) {
			setFlag(problem);
		}
	}

	private void setFlag(ValidationProblem validationProblem) {
		for (Section section : getSections()) {
			setFlagOnSection(validationProblem, section);
		}
	}

	private void setFlagOnSection(ValidationProblem validationProblem,
			Section section) {
		for (Component formComponent : section) {
			setFlag(validationProblem, formComponent);
		}
	}

	private void setFlag(ValidationProblem validationProblem,
			Component component) {
		java.util.List<String> componentKeys;
		try {
			componentKeys = getComponentKeys(component);
		} catch (Exception e) {
			throw new RuntimeException("Exception getting component keys.", e);
		}
		if (componentKeys.contains(validationProblem.getTextKey())) {
			setFlag(component, new Flag(validationProblem.getMessageType(),
					validationProblem.getMessage()));
		}
	}

	protected java.util.List<String> getComponentKeys(Component component)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		java.util.List<String> componentKeys = new java.util.ArrayList<String>();

		Class<? extends Component> componentClass = component.getClass();
		for (Method method : componentClass.getMethods()) {
			if (isKeyGetter(method)) {
				String key = (String) method.invoke(component, new Object[0]);
				if (key != null && !key.equals("")) {
					componentKeys.add(key);
				}
			}
		}

		if (component.getName() != null && !component.getName().equals("")) {
			componentKeys.add(component.getName());
		}

		// See TODO in storeContainer
		if (component instanceof Container) {
			for (Component childComponenr : (Container) component) {
				componentKeys.addAll(getComponentKeys(childComponenr));
			}
		}

		return componentKeys;
	}

	private boolean isKeyGetter(Method method) {
		return String.class.equals(method.getReturnType())
				&& method.getParameterTypes().length == 0
				&& method.getName().startsWith("get")
				&& method.getName().endsWith("Key");
	}

	private class ValidatingFormListenerList extends
			ListenerList<ValidatingFormListener> implements
			ValidatingFormListener {

		
		public void contextChanged(Object context) {
			for (ValidatingFormListener listener : this) {
				listener.contextChanged(context);
			}
		}

		
		public void contextStoredAndValidated(Object context) {
			for (ValidatingFormListener listener : this) {
				listener.contextStoredAndValidated(context);
			}

		}

		
		public void contextValidationError(BeanValidationException e) {
			for (ValidatingFormListener listener : this) {
				listener.contextValidationError(e);
			}

		}

	}
}
