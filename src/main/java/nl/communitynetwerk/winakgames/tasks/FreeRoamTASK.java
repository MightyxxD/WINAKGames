package nl.communitynetwerk.winakgames.tasks;

import nl.communitynetwerk.winakgames.WINAKGames;
import nl.communitynetwerk.winakgames.data.Language;
import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FreeRoamTASK implements Runnable {

	private final Game game;
	private final int id;
	private final int roamTime;

	public FreeRoamTASK(Game game) {
		this.game = game;
		this.roamTime = game.getGameArenaData().getRoamTime();

		Language lang = WINAKGames.getPlugin().getLang();
		String gameStarted = lang.roam_game_started;
		String roamTimeString = lang.roam_time.replace("<roam>", "" + roamTime);

		for (UUID u : game.getGamePlayerData().getPlayers()) {
			Player player = Bukkit.getPlayer(u);
			if (player != null) {
				Util.scm(player, gameStarted);
				if (roamTime > 0) {
					Util.scm(player, roamTimeString);
				}
				player.setHealth(20);
				player.setFoodLevel(20);
				game.getGamePlayerData().unFreeze(player);
			}
		}
		this.id = Bukkit.getScheduler().scheduleSyncDelayedTask(WINAKGames.getPlugin(), this, roamTime * 20L);
	}

	@Override
	public void run() {
		if (roamTime > 0) {
			game.getGamePlayerData().msgAll(WINAKGames.getPlugin().getLang().roam_finished);
		}
		game.startGame();
	}

	public void stop() {
		Bukkit.getScheduler().cancelTask(id);
	}
}
