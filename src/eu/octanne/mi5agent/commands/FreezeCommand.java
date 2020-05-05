package eu.octanne.mi5agent.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.octanne.mi5agent.Mi5AgentBukkit;

public class FreezeCommand implements CommandExecutor{

	String COMAND_TAG = "§bFreeze §8|§r ";
	
	static public ArrayList<UUID> freeze = new ArrayList<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("mi5-agent.commands.freeze")) {
			if(args.length > 0) {
				Player p = Bukkit.getPlayer(args[0]);
				if(p != null) {
					if(!freeze.contains(p.getUniqueId())) {
						sender.sendMessage(COMAND_TAG+"§9"+p.getName()+" §aest désormais freeze.");
						p.sendMessage(COMAND_TAG+Mi5AgentBukkit.instance.getConfig().getString("freeze-message", "null").replace("{MODO}", sender.getName()));
						p.teleport(p.getWorld().getSpawnLocation());
						//p.setWalkSpeed(0f);
						freeze.add(p.getUniqueId());
					}else {
						sender.sendMessage(COMAND_TAG+"§9"+p.getName()+" §an'est plus freeze.");
						//p.setWalkSpeed(0.3f);
						p.sendMessage(COMAND_TAG+"§7Vous n'êtes désormais plus freeze.");
						freeze.remove(p.getUniqueId());
					}
					return true;
				}else {
					sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas un joueur valide.");
					return false;
				}
			}else {
				sender.sendMessage(COMAND_TAG+"§cUsage : /freeze <joueur>");
				return false;
			}
		}else {
			sender.sendMessage("§cErreur §8| §cCommande reservée à l'administration.");
			return false;
		}
	}

}
