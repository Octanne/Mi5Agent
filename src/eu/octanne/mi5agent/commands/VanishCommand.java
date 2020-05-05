package eu.octanne.mi5agent.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {
	
	static public ArrayList<UUID> vanish = new ArrayList<>();
	
	String COMAND_TAG = "§eVanish §8|§r ";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("mi5-agent.commands.vanish")) {
			Player p;
			if(!(sender instanceof Player) && args.length < 1) {
				sender.sendMessage(COMAND_TAG+"§cUsage : /vanish [joueur]");
				return false;
			}else {
				p = (Player) sender;
			}
			if(args.length > 0 && sender.hasPermission("mi5-agent.commands.vanish.other")) {
				p = Bukkit.getPlayer(args[0]);
				if(p == null) {
					sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas un joueur valide.");
					return false;
				}
			}
			if(sender.getName().equals(p.getName())) {
				if(vanish.contains(p.getUniqueId())) {
					for(Player ps : Bukkit.getOnlinePlayers()) {
						if(!p.hasPermission("mi5-agent.commands.vanish")) {
							ps.showPlayer(p);
						}
					}
					vanish.remove(p.getUniqueId());
					sender.sendMessage(COMAND_TAG+"§7Vous n'êtes plus en vanish.");
				}else {
					for(Player ps : Bukkit.getOnlinePlayers()) {
						if(!p.hasPermission("mi5-agent.commands.vanish")) {
							ps.hidePlayer(p);
						}
					}
					vanish.add(p.getUniqueId());
					sender.sendMessage(COMAND_TAG+"§aVous êtes désormais en vanish.");
				}
				return true;
			}else {
				if(vanish.contains(p.getUniqueId())) {
					for(Player ps : Bukkit.getOnlinePlayers()) {
						if(!p.hasPermission("mi5-agent.commands.vanish")) {
							ps.showPlayer(p);
						}
					}
					vanish.remove(p.getUniqueId());
					sender.sendMessage(COMAND_TAG+"§9"+p.getName()+" §7n'est plus en vanish.");
				}else {
					for(Player ps : Bukkit.getOnlinePlayers()) {
						if(!p.hasPermission("mi5-agent.commands.vanish")) {
							ps.hidePlayer(p);
						}
					}
					vanish.add(p.getUniqueId());
					sender.sendMessage(COMAND_TAG+"§9"+p.getName()+" §aest désormais en vanish.");
					sender.sendMessage(COMAND_TAG+"§aVous êtes désormais en vanish.");
				}
				return true;
			}
		}else {
			sender.sendMessage("§cErreur §8| §cCommande reservée à l'administration.");
			return false;
		}
	}
}
