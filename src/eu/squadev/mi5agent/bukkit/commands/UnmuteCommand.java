package eu.squadev.mi5agent.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import eu.squadev.mi5agent.bukkit.Mi5Agent;

public class UnmuteCommand implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender.hasPermission("mi5agent.unmute")) {
			if(args.length > 0) {
				if(Mi5Agent.getManager().unMute(args[0])) {
					sender.sendMessage("§8§l[Mi5Agent] §a"+args[0]+" §8a été unmute");
					return true;
				}else {
					sender.sendMessage("§cErreur: Ce joueur n'est pas mute...");
					return false;
				}
			}else {
				sender.sendMessage("§cUsage: /unmute <player>");
				return false;
			}
		}else {
			sender.sendMessage("Tu n'as pas la permission d'exécuter cette commande.");
			return false;
		}
	}
}
