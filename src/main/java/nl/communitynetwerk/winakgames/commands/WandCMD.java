package nl.communitynetwerk.winakgames.commands;

import nl.communitynetwerk.winakgames.data.PlayerSession;
import nl.communitynetwerk.winakgames.utils.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class WandCMD extends BaseCMD {

	private final ItemStack WAND;

	public WandCMD() {
		forcePlayer = true;
		cmdName = "wand";
		argLength = 1;
		WAND = new ItemStack(Material.BLAZE_ROD, 1);
		ItemMeta meta = WAND.getItemMeta();
		assert meta != null;
		meta.setDisplayName(Util.getColString("&3HungerGames Wand"));
		meta.setLore(new ArrayList<>(Arrays.asList(
				Util.getColString("&7Left or Right Click"),
				Util.getColString("&7to set positions")
		)));
		WAND.setItemMeta(meta);
	}

	@Override
	public boolean run() {
		if (plugin.getPlayerSessions().containsKey(player.getUniqueId())) {
			plugin.getPlayerSessions().remove(player.getUniqueId());
			Util.sendPrefixedMessage(player, "&cWand disabled!");
		} else {
			if (!player.getInventory().getItemInMainHand().isSimilar(WAND)) {
				player.getInventory().addItem(WAND);
			}
			plugin.getPlayerSessions().put(player.getUniqueId(), new PlayerSession(null, null));
			Util.sendPrefixedMessage(player, "&aWand enabled!");
		}
		return true;
	}
}
