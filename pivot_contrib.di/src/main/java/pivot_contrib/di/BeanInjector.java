/**
 * 
 */
package pivot_contrib.di;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

/**
 * Provide dependency injection functionality.
 * 
 * @author Karel Hübl
 */
public abstract class BeanInjector {
	private static BeanInjector beanInjector;

	public static BeanInjector getBeanInjector() {
		if (beanInjector == null) {
			beanInjector = new FieldInjector();
		}
		return beanInjector;
	}

	public abstract Object injectDependencies(Object target);

	public static void setBeanInjector(BeanInjector beanInjector) {
		BeanInjector.beanInjector = beanInjector;
	}

	public static class FieldInjector extends BeanInjector {

		public Object injectDependencies(Object target) {
			injectFields(target);
			initTarget(target);
			return target;
		}

		private void initTarget(Object target) {
			for (Method method : target.getClass().getDeclaredMethods()) {
				if (isPostCreate(method)) {
					try {
						if (!method.isAccessible()) {
							method.setAccessible(true);
						}
						method.invoke(target, new Object[] {});
					} catch (Exception e) {
						throw new RuntimeException(
								"Unhandled exception calling PostCreate method "
										+ method.getName() + " on "
										+ target.getClass().getName(), e);
					}
				}
			}

		}

		private boolean isPostCreate(Method method) {
			PostConstruct postConstructAnnotation = method
					.getAnnotation(PostConstruct.class);
			return postConstructAnnotation != null
					&& method.getParameterTypes().length == 0;
		}

		private void injectFields(Object injectionTarget) {
			Class<?> type = injectionTarget.getClass();
			while (type != null) {
				Field[] fields = type.getDeclaredFields();
				for (Field field : fields) {

					if (BeanFactoryBuilder.isInjectable(field)) {
					Object fieldValue = BeanFactoryBuilder
							.getBeanInstance(field);
					
						try {
							if (!(field.isAccessible())) {
								field.setAccessible(true);
							}
							field.set(injectionTarget, fieldValue);
						} catch (Exception e) {
							throw new RuntimeException(
									"Unable to inject filed "
											+ field.getName()
											+ " on target "
											+ injectionTarget.getClass()
													.getName() + " ["
											+ injectionTarget.toString() + "]",
									e);
						}
					}
				}

				if (type.getGenericSuperclass() instanceof Class<?>) {
					type = (Class<?>) type.getSuperclass();
				} else {
					type = null;
				}
			}

		}

	}

}
