package nl.communitynetwerk.winakgames;

import nl.communitynetwerk.winakgames.data.Language;
import nl.communitynetwerk.winakgames.utils.Util;

public enum Status {

	/**
	 * Game is running
	 */
	RUNNING,
	/**
	 * Game has stopped
	 */
	STOPPED,
	/**
	 * Game is ready to run
	 */
	READY,
	/**
	 * Game is waiting
	 */
	WAITING,
	/**
	 * Game is broken
	 */
	BROKEN,
	/**
	 * Game is currently rolling back blocks
	 */
	ROLLBACK,
	/**
	 * Game is not ready
	 */
	NOTREADY,
	/**
	 * Game is starting to run
	 */
	BEGINNING,
	/**
	 * Game is counting down to start
	 */
	COUNTDOWN;

	Language lang = WINAKGames.getPlugin().getLang();

	public String getName() {
		switch (this) {
			case RUNNING:
				return Util.getColString(lang.status_running);
			case STOPPED:
				return Util.getColString(lang.status_stopped);
			case READY:
				return Util.getColString(lang.status_ready);
			case WAITING:
				return Util.getColString(lang.status_waiting);
			case BROKEN:
				return Util.getColString(lang.status_broken);
			case ROLLBACK:
				return Util.getColString(lang.status_rollback);
			case NOTREADY:
				return Util.getColString(lang.status_not_ready);
			case BEGINNING:
				return Util.getColString(lang.status_beginning);
			case COUNTDOWN:
				return Util.getColString(lang.status_countdown);
			default:
				return Util.getColString("&4&lERROR");
		}
	}
}
