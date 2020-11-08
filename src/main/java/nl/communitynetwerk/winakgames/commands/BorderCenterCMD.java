package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.utils.Util;
import org.bukkit.Location;

public class BorderCenterCMD extends BaseCMD {

	public BorderCenterCMD() {
		forcePlayer = true;
		cmdName = "bordercenter";
		forceInGame = false;
		argLength = 2;
		usage = "<arena-name>";
	}

	@Override
	public boolean run() {
		Game game = gameManager.getGame(args[1]);
		if (game != null) {
			String name = game.getGameArenaData().getName();
			Location l = player.getLocation();
			assert l.getWorld() != null;
			String loc = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
			arenaConfig.getCustomConfig().set("arenas." + name + ".border.center", loc);
			game.getGameBorderData().setBorderCenter(l);
			arenaConfig.saveCustomConfig();
			Util.scm(player, lang.cmd_border_center.replace("<arena>", name));
		} else {
			Util.scm(player, lang.cmd_delete_noexist);
		}
		return true;
	}
}
