package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.utils.NBTAPI;
import nl.communitynetwerk.winakgames.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NBTCMD extends BaseCMD {

	private final NBTAPI api;

	public NBTCMD() {
		forcePlayer = true;
		cmdName = "nbt";
		forceInGame = false;
		argLength = 1;
		usage = "";
		api = plugin.getNbtApi();
	}


	@Override
	public boolean run() {
		Player player = (Player) sender;
		CommandSender console = Bukkit.getConsoleSender();
		if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
			ItemStack item = player.getInventory().getItemInMainHand();
			Material type = item.getType();
			Util.scm(player, "&3NBT:");
			String nbtString = api.getNBT(item);
			if (nbtString == null) {
				Util.scm(player, "&cNO NBT FOUND!");
			} else {
				Util.scm(player, type.toString() + " " + item.getAmount() + " data:" + nbtString.replace(" ", "~"));
				Util.scm(player, "&6NBT String also sent to console for easy copy/pasting");
				Util.scm(console, "&3NBT string from &b" + player.getName() + "&3:");
				System.out.println(type.toString() + " " + item.getAmount() + " data:" + nbtString.replace(" ", "~"));
			}

		}
		return true;
	}
}
