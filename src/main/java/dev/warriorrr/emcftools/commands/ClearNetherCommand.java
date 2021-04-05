package dev.warriorrr.emcftools.commands;

import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import net.md_5.bungee.api.ChatColor;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.persist.data.FLocation;
import net.prosavage.factionsx.persist.data.Factions;

import static dev.warriorrr.emcftools.EMCFTools.prefix;

import java.util.Map.Entry;

public class ClearNetherCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(prefix + ChatColor.RED + " This command can only be run via console.");
            return true;
        }

        int netherClaims = 0;
        for (Entry<Long, Faction> entry : Factions.INSTANCE.getFactions().entrySet()) {
            for (FLocation fLocation : GridManager.INSTANCE.getAllClaims(entry.getValue())) {
                if (fLocation.getChunk().getWorld().getEnvironment() == Environment.NETHER) {
                    GridManager.INSTANCE.unclaim(entry.getValue(), fLocation);
                    netherClaims += 1;
                }
            }
        }

        sender.sendMessage(ChatColor.stripColor(prefix) + ChatColor.AQUA + " Removed " + netherClaims + " claims inside the nether.");
        return true;
    }
}
