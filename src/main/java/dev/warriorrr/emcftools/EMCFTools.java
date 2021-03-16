package dev.warriorrr.emcftools;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import dev.warriorrr.emcftools.listeners.BlockListener;
import dev.warriorrr.emcftools.listeners.PlayerJoinListener;
import net.md_5.bungee.api.ChatColor;

import java.awt.Color;

public class EMCFTools extends JavaPlugin implements Listener {

    public static String prefix = ChatColor.GOLD + "[" + Utils.gradient("EMCF", Color.GREEN, new Color(0, 102, 0)) + ChatColor.GOLD + "]";

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
    }
}
