package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.Status;
import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.utils.Util;

public class ChestRefillNowCMD extends BaseCMD {

	public ChestRefillNowCMD() {
		forcePlayer = false;
		cmdName = "chestrefillnow";
		forceInGame = false;
		argLength = 2;
		usage = "<arena-name>";
	}

	@Override
	public boolean run() {
		Game game = gameManager.getGame(args[1]);
		if (game != null) {
			if (game.getGameArenaData().getStatus() != Status.RUNNING) {
				Util.scm(sender, lang.listener_not_running);
				return true;
			}
			game.getGameBlockData().refillChests();
			Util.scm(sender, lang.cmd_chest_refill_now.replace("<arena>", game.getGameArenaData().getName()));
		} else {
			Util.scm(sender, lang.cmd_delete_noexist);
		}
		return true;
	}
}
