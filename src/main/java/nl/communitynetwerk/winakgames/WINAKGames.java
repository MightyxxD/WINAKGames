package nl.communitynetwerk.winakgames;

import io.lumine.xikage.mythicmobs.mobs.MobManager;
import nl.communitynetwerk.winakgames.commands.*;
import nl.communitynetwerk.winakgames.data.*;
import nl.communitynetwerk.winakgames.game.Game;
import nl.communitynetwerk.winakgames.listeners.CancelLIST;
import nl.communitynetwerk.winakgames.listeners.CommandLIST;
import nl.communitynetwerk.winakgames.listeners.GameLIST;
import nl.communitynetwerk.winakgames.listeners.WandLIST;
import nl.communitynetwerk.winakgames.managers.*;
import nl.communitynetwerk.winakgames.utils.NBTAPI;
import nl.communitynetwerk.winakgames.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class WINAKGames extends JavaPlugin {

	private Map<String, BaseCMD> cmds;
	private Map<UUID, PlayerSession> playerSession;
	private Map<Integer, ItemStack> items;
	private Map<Integer, ItemStack> bonusItems;

	private List<Game> games;

	private static WINAKGames plugin;
	private Config config;
	private Manager manager;
	private PlayerMANAGER playerManager;
	private ArenaConfig arenaconfig;
	private KillMANAGER killManager;
	private RandomItems randomItems;
	private Language lang;
	private KitMANAGER kitManager;
	private ItemStackMANAGER itemStackManager;
	private Leaderboard leaderboard;
	private MobManager mmMobManager;

	private MobConfig mobConfig;

	private NBTAPI nbtApi;

	@Override
	public void onEnable() {
		if (!Util.isRunningMinecraft(1, 13)) {
			Util.warning("WINAKGames ondersteund jouw versie niet");
			Util.warning("Dit ondersteund alleen versie 1.13+");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		loadPlugin(true);
	}
	public void loadPlugin(boolean load) {
		long start = System.currentTimeMillis();
		plugin = this;

		if (load) {
			cmds = new HashMap<>();
		}
		games = new ArrayList<>();
		playerSession = new HashMap<>();
		items = new HashMap<>();
		bonusItems = new HashMap<>();
		bonusItems = new HashMap<>();

		config = new Config(this);

		nbtApi = new NBTAPI();

		lang = new Language(this);
		kitManager = new KitMANAGER();
		itemStackManager = new ItemStackMANAGER(this);
		mobConfig = new MobConfig(this);
		randomItems = new RandomItems(this);
		playerManager = new PlayerMANAGER();
		arenaconfig = new ArenaConfig(this);
		killManager = new KillMANAGER();
		manager = new Manager(this);
		leaderboard = new Leaderboard(this);

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new Placeholders(this).register();
			Util.log("&7PAPI found, Placeholders have been &aenabled");
		} else {
			Util.log("&7PAPI not found, Placeholders have been &cdisabled");
		}

		getCommand("winakgames").setExecutor(new CommandLIST(this));
		if (load) {
			loadCmds();
		}
		getServer().getPluginManager().registerEvents(new WandLIST(this), this);
		getServer().getPluginManager().registerEvents(new CancelLIST(this), this);
		getServer().getPluginManager().registerEvents(new GameLIST(this), this);

		if (this.getDescription().getVersion().contains("Beta")) {
			Util.log("&cJE GEBRUIKT EEN BETA VERSIE, wees voorzichtig");
			Util.log("&cMeld eventuele problemen aan: &bhttps://github.com/ShaneBeeStudios/HungerGames/issues");
		}

		Util.log("HungerGames has been &aenabled&7 in &b%.2f seconds&7!", (float)(System.currentTimeMillis() - start) / 1000);
	}

	public void reloadPlugin() {
		unloadPlugin(true);
	}

	private void unloadPlugin(boolean reload) {
		stopAll();
		games = null;
		playerSession = null;
		items = null;
		bonusItems = null;
		plugin = null;
		config = null;
//		metrics = null;
		nbtApi = null;
		mmMobManager = null;
		lang = null;
		kitManager = null;
		itemStackManager = null;
		mobConfig = null;
		randomItems = null;
		playerManager = null;
		arenaconfig = null;
		killManager = null;
		manager = null;
		leaderboard = null;
		HandlerList.unregisterAll(this);
		if (reload) {
			loadPlugin(false);
		} else {
			cmds = null;
		}
	}

	@Override
	public void onDisable() {
		unloadPlugin(false);
		Util.log("WINAKGames is uitgeschakeld.");
	}

	private void loadCmds() {
		cmds.put("team", new TeamCMD());
		cmds.put("addspawn", new AddSpawnCMD());
		cmds.put("create", new CreateCMD());
		cmds.put("join", new JoinCMD());
		cmds.put("leave", new LeaveCMD());
		cmds.put("reload", new ReloadCMD());
		cmds.put("setlobbywall", new SetLobbyWallCMD());
		cmds.put("wand", new WandCMD());
		cmds.put("kit", new KitCMD());
		cmds.put("debug", new DebugCMD());
		cmds.put("list", new ListCMD());
		cmds.put("listgames", new ListGamesCMD());
		cmds.put("forcestart", new StartCMD());
		cmds.put("stop", new StopCMD());
		cmds.put("toggle", new ToggleCMD());
		cmds.put("setexit", new SetExitCMD());
		cmds.put("delete", new DeleteCMD());
		cmds.put("chestrefill", new ChestRefillCMD());
		cmds.put("chestrefillnow", new ChestRefillNowCMD());
		cmds.put("bordersize", new BorderSizeCMD());
		cmds.put("bordercenter", new BorderCenterCMD());
		cmds.put("bordertimer", new BorderTimerCMD());
		if (Config.spectateEnabled) {
			cmds.put("spectate", new SpectateCMD());
		}
		if (nbtApi != null) {
			cmds.put("nbt", new NBTCMD());
		}

		ArrayList<String> cArray = new ArrayList<>();
		cArray.add("join");
		cArray.add("leave");
		cArray.add("kit");
		cArray.add("listgames");
		cArray.add("list");

		for (String bc : cmds.keySet()) {
			getServer().getPluginManager().addPermission(new Permission("winakgames." + bc));
			if (cArray.contains(bc))
				getServer().getPluginManager().getPermission("winakgames." + bc).setDefault(PermissionDefault.TRUE);

		}
	}

	public void stopAll() {
		ArrayList<UUID> ps = new ArrayList<>();
		for (Game g : games) {
			g.cancelTasks();
			g.getGameBlockData().forceRollback();
			ps.addAll(g.getGamePlayerData().getPlayers());
			ps.addAll(g.getGamePlayerData().getSpectators());
		}
		for (UUID u : ps) {
			Player p = Bukkit.getPlayer(u);
			if (p != null) {
				p.closeInventory();
				if (playerManager.hasPlayerData(u)) {
					playerManager.getPlayerData(u).getGame().getGamePlayerData().leave(p, false);
					playerManager.removePlayerData(u);
				}
				if (playerManager.hasSpectatorData(u)) {
					playerManager.getSpectatorData(u).getGame().getGamePlayerData().leaveSpectate(p);
					playerManager.removePlayerData(u);
				}
			}
		}
		games.clear();
	}

	public static WINAKGames getPlugin() {
		return plugin;
	}

	public RandomItems getRandomItems() {
		return this.randomItems;
	}

	public KillMANAGER getKillManager() {
		return this.killManager;
	}

	public KitMANAGER getKitManager() {
		return this.kitManager;
	}

	public ItemStackMANAGER getItemStackManager() {
		return this.itemStackManager;
	}

	public Manager getManager() {
		return this.manager;
	}

	public PlayerMANAGER getPlayerManager() {
		return playerManager;
	}

	public ArenaConfig getArenaConfig() {
		return this.arenaconfig;
	}

	public Leaderboard getLeaderboard() {
		return this.leaderboard;
	}

	public List<Game> getGames() {
		return this.games;
	}

	public Map<UUID, PlayerSession> getPlayerSessions() {
		return this.playerSession;
	}

	public Map<Integer, ItemStack> getItems() {
		return this.items;
	}

	public Map<Integer, ItemStack> getBonusItems() {
		return this.bonusItems;
	}

	public Map<String, BaseCMD> getCommands() {
		return this.cmds;
	}

	public Language getLang() {
		return this.lang;
	}

	public Config getHGConfig() {
		return config;
	}

	public MobConfig getMobConfig() {
		return this.mobConfig;
	}

	public NBTAPI getNbtApi() {
		return this.nbtApi;
	}

//	public Metrics getMetrics() {
//		return this.metrics;
//	}

	public MobManager getMmMobManager() {
		return this.mmMobManager;
	}
}
