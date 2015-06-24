package pivot_contrib.util.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.apache.pivot.collections.Map;

import pivot_contrib.di.ProxyFactory;

public class ModelFactory extends ProxyFactory {

	public Object createInstance(Field field) {
		return createInstance(field.getType());
	}

	protected InvocationHandler getInvocationHandler(Class<?> interfaceType) {
		return new ModelInvocationHandler(interfaceType);
	}

	@SuppressWarnings("unchecked")
	public <C> C createInstance(Class<C> interfaceClass) {
		checkInterfaceClass(interfaceClass);
		InvocationHandler invocationHandler = getInvocationHandler(interfaceClass);
		Object proxy = Proxy.newProxyInstance(Thread.currentThread()
				.getContextClassLoader(), new Class[] { interfaceClass,
				Map.class }, invocationHandler);
		return (C) proxy;
	}

}
