package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.game.GameArenaData;
import nl.communitynetwerk.winakgames.utils.Util;
import org.bukkit.Location;

public class SetExitCMD extends BaseCMD {

	public SetExitCMD() {
		forcePlayer = true;
		cmdName = "setexit";
		forceInGame = false;
		argLength = 2;
		usage = "<arena-name>";
	}

	@Override
	public boolean run() {
		Location loc = player.getLocation();
		String stringLoc = player.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ() + ":" + loc.getYaw() + ":" + loc.getPitch();
		if (args[1].equalsIgnoreCase("all")) {
			plugin.getArenaConfig().getConfig().set("global-exit-location", stringLoc);
			Util.scm(player, lang.cmd_exit_set + " " + stringLoc.replace(":", "&6,&c "));
			for (Game game : plugin.getGames())
				game.getGameArenaData().setExit(loc);
		} else {
			Game game = gameManager.getGame(args[1]);
			GameArenaData gameArenaData = game.getGameArenaData();
			if (game == null) {
				Util.scm(player, lang.cmd_delete_noexist);
				return true;
			}
			plugin.getArenaConfig().getConfig().set("arenas." + gameArenaData.getName() + ".exit-location", stringLoc);
			String msg = lang.cmd_exit_set_arena.replace("<arena>", gameArenaData.getName());
			Util.scm(player, msg + " " + stringLoc.replace(":", "&6,&c "));
			gameArenaData.setExit(loc);
		}
		plugin.getArenaConfig().saveCustomConfig();

		return true;
	}
}
