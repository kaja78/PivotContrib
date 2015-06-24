package pivot_contrib.util.listener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.pivot.util.ListenerList;

/**
 * Implementation of dynamic ListenerList. This class provides eventDispatcher -
 * dynamically created proxy implementing specified Listener interface.
 */
public class DynaListenerList<Listener> extends ListenerList<Listener> {

	private Listener eventDispatcher;
	private Class<Listener> listenerInterfaceClass;

	public Class<Listener> getListenerInterfaceClass() {
		return listenerInterfaceClass;
	}

	public DynaListenerList(Class<Listener> listenerInterfaceClass) {
		this.listenerInterfaceClass = listenerInterfaceClass;
	}

	public Listener getEventDispatcher() {
		if (eventDispatcher == null) {
			eventDispatcher = createEventDispatcher();
		}
		return eventDispatcher;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Listener createEventDispatcher() {
		return (Listener) Proxy.newProxyInstance(
				listenerInterfaceClass.getClassLoader(),
				new Class[] { listenerInterfaceClass },
				new DispatcherInvocationHandler());
	}

	private class DispatcherInvocationHandler implements InvocationHandler {
		
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			for (Listener l : DynaListenerList.this) {
				method.invoke(l, args[0]);
			}
			return null;
		}

	}
}
