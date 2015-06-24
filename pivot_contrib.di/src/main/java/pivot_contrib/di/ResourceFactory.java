package pivot_contrib.di;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ResourceFactory implements BeanFactory {
	private Map<Class<?>, Object> defaultResourceMap = new HashMap<Class<?>, Object>();
	private Map<Class<?>, String> defaultResourceNameMap = new HashMap<Class<?>, String>();

	@SuppressWarnings("unchecked")
	public <C> C createInstance(Class<C> type) {
		C resource = (C) getDefaultResource(type);
		if (resource == null) {
			throw new RuntimeException("Default resource for type "
					+ type.getName() + " is not registred.");
		}
		return resource;
	}

	public Object createInstance(Field field) {
		Object instance = lookup(field);
		if (instance == null) {
			instance = createInstance(field.getType());
		}
		return instance;
	}

	private Object lookup(Field field) {
		String jndiName = getJndiName(field);
		try {
			return lookup(jndiName);

		} catch (NamingException e) {
			throw new RuntimeException("Unable to lookup name " + jndiName
					+ " to inject into " + field.toString(), e);
		}

	}

	private Object lookup(String jndiName) throws NamingException {
		if (jndiName == null || "".equals(jndiName)) {
			return null;
		}
		Context context = new InitialContext();
		Context envContext = (Context) context.lookup("java:comp/env");
		return envContext.lookup(jndiName);
	}

	private String getJndiName(Field field) {
		String jndiName = null;
		Resource resourceAnnotation = field.getAnnotation(Resource.class);
		if (resourceAnnotation != null) {
			jndiName = resourceAnnotation.value();
		}
		return jndiName;
	}

	public void registerDefaultResource(Class<?> type, Object value) {
		defaultResourceMap.put(type, value);
	}

	public void registerDefaultResourceName(Class<?> type, String name) {
		defaultResourceNameMap.put(type, name);
	}

	public Object getDefaultResource(Class<?> type) {
		Object instance = defaultResourceMap.get(type);

		if (instance == null) {
			String jndiName = defaultResourceNameMap.get(type);
			try {
				instance = lookup(jndiName);
			} catch (NamingException e) {
				throw new RuntimeException("Unable to lookup default name "
						+ jndiName + " for type " + type.getName(), e);
			}
		}
		return instance;
	}

	public void register() {
		BeanFactoryBuilder.registerBeanFactory(ResourceFactory.class, this);
	}

}
