package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.game.GameArenaData;
import nl.communitynetwerk.winakgames.utils.Util;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;

import java.util.List;

public class AddSpawnCMD extends BaseCMD {

	public AddSpawnCMD() {
		forcePlayer = true;
		cmdName = "addspawn";
		argLength = 1;
		forceInRegion = true;
	}

	@Override
	public boolean run() {
		Game game = gameManager.getGame(player.getLocation());
		GameArenaData gameArenaData = game.getGameArenaData();
		int num = gameArenaData.getSpawns().size() + 1;
		Configuration c = arenaConfig.getCustomConfig();
		List<String> d = c.getStringList("arenas." + gameArenaData.getName() + ".spawns");
		Location l = player.getLocation();
		for (Location lb : gameArenaData.getSpawns()) {
			if (lb.getBlock().equals(l.getBlock())) {
				Util.sendPrefixedMessage(player, lang.cmd_spawn_same);
				return true;
			}
		}
		d.add(l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ() + ":" + l.getYaw() + ":" + l.getPitch());
		c.set("arenas." + gameArenaData.getName() + ".spawns", d);
		gameArenaData.addSpawn(l);
		arenaConfig.saveCustomConfig();
		Util.sendPrefixedMessage(player, lang.cmd_spawn_set.replace("<number>", String.valueOf(num)));

		gameManager.checkGame(game, player);
		return true;
	}
}
