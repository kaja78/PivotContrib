package pivot_contrib.util.task;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.Cursor;
import org.apache.pivot.wtk.TaskAdapter;

import pivot_contrib.util.launcher.Launcher;
import pivot_contrib.util.serializer.ApplicationContextHelper;

public abstract class AbstractTask<T> {
	
	private static AtomicInteger backgroundTaskInProgressCount=new AtomicInteger();

	public abstract T execute();

	public abstract void taskExecuted(T taskResult);

	public void taskFailed(Throwable t) {
		Exception e;
		if (t instanceof Exception) {
			e=(Exception)t;
		} else {
			e=new RuntimeException("Execution of task "+this.getClass().getName()+" failed.",t);
		}
		ApplicationContextHelper.handleUncaughtException(e);
	}

	public void executeInBackground() {
		registerBackgroundTask();
		Task<T> task = new Task<T>() {
			public T execute() {
				return AbstractTask.this.execute();
			}
		};
		TaskListener<T> taskListener = new TaskListener<T>() {
			public void taskExecuted(Task<T> task) {
				unregisterBackgroundTask();
				AbstractTask.this.taskExecuted(task.getResult());

			}

			public void executeFailed(Task<T> task) {
				unregisterBackgroundTask();
				taskFailed(task.getFault());
			}
		};
		task.execute(new TaskAdapter<T>(taskListener));
	}
	
	protected void registerBackgroundTask() {
		backgroundTaskInProgressCount.incrementAndGet();
		if (Launcher.getComponent() != null) {
			Launcher.getComponent().setCursor(Cursor.WAIT);
		}
	}
	
	protected void unregisterBackgroundTask() {
		int currentCount=backgroundTaskInProgressCount.decrementAndGet();
		if (currentCount==0 && Launcher.getComponent() !=null) {
			Launcher.getComponent().setCursor(Cursor.DEFAULT);
		}
	}

}
