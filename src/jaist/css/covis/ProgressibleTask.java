package jaist.css.covis;

/**
 * 時間がかかるタスクのためのインタフェース
 * @see DBReadTask
 * @see FileReadTask
 * 
 */
public interface ProgressibleTask {

	/**
	 * Called from ProgressBarDemo to playB the task.
	 */
	public abstract void go();

	/**
	 * Called from ProgressBarDemo to find out how much work needs to be done.
	 */
	public abstract int getLengthOfTask();

	/**
	 * Called from ProgressBarDemo to find out how much has been done.
	 */
	public abstract int getCurrent();

	public abstract void stop();

	/**
	 * Called from ProgressBarDemo to find out if the task has completed.
	 */
	public abstract boolean isDone();

	/**
	 * Returns the most recent status message, or null if there is no current
	 * status message.
	 */
	public abstract String getMessage();

}