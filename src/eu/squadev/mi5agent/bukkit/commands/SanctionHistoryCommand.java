package eu.squadev.mi5agent.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.squadev.mi5agent.bukkit.Mi5Agent;

public class SanctionHistoryCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender.hasPermission("mi5agent.sanctionhistory")) {
			if(args.length > 0) {
				if(Mi5Agent.getManager().getBans(args[0]) != null && Mi5Agent.getManager().getMutes(args[0]) != null) {
					Mi5Agent.getManager().openCasier(args[0], (Player)sender);
					return true;
				}else {
					sender.sendMessage("§9Ce joueur n'as pas d'antécédents...");
					return false;
				}
			}else {
				sender.sendMessage("§cUsage: /sanctionhistory <player>");
				return false;
			}
		}else {
			sender.sendMessage("Tu n'as pas la permission d'exécuter cette commande.");
			return false;
		}
	}

}
