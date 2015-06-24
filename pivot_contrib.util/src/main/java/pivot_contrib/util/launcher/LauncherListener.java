package pivot_contrib.util.launcher;


public interface LauncherListener {
	
	public void beforeLaunch();
	public void afterLaunch();

	public class Adapter implements LauncherListener {

		public void beforeLaunch() {
		}

		public void afterLaunch() {
		}
		
	}

}
