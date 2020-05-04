package eu.octanne.mi5agent.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import eu.octanne.mi5agent.Mi5AgentBukkit;

public class TBanCommand implements CommandExecutor{
	
	String COMAND_TAG = "§4Ban §8|§r ";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("mi5-agent.commands.tempban")) {
			if(args.length > 2) {
				for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
					if(p.getName().equalsIgnoreCase(args[0])) {
						int values[] = {0,0,0,0,0,0};
						String units[] = {"a","mo","j","h","m","s"};
						
						for(int iUS = 0; iUS < units.length; iUS++) {
							int iUnit = args[1].indexOf(units[iUS]);
							String number = "";
							for(int i = iUnit-1; Character.isDigit(args[1].charAt(i)); i--) {
								number = args[1].charAt(i)+number;
							}
							try {
								values[iUS] = Integer.parseInt(number);
							}catch(NumberFormatException e) {
								values[iUS] = 0;
							}
						}
					}
				}
				sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas un joueur valide.");
				return false;
			}else {
				sender.sendMessage(COMAND_TAG+"§cUsage : /ban <joueur> <#a #mo #j #h #m #s> <raison>");
				return false;
			}
		}else {
			sender.sendMessage("§cErreur §8| §cCommande reservée à l'administration.");
			return false;
		}
	}
}