package dev.warriorrr.emcftools;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import dev.warriorrr.emcftools.commands.ClearNetherCommand;
import dev.warriorrr.emcftools.commands.SuicideCommand;
import dev.warriorrr.emcftools.listeners.BlockListener;
import dev.warriorrr.emcftools.listeners.EntityListener;
import dev.warriorrr.emcftools.listeners.PlayerListener;
import net.md_5.bungee.api.ChatColor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

public class EMCFTools extends JavaPlugin implements Listener {

    public static String prefix = ChatColor.GOLD + "[" + Utils.gradient("EMCF", Color.GREEN, new Color(0, 102, 0)) + ChatColor.GOLD + "]";
    public static Map<UUID, Long> suicideCooldowns = new HashMap<>();
    public static List<UUID> recentlySuicided = new ArrayList<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);

        getCommand("suicide").setExecutor(new SuicideCommand());
        getCommand("clearnether").setExecutor(new ClearNetherCommand());
        registerRecipes();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Entry<UUID, Long> entry : suicideCooldowns.entrySet()) {
                    if (System.currentTimeMillis() > entry.getValue())
                        suicideCooldowns.remove(entry.getKey());
                }
            }
        }, 1200, 1200);
    }

    private void registerRecipes() {
        for (Material coral : Tag.CORAL_PLANTS.getValues()) {
            ShapedRecipe coralRecipe = new ShapedRecipe(new NamespacedKey(this, coral.toString().toLowerCase() + "Recipe"), new ItemStack(Material.valueOf(coral.toString() + "_BLOCK")));
            coralRecipe.shape("CC", "CC").setIngredient('C', coral);
            Bukkit.addRecipe(coralRecipe);
        } 

        for (Material carpet : Tag.CARPETS.getValues()) {
            ShapelessRecipe carpetRecipe = new ShapelessRecipe(new NamespacedKey(this, carpet.toString().toLowerCase() + "Recipe"), new ItemStack(carpet, 2));
            carpetRecipe.addIngredient(carpet);
            Bukkit.addRecipe(carpetRecipe);
        }
    }
}
