package nl.communitynetwerk.winakgames.tasks;

import nl.communitynetwerk.winakgames.Status;
import nl.communitynetwerk.winakgames.WINAKGames;
import nl.communitynetwerk.winakgames.data.Config;
import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.game.GameArenaData;
import org.bukkit.Bukkit;

import java.util.Objects;

public class TimerTASK implements Runnable {

	private int timer = 0;
	private int remainingtime;
	private final int teleportTimer;
	private final int borderCountdownStart;
	private final int borderCountdownEnd;
	private final int id;
	private final Game game;

	public TimerTASK(Game g, int time) {
		this.remainingtime = time;
		this.game = g;
		this.teleportTimer = Config.teleportEndTime;
		this.borderCountdownStart = g.getGameBorderData().getBorderTimer().get(0);
		this.borderCountdownEnd = g.getGameBorderData().getBorderTimer().get(1);
		g.getGamePlayerData().getPlayers().forEach(uuid -> Objects.requireNonNull(Bukkit.getPlayer(uuid)).setInvulnerable(false));

		this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(WINAKGames.getPlugin(), this, 0, 30 * 20L);
	}

	@Override
	public void run() {
		GameArenaData gameArenaData = game.getGameArenaData();
		if (game == null || gameArenaData.getStatus() != Status.RUNNING) stop(); //A quick null check!


		if (Config.bossbar) game.getGameBarData().bossbarUpdate(remainingtime);

		if (Config.borderEnabled && remainingtime == borderCountdownStart) {
			int closingIn = remainingtime - borderCountdownEnd;
			game.getGameBorderData().setBorder(closingIn);
			game.getGamePlayerData().msgAll(WINAKGames.getPlugin().getLang().game_border_closing.replace("<seconds>", String.valueOf(closingIn)));
		}

		if (gameArenaData.getChestRefillTime() > 0 && remainingtime == gameArenaData.getChestRefillTime()) {
			game.getGameBlockData().refillChests();
			game.getGamePlayerData().msgAll(WINAKGames.getPlugin().getLang().game_chest_refill);
		}

		int refillRepeat = gameArenaData.getChestRefillRepeat();
		if (refillRepeat > 0 && timer % refillRepeat == 0) {
			game.getGameBlockData().refillChests();
			game.getGamePlayerData().msgAll(WINAKGames.getPlugin().getLang().game_chest_refill);
		}

		if (remainingtime == teleportTimer && Config.teleportEnd) {
			game.getGamePlayerData().msgAll(WINAKGames.getPlugin().getLang().game_almost_over);
			game.getGamePlayerData().respawnAll();
		} else if (this.remainingtime < 10) {
			stop();
			game.stop(false);
		} else {
			if (!Config.bossbar) {
				int minutes = this.remainingtime / 60;
				int asd = this.remainingtime % 60;
				if (minutes != 0) {
					if (asd == 0)
						game.getGamePlayerData().msgAll(WINAKGames.getPlugin().getLang().game_ending_min.replace("<minutes>", String.valueOf(minutes)));
					else

						game.getGamePlayerData().msgAll(WINAKGames.getPlugin().getLang().game_ending_minsec.replace("<minutes>", String.valueOf(minutes)).replace("<seconds>", String.valueOf(asd)));
				} else game.getGamePlayerData().msgAll(WINAKGames.getPlugin().getLang().game_ending_sec.replace("<seconds>", String.valueOf(this.remainingtime)));
			}
		}
		remainingtime = (remainingtime - 30);
		timer += 30;
	}

	public void stop() {
		Bukkit.getScheduler().cancelTask(id);
	}
}
