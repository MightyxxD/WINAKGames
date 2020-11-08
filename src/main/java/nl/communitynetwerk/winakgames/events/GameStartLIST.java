package nl.communitynetwerk.winakgames.events;

import nl.communitynetwerk.winakgames.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartLIST extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Game game;

	public GameStartLIST(Game game) {
		this.game = game;
	}

	/** Get the game involved in this event
	 * @return The game
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
}
