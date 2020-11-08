package nl.communitynetwerk.winakgames.events;

import nl.communitynetwerk.winakgames.game.Game;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChestOpenLIST extends Event {

	private final Game game;
	private final Block block;
	private final boolean bonus;

	/** Create a new player open chest event
	 * @param game The game this is happening in
	 * @param block The block that is opening
	 * @param bonus If the chest is a bonus chest
	 */
	public ChestOpenLIST(Game game, Block block, boolean bonus) {
		this.game = game;
		this.block = block;
		this.bonus = bonus;
	}

	/** Get the game in this event
	 * @return The game for this event
	 */
	public Game getGame() {
		return game;
	}

	/** Get the chest that has been opened
	 * @return The chest in the event
	 */
	public Block getChest() {
		return block;
	}

	/** Check if this chest is a bonus chest
	 * @return True if bonus chest
	 */
	public boolean isBonus() {
		return bonus;
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
