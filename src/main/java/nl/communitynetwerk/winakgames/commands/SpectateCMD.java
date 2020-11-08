package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.Status;
import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.game.GamePlayerData;
import nl.communitynetwerk.winakgames.utils.Util;

public class SpectateCMD extends BaseCMD {

	public SpectateCMD() {
		forcePlayer = true;
		cmdName = "spectate";
		forceInGame = false;
		argLength = 2;
		usage = "<arena-name>";
	}

	@Override
	public boolean run() {
		if (playerManager.hasPlayerData(player) || playerManager.hasSpectatorData(player)) {
			Util.scm(player, lang.cmd_join_in_game);
		} else {
			Game game = gameManager.getGame(args[1]);
			GamePlayerData gamePlayerData = game.getGamePlayerData();
			if (game != null && !gamePlayerData.getPlayers().contains(player.getUniqueId()) && !gamePlayerData.getSpectators().contains(player.getUniqueId())) {
				Status status = game.getGameArenaData().getStatus();
				if (status == Status.RUNNING || status == Status.BEGINNING) {
					gamePlayerData.spectate(player);
				} else {
					Util.scm(player, "This game is not running, status: " + status);
				}
			} else {
				Util.scm(player, lang.cmd_delete_noexist);
			}
		}
		return true;
	}
}
