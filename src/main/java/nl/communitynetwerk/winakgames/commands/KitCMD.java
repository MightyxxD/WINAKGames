package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.Status;
import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.utils.Util;

public class KitCMD extends BaseCMD {

	public KitCMD() {
		forcePlayer = true;
		cmdName = "kit";
		forceInGame = true;
		argLength = 2;
		usage = "<kit>";
	}

	@Override
	public boolean run() {
		Game game = playerManager.getPlayerData(player).getGame();
		Status st = game.getGameArenaData().getStatus();
		if (!game.getKitManager().hasKits()) {
			Util.scm(player, lang.kit_disabled);
			return false;
		}
		if (st == Status.WAITING || st == Status.COUNTDOWN) {
			game.getKitManager().setKit(player, args[1]);
		} else {
			Util.scm(player, lang.cmd_kit_no_change);
		}
		return true;
	}
}
