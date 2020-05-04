package eu.octanne.mi5agent.commands;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UBanCommand implements CommandExecutor{

	String COMAND_TAG = "§4Ban §8|§r ";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("mi5-agent.commands.ban")) {
			if(args.length > 0) {
				if(Bukkit.getBanList(Type.NAME).isBanned(args[0])) {
					Bukkit.getBanList(Type.NAME).pardon(args[0]);
					sender.sendMessage(COMAND_TAG+"§9"+args[0]+" §7viens d'être débanni.");
					return true;
				}else {
					sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas banni.");
					return false;
				}
			}else {
				sender.sendMessage(COMAND_TAG+"§cUsage : /unban <joueur>");
				return false;
			}
		}else {
			sender.sendMessage("§cErreur §8| §cCommande reservée à l'administration.");
			return false;
		}
	}

}