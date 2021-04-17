package dev.warriorrr.emcftools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import static dev.warriorrr.emcftools.EMCFTools.prefix;
import static dev.warriorrr.emcftools.EMCFTools.clientBrands;
import static dev.warriorrr.emcftools.EMCFTools.blacklists;

public class ClientBrandListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] msg) {
        String clientBrand = new String(msg).substring(1);
        if (clientBrand.toLowerCase().contains("lunarclient")) {
            p.kickPlayer(prefix + ChatColor.RED + " Lunar client is not allowed on this server.");
            blacklists.put(p.getUniqueId(), System.currentTimeMillis() + 180 * 1000);

            //Send alert.
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("emcftools.notification"))
                    player.sendMessage(prefix + ChatColor.RED + " " + p.getName() + " tried to join, but was using lunar client.");
            }
        } else {
            clientBrands.put(p.getUniqueId(), clientBrand);
        }
    }
}
