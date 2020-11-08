package nl.communitynetwerk.winakgames.events;

import nl.communitynetwerk.winakgames.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinGameLIST extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final Game game;
	private final Player player;
	private boolean isCancelled;

	public PlayerJoinGameLIST(Game game, Player player) {
		this.game = game;
		this.player = player;
		this.isCancelled = false;
	}

	/** Get the player that joined a game
	 * @return The player that joined the game
	 */
	public Player getPlayer() {
		return this.player;
	}

	/** Get the game the player joined
	 * @return The game the player joined
	 */
	public Game getGame() {
		return this.game;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
}
