package pivot_contrib.rmiServer;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ServicesExplorer {

	public void traceServices() {
		ZipInputStream in = new ZipInputStream(
				RMIRequestContext
						.getRMIRequestContext()
						.getRequest()
						.getSession()
						.getServletContext()
						.getResourceAsStream(
								"/WEB-INF/lib/pivot-extensions-common.jar"));
		try {
			ZipEntry entry = in.getNextEntry();
			while (entry != null) {
				System.out.println(entry.getName());
				entry = in.getNextEntry();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	
	}

}
