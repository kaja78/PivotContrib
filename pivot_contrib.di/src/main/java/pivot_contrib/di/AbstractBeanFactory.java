/**
 * 
 */
package pivot_contrib.di;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * BeanFactory provides instances of beans to be used for dependency injection
 * or programatically bv client. 
 * 
 * @author Karel Hübl
 * 
 */
public abstract class AbstractBeanFactory implements BeanFactory {

	/**
	 * Provide instance of bean inject into member (field, method, constructor).
	 * */
	
	public Object createInstance(Field field) {
		return createInstance(field.getType());
	}

	/**
	 * Registers this instance in BeanFactoryBuilder as BeanFactory.
	 */
	public void register() {
		BeanFactoryBuilder.registerBeanFactory(BeanFactory.class, this);
	}

	/**
	 * Default implementation of BeanFactory. The following rules are used to
	 * map class name for client requests. If the beanClass is interface, than
	 * value of DefaultImplementor annotation on that interface is used to
	 * determine the target class to provide. If DefaultImplementor is not
	 * specified, than interface name is suffixed by "Bean".
	 * */
	public static class BeanFactoryImpl extends AbstractBeanFactory {

		private HashMap<Class<?>, Class<?>> classMapping = new HashMap<Class<?>, Class<?>>();

		@SuppressWarnings("unchecked")
		public <C> C createInstance(Class<C> beanType) {
			Class<C> targetBeanClass=getTargetBeanType(beanType);
			Object instance = null;
			try {
				instance = targetBeanClass.getConstructor(new Class[] {})
						.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			BeanInjector.getBeanInjector().injectDependencies(instance);
			return (C) instance;
		}

		protected String getClassNameForInterface(Class<?> interfaceClass) {
			return interfaceClass.getName() + "Bean";
		}

		@SuppressWarnings("unchecked")
		protected <C> Class<C> getTargetBeanType(Class<C> beanType) {
			Class<?> targetBeanClass = classMapping.get(beanType);
			if (targetBeanClass != null) {
				return (Class<C>) targetBeanClass;
			}
		
			if (beanType.isInterface()) {
				String targetBeanClassName;
				targetBeanClassName = getClassNameForInterface(beanType);
				try {
					targetBeanClass = Class.forName(targetBeanClassName);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("Bean class " + targetBeanClassName
							+ " for interface " + beanType.getName()
							+ " not found.", e);
				}
			} else {
				targetBeanClass = beanType;
			}
			classMapping.put(beanType, targetBeanClass);
			return (Class<C>) targetBeanClass;
		}

		/**
		 * Configure the BeanFactory to provide targetClass instances for keyClass
		 * requests.
		 * */
		public void addBeanMapping(Class<?> keyClass, Class<?> targetClass) {
			classMapping.put(keyClass, targetClass);
		}


	}

	/**
	 * Implementation of mocking BeanFactory. To determine the target class for
	 * requested interface, the interface name is suffixed by "Mock".
	 * */
	public static class MockFactory extends BeanFactoryImpl {
		
		protected String getClassNameForInterface(Class<?> interfaceClass) {
			return interfaceClass.getName() + "Mock";
		}
	}

}
