package eu.octanne.mi5agent.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderChestCommand implements CommandExecutor{

	String COMAND_TAG = "§5EnderChest §8|§r ";

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("mi5-agent.commands.enderchest")) {
				Player p;
				if(args.length > 0) {
					p = Bukkit.getPlayer(args[0]);
					if(p == null) {
						sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas un joueur valide.");
						return false;
					}
				}else p = (Player) sender;
				((Player) sender).openInventory(p.getEnderChest());
				sender.sendMessage(COMAND_TAG+"§a Ouverture de l'enderchest de §9"+p.getName()+"§a.");
				return true;
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
