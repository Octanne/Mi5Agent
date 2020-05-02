package eu.squadev.mi5agent.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import eu.squadev.mi5agent.bukkit.Mi5Agent;

public class WarningCommand implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender.hasPermission("mi5agent.warn")) {
			if(args.length > 1) {
				String reason = "";
				for(int nbrArg = 1; nbrArg < args.length; nbrArg++) {
					if(nbrArg == args.length -1) {
						reason=reason+args[nbrArg];
					}else {
						reason=reason+args[nbrArg]+" ";
					}
			}
			Mi5Agent.getManager().warn(args[0], reason, sender.getName());
			sender.sendMessage("§8§l[MI5Agent] §a"+args[0]+" §8a reçu le warn pour§c "+reason);
			if(Bukkit.getPlayer(args[0]) != null) {
				Bukkit.getPlayer(args[0]).sendMessage("§8§l[MI5Agent] §8Vous avez reçu un warn pour§c "+reason);
			}
			return true;
			}else {
				sender.sendMessage("§cUsage: /warn <player> <reason>");
				return false;
			}
		}else {
			sender.sendMessage("Tu n'as pas la permission d'exécuter cette commande.");
			return false;
		}
	}
}
