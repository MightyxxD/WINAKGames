package nl.communitynetwerk.winakgames.game;

import nl.communitynetwerk.winakgames.WINAKGames;
import nl.communitynetwerk.winakgames.data.Language;

public abstract class Data {

	final Game game;
	final WINAKGames plugin;
	final Language lang;

	protected Data(Game game) {
		this.game = game;
		this.plugin = game.plugin;
		this.lang = game.lang;
	}

	/**
	 * Get the {@link Game} this data belongs to
	 *
	 * @return Game this data belongs to
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Quick method to access the main plugin
	 *
	 * @return Instance of {@link HG plugin}
	 */
	public WINAKGames getPlugin() {
		return plugin;
	}
}
