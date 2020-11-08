package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.utils.Util;

public class ListCMD extends BaseCMD {

	public ListCMD() {
		forcePlayer = true;
		cmdName = "list";
		forceInGame = true;
		argLength = 1;
	}

	@Override
	public boolean run() {
		StringBuilder p = new StringBuilder();
		Game g = playerManager.getGame(player);
		for (String s : Util.convertUUIDListToStringList(g.getGamePlayerData().getPlayers())) {
			p.append("&6, &c").append(s);
		}
		p = new StringBuilder(p.substring(3));
		Util.scm(player, "&6Players:" + p);
		return true;
	}
}
