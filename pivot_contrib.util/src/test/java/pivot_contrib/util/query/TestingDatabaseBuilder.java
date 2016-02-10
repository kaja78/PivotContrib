package pivot_contrib.util.query;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSource;

import pivot_contrib.di.BeanFactoryBuilder;
import pivot_contrib.di.ResourceFactory;

public class TestingDatabaseBuilder {
	

	
	public static void buildDatabase() {
		try {
			createDatabase();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static void createDatabase() throws SQLException {
		JDBCDataSource ds= new JDBCDataSource();
		ds.setDatabase("jdbc:hsqldb:mem:aname");
		ds.setUser("SA");
		Connection c=ds.getConnection();
		c.createStatement().execute("CREATE TABLE CUSTOMER(NAME VARCHAR(30),LOCATION VARCHAR(30))");
		c.createStatement().execute("INSERT INTO CUSTOMER VALUES ('Joe','Prague')");
		c.createStatement().execute("INSERT INTO CUSTOMER VALUES ('Boris','London')");
		//c.createStatement().execute("CREATE FUNCTION addition (a  INT, b  INT) RETURNS INT RETURN a+b");
		((ResourceFactory)BeanFactoryBuilder.getBeanFactory(ResourceFactory.class)).registerDefaultResource(DataSource.class, ds);
	}
	
	public static void dropDatabase() {
		try {
		ConnectionProvider.get().getConnection().createStatement().execute("DROP TABLE CUSTOMER");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		ConnectionProvider.get().closeConnection();		
	}

}
