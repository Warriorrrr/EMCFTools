package dev.warriorrr.emcftools.listeners;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.GridManager;

public class EntityListener implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        for (Entity passenger : event.getVehicle().getPassengers()) {
            if (passenger.isDead())
                continue;
                
            passenger.teleport(passenger.getLocation().add(0, 1, 0));
            if (!(passenger instanceof Player))
                ((LivingEntity) passenger).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2, 4));
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPearlLand(ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.ENDER_PEARL)
            return;

        if (event.getHitBlock() != null) {
            Faction faction = GridManager.INSTANCE.getFactionAt(event.getHitBlock().getChunk());
            if (faction.isWilderness() || faction.getFactionMembers().contains(((Player) event.getEntity().getShooter()).getUniqueId()))
                return;

            double[] offset = getOffset(event.getHitBlockFace());
            if (offset[0] > -1.0)
                event.getEntity().teleport(event.getHitBlock().getLocation().add(offset[0], offset[1], offset[2]));
        }
    }

    public double[] getOffset(BlockFace blockFace) {
        switch (blockFace) {
            case UP:
                return new double[]{0.5, 1.0, 0.5};
            case DOWN:
                return new double[]{0.5, -2.0, 0.5};
            case NORTH:
                return new double[]{0.5, 0.0, -0.5};
            case EAST:
                return new double[]{1.5, 0.0, 0.5};
            case SOUTH:
                return new double[]{0.5, 0.0, 1.5};
            case WEST:
                return new double[]{-0.5, 0.0, 0.5};
            default:
                return new double[]{-1.0, 0.0, 0.0};
        }
    }
}
