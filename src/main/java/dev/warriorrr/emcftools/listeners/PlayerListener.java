package dev.warriorrr.emcftools.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.inventory.ItemStack;

import dev.warriorrr.emcftools.Utils;
import net.md_5.bungee.api.ChatColor;

import static dev.warriorrr.emcftools.EMCFTools.prefix;
import static dev.warriorrr.emcftools.EMCFTools.recentlySuicided;
import static dev.warriorrr.emcftools.EMCFTools.blacklists;
import static dev.warriorrr.emcftools.EMCFTools.clientBrands;

public class PlayerListener  implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPrePlayerLogin(AsyncPlayerPreLoginEvent event) {
        if (!blacklists.containsKey(event.getUniqueId()))
            return;
        
        long seconds = (blacklists.get(event.getUniqueId()) - System.currentTimeMillis()) / 1000 % 180;
        event.disallow(Result.KICK_OTHER, prefix + ChatColor.RED + " You are currently blacklisted from this server.\n\nExpires in " + seconds + " seconds.");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Send string that disables voxelmap
        event.getPlayer().sendMessage("§3 §6 §3 §6 §3 §6 §e §r §3 §6 §3 §6 §3 §6 §d ");
        
        if (Utils.isHiddenOnDynmap(event.getPlayer())) {
            if (!Utils.canBeHidden(event.getPlayer()))
                Utils.showPlayer(event.getPlayer());
            else
                event.getPlayer().sendMessage(prefix + ChatColor.AQUA + " You are still hidden on dynmap.");
        }
    }

    @EventHandler
    public void onPlayerEvent(PlayerQuitEvent event) {
        clientBrands.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (recentlySuicided.contains(event.getEntity().getUniqueId())) {
            event.setDeathMessage(event.getEntity().getName() + " suicided.");
            recentlySuicided.remove(event.getEntity().getUniqueId());
        }
    }

    /*
    * Replace containers with bedrock when they get opened.
    */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onOpenChest(PlayerInteractEvent event) {
        if (isContainter(event.getClickedBlock().getType()) && event.getClickedBlock().getY() == 0) {
            event.getPlayer().sendMessage(prefix + ChatColor.RED + " You cannot interact with this block at this location.");
            Container container = (Container) event.getClickedBlock().getState();
            for (ItemStack item : container.getInventory().getContents()) {
                if (item != null) {
                    event.getClickedBlock().getWorld().dropItem(event.getClickedBlock().getLocation().add(0, 1, 0), item);
                }
            }
            container.getInventory().clear();
            event.getClickedBlock().setType(Material.BEDROCK);  
        }
    }

    @EventHandler
    public void onGameModeSwitch(PlayerGameModeChangeEvent event) {
        if (!Utils.canBeHidden(event.getPlayer()))
            return;

        if (event.getNewGameMode() == GameMode.SPECTATOR && !Utils.isHiddenOnDynmap(event.getPlayer()))
            Utils.hidePlayer(event.getPlayer());
        else if (event.getNewGameMode() == GameMode.SURVIVAL && Utils.isHiddenOnDynmap(event.getPlayer()))
            event.getPlayer().sendMessage(prefix + ChatColor.AQUA + " You are still hidden on dynmap.");
    }

    private boolean isContainter(Material material) {
        switch(material) {
            case CHEST:
            case TRAPPED_CHEST:
            case BARREL:
            case HOPPER:
            case DROPPER:
            case DISPENSER:
            case FURNACE:
                return true;
            default:
                return false;
        }
    }
}
