package nl.communitynetwerk.winakgames.events;

import nl.communitynetwerk.winakgames.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

public class PlayerDeathGameLIST extends PlayerDeathEvent {

	private static final HandlerList handlers = new HandlerList();
	private final Game game;

	public PlayerDeathGameLIST(@NotNull Player player, @Nullable String deathMessage, @NotNull Game game) {
		super(player, Collections.emptyList(), 0, deathMessage);
		this.game = game;
	}

	/**
	 * Get the game the player died in
	 *
	 * @return Game player died in
	 */
	public Game getGame() {
		return game;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@NotNull
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
