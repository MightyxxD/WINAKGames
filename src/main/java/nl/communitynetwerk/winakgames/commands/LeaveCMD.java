package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.Status;
import nl.communitynetwerk.winakgames.data.Config;
import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.game.GameArenaData;
import nl.communitynetwerk.winakgames.utils.Util;
import nl.communitynetwerk.winakgames.utils.Vault;

public class LeaveCMD extends BaseCMD {

	public LeaveCMD() {
		forcePlayer = true;
		cmdName = "leave";
		forceInGame = true;
		argLength = 1;
	}

	@Override
	public boolean run() {
		Game game;
		if (playerManager.hasPlayerData(player)) {
			game = playerManager.getPlayerData(player).getGame();
			if (Config.economy) {
				GameArenaData gameArenaData = game.getGameArenaData();
				Status status = gameArenaData.getStatus();
				if ((status == Status.WAITING || status == Status.COUNTDOWN) && gameArenaData.getCost() > 0) {
					Vault.economy.depositPlayer(player, gameArenaData.getCost());
					Util.scm(player, lang.prefix +
							lang.cmd_leave_refund.replace("<cost>", String.valueOf(gameArenaData.getCost())));
				}
			}
			game.getGamePlayerData().leave(player, false);
		} else {
			game = playerManager.getSpectatorData(player).getGame();
			game.getGamePlayerData().leaveSpectate(player);
		}
		Util.scm(player, lang.prefix + lang.cmd_leave_left.replace("<arena>", game.getGameArenaData().getName()));
		return true;
	}
}
