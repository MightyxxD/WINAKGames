package nl.communitynetwerk.winakgames.tasks;

import nl.communitynetwerk.winakgames.WINAKGames;
import nl.communitynetwerk.winakgames.data.Config;
import nl.communitynetwerk.winakgames.game.Bound;
import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.listeners.ChestDropLIST;
import nl.communitynetwerk.winakgames.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChestDropTASK implements Runnable {

	private final Game game;
	private final int timerID;
	private final List<ChestDropLIST> chests = new ArrayList<>();

	public ChestDropTASK(Game game) {
		this.game = game;
		timerID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(WINAKGames.getPlugin(), this, Config.randomChestInterval, Config.randomChestInterval);
	}

	@Override
	public void run() {
		Bound bound = game.getGameArenaData().getBound();
		Integer[] i = bound.getRandomLocs();

		int x = i[0];
		int y = i[1];
		int z = i[2];
		World w = bound.getWorld();

		while (w.getBlockAt(x, y, z).getType() == Material.AIR) {
			y--;

			if (y <= 0) {
				i = bound.getRandomLocs();

				x = i[0];
				y = i[1];
				z = i[2];
			}
		}

		y = y + 10;

		Location l = new Location(w, x, y, z);

		FallingBlock fb = w.spawnFallingBlock(l, Bukkit.getServer().createBlockData(Material.STRIPPED_SPRUCE_WOOD));

		chests.add(new ChestDropLIST(fb));

		for (UUID u : game.getGamePlayerData().getPlayers()) {
			Player p = Bukkit.getPlayer(u);
			if (p != null) {
				Util.scm(p, WINAKGames.getPlugin().getLang().chest_drop_1);
				Util.scm(p, WINAKGames.getPlugin().getLang().chest_drop_2
						.replace("<x>", String.valueOf(x))
						.replace("<y>", String.valueOf(y))
						.replace("<z>", String.valueOf(z)));
				Util.scm(p, WINAKGames.getPlugin().getLang().chest_drop_1);
			}
		}
	}

	public void shutdown() {
		Bukkit.getScheduler().cancelTask(timerID);
		for (ChestDropLIST cd : chests) {
			if (cd != null) cd.remove();
		}
	}
}
