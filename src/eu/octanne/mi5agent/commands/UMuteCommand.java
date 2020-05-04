package eu.octanne.mi5agent.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import eu.octanne.mi5agent.Mi5AgentBukkit;

public class UMuteCommand implements CommandExecutor{

	String COMAND_TAG = "§cMute §8|§r ";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("mi5-agent.commands.mute")) {
			if(args.length > 0) {
				for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
					if(p.getName().equalsIgnoreCase(args[0])) {
						if(Mi5AgentBukkit.isMute(p.getPlayer())) {
							Mi5AgentBukkit.getContainer().unMute(p.getUniqueId());
							sender.sendMessage(COMAND_TAG+"§9"+p.getName()+" §7viens d'être démuté.");
							if(p.isOnline()) {
								p.getPlayer().sendMessage(COMAND_TAG+"§7Vous venez d'être démuté par §9"+sender.getName());
							}
							return true;
						}else {
							sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas mute.");
							return false;
						}
					}
				}
				sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas un joueur valide.");
				return false;
			}else {
				sender.sendMessage(COMAND_TAG+"§cUsage : /unmute <joueur>");
				return false;
			}
		}else {
			sender.sendMessage("§cErreur §8| §cCommande reservée à l'administration.");
			return false;
		}
	}

}
