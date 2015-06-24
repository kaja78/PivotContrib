package pivot_contrib.util.launcher;

import pivot_contrib.rmi.RemoteServiceProxyFactory;

public class LaunchThin extends Launch {
	
	private String rmiEndpoint;
	
	public LaunchThin(String rmiEndpoint) {
		super();
		this.rmiEndpoint = rmiEndpoint;
	}

	public static void main(String args[]) {
		String bxmlPath=args[0];
		String rmiEndpointUrl=args[1];
		launch(bxmlPath,rmiEndpointUrl);
	}
	
	public static void launch(String bxmlPath,String rmiEndpoint) {
		new LaunchThin(rmiEndpoint).launch(bxmlPath);
	}
	
	
	protected void config() {
		new RemoteServiceProxyFactory(rmiEndpoint).register();
	}
}
