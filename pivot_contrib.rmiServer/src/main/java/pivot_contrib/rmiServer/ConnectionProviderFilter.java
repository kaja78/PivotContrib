package pivot_contrib.rmiServer;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.sql.DataSource;

import pivot_contrib.di.BeanFactoryBuilder;
import pivot_contrib.di.ResourceFactory;
import pivot_contrib.rmi.ApplicationException;
import pivot_contrib.util.query.ConnectionProvider;

public class ConnectionProviderFilter implements Filter {

	public static final String DATASOURCE_JNDI_NAME = "dataSourceJndiName";

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		ConnectionProvider.remove();
		boolean doRollback = false;
		try {
			chain.doFilter(request, response);
		} catch (ApplicationException e) {
			doRollback=e.isDoRollback();
		} catch (RuntimeException e) {
			throw e;
		} finally {
			releaseConnection(doRollback);
		}
	}

	private void releaseConnection(boolean doRollback) {
		try {
			if (doRollback) {
				ConnectionProvider.get().rollback();
			} else {
				ConnectionProvider.get().commit();
			}

		} finally {
			try {
				ConnectionProvider.get().closeConnection();
			} finally {
				ConnectionProvider.remove();
			}
		}
	}

	public void init(FilterConfig config) throws ServletException {
		String dataSourceJndiName = config.getInitParameter(DATASOURCE_JNDI_NAME);
		if (dataSourceJndiName==null) {
			throw new RuntimeException("Exception initializing ConnectionProviderFilter. Please specify initialisation property "+DATASOURCE_JNDI_NAME+" in web.xml.");
		}
		ResourceFactory resourceFactory = (ResourceFactory) BeanFactoryBuilder
				.getBeanFactory(ResourceFactory.class);
		resourceFactory.registerDefaultResourceName(DataSource.class,
				dataSourceJndiName);



	}

}
