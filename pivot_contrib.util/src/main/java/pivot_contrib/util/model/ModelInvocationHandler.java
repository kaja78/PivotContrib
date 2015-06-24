package pivot_contrib.util.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.pivot.collections.HashMap;
import org.apache.pivot.util.ListenerList;

public class ModelInvocationHandler implements InvocationHandler {

	private HashMap<String, Object> propertyMap = new HashMap<String, Object>();
	private String modelListenerGetterName;

	@SuppressWarnings("rawtypes")
	private ListenerList listenerList = new ListenerList() {
	};

	private final Class<?> listenerInterfaceType;
	
	public ModelInvocationHandler(Class<?> modelInterfaceType) {
		this.listenerInterfaceType=getListenerInterfaceInstance(modelInterfaceType);
		modelListenerGetterName = "get" + modelInterfaceType.getSimpleName()
				+ "Listeners";
		initKeys(modelInterfaceType);
	}

	private Class<?> getListenerInterfaceInstance(Class<?> modelInterfaceType) {
		String modelInterfaceName = modelInterfaceType.getName()+"Listener";
		try {			
			return Class.forName(modelInterfaceName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("ModelListener interface "+modelInterfaceName+" not found.");
		}
		
	}

	private void initKeys(Class<?> interfaceType) {
		Method[] methods = interfaceType.getDeclaredMethods();
		for (Method method : methods) {
			if (isGetter(method.getName())) {
				String key = getPropertyName(method.getName());
				propertyMap.put(key, null);
			}
		}
	}

	public Object invoke(Object proxy, Method method, Object[] arguments)
			throws Throwable {
		String methodName = method.getName();
		if (methodName.equals(modelListenerGetterName)) {
			return listenerList;
		} else if (methodName.equals("getMapListeners")) {
			return propertyMap.getMapListeners();
		} else if (methodName.equals("get")) {
			return propertyMap.get((String) arguments[0]);
		} else if (methodName.equals("containsKey")) {
			return propertyMap.containsKey((String) arguments[0]);
		} else if (methodName.equals("isEmpty")) {
			return propertyMap.isEmpty();
		} else if (methodName.equals("getComparator")) {
			return propertyMap.getComparator();
		} else if (methodName.equals("iterator")) {
			return propertyMap.iterator();
		} else if (methodName.equals("put")) {
			invokeSet((String) arguments[0], arguments[1]);
			invokePropertyChangedOnListeners((String) arguments[0], proxy);
			return null;
		} else if (methodName.equals("remove")) {
			throw new UnsupportedOperationException();
		} else if (methodName.equals("clear")) {
			throw new UnsupportedOperationException();
		} else if (methodName.equals("getCount")) {
			return propertyMap.getCount();
		} else if (isGetter(methodName)) {
			checkGetterArguments(arguments);
			return invokeGetter(method);
		} else if (isSetter(methodName)) {
			checkSetterArguments(arguments);
			String propertyName = getPropertyName(methodName);
			invokeSet(propertyName, arguments[0]);
			invokePropertyChangedOnListeners(propertyName, proxy);
			return null;
		} else if (methodName.equals("setComparator")) {
			throw new UnsupportedOperationException();
		}
		throw new UnsupportedOperationException("Method " + method.getName()
				+ " is not supported.");
	}

	private void invokeSet(String propertyName, Object value) {
		propertyMap.put(propertyName, value);
	}

	private void invokePropertyChangedOnListeners(String propertyName,
			Object proxy) {
		String methodName = propertyName + "Changed";
		for (Object listener : listenerList) {
			try {
				Method method = listenerInterfaceType.getMethod(methodName,
						new Class[] { Object.class });
				method.invoke(listener, proxy);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(
						"Unable to invoke method " + methodName
								+ " on model listener class "
								+ listener.getClass().getName() + ". "
								+ e.getMessage(), e);
			}
		}
	}

	private void checkSetterArguments(Object[] arguments) {
		if (arguments.length != 1) {
			throw new RuntimeException(
					"Setter method must have exactly one parameter.");
		}
	}

	private boolean isSetter(String methodName) {
		return (methodName.startsWith("set"));
	}

	private Object invokeGetter(Method method) {
		String propertyName = getPropertyName(method.getName());
		Object result = propertyMap.get(propertyName);
		return result;
	}

	private String getPropertyName(String methodName) {
		StringBuffer propertyName;
		if (methodName.startsWith("get") || methodName.startsWith("set")) {
			propertyName = new StringBuffer(methodName.substring(3));
		} else if (methodName.startsWith("is")) {
			propertyName = new StringBuffer(methodName.substring(2));
		} else {
			throw new IllegalArgumentException("Not setter or getter: "
					+ methodName);
		}
		propertyName
				.setCharAt(0, Character.toLowerCase(propertyName.charAt(0)));
		return propertyName.toString();

	}

	private void checkGetterArguments(Object[] arguments) {
		if (arguments != null && arguments.length != 0) {
			throw new RuntimeException(
					"Getter method may not have any parameters.");
		}
	}

	private boolean isGetter(String methodName) {
		return (methodName.startsWith("is") || methodName.startsWith("get"));
	}

}
