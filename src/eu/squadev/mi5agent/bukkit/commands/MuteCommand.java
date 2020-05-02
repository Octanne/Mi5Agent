package eu.squadev.mi5agent.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import eu.squadev.mi5agent.bukkit.Mi5Agent;

public class MuteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender.hasPermission("mi5agent.mute")) {
			if(args.length > 0) {
				String reason = "non précisé";
				if(args.length > 1) {
					reason = "";
					for(int nbrArg = 1; nbrArg < args.length; nbrArg++) {
						if(nbrArg == args.length -1) {
							reason=reason+args[nbrArg];
						}else {
							reason=reason+args[nbrArg]+" ";
						}
					}
				}
				Mi5Agent.getManager().mute(args[0], reason, sender.getName());
				Bukkit.broadcastMessage("§8§l[MI5Agent] §a"+args[0]+" §8a été mute pour§c "+reason);
				return true;
			}else {
				sender.sendMessage("§cUsage: /mute <player> [reason]");
				return false;
			}
		}else {
			sender.sendMessage("Tu n'as pas la permission d'exécuter cette commande.");
			return false;
		}
	}
}
