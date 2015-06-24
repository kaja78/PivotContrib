package pivot_contrib.rmiServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import pivot_contrib.di.ResourceFactory;
import pivot_contrib.rmi.ApplicationException;
import pivot_contrib.util.query.ConnectionProvider;

public class TestConnectionProviderFilter {

	private TestingConnection mock;
	private ConnectionProviderFilter filter;

	@Before
	public void init() throws Exception {
		ResourceFactory rf = new ResourceFactory();
		rf.registerDefaultResource(DataSource.class, new TestingDataSource());
		rf.register();
		mock = new TestingConnection();
		filter = new ConnectionProviderFilter();
	}

	@Test
	public void testRuntimeException() throws Exception {
		doFilter(new RuntimeException());
		assertRollback();
	}

	@Test
	public void testApplicationExceptionRollback() throws Exception {
		doFilter(new ApplicationException("", true));
		assertRollback();
	}

	@Test
	public void testApplicationExceptionCommit() throws Exception {
		doFilter(new ApplicationException("", false));
		assertCommit();
	}

	@Test
	public void testNoException() throws Exception {
		doFilter(null);
		assertCommit();
	}

	private void assertCommit() {
		Assert.assertTrue(mock.commitInvoked);
		Assert.assertFalse(mock.rollbackInvoked);
	}

	private void assertRollback() {
		Assert.assertFalse(mock.commitInvoked);
		Assert.assertTrue(mock.rollbackInvoked);
	}

	private void doFilter(RuntimeException e) {
		try {
			filter.doFilter(null, null, new TestingChain(e));
		} catch (Exception ee) {
		}
	}

	class TestingDataSource implements DataSource {

		
		public PrintWriter getLogWriter() throws SQLException {

			return null;
		}

		
		public void setLogWriter(PrintWriter out) throws SQLException {

		}

		
		public void setLoginTimeout(int seconds) throws SQLException {

		}

		
		public int getLoginTimeout() throws SQLException {

			return 0;
		}

		
		public Logger getParentLogger() throws SQLFeatureNotSupportedException {

			return null;
		}

		
		public <T> T unwrap(Class<T> iface) throws SQLException {

			return null;
		}

		
		public boolean isWrapperFor(Class<?> iface) throws SQLException {

			return false;
		}

		
		public Connection getConnection() throws SQLException {
			return mock;
		}

		
		public Connection getConnection(String username, String password)
				throws SQLException {

			return null;
		}

	}

	static class TestingChain implements FilterChain {
		private RuntimeException e;

		public TestingChain() {
		}

		public TestingChain(RuntimeException e) {
			this.e = e;
		}

		
		public void doFilter(ServletRequest arg0, ServletResponse arg1)
				throws IOException, ServletException {
			ConnectionProvider.get().getConnection();
			if (e != null) {
				throw e;
			}
		}
	}

	static class TestingConnection implements Connection {

		boolean commitInvoked;
		boolean rollbackInvoked;

		
		public <T> T unwrap(Class<T> iface) throws SQLException {

			return null;
		}

		
		public boolean isWrapperFor(Class<?> iface) throws SQLException {

			return false;
		}

		
		public Statement createStatement() throws SQLException {

			return null;
		}

		
		public PreparedStatement prepareStatement(String sql)
				throws SQLException {

			return null;
		}

		
		public CallableStatement prepareCall(String sql) throws SQLException {

			return null;
		}

		
		public String nativeSQL(String sql) throws SQLException {

			return null;
		}

		
		public void setAutoCommit(boolean autoCommit) throws SQLException {

		}

		
		public boolean getAutoCommit() throws SQLException {

			return false;
		}

		
		public void commit() throws SQLException {
			commitInvoked = true;

		}

		
		public void rollback() throws SQLException {
			rollbackInvoked = true;

		}

		
		public void close() throws SQLException {

		}

		
		public boolean isClosed() throws SQLException {

			return false;
		}

		
		public DatabaseMetaData getMetaData() throws SQLException {

			return null;
		}

		
		public void setReadOnly(boolean readOnly) throws SQLException {

		}

		
		public boolean isReadOnly() throws SQLException {

			return false;
		}

		
		public void setCatalog(String catalog) throws SQLException {

		}

		
		public String getCatalog() throws SQLException {

			return null;
		}

		
		public void setTransactionIsolation(int level) throws SQLException {

		}

		
		public int getTransactionIsolation() throws SQLException {

			return 0;
		}

		
		public SQLWarning getWarnings() throws SQLException {

			return null;
		}

		
		public void clearWarnings() throws SQLException {

		}

		
		public Statement createStatement(int resultSetType,
				int resultSetConcurrency) throws SQLException {

			return null;
		}

		
		public PreparedStatement prepareStatement(String sql,
				int resultSetType, int resultSetConcurrency)
				throws SQLException {

			return null;
		}

		
		public CallableStatement prepareCall(String sql, int resultSetType,
				int resultSetConcurrency) throws SQLException {

			return null;
		}

		
		public Map<String, Class<?>> getTypeMap() throws SQLException {

			return null;
		}

		
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {

		}

		
		public void setHoldability(int holdability) throws SQLException {

		}

		
		public int getHoldability() throws SQLException {

			return 0;
		}

		
		public Savepoint setSavepoint() throws SQLException {

			return null;
		}

		
		public Savepoint setSavepoint(String name) throws SQLException {

			return null;
		}

		
		public void rollback(Savepoint savepoint) throws SQLException {

		}

		
		public void releaseSavepoint(Savepoint savepoint) throws SQLException {

		}

		
		public Statement createStatement(int resultSetType,
				int resultSetConcurrency, int resultSetHoldability)
				throws SQLException {

			return null;
		}

		
		public PreparedStatement prepareStatement(String sql,
				int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {

			return null;
		}

		
		public CallableStatement prepareCall(String sql, int resultSetType,
				int resultSetConcurrency, int resultSetHoldability)
				throws SQLException {

			return null;
		}

		
		public PreparedStatement prepareStatement(String sql,
				int autoGeneratedKeys) throws SQLException {

			return null;
		}

		
		public PreparedStatement prepareStatement(String sql,
				int[] columnIndexes) throws SQLException {

			return null;
		}

		
		public PreparedStatement prepareStatement(String sql,
				String[] columnNames) throws SQLException {

			return null;
		}

		
		public Clob createClob() throws SQLException {

			return null;
		}

		
		public Blob createBlob() throws SQLException {

			return null;
		}

		
		public NClob createNClob() throws SQLException {

			return null;
		}

		
		public SQLXML createSQLXML() throws SQLException {

			return null;
		}

		
		public boolean isValid(int timeout) throws SQLException {

			return false;
		}

		
		public void setClientInfo(String name, String value)
				throws SQLClientInfoException {

		}

		
		public void setClientInfo(Properties properties)
				throws SQLClientInfoException {

		}

		
		public String getClientInfo(String name) throws SQLException {

			return null;
		}

		
		public Properties getClientInfo() throws SQLException {

			return null;
		}

		
		public Array createArrayOf(String typeName, Object[] elements)
				throws SQLException {

			return null;
		}

		
		public Struct createStruct(String typeName, Object[] attributes)
				throws SQLException {

			return null;
		}

		
		public void setSchema(String schema) throws SQLException {

		}

		
		public String getSchema() throws SQLException {

			return null;
		}

		
		public void abort(Executor executor) throws SQLException {

		}

		
		public void setNetworkTimeout(Executor executor, int milliseconds)
				throws SQLException {

		}

		
		public int getNetworkTimeout() throws SQLException {
			return 0;
		}

	}

}
