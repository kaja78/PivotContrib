/**
 * 
 */
package pivot_contrib.util.listener;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class NotifyingProxySupport<Listener> {
	public static final String PROPERTY_CHANGE_METHOD_SUFFIX = "Changed";

	private Object notifyingProxy;
	private DynaListenerList<Listener> listenerList;

	public NotifyingProxySupport(Object notifyingProxy,
			Class<Listener> listenerInterface) {
		this.notifyingProxy = notifyingProxy;
		this.listenerList = new DynaListenerList<Listener>(listenerInterface);
		checkSettersOnProxy();
	}
	
	public DynaListenerList<Listener> getListenerList() {
		return listenerList;
	}

	public void firePropertyChange(String propertyName) {
		Method notifyMethod = getNotifyMethod(propertyName);
		invokeMethodOnEventDispatcher(notifyMethod,
				new Object[] { notifyingProxy });
	}

	private void checkSettersOnProxy() {
		String[] expectedNotifyingPropertyNames = getExpectedNotifyingPropertyNames();
		for (String expectedNotifyingPropertyName : expectedNotifyingPropertyNames) {
			checkSetterExists(expectedNotifyingPropertyName);
		}
	}
	
	private void checkSetterExists(String propertyName) {
		Method setter = null;
		try {
			setter = getPropertyDescriptor(propertyName).getWriteMethod();
		} catch (Exception e) {
		}
		if (setter == null) {
			throw new RuntimeException("According to "
					+ this.listenerList.getListenerInterfaceClass().getName()
					+ ", class " + notifyingProxy.getClass().getName()
					+ " must override setter for " + propertyName
					+ " and implement notifycation implementation.");
		}

	}

	private String[] getExpectedNotifyingPropertyNames() {
		java.util.ArrayList<String> expectedNotifyingPropertyNames = new java.util.ArrayList<String>();
		Method[] listenerMethods = this.listenerList
				.getListenerInterfaceClass().getDeclaredMethods();
		for (Method listenerMethod : listenerMethods) {
			String methodName = listenerMethod.getName();
			if (methodName.endsWith(PROPERTY_CHANGE_METHOD_SUFFIX)) {
				expectedNotifyingPropertyNames.add(methodName.substring(
						0,
						methodName.length()
								- PROPERTY_CHANGE_METHOD_SUFFIX.length()));
			}
		}
		return expectedNotifyingPropertyNames
				.toArray(new String[expectedNotifyingPropertyNames.size()]);
	}

	private Method getNotifyMethod(String propertyName) {
		String notifyingMethodName = propertyName
				+ PROPERTY_CHANGE_METHOD_SUFFIX;
		try {
			return listenerList.getListenerInterfaceClass().getMethod(
					notifyingMethodName, new Class[] { Object.class });
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Notify method " + notifyingMethodName
					+ " not found on "
					+ listenerList.getListenerInterfaceClass().getName());
		}
	}

	private Object invokeMethodOnEventDispatcher(Method method, Object[] args) {
		try {
			return method.invoke(listenerList.getEventDispatcher(), args);
		} catch (Throwable e) {
			throw new RuntimeException("Exception invoking " + method.getName()
					+ " on "
					+ listenerList.getEventDispatcher().getClass().getName(), e);
		}
	}

	private PropertyDescriptor getPropertyDescriptor(String propertyName) {
		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (propertyName.equals(propertyDescriptor.getName())) {
				return propertyDescriptor;
			}
		}
		throw new IllegalArgumentException("Property descriptor for "
				+ propertyName + " not found on "
				+ notifyingProxy.getClass().getName());
	}

	private PropertyDescriptor[] getPropertyDescriptors() {
		try {
			return Introspector.getBeanInfo(
					notifyingProxy.getClass())
					.getPropertyDescriptors();
		} catch (IntrospectionException e) {
			throw new RuntimeException("Exception introspecting "
					+ notifyingProxy.getClass().getName(), e);
		}
	}

}
