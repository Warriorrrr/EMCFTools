package dev.warriorrr.emcftools.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityListener implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        if (event.getVehicle().getLocation().getY() < 2) {
            for (Entity passenger : event.getVehicle().getPassengers()) {
                if (passenger.isDead())
                    continue;
                
                passenger.teleport(passenger.getLocation().add(0, 1, 0));
                if (!(passenger instanceof Player))
                    ((LivingEntity) passenger).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2, 4));
            }
        }
    }
}
