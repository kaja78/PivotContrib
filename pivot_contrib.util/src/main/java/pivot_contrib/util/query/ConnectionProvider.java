package pivot_contrib.util.query;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import pivot_contrib.di.BeanFactoryBuilder;
import pivot_contrib.di.ResourceFactory;

public class ConnectionProvider {

	private static ThreadLocal<ConnectionProvider> instance = new ThreadLocal<ConnectionProvider>();

	private Connection connection;

	private ConnectionProvider() {
	}

	public static ConnectionProvider get() {
		ConnectionProvider connectionProvider = instance.get();
		if (connectionProvider == null) {
			connectionProvider = new ConnectionProvider();
			instance.set(connectionProvider);
		}
		return connectionProvider;
	}

	public static void remove() {
		instance.remove();
	}

	public void commit() {
		if (connection != null) {
			try {
				connection.commit();
			} catch (SQLException e) {
				throw new RuntimeException("Exception commiting transaction.",
						e);
			}
		}
	}

	public void rollback() {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				throw new RuntimeException(
						"Exception rollbacking transaction.", e);
			}
		}
	}

	public Connection getConnection() {
		if (connection == null) {
			createConnection();
		}
		return connection;
	}

	private void createConnection() {
		DataSource dataSource = BeanFactoryBuilder.getBeanFactory(
				ResourceFactory.class).createInstance(DataSource.class);
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException(
					"Exception getting connection from data source.", e);
		}
	}

	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException("Exception closing connection.", e);
			} finally {
				connection = null;
			}
		}
	}

}
