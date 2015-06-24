package pivot_contrib.util.launcher;

public abstract class Launch {

	public final void launch(String bxmlPath) {
		config();
		Launcher.launch(bxmlPath);
	}
	
	public final void launch(String bxmlPath,LauncherListener launcherListener) {
		config();
		Launcher.launch(bxmlPath,launcherListener);
	}

	protected abstract void config();

}
