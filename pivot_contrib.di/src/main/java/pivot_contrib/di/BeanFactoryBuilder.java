package pivot_contrib.di;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import pivot_contrib.di.AbstractBeanFactory.BeanFactoryImpl;

public class BeanFactoryBuilder {

	private static BeanFactory defaultBeanFactory;
	private static Map<Class<? extends BeanFactory>, BeanFactory> beanFactoryMapping = new HashMap<Class<? extends BeanFactory>, BeanFactory>();
	private static HashMap<Class<?>, Object> runtimeCache = new HashMap<Class<?>, Object>();

	public static BeanFactory getDefaultBeanFactory() {
		if (defaultBeanFactory == null) {
			defaultBeanFactory = new BeanFactoryImpl();
		}
		return defaultBeanFactory;
	}
	
	public static void setDefaultBeanFactory(BeanFactory beanFactory) {
		BeanFactoryBuilder.defaultBeanFactory = beanFactory;
	}

	public static <F extends BeanFactory> void registerBeanFactory(
			Class<? extends BeanFactory> beanFactoryType, F beanFactory) {
		beanFactoryMapping.put(beanFactoryType, beanFactory);
	}	

	public static BeanFactory getBeanFactory(
			Class<? extends BeanFactory> beanFactoryType) {
		BeanFactory beanFactory = beanFactoryMapping.get(beanFactoryType);
		if (beanFactory == null) {
			beanFactory = createBeanFactory(beanFactoryType);
			beanFactoryMapping.put(beanFactoryType, beanFactory);
		}
		return beanFactory;
	}
	
	public static <BeanType> BeanType getBeanInstance(Class<BeanType> beanType) {
		if (isRuntimeScoped(beanType)) {
			return getRuntimeScopedBeanInstance(beanType, null);
		}
		return createBeanInstance(beanType);
	}

	public static Object getBeanInstance(Field field) {
		Class<?> beanType = field.getType();
		if (isRuntimeScoped(beanType)) {
			return getRuntimeScopedBeanInstance(beanType, field);
		}
		return createBeanInstance(field);
	}
	
	public static boolean isInjectable(Field field) {
		for (Annotation annotation : field.getDeclaredAnnotations()) {
			Provider injector = annotation.annotationType().getAnnotation(
					Provider.class);
			if (injector != null) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private static <BeanType> BeanType getRuntimeScopedBeanInstance(
			Class<BeanType> beanType, Field field) {
		BeanType instance = (BeanType) runtimeCache.get(beanType);
		if (instance == null) {
			if (field == null) {
				instance = createBeanInstance(beanType);
			} else {
				instance = (BeanType) createBeanInstance(field);
			}
			runtimeCache.put(beanType, instance);
		}
		return instance;
	}

	private static <BeanType> BeanType createBeanInstance(
			Class<BeanType> beanType) {
		Class<? extends BeanFactory> beanFactoryType = getBeanFactoryByBeanType(beanType);
		return (BeanType) getBeanFactory(beanFactoryType).createInstance(
				beanType);
	}

	private static Object createBeanInstance(Field field) {
		Class<? extends BeanFactory> beanFactoryType = getBeanFactoryByField(field);
		return getBeanFactory(beanFactoryType).createInstance(field);
	}

	private static Class<? extends BeanFactory> getBeanFactoryByField(Field field) {

		for (Annotation annotation : field.getDeclaredAnnotations()) {
			Provider injector = annotation.annotationType().getAnnotation(
					Provider.class);
			if (injector != null) {
				return injector.beanFactory();
			}
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	private static Class<? extends BeanFactory> getBeanFactoryByBeanType(Class<?> beanType) {
		Class<BeanFactory> beanFactoryType = BeanFactory.class;

		for (Annotation annotation : beanType.getDeclaredAnnotations()) {
			Provider provider = annotation.annotationType().getAnnotation(
					Provider.class);
			if (provider != null) {
				beanFactoryType = (Class<BeanFactory>) provider.beanFactory();
				break;
			}
		}

		return beanFactoryType;
	}

	private static BeanFactory createBeanFactory(
			Class<? extends BeanFactory> beanFactoryType) {
		try {
			if (beanFactoryType.isInterface()) {
				return getDefaultBeanFactory();
			}
			return beanFactoryType.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(
					"Exception creating BeanFactory of type "
							+ beanFactoryType.getName(), e);
		}
	}

	private static boolean isRuntimeScoped(Class<?> beanClass) {
		return beanClass.getAnnotation(RuntimeScoped.class) != null;
	}

}
