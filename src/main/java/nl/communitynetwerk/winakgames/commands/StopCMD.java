package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.Status;
import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.utils.Util;

public class StopCMD extends BaseCMD {

	public StopCMD() {
		forcePlayer = false;
		cmdName = "stop";
		forceInGame = false;
		argLength = 2;
		usage = "<game>";
	}

	@Override
	public boolean run() {
		if (args[1].equalsIgnoreCase("all")) {
			for (Game game : plugin.getGames()) {
				Status status = game.getGameArenaData().getStatus();
				if (status == Status.RUNNING || status == Status.WAITING || status == Status.BEGINNING || status == Status.COUNTDOWN) {
					game.stop(false);
				}
			}
			Util.scm(sender, lang.cmd_stop_all);
			return true;
		}
		Game g = gameManager.getGame(args[1]);
		if (g != null) {
			g.stop(false);
			Util.scm(sender, lang.cmd_stop_arena.replace("<arena>", args[1]));
		} else {
			Util.scm(sender, lang.cmd_stop_noexist.replace("<arena>", args[1]));
		}
		return true;
	}
}
