package nl.communitynetwerk.winakgames.listeners;

import com.gmail.nossr50.events.experience.McMMOPlayerExperienceEvent;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelDownEvent;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.gmail.nossr50.events.fake.*;
import com.gmail.nossr50.events.items.McMMOItemSpawnEvent;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import com.gmail.nossr50.events.skills.secondaryabilities.SubSkillEvent;
import nl.communitynetwerk.winakgames.WINAKGames;
import nl.communitynetwerk.winakgames.data.Config;
import nl.communitynetwerk.winakgames.managers.PlayerMANAGER;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class McMMoLIST implements Listener {

	private final WINAKGames plugin;
	private final PlayerMANAGER playerManager;

	public McMMoLIST(WINAKGames plugin) {
		this.plugin = plugin;
		this.playerManager = plugin.getPlayerManager();
	}

	// Handle mcMMO EXP gain events
	@EventHandler
	private void mcMMOLevelUp(McMMOPlayerLevelUpEvent event) {
		handleExpEvent(event);
	}

	@EventHandler
	private void mcMMOLevelDown(McMMOPlayerLevelDownEvent event) {
		handleExpEvent(event);
	}

	@EventHandler
	private void mcMMOXpGain(McMMOPlayerXpGainEvent event) {
		handleExpEvent(event);
	}

	private void handleExpEvent(McMMOPlayerExperienceEvent event) {
		if (!Config.mcmmoGainExp) {
			Player player = event.getPlayer();
			if (playerManager.hasPlayerData(player.getUniqueId())) {
				//if (playerManager.hasPlayerData(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

	// Handle mcMMO skill use events
	@EventHandler
	private void mcMMOUseSkill(McMMOPlayerAbilityActivateEvent event) {
		if (!Config.mcmmoUseSkills) {
			Player player = event.getPlayer();
			if (playerManager.hasPlayerData(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void mcMMOUseSubSkill(SubSkillEvent event) {
		if (!Config.mcmmoUseSkills) {
			Player player = event.getPlayer();
			if (playerManager.hasPlayerData(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void blockBreakEvent(FakeBlockBreakEvent event) {
		if (!Config.mcmmoUseSkills) {
			Player player = event.getPlayer();
			if (playerManager.hasPlayerData(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	protected void blockDamageEvent(FakeBlockDamageEvent event) {
		if (!Config.mcmmoUseSkills) {
			Player player = event.getPlayer();
			if (playerManager.hasPlayerData(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void entityDamageByEntityEvent(FakeEntityDamageByEntityEvent event) {
		if (!Config.mcmmoUseSkills) {
			Entity damager = event.getDamager();
			Entity victim = event.getEntity();
			if (playerManager.hasPlayerData(damager.getUniqueId()) || playerManager.hasPlayerData(victim.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void entityDamageEvent(FakeEntityDamageEvent event) {
		if (!Config.mcmmoUseSkills) {
			if (event.getEntity() instanceof Player) {
				Player player = ((Player) event.getEntity());
				if (playerManager.hasPlayerData(player.getUniqueId())) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void fishEvent(FakePlayerFishEvent event) {
		if (!Config.mcmmoUseSkills) {
			Player player = event.getPlayer();
			if (playerManager.hasPlayerData(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void itemSpawnEvent(McMMOItemSpawnEvent event) {
		if (!Config.mcmmoUseSkills) {
			Location loc = event.getLocation();
			plugin.getGames().stream().filter(game -> game.getGameArenaData().isInRegion(loc)).map(game -> true).forEach(event::setCancelled);
		}
	}
}
