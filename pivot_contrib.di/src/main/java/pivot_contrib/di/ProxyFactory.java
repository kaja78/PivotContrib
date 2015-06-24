package pivot_contrib.di;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public abstract class ProxyFactory extends AbstractBeanFactory implements BeanFactory {

	@SuppressWarnings("unchecked")
	public <C> C createInstance(Class<C> interfaceType) {
		checkInterfaceClass(interfaceType);
		InvocationHandler invocationHandler = getInvocationHandler(interfaceType);
		Object proxy = Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				new Class[] { interfaceType}, invocationHandler);
		return (C) proxy;
	}
	
	protected String getClassNameForInterface(Class<?> interfaceClass) {
		throw new UnsupportedOperationException();
	}

	public Object createInstance(Field field) {
		return createInstance(field.getType());
	}

	protected abstract InvocationHandler getInvocationHandler(
			Class<?> interfaceType);

	protected void checkInterfaceClass(Class<?> interfaceClass) {
		if (!interfaceClass.isInterface()) {
			throw new IllegalArgumentException(interfaceClass.getName()
					+ " is not interface.");
		}
	}
	
}
