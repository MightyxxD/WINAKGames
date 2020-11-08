package nl.communitynetwerk.winakgames.tasks;

import nl.communitynetwerk.winakgames.WINAKGames;
import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.utils.Util;
import org.bukkit.Bukkit;

public class StartingTASK implements Runnable {

	private int timer;
	private final int id;
	private final Game game;

	public StartingTASK(Game g) {
		this.timer = 30;
		this.game = g;
		String name = g.getGameArenaData().getName();
		Util.broadcast(WINAKGames.getPlugin().getLang().game_started
				.replace("<arena>", name)
				.replace("<seconds>", "" + timer));
		Util.broadcast(WINAKGames.getPlugin().getLang().game_join.replace("<arena>", name));

		this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(WINAKGames.getPlugin(), this, 5 * 20L, 5 * 20L);
	}

	@Override
	public void run() {
		timer = (timer - 5);

		if (timer <= 0) {
			stop();
			game.startFreeRoam();
		} else {
			game.getGamePlayerData().msgAll(WINAKGames.getPlugin().getLang().game_countdown.replace("<timer>", String.valueOf(timer)));
		}
	}

	public void stop() {
		Bukkit.getScheduler().cancelTask(id);
	}
}
