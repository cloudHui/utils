package threadtutil.timer;

public interface Runner<T> {
	/**
	 * @return 是否完成(true 完成后 销毁任务 false 完成任务后继续任务)
	 */
	boolean run(T var1) throws Exception;
}
