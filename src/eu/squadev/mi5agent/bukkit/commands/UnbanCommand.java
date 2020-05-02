package eu.squadev.mi5agent.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import eu.squadev.mi5agent.bukkit.Mi5Agent;

public class UnbanCommand implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender.hasPermission("mi5agent.unban")) {
			if(args.length > 0) {
				if(Mi5Agent.getManager().unBan(args[0])) {
					sender.sendMessage("§8§l[Mi5Agent] §a"+args[0]+" §8a été unban");
					return true;
				}else {
					sender.sendMessage("§cErreur: Ce joueur n'est pas ban...");
					return false;
				}
			}else {
				sender.sendMessage("§cUsage: /unban <player>");
				return false;
			}
		}else {
			sender.sendMessage("Tu n'as pas la permission d'exécuter cette commande.");
			return false;
		}
	}
}
