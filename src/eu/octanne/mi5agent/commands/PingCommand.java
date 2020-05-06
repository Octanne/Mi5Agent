package eu.octanne.mi5agent.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

	String COMAND_TAG = "§aPing §8|§r ";
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("mi5-agent.commands.ping")) {
			Player p;
			if(args.length > 0) {
				p = Bukkit.getPlayer(args[0]);
				if(p == null) {
					sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas un joueur valide.");
					return false;
				}
			}else if(sender instanceof Player) p = (Player) sender;
			else {
				sender.sendMessage(COMAND_TAG+"§cUsage : /ping <joueur>");
				return false;
			}
			sender.sendMessage(COMAND_TAG+"§7 Le ping de §9"+p.getName()+" est de §9"+((CraftPlayer) p).getHandle().ping+"§ams.");
			return true;
		}else {
			sender.sendMessage("§cErreur §8| §cCommande reservée à l'administration.");
			return false;
		}
	}

}
