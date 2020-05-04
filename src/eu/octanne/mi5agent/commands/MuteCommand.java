package eu.octanne.mi5agent.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.octanne.mi5agent.Mi5AgentBukkit;
import eu.octanne.mi5agent.sanctions.Mute;

public class MuteCommand implements CommandExecutor{
	
	String COMAND_TAG = "§cMute §8|§r ";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("mi5-agent.commands.mute")) {
			if(args.length > 1) {
				for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
					if(p.getName().equalsIgnoreCase(args[0])) {
						String reason = "";
						for(int i = 1; i < args.length; i++) {
							reason+=" "+args[i];
						}
						reason = reason.substring(1);
						Mute mute = Mi5AgentBukkit.getContainer().applyMute(p.getUniqueId(), 
								sender instanceof Player ? ((Player) sender).getUniqueId() : null, reason, null);
						Mi5AgentBukkit.mutePlayers.put(p.getUniqueId(), mute);
						sender.sendMessage(COMAND_TAG+"§9"+p.getName()+" §7viens d'être mute pour : §9"+reason);
						if(p.isOnline()) {
							sender.sendMessage(COMAND_TAG+"§7Vous venez d'être mute par §9"+sender.getName()+" §7pour : §9"+reason);
						}
						return true;
					}
				}
				sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas un joueur valide.");
				return false;
			}else {
				sender.sendMessage(COMAND_TAG+"§cUsage : /mute <joueur> <raison>");
				return false;
			}
		}else {
			sender.sendMessage("§cErreur §8| §cCommande reservée à l'administration.");
			return false;
		}
	}
	
}
