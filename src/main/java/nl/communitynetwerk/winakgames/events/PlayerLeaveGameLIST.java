package nl.communitynetwerk.winakgames.events;

import nl.communitynetwerk.winakgames.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveGameLIST extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Game game;
	private final Player player;
	private final boolean death;

	public PlayerLeaveGameLIST(Game game, Player player, boolean death) {
		this.game = game;
		this.player = player;
		this.death = death;
	}

	/** Get the game the player left
	 * @return The game the player left
	 */
	public Game getGame() {
		return this.game;
	}

	/** Get the player that left the game
	 * @return The player that left the game
	 */
	public Player getPlayer() {
		return this.player;
	}

	/** Check if the player died when they left the game
	 * @return If the player died when they left the game
	 */
	public boolean getDied() {
		return death;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
