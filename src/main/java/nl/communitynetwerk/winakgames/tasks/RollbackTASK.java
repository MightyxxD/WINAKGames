package nl.communitynetwerk.winakgames.tasks;

import nl.communitynetwerk.winakgames.Status;
import nl.communitynetwerk.winakgames.WINAKGames;
import nl.communitynetwerk.winakgames.data.Config;
import nl.communitynetwerk.winakgames.data.ItemFrameData;
import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.game.GameBlockData;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;

import java.util.Iterator;

public class RollbackTASK implements Runnable {

	private final Iterator<BlockState> session;
	private final Iterator<ItemFrameData> itemFrameDataIterator;
	private final Game game;
	private final GameBlockData gameBlockData;
	private final int blocks_per_second;
	private int timerID;

	public RollbackTASK(Game game) {
		this.game = game;
		this.gameBlockData = game.getGameBlockData();
		this.blocks_per_second = Config.blocks_per_second / 10;
		game.getGameArenaData().setStatus(Status.ROLLBACK);
		this.session = gameBlockData.getBlocks().iterator();
		this.itemFrameDataIterator = gameBlockData.getItemFrameData().iterator();
		timerID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WINAKGames.getPlugin(), this, 2);
	}

	@Override
	public void run() {
		int i = 0;
		// Rollback blocks
		while (i < blocks_per_second && session.hasNext()) {
			BlockState state = session.next();
			if (state != null) {
				state.update(true);
			}
			i++;
		}
		if (session.hasNext()) {
			timerID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WINAKGames.getPlugin(), this, 2);
			return;
		}

		// Rollback item frames
		while (itemFrameDataIterator.hasNext()) {
			ItemFrameData data = itemFrameDataIterator.next();
			if (data != null) {
				data.resetItem();
			}
		}

		Bukkit.getServer().getScheduler().cancelTask(timerID);
		gameBlockData.resetBlocks();
		gameBlockData.resetItemFrames();
		game.getGameArenaData().setStatus(Status.READY);
	}

}
