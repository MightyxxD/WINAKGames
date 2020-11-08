package nl.communitynetwerk.winakgames.data;

import nl.communitynetwerk.winakgames.WINAKGames;
import nl.communitynetwerk.winakgames.utils.Util;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MobConfig {

	private final WINAKGames plugin;
	private FileConfiguration mobs = null;
	private File mobFile = null;

	public MobConfig(WINAKGames plugin) {
		this.plugin = plugin;
		loadMobFile();
	}

	private void loadMobFile() {
		if (mobFile == null) {
			mobFile = new File(plugin.getDataFolder(), "mobs.yml");
		}
		if (!mobFile.exists()) {
			plugin.saveResource("mobs.yml", false);
			mobs = YamlConfiguration.loadConfiguration(mobFile);
			Util.log("&7New mobs.yml created");
		} else {
			mobs = YamlConfiguration.loadConfiguration(mobFile);
		}
	}

	/** Get the mob config
	 * @return Mob config
	 */
	public FileConfiguration getMobs() {
		return this.mobs;
	}
}
