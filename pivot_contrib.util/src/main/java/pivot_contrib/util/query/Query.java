package pivot_contrib.util.query;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pivot_contrib.di.BeanInjector;

/**
 * Represents SQL query or stored procedure call.
 * 
 * Parameter binding - by default all parameter place holder characters (?) in
 * query are bound. By setting the parameterBindingEnabled property to false,
 * all parameter place holders will be replaced by parameter values. This can be
 * useful for dynamic SQL templates or better execution plan of analytical
 * queries.
 * */
public class Query {
	private String template;
	private boolean parameterBindingEnabled = true;
	private List<?> parameters = new ArrayList<Object>();
	private int maxRows = 0;

	public static Query create(String template) {
		Query query = new Query();
		BeanInjector.getBeanInjector().injectDependencies(query);
		query.setTemplate(template);
		return query;
	}

	public static Query load(String resourceName) {
		Query query = new Query();
		BeanInjector.getBeanInjector().injectDependencies(query);
		query.loadTemplateFromResource(resourceName);
		return query;
	}

	public boolean isParameterBindingEnabled() {
		return parameterBindingEnabled;
	}

	public void setParameterBindingEnabled(boolean bindParametersEnabled) {
		this.parameterBindingEnabled = bindParametersEnabled;
	}

	public GenericQueryResult executeQuery() {
		try {
			return new Executor(maxRows).executeQuery();
		} catch (SQLException e) {
			throw new RuntimeException(
					"Exception executing query: " + template, e);
		}
	}

	public int executeUpdate() {
		try {
			return new Executor(maxRows).executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Exception executing statement: "
					+ template, e);
		}
	}

	public <VO> QueryResult<VO> executeQuery(Class<VO> valueObjectType) {
		try {
			return new Executor(maxRows).executeQuery(valueObjectType);
		} catch (SQLException e) {
			throw new RuntimeException(
					"Exception executing query: " + template, e);
		}
	}

	public void executeCall() {
		try {
			new Executor(maxRows).executeCall();
		} catch (SQLException e) {
			throw new RuntimeException("Exception executing call: " + template,
					e);
		}
	}

	private class Executor {
		private final int maxRows;

		public Executor(int maxRows) {
			this.maxRows = maxRows;
		}

		public <VO> QueryResult<VO> executeQuery(Class<VO> valueObjectType)
				throws SQLException {
			Connection c = getConnection();
			PreparedStatement stmt = prepareStatement(c);
			stmt.setMaxRows(this.maxRows);
			ResultSet res = null;
			try {
				res = stmt.executeQuery();
				QueryResult<VO> queryResult = new QueryResult<VO>(res,
						valueObjectType);
				return queryResult;
			} finally {
				if (res != null) {
					res.close();
				}
				stmt.close();
			}
		}

		public GenericQueryResult executeQuery() throws SQLException {
			Connection c = getConnection();
			PreparedStatement stmt = prepareStatement(c);
			stmt.setMaxRows(this.maxRows);
			ResultSet res = null;
			try {
				res = stmt.executeQuery();
				GenericQueryResult queryResult = new GenericQueryResult(res);
				return queryResult;
			} finally {
				if (res != null) {
					res.close();
				}
				stmt.close();
			}
		}

		public void executeCall() throws SQLException {
			Connection c = getConnection();
			CallableStatement stmt = prepareCall(c);
			try {
				stmt.execute();
				fetchOutParameters(stmt);
			} finally {
				stmt.close();
			}
		}

		private void fetchOutParameters(CallableStatement stmt)
				throws SQLException {
			int i = 1;
			for (Object parameter : parameters) {
				if (parameter instanceof Parameter) {
					((Parameter) parameter).fetchStatement(stmt, i);
				}
				i++;
			}

		}

		public int executeUpdate() throws SQLException {
			Connection c = getConnection();
			PreparedStatement stmt = prepareStatement(c);
			try {
				return stmt.executeUpdate();
			} finally {
				stmt.close();
			}
		}

	}

	protected Connection getConnection() throws SQLException {
		return ConnectionProvider.get().getConnection();
	}

	private PreparedStatement prepareStatement(Connection c)
			throws SQLException {
		if (isParameterBindingEnabled()) {
			return prepareBindStatement(c);
		} else {
			return prepareDisableBindStatement(c);
		}
	}

	private PreparedStatement prepareDisableBindStatement(Connection c)
			throws SQLException {
		String statement = replaceParameters(template);
		PreparedStatement stmt = c.prepareStatement(statement);
		return stmt;
	}

	private String replaceParameters(String template) {
		String query = template;
		for (Object parameter : parameters) {
			if (!(parameter instanceof String)) {
				throw new RuntimeException(
						"Only string parameters are support for queries with disabled parameter binding.");
			}
			String value = (String) parameter;
			query = query.replaceFirst("\\?", value);
		}
		return query;
	}

	private PreparedStatement prepareBindStatement(Connection c)
			throws SQLException {
		PreparedStatement stmt = c.prepareStatement(template);
		bindParameters(stmt);
		return stmt;
	}

	private void bindParameters(PreparedStatement stmt) throws SQLException {
		int i = 1;
		for (Object parameter : parameters) {
			if (parameter instanceof Parameter) {
				((Parameter) parameter).initStatement(stmt, i);
			} else {
				Object parameterValue = convert(parameter);
				stmt.setObject(i, parameterValue);
			}
			i++;
		}
	}

	private Object convert(Object parameter) {
		if (parameter instanceof Date) {
			return new Timestamp(((Date) parameter).getTime());
		}
		return parameter;
	}

	private CallableStatement prepareCall(Connection c) throws SQLException {
		CallableStatement stmt = c.prepareCall(template);
		bindParameters(stmt);
		return stmt;
	}

	public String getTemplate() {
		return template;
	}

	public Query setTemplate(String template) {
		this.template = template;
		return this;
	}

	public Query loadTemplateFromResource(String resourceName) {
		return loadTemplateFromResource(resourceName, getClass());
	}

	public Query loadTemplateFromResource(String resourceName,
			Class<?> baseClass) {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					baseClass.getResourceAsStream(resourceName), "UTF-8"));
			for (int c = br.read(); c != -1; c = br.read()) {
				sb.append((char) c);
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Unable to load query template from resource "
							+ resourceName, e);
		}
		setTemplate(sb.toString());
		return this;
	}

	public Query setParameters(Object... parameters) {
		this.parameters = Arrays.asList(parameters);
		return this;
	}

	public Query setMaxRows(int maxRows) {
		this.maxRows = maxRows;
		return this;
	}

	public static class Parameter {
		private Object value;
		private int sqlType;

		protected Parameter() {
		}

		public Parameter(Object value, int sqlType) {
			super();
			this.value = value;
			this.sqlType = sqlType;
		}

		public void initStatement(PreparedStatement stmt, int index)
				throws SQLException {
			try {
				stmt.setObject(index, value, sqlType);
			} catch (SQLException e) {
				throw new SQLException("Exception setting parameter " + index
						+ " with value " + value + ".", e);
			}
		}

		public void fetchStatement(CallableStatement stmt, int index)
				throws SQLException {
			// Do nothing for input parameter.
		}

		public Object getValue() {
			return value;
		}

		protected void setValue(Object value) {
			this.value = value;
		}

		public int getSqlType() {
			return sqlType;
		}

		protected void setSqlType(int sqlType) {
			this.sqlType = sqlType;
		}

	}

	public static class OutParameter extends Parameter {

		public OutParameter(int sqlType) {
			setSqlType(sqlType);
		}

		@Override
		public void initStatement(PreparedStatement stmt, int index)
				throws SQLException {
			checkCallableStatement(stmt);
			try {
				((CallableStatement) stmt).registerOutParameter(index,
						getSqlType());
			} catch (SQLException e) {
				throw new SQLException(
						"Exception registering output parameter " + index + ".",
						e);
			}
		}

		protected void checkCallableStatement(PreparedStatement stmt)
				throws SQLException {
			if (!(stmt instanceof CallableStatement)) {
				throw new SQLException(
						"Output parameters are supported only for JDBC calls. Use call... method.");
			}
		}

		@Override
		public void fetchStatement(CallableStatement stmt, int index)
				throws SQLException {
			try {
				setValue(stmt.getObject(index));
			} catch (SQLException e) {
				throw new SQLException(
						"Exception fetching value of output parameter " + index
								+ ".", e);
			}
		}
	}

	public static class OutCursor<T> extends OutParameter {
		private Class<T> rowType;
		private QueryResult<T> result;

		public OutCursor(Class<T> rowType, int sqlType) {
			super(sqlType);
			this.rowType = rowType;
		}

		public OutCursor(Class<T> rowType) {
			super(Types.OTHER);
			this.rowType = rowType;
		}

		@Override
		public void fetchStatement(CallableStatement stmt, int index)
				throws SQLException {
			try {
				ResultSet res = (ResultSet) stmt.getObject(index);
				try {
					result = new QueryResult<T>(res, rowType);
				} finally {
					res.close();
				}
			} catch (SQLException e) {
				throw new SQLException(
						"Exception fetching value of output parameter " + index
								+ ".", e);
			}
		}

		@Override
		public Object getValue() {
			throw new UnsupportedOperationException("Use getResult() method.");
		}

		public QueryResult<T> getResult() {
			return result;
		}

	}
}
