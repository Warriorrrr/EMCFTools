package dev.warriorrr.emcftools;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

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
        ShapedRecipe tubeCoral = new ShapedRecipe(new NamespacedKey(this, "emcf_tubeCoral"), new ItemStack(Material.TUBE_CORAL_BLOCK));
        tubeCoral.shape("CC", "CC").setIngredient('C', Material.TUBE_CORAL);
        Bukkit.addRecipe(tubeCoral);

        ShapedRecipe brainCoral = new ShapedRecipe(new NamespacedKey(this, "emcf_brainCoral"), new ItemStack(Material.BRAIN_CORAL_BLOCK));
        brainCoral.shape("CC", "CC").setIngredient('C', Material.BRAIN_CORAL);
        Bukkit.addRecipe(brainCoral);

        ShapedRecipe bubbleCoral = new ShapedRecipe(new NamespacedKey(this, "emcf_bubbleCoral"), new ItemStack(Material.BUBBLE_CORAL_BLOCK));
        bubbleCoral.shape("CC", "CC").setIngredient('C', Material.BUBBLE_CORAL);
        Bukkit.addRecipe(bubbleCoral);

        ShapedRecipe fireCoral = new ShapedRecipe(new NamespacedKey(this, "emcf_fireCoral"), new ItemStack(Material.FIRE_CORAL_BLOCK));
        fireCoral.shape("CC", "CC").setIngredient('C', Material.FIRE_CORAL);
        Bukkit.addRecipe(fireCoral);

        ShapedRecipe hornCoral = new ShapedRecipe(new NamespacedKey(this, "emcf_hornCoral"), new ItemStack(Material.HORN_CORAL_BLOCK));
        hornCoral.shape("CC", "CC").setIngredient('C', Material.HORN_CORAL);
        Bukkit.addRecipe(hornCoral);
    }
}
