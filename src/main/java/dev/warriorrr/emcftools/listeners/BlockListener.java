package dev.warriorrr.emcftools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
//import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;

import net.md_5.bungee.api.ChatColor;

import static dev.warriorrr.emcftools.EMCFTools.prefix;

public class BlockListener implements Listener {
    /*
    * Prevent nether portals from breaking bedrock
    */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPortalCreate(PortalCreateEvent event) {
        for (BlockState blockState : event.getBlocks()) {
            if (blockState.getY() == 0)
                blockState.setBlockData(Bukkit.createBlockData(Material.BEDROCK));
        }
    }

    /*
    * Prevent blocks from being placed in places where bedrock is broken
    */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getY() == 0 && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.getPlayer().sendMessage(prefix + ChatColor.RED + " You cannot place this block at this location.");
            if (!isBlockEntity(event.getBlockPlaced().getType())) // Replacing block entities seems to cause console spam.
                event.getBlock().setType(Material.BEDROCK);
            else
                event.setCancelled(true);
        }
    }

    /*
    * Also prevent blocks from being placed, instead replace it with bedrock.
    */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getY() == 0 && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.getPlayer().sendMessage(prefix + ChatColor.RED + " You cannot break this block at this location.");
            event.setCancelled(true);
            event.getBlock().setType(Material.BEDROCK);
        }
    }

    //@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    //public void onOpenChest(PlayerInteractEvent event) {
    //    if (isContainter(event.getClickedBlock().getType()) && event.getClickedBlock().getY() == 0) {
    //        event.getPlayer().sendMessage(prefix + ChatColor.RED + " You cannot interact with this block at this location.");
    //        event.getClickedBlock().setType(Material.BEDROCK);
    //    }
    //}

    private boolean isBlockEntity(Material material) {
        if (Tag.SIGNS.isTagged(material) || Tag.WALL_SIGNS.isTagged(material) || Tag.BANNERS.isTagged(material) || Tag.SHULKER_BOXES.isTagged(material))
            return true;
        switch(material) {
            case BEEHIVE:
            case BEE_NEST:
            case CHEST:
            case TRAPPED_CHEST:
            case DISPENSER:
            case FURNACE:
            case BREWING_STAND:
            case HOPPER:
            case DROPPER:
            case BARREL:
            case SMOKER:
            case BLAST_FURNACE:
            case CAMPFIRE:
            case SOUL_CAMPFIRE:
            case LECTERN:
            case BEACON:
            case SPAWNER:
            case JUKEBOX:
            case ENCHANTING_TABLE:
            case END_PORTAL:
            case ENDER_CHEST:
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
            case CAULDRON:
            case CONDUIT:
            case BELL:
                return true;
            default:
                return false;
        }
    }
    
    @SuppressWarnings("unused")
    private boolean isContainter(Material material) {
        switch(material) {
            case CHEST:
            case TRAPPED_CHEST:
            case BARREL:
            case HOPPER:
            case DROPPER:
            case DISPENSER:
                return true;
            default:
                return false;
        }
    }
}
