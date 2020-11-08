package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.WINAKGames;
import nl.communitynetwerk.winakgames.data.ArenaConfig;
import nl.communitynetwerk.winakgames.data.Language;
import nl.communitynetwerk.winakgames.managers.Manager;
import nl.communitynetwerk.winakgames.managers.PlayerMANAGER;
import nl.communitynetwerk.winakgames.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCMD {

	WINAKGames plugin;
	Language lang;
	PlayerMANAGER playerManager;
	Manager gameManager;
	ArenaConfig arenaConfig;

	public BaseCMD() {
		this.plugin = WINAKGames.getPlugin();
	}

	public CommandSender sender;
	public String[] args;
	public String cmdName;
	public int argLength = 0;
	public boolean forcePlayer = true;
	public boolean forceInGame = false;
	public boolean forceInRegion = false;
	public String usage = "";
	public Player player;

	public boolean processCMD(WINAKGames plugin, CommandSender sender, String[] args) {
		this.sender = sender;
		this.args = args;
		this.playerManager = plugin.getPlayerManager();
		this.gameManager = plugin.getManager();
		this.arenaConfig = plugin.getArenaConfig();
		this.lang = plugin.getLang();

		if (forcePlayer) {
			if (!(sender instanceof Player)) return false;
			else player = (Player) sender;
		}
		if (!sender.hasPermission("winakgames." + cmdName))
			Util.scm(this.sender, lang.cmd_base_noperm.replace("<command>", cmdName));
		else if (forceInGame && !playerManager.hasPlayerData(player) && !playerManager.hasSpectatorData(player))
			Util.scm(this.sender, lang.cmd_base_nogame);
		else if (forceInRegion && !gameManager.isInRegion(player.getLocation()))
			Util.scm(this.sender, lang.cmd_base_noregion);
		else if (argLength > args.length)
			Util.scm(sender, lang.cmd_base_wrongusage + " " + sendHelpLine());
		else return run();
		return true;
	}

	public abstract boolean run();

	public String sendHelpLine() {
		return "&6/winakgames " + cmdName + " " + usage.replaceAll("<", "&7<").replaceAll(">", "&7>");
	}
}
