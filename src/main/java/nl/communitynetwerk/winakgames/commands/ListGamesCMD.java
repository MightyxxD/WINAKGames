package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.game.GameArenaData;
import nl.communitynetwerk.winakgames.utils.Util;

public class ListGamesCMD extends BaseCMD {

	public ListGamesCMD() {
		forcePlayer = false;
		cmdName = "listgames";
		forceInGame = false;
		argLength = 1;
	}

	@Override
	public boolean run() {
		Util.scm(sender, "&6&l Games:");
		for (Game game : plugin.getGames()) {
			GameArenaData gameArenaData = game.getGameArenaData();
			Util.scm(sender, " &4 - &6" + gameArenaData.getName() + "&4:&6" + gameArenaData.getStatus().getName());
		}
		return true;
	}
}
