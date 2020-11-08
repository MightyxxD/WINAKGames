package nl.communitynetwerk.winakgames.events;

import nl.communitynetwerk.winakgames.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;

public class GameEndLIST extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final Game game;
	private final Collection<Player> winners;
	private final boolean death;

	public GameEndLIST(Game game, Collection<Player> winners, boolean death) {
		this.game = game;
		this.winners = winners;
		this.death = death;
	}

	/** Get the game that ended
	 * @return Game that ended
	 */
	public Game getGame() {
		return this.game;
	}

	/** Get the winners of this game
	 * @return Winners of the game
	 */
	public Collection<Player> getWinners() {
		return this.winners;
	}

	/** Get whether or not the game ended by death
	 * @return True if the game finished by the result of death, false if the game was force stopped
	 */
	public boolean byDeath() {
		return this.death;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
