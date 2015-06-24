package pivot_contrib.util.query;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.reflect.FieldUtils;

public class QueryResult<VO> implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient final ResultSet resultSet;
	public final int columnCount;
	public final String[] columnNames;
	public final String[] columnClassNames;
	public final VO[] rows;
	public final Class<VO> rowType;

	public QueryResult(ResultSet res, Class<VO> rowType) {
		this.resultSet = res;
		this.rowType = rowType;
		try {
			this.columnCount = getColumnCount();
			this.columnNames = getColumnNames();
			checkUniqueColumnNames();
			this.columnClassNames = getColumnClassNames();

			this.rows = getRows();
		} catch (SQLException e) {
			throw new RuntimeException("Exception creating query result.", e);
		}
	}

	private void checkUniqueColumnNames() {
		HashSet<String> columnNameSet = new HashSet<String>(
				Arrays.asList(this.columnNames));
		if (columnNameSet.size() < this.columnCount) {
			throw new RuntimeException(
					"The query returned resultset with non unique column names.");
		}
	}

	public VO getSingleResult() {
		if (rows == null || rows.length == 0) {
			return null;
		} else if (rows.length > 1) {
			throw new RuntimeException("Query returned more than 1 row.");
		}
		return rows[0];
	}

	private int getColumnCount() throws SQLException {
		return resultSet.getMetaData().getColumnCount();
	}

	@SuppressWarnings("unchecked")
	private VO[] getRows() throws SQLException {
		ArrayList<VO> rows = new ArrayList<VO>();
		while (resultSet.next()) {
			rows.add((VO) getRow());
		}
		return rows.toArray((VO[]) Array.newInstance(rowType, rows.size()));
	}

	private Object getRow() throws SQLException {
		if (rowType.equals(ResultMap.class)) {
			return createRowAsMap();
		} else {
			try {
				return createRowObject();
			} catch (Exception e) {
				throw new RuntimeException("Exception creating row of type "
						+ rowType, e);
			}
		}
	}

	private VO createRowObject() throws InstantiationException,
			IllegalAccessException, SecurityException, NoSuchFieldException {
		VO valueObject = rowType.newInstance();
		for (int i = 0; i < columnNames.length; i++) {
			String fieldName = columnNames[i];

			Field field = FieldUtils.getField(rowType, fieldName, true);
			rowType.getDeclaredField(fieldName);
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			try {
				Object value = getValueForField(i+1, field);
				field.set(valueObject, value);
			} catch (SQLException e) {
				throw new RuntimeException(
						"Exception retrieving value of column " + fieldName, e);
			}
		}
		return valueObject;
	}

	private Object getValueForField(int i, Field field) throws SQLException {
		Object value = null;
		Class<?> fieldType = field.getType();

		if (Timestamp.class.getName().equals(fieldType.getName())) {//Fix older oracle jdbc SQL Date mapping bug. (between v9 - v11.1 Oracle DATE is mapped to java.sql.Date which causes loosing of time information.)
			value=resultSet.getTimestamp(i);
		} else {
			Object object = resultSet.getObject(i);
			if (object != null) {
				value = convert(object, fieldType);
			}
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	private <T> T convert(Object object, Class<T> type) {
		return (T) ConvertUtils.convert(object, type);

	}

	private Map<String, Object> createRowAsMap() throws SQLException {
		Map<String, Object> row = new ResultMap(this.columnCount);
		for (int i = 0; i < this.columnCount; i++) {
			Object value = this.resultSet.getObject(i + 1);
			row.put(this.columnNames[i], value);
		}
		return row;
	}

	private String[] getColumnClassNames() throws SQLException {
		String[] r = new String[this.columnCount];
		for (int i = 0; i < this.columnCount; i++) {
			r[i] = this.resultSet.getMetaData().getColumnClassName(i + 1);
		}
		return r;
	}

	private String[] getColumnNames() throws SQLException {
		String[] r = new String[this.columnCount];
		for (int i = 0; i < this.columnCount; i++) {
			r[i] = this.resultSet.getMetaData().getColumnName(i + 1);
		}
		return r;
	}

	public String toText() {
		StringBuffer sb = new StringBuffer();

		for (String columnName : columnNames) {
			sb.append(columnName);
			sb.append('\t');
		}
		sb.append('\n');

		if (rows != null) {
			for (Object row : rows) {
				if (row instanceof Object[]) {
					Object[] columns = (Object[]) row;
					for (Object value : columns) {
						sb.append(value);
						sb.append('\t');
					}
					sb.append('\n');
				}
			}
		}
		return sb.toString();
	}
}
