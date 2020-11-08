package nl.communitynetwerk.winakgames.game;

import nl.communitynetwerk.winakgames.Status;
import nl.communitynetwerk.winakgames.WINAKGames;
import nl.communitynetwerk.winakgames.data.Config;
import nl.communitynetwerk.winakgames.data.Language;
import nl.communitynetwerk.winakgames.data.Leaderboard;
import nl.communitynetwerk.winakgames.events.GameEndLIST;
import nl.communitynetwerk.winakgames.events.GameStartLIST;
import nl.communitynetwerk.winakgames.managers.KitMANAGER;
import nl.communitynetwerk.winakgames.managers.MobMANAGER;
import nl.communitynetwerk.winakgames.managers.PlayerMANAGER;
import nl.communitynetwerk.winakgames.tasks.*;
import nl.communitynetwerk.winakgames.utils.Util;
import nl.communitynetwerk.winakgames.utils.Vault;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class Game {

	final WINAKGames plugin;
	final Language lang;

	// Managers
	KitMANAGER kitManager;
	private final MobMANAGER mobManager;
	private final PlayerMANAGER playerManager;

	// Task ID's here!
	private SpawnerTASK spawner;
	private FreeRoamTASK freeRoam;
	private StartingTASK starting;
	private TimerTASK timer;
	private ChestDropTASK chestDrop;

	// Data Objects
	final GameArenaData gameArenaData;
	final GameBarData bar;
	final GamePlayerData gamePlayerData;
	final GameBlockData gameBlockData;
	final GameItemData gameItemData;
	final GameCommandData gameCommandData;
	final GameBorderData gameBorderData;

	/**
	 * Create a new game
	 * <p>Internally used when loading from config on server start</p>
	 *
	 * @param name       Name of this game
	 * @param bound      Bounding region of this game
	 * @param spawns     List of spawns for this game
	 * @param lobbySign  Lobby sign block
	 * @param timer      Length of the game (in seconds)
	 * @param minPlayers Minimum players to be able to start the game
	 * @param maxPlayers Maximum players that can join this game
	 * @param roam       Roam time for this game
	 * @param isReady    If the game is ready to start
	 * @param cost       Cost of this game
	 */
	public Game(String name, Bound bound, List<Location> spawns, Sign lobbySign, int timer, int minPlayers, int maxPlayers, int roam, boolean isReady, int cost) {
		this(name, bound, timer, minPlayers, maxPlayers, roam, cost);
		gameArenaData.spawns.addAll(spawns);
		this.gameBlockData.sign1 = lobbySign;
		gameArenaData.setStatus(isReady ? Status.READY : Status.BROKEN);

		this.gameBlockData.setLobbyBlock(lobbySign);

		this.kitManager = plugin.getKitManager();
	}

	/**
	 * Create a new game
	 * <p>Internally used when creating a game with the <b>/hg create</b> command</p>
	 *
	 * @param name       Name of this game
	 * @param bound      Bounding region of this game
	 * @param timer      Length of the game (in seconds)
	 * @param minPlayers Minimum players to be able to start the game
	 * @param maxPlayers Maximum players that can join this game
	 * @param roam       Roam time for this game
	 * @param cost       Cost of this game
	 */
	public Game(String name, Bound bound, int timer, int minPlayers, int maxPlayers, int roam, int cost) {
		this.plugin = WINAKGames.getPlugin();
		this.gameArenaData = new GameArenaData(this, name, bound, timer, minPlayers, maxPlayers, roam, cost);
		this.gameArenaData.status = Status.NOTREADY;
		this.playerManager = WINAKGames.getPlugin().getPlayerManager();
		this.lang = plugin.getLang();
		this.kitManager = plugin.getKitManager();
		this.mobManager = new MobMANAGER(this);
		this.bar = new GameBarData(this);
		this.gamePlayerData = new GamePlayerData(this);
		this.gameBlockData = new GameBlockData(this);
		this.gameItemData = new GameItemData(this);
		this.gameCommandData = new GameCommandData(this);
		this.gameBorderData = new GameBorderData(this);
		this.gameBorderData.setBorderSize(Config.borderFinalSize);
		this.gameBorderData.setBorderTimer(Config.borderCountdownStart, Config.borderCountdownEnd);
	}

	/**
	 * Get an instance of the GameArenaData
	 *
	 * @return Instance of GameArenaData
	 */
	public GameArenaData getGameArenaData() {
		return gameArenaData;
	}

	/**
	 * Get an instance of the GameBarData
	 *
	 * @return Instance of GameBarData
	 */
	public GameBarData getGameBarData() {
		return bar;
	}

	/**
	 * Get an instance of the GamePlayerData
	 *
	 * @return Instance of GamePlayerData
	 */
	public GamePlayerData getGamePlayerData() {
		return gamePlayerData;
	}

	/**
	 * Get an instance of the GameBlockData
	 *
	 * @return Instance of GameBlockData
	 */
	public GameBlockData getGameBlockData() {
		return gameBlockData;
	}

	/**
	 * Get an instance of the GameItemData
	 *
	 * @return Instance of GameItemData
	 */
	public GameItemData getGameItemData() {
		return gameItemData;
	}

	/**
	 * Get an instance of the GameCommandData
	 *
	 * @return Instance of GameCommandData
	 */
	public GameCommandData getGameCommandData() {
		return gameCommandData;
	}

	/**
	 * Get an instance of the GameBorderData
	 *
	 * @return Instance of GameBorderData
	 */
	public GameBorderData getGameBorderData() {
		return gameBorderData;
	}

	public StartingTASK getStartingTask() {
		return this.starting;
	}

	/**
	 * Get the location of the lobby for this game
	 *
	 * @return Location of the lobby sign
	 */
	public Location getLobbyLocation() {
		return gameBlockData.sign1.getLocation();
	}

	/**
	 * Get the kits for this game
	 *
	 * @return The KitManager kit for this game
	 */
	public KitMANAGER getKitManager() {
		return this.kitManager;
	}

	/**
	 * Set the kits for this game
	 *
	 * @param kit The KitManager kit to set
	 */
	public void setKitManager(KitMANAGER kit) {
		this.kitManager = kit;
	}

	/**
	 * Get this game's MobManager
	 *
	 * @return MobManager for this game
	 */
	public MobMANAGER getMobManager() {
		return this.mobManager;
	}

	/**
	 * Start the pregame countdown
	 */
	public void startPreGame() {
		// Call the GameStartEvent
		GameStartLIST event = new GameStartLIST(this);
		Bukkit.getPluginManager().callEvent(event);

		gameArenaData.status = Status.COUNTDOWN;
		starting = new StartingTASK(this);
		gameBlockData.updateLobbyBlock();
	}

	/**
	 * Start the free roam state of the game
	 */
	public void startFreeRoam() {
		gameArenaData.status = Status.BEGINNING;
		gameBlockData.updateLobbyBlock();
		gameArenaData.bound.removeEntities();
		freeRoam = new FreeRoamTASK(this);
		gameCommandData.runCommands(GameCommandData.CommandType.START, null);
	}

	/**
	 * Start the game
	 */
	public void startGame() {
		gameArenaData.status = Status.RUNNING;
		if (Config.spawnmobs) spawner = new SpawnerTASK(this, Config.spawnmobsinterval);
		if (Config.randomChest) chestDrop = new ChestDropTASK(this);
		gameBlockData.updateLobbyBlock();
		if (Config.bossbar) {
			bar.createBossbar(gameArenaData.timer);
		}
		if (Config.borderEnabled && Config.borderOnStart) {
			gameBorderData.setBorder(gameArenaData.timer);
		}
		timer = new TimerTASK(this, gameArenaData.timer);
	}

	public void cancelTasks() {
		if (spawner != null) spawner.stop();
		if (timer != null) timer.stop();
		if (starting != null) starting.stop();
		if (freeRoam != null) freeRoam.stop();
		if (chestDrop != null) chestDrop.shutdown();
	}

	/**
	 * Stop the game
	 */
	public void stop() {
		stop(false);
	}

	/**
	 * Stop the game
	 *
	 * @param death Whether the game stopped after the result of a death (false = no winnings payed out)
	 */
	public void stop(Boolean death) {
		if (Config.borderEnabled) {
			gameBorderData.resetBorder();
		}
		gameArenaData.bound.removeEntities();
		List<UUID> win = new ArrayList<>();
		cancelTasks();
		for (UUID uuid : gamePlayerData.players) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null) {
				gamePlayerData.heal(player);
				playerManager.getPlayerData(uuid).restore(player);
				playerManager.removePlayerData(uuid);
				win.add(uuid);
				gamePlayerData.exit(player);
			}
		}
		gamePlayerData.clearPlayers();

		for (UUID uuid : gamePlayerData.getSpectators()) {
			Player spectator = Bukkit.getPlayer(uuid);
			if (spectator != null) {
				gamePlayerData.leaveSpectate(spectator);
			}
		}
		gamePlayerData.clearSpectators();
		gamePlayerData.clearTeams();

		if (gameArenaData.status == Status.RUNNING) {
			bar.clearBar();
		}

		if (!win.isEmpty() && death) {
			double db = (double) Config.cash / win.size();
			for (UUID u : win) {
				if (Config.giveReward) {
					Player p = Bukkit.getPlayer(u);
					assert p != null;
					if (!Config.rewardCommands.isEmpty()) {
						for (String cmd : Config.rewardCommands) {
							if (!cmd.equalsIgnoreCase("none"))
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("<player>", p.getName()));
						}
					}
					if (!Config.rewardMessages.isEmpty()) {
						for (String msg : Config.rewardMessages) {
							if (!msg.equalsIgnoreCase("none"))
								Util.scm(p, msg.replace("<player>", p.getName()));
						}
					}
					if (Config.cash != 0) {
						Vault.economy.depositPlayer(Bukkit.getServer().getOfflinePlayer(u), db);
						Util.scm(p, WINAKGames.getPlugin().getLang().winning_amount.replace("<amount>", String.valueOf(db)));
					}
				}
				plugin.getLeaderboard().addStat(u, Leaderboard.Stats.WINS);
				plugin.getLeaderboard().addStat(u, Leaderboard.Stats.GAMES);
			}
		}
		gameBlockData.clearChests();
		String winner = Util.translateStop(Util.convertUUIDListToStringList(win));
		// prevent not death winners from gaining a prize
		if (death)
			Util.broadcast(WINAKGames.getPlugin().getLang().player_won.replace("<arena>", gameArenaData.name).replace("<winner>", winner));
		if (gameBlockData.requiresRollback()) {
			if (plugin.isEnabled()) {
				new RollbackTASK(this);
			} else {
				// Force rollback if server is stopping
				gameBlockData.forceRollback();
			}
		} else {
			gameArenaData.status = Status.READY;
			gameBlockData.updateLobbyBlock();
		}
		gameArenaData.updateBoards();
		gameCommandData.runCommands(GameCommandData.CommandType.STOP, null);

		// Call GameEndEvent
		Collection<Player> winners = new ArrayList<>();
		for (UUID uuid : win) {
			winners.add(Bukkit.getPlayer(uuid));
		}
		Bukkit.getPluginManager().callEvent(new GameEndLIST(this, winners, death));
	}

	void updateAfterDeath(Player player, boolean death) {
		Status status = gameArenaData.status;
		if (status == Status.RUNNING || status == Status.BEGINNING || status == Status.COUNTDOWN) {
			if (isGameOver()) {
				if (!death) {
					for (UUID uuid : gamePlayerData.players) {
						if (gamePlayerData.kills.get(Bukkit.getPlayer(uuid)) >= 1) {
							death = true;
						}
					}
				}
				boolean finalDeath = death;
				if (plugin.isEnabled()) {
					Bukkit.getScheduler().runTaskLater(plugin, () -> {
						stop(finalDeath);
						gameBlockData.updateLobbyBlock();
						gameArenaData.updateBoards();
					}, 20);
				} else {
					stop(finalDeath);
				}

			}
		} else if (status == Status.WAITING) {
			gamePlayerData.msgAll(lang.player_left_game.replace("<player>", player.getName()) +
					(gameArenaData.minPlayers - gamePlayerData.players.size() <= 0 ? "!" : ": " + lang.players_to_start
							.replace("<amount>", String.valueOf((gameArenaData.minPlayers - gamePlayerData.players.size())))));
		}
		gameBlockData.updateLobbyBlock();
		gameArenaData.updateBoards();
	}

	boolean isGameOver() {
		if (gamePlayerData.players.size() <= 1) return true;
		for (UUID uuid : gamePlayerData.players) {
			Team team = playerManager.getPlayerData(uuid).getTeam();

			if (team != null && (team.getPlayers().size() >= gamePlayerData.players.size())) {
				for (UUID u : gamePlayerData.players) {
					if (!team.getPlayers().contains(u)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Game{name='" + gameArenaData.name + '\'' + ", bound=" + gameArenaData.bound + '}';
	}
}
