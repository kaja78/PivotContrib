/**
 * 
 */
package pivot_contrib.util.launcher;

import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

import pivot_contrib.di.BeanInjector;
import pivot_contrib.util.serializer.InjectingSerializer;

/**
 * @author khubl
 * 
 */
public class Launcher extends Application.Adapter {
	private static Class<? extends Application> applicationClass = Launcher.class;
	private static String bxmlResourceName;
	private static Component component;
	private static Map<String, Object> namespace;
	private static Object syncToken = new Object();

	public static Class<? extends Application> getApplicationClass() {
		return applicationClass;
	}

	public static void setApplicationClass(
			Class<? extends Application> applicationClass) {
		Launcher.applicationClass = applicationClass;
	}

	public static void setBxmlResourceName(String bxmlResourceName) {
		Launcher.bxmlResourceName = bxmlResourceName;
	}

	public static Map<String, Object> launch(String bxmlResourceName) {
		return launch(bxmlResourceName, new LauncherListener.Adapter());
	}

	public static Map<String, Object> launch(String bxmlResourceName,
			LauncherListener listener) {
		Launcher.setBxmlResourceName(bxmlResourceName);
		listener.beforeLaunch();
		DesktopApplicationContext.main(Launcher.getApplicationClass(),
				new String[] { "--preserveSplashScreen=true" });
		waitForApplicationStartup();
		listener.afterLaunch();
		return namespace;
	}

	private static void waitForApplicationStartup() {
		try {
			synchronized (syncToken) {
				syncToken.wait();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public Launcher() {
		BeanInjector.getBeanInjector().injectDependencies(this);
	}

	public void startup(final Display display, Map<String, String> properties)
			throws Exception {
		Window window = createWindow();
		window.open(display);
		synchronized (syncToken) {
			syncToken.notifyAll();
		}
		

		java.awt.Window awtWindow = display.getHostWindow();
		awtWindow.setVisible(true);
		awtWindow.toFront();
	}

	private Window createWindow() {
		Component component = createComponent();
		if (component instanceof Window) {
			return (Window) component;
		}
		Window window = new Window();
		window.setContent(component);
		window.setMaximized(true);
		return window;
	}

	private Component createComponent() {
		try {
			InjectingSerializer serializer = new InjectingSerializer();
			Component component = (Component) serializer.readObject(
					this.getClass(), Launcher.getBxmlResourceName());
			Launcher.namespace = serializer.getNamespace();
			Launcher.component = component;
			return component;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String getBxmlResourceName() {
		return bxmlResourceName;
	}

	public static Map<String, Object> getNamespace() {
		return namespace;
	}

	public static Component getComponent() {
		return component;
	}
}
