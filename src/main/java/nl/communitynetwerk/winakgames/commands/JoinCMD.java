package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.WINAKGames;
import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.utils.Util;

public class JoinCMD extends BaseCMD {

	public JoinCMD() {
		forcePlayer = true;
		cmdName = "join";
		forceInGame = false;
		argLength = 2;
		usage = "<arena-name>";
	}

	@Override
	public boolean run() {

		if (playerManager.hasPlayerData(player) || playerManager.hasSpectatorData(player)) {
			Util.scm(player, WINAKGames.getPlugin().getLang().cmd_join_in_game);
		} else {
			Game g = gameManager.getGame(args[1]);
			if (g != null && !g.getGamePlayerData().getPlayers().contains(player.getUniqueId())) {
				g.getGamePlayerData().join(player);
			} else {
				Util.scm(player, lang.cmd_delete_noexist);
			}
		}
		return true;
	}
}
