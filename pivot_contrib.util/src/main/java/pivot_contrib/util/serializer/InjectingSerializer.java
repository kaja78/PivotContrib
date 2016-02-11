/**
 * 
 */
package pivot_contrib.util.serializer;

import java.lang.reflect.Field;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;

import pivot_contrib.di.BeanFactoryBuilder;

/**
 * Loads an object hierarchy from an XML document and inject dependencies to
 * namespace and instances.
 * 
 * @author Karel H�bl
 */
public class InjectingSerializer extends BXMLSerializer {
	static {
		BXMLSerializer.getMimeTypes().put(MIME_TYPE, InjectingSerializer.class);
	}

	
	protected Object newTypedObject(Class<?> type)
			throws InstantiationException, IllegalAccessException {
		Object newObject = BeanFactoryBuilder.getBeanInstance(type);
		addFieldsToNamespace(newObject);
		return newObject;
	}

	private void addFieldsToNamespace(Object newObject) {
		Map<String, Object> namespace = getNamespace();
		Class<?> type = newObject.getClass();
		while (type != null) {
			for (Field field : type.getDeclaredFields()) {
				BXML bxmlAnnotation = field.getAnnotation(BXML.class);
				if (bxmlAnnotation != null) {
					String nameInNamespace = bxmlAnnotation.id();
					if ("\0".equals(nameInNamespace)) {
						nameInNamespace = field.getName();
					}
					try {
						field.setAccessible(true);
						Object fieldValue = field.get(newObject);
						if (fieldValue != null) {
							namespace.put(nameInNamespace, fieldValue);
						}
					} catch (IllegalAccessException e) {
						throw new RuntimeException(
								"Unable to read value of field "
										+ field.getName()
										+ " on object of type "
										+ newObject.getClass().getName(), e);
					}
				}
			}
			if (type.getGenericSuperclass() instanceof Class<?>) {
				type = (Class<?>) type.getGenericSuperclass();
			} else {
				type = null;
			}
		}
	}
	
	@Override
	protected void reportException(Throwable t) {
		Exception e;
		if (t instanceof Exception) {
			e=(Exception)t;
		} else {
			e=new RuntimeException(t);
		}
		ApplicationContextHelper.handleUncaughtException(e);
	}

	
	
	

}
