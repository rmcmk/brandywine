package me.ryleykimmel.brandywine.game.update.task;

/**
 * Represents some task that may be performed by some {@link Updater}.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public interface UpdateTask extends Runnable {

	/**
	 * Invoked when an exception is thrown during execution of this UpdateTask.
	 * 
	 * @param cause The cause of the exception.
	 */
	default void exceptionCaught(Throwable cause) {
		// Method intended to be overridden.
	}

}