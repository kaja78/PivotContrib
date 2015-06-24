package pivot_contrib.di;

import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

public class TestingInitialContextFactory implements InitialContextFactory {
	protected static Object testingInstance = new Object();
	protected static Object testingInstance3 = new Object();
	
	private static Context context = new Context() {

		public Object lookup(Name name)
				throws NamingException {
			return null;
		}

		public Object lookup(String name)
				throws NamingException {
			if ("testingInstance".equals(name)) {
				return testingInstance;
			} else if ("testingInstance3".equals(name)) {
				return testingInstance3;
			} else if ("java:comp/env".equals(name)) {
				return this;
			}
			return null;
		}

		public void bind(Name name, Object obj)
				throws NamingException {
		}

		public void bind(String name, Object obj)
				throws NamingException {
		}

		public void rebind(Name name, Object obj)
				throws NamingException {
		}

		public void rebind(String name, Object obj)
				throws NamingException {
		}

		public void unbind(Name name)
				throws NamingException {
		}

		public void unbind(String name)
				throws NamingException {
		}

		public void rename(Name oldName, Name newName)
				throws NamingException {
		}

		public void rename(String oldName, String newName)
				throws NamingException {
		}

		public NamingEnumeration<NameClassPair> list(
				Name name) throws NamingException {
			return null;
		}

		public NamingEnumeration<NameClassPair> list(
				String name) throws NamingException {
			return null;
		}

		public NamingEnumeration<Binding> listBindings(
				Name name) throws NamingException {
			return null;
		}

		public NamingEnumeration<Binding> listBindings(
				String name) throws NamingException {
			return null;
		}

		public void destroySubcontext(Name name)
				throws NamingException {
		}

		public void destroySubcontext(String name)
				throws NamingException {
		}

		public Context createSubcontext(Name name)
				throws NamingException {
			return null;
		}

		public Context createSubcontext(String name)
				throws NamingException {
			return null;
		}

		public Object lookupLink(Name name)
				throws NamingException {
			return null;
		}

		public Object lookupLink(String name)
				throws NamingException {
			return null;
		}

		public NameParser getNameParser(Name name)
				throws NamingException {
			return null;
		}

		public NameParser getNameParser(String name)
				throws NamingException {
			return null;
		}

		public Name composeName(Name name, Name prefix)
				throws NamingException {
			return null;
		}

		public String composeName(String name, String prefix)
				throws NamingException {
			return null;
		}

		public Object addToEnvironment(String propName,
				Object propVal) throws NamingException {
			return null;
		}

		public Object removeFromEnvironment(String propName)
				throws NamingException {
			return null;
		}

		public Hashtable<?, ?> getEnvironment()
				throws NamingException {
			return null;
		}

		public void close() throws NamingException {
		}

		public String getNameInNamespace()
				throws NamingException {
			return null;
		}
	};

	public Context getInitialContext(Hashtable<?, ?> environment)
			throws NamingException {
		return context;
	}
	
}