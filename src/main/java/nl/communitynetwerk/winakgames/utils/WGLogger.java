package nl.communitynetwerk.winakgames.utils;

import java.util.logging.Logger;

public class WGLogger extends Logger {

	protected WGLogger(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}

	/** Get an instance of HgLogger
	 * @return new instance of HgLogger
	 */
	public static WGLogger getLogger() {
		return new WGLogger("", null);
	}

	@Override
	public void info(String msg) {
		String message = msg.replace("[NBTAPI]", "&7[&bNBT&3API&7]");
		Util.log(message);
	}
}
