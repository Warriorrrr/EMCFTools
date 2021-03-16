package dev.warriorrr.emcftools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.dynmap.DynmapAPI;

import net.md_5.bungee.api.ChatColor;

import static dev.warriorrr.emcftools.EMCFTools.prefix;

public class PlayerJoinListener  implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        DynmapAPI api = ((DynmapAPI) Bukkit.getPluginManager().getPlugin("dynmap"));
                
        if (!api.getPlayerVisbility(event.getPlayer())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dynmap show " + event.getPlayer().getName());
            event.getPlayer().sendMessage(prefix + ChatColor.AQUA + " You are now shown on Dynmap.");
        }
    }
}
