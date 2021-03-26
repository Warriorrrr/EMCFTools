package dev.warriorrr.emcftools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.dynmap.DynmapAPI;

import net.md_5.bungee.api.ChatColor;

import static dev.warriorrr.emcftools.EMCFTools.prefix;
import static dev.warriorrr.emcftools.EMCFTools.recentlySuicided;

public class PlayerListener  implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("§3 §6 §3 §6 §3 §6 §e §r §3 §6 §3 §6 §3 §6 §d ");
        
        DynmapAPI api = ((DynmapAPI) Bukkit.getPluginManager().getPlugin("dynmap"));
                
        if (!api.getPlayerVisbility(event.getPlayer())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dynmap show " + event.getPlayer().getName());
            event.getPlayer().sendMessage(prefix + ChatColor.AQUA + " You are now shown on Dynmap.");
        }
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
