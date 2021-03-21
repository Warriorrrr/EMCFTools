package dev.warriorrr.emcftools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.PortalCreateEvent;

import net.md_5.bungee.api.ChatColor;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.GridManager;

import static dev.warriorrr.emcftools.EMCFTools.prefix;

import java.util.ArrayList;
import java.util.List;

public class BlockListener implements Listener {
    /*
    * Prevent nether portals from breaking bedrock.
    * Prevent nether portals being used to raid other factions.
    */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPortalCreate(PortalCreateEvent event) {
        List<Chunk> chunks = new ArrayList<Chunk>();
        List<Faction> factions = new ArrayList<Faction>();
        for (BlockState blockState : event.getBlocks()) {
            if (!chunks.contains(blockState.getChunk()))
                chunks.add(blockState.getChunk());
        }
        for (Chunk chunk : chunks) {
            Faction faction = GridManager.INSTANCE.getFactionAt(chunk);
            if (!factions.contains(faction) && !faction.isWilderness())
                factions.add(faction);
        }
        if (!factions.isEmpty()) {
            if (!(event.getEntity() instanceof Player)) {
                event.setCancelled(true);
                return;
            }
            Player player = (Player) event.getEntity();
            for (Faction faction : factions) {
                if (!faction.getFactionMembers().contains(player.getUniqueId())) {
                    event.setCancelled(true);
                    player.sendMessage(prefix + ChatColor.RED + " Nether portal creation cancelled. Try moving it somewhere else.");
                    return;
                }
            }
        }

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
        } else if (Tag.LOGS.isTagged(event.getBlock().getType())) {
            int blockX = (int) event.getBlock().getLocation().getX();
            int blockY = (int) event.getBlock().getLocation().getY();
            int blockZ = (int) event.getBlock().getLocation().getZ();
            World world = event.getBlock().getWorld();
            int radius = 5;
            for (int x = blockX - radius; x <= blockX + radius; x++) {
                for (int y = blockY - radius; y <= blockY + radius; y++) {
                    for (int z = blockZ - radius; z <= blockZ + radius; z++) {
                        Block block = world.getBlockAt(x, y, z);
                        if (Tag.LEAVES.isTagged(block.getType())) {
                            BlockState blockState = block.getState();
                            Leaves leafBlock = (Leaves) blockState.getBlockData();
                            if (leafBlock.isPersistent()) {
                                leafBlock.setPersistent(false);
                                blockState.setBlockData(leafBlock);
                                block.setBlockData(blockState.getBlockData());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBoneMeal(BlockFertilizeEvent event) {
        for (BlockState blockState : event.getBlocks()) {
            if (!Tag.CORAL_PLANTS.isTagged(blockState.getType()))
                continue;
            
            int randomNum = (int) (Math.random() * 150) + 1;
            if (randomNum == 150) {
                int pickleAmount = (int) (Math.random() * 4) + 1;
                SeaPickle seaPickle = (SeaPickle) Bukkit.createBlockData(Material.SEA_PICKLE);
                seaPickle.setWaterlogged(true);
                seaPickle.setPickles(pickleAmount);
                blockState.setBlockData(seaPickle);
            }
        }
    }

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
}
