package eu.octanne.mi5agent.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvseeCommand implements CommandExecutor{

	String COMAND_TAG = "§eInvsee §8|§r ";
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("mi5-agent.commands.invsee")) {
				if(args.length > 0) {
					Player p = Bukkit.getPlayer(args[0]);
					if(p != null) {
						((Player) sender).openInventory(p.getInventory());
						sender.sendMessage(COMAND_TAG+"§aOuverture de l'inventaire de §9"+p.getName()+"§a.");
						return true;
					}else {
						sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas un joueur valide.");
						return false;
					}
				}else {
					sender.sendMessage(COMAND_TAG+"§cUsage : /invsee <joueur>");
					return false;
				}
			}else {
				sender.sendMessage("§cErreur §8| §cCommande reservée à l'administration.");
				return false;
			}
		}else {
			sender.sendMessage("§cErreur §8| §cCommande reservée au client.");
			return false;
		}
	}
	
}
