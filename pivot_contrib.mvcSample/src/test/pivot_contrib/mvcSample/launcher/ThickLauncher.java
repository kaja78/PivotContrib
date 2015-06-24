package pivot_contrib.mvcSample.launcher;

import javax.sql.DataSource;

import org.hsqldb.jdbc.jdbcDataSource;

import pivot_contrib.di.BeanFactoryBuilder;
import pivot_contrib.di.ResourceFactory;
import pivot_contrib.util.launcher.Launcher;

public class ThickLauncher {

	public static void main(String[] args) {

		jdbcDataSource ds = new jdbcDataSource();
		ds.setDatabase("jdbc:hsqldb:mem:aname");
		ds.setUser("SA");
		
		ResourceFactory resourceFactory = (ResourceFactory)BeanFactoryBuilder.getBeanFactory(ResourceFactory.class);				
		resourceFactory.registerDefaultResource(DataSource.class, ds);

		Launcher.launch("/pivot_contrib/mvcSample/SampleView.bxml");
	}

}
