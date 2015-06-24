package pivot_contrib.mvcSample.launcher;

import pivot_contrib.rmi.RemoteServiceProxyFactory;
import pivot_contrib.util.launcher.Launcher;

public class ThinLauncher {

	public static void main(String[] args) {
		new RemoteServiceProxyFactory("http://localhost:8080/pivot_contrib.mvcSample-server/rmi").register();
		Launcher.launch("/pivot_contrib/mvcSample/SampleView.bxml");
	}

}
