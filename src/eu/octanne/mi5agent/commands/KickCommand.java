package eu.octanne.mi5agent.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.octanne.mi5agent.Mi5AgentBukkit;

public class KickCommand implements CommandExecutor{

	String COMAND_TAG = "§cKick §8|§r ";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("mi5-agent.commands.kick")) {
			if(args.length > 1) {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(p.getName().equalsIgnoreCase(args[0])) {
						String reason = "";
						for(int i = 1; i < args.length; i++) {
							reason+=" "+args[i];
						}
						reason = reason.substring(1);
						Mi5AgentBukkit.getContainer().applyKick(p.getUniqueId(), 
								sender instanceof Player ? ((Player) sender).getUniqueId() : null, reason);
						sender.sendMessage(COMAND_TAG+"§9"+p.getName()+" §7viens d'être éjecté pour : §9"+reason);
						p.getPlayer().kickPlayer("\n§7Vous avez été éjecté :" 
								+ "\n\n" +"par §c" + sender.getName() + "\n\n" 
								+"§7MOTIF :\n" + "§c" + reason);
						return true;
					}
				}
				sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas un joueur valide.");
				return false;
			}else {
				sender.sendMessage(COMAND_TAG+"§cUsage : /kick <joueur> <raison>");
				return false;
			}
		}else {
			sender.sendMessage("§cErreur §8| §cCommande reservée à l'administration.");
			return false;
		}
	}
	
}
