package eu.octanne.mi5agent.commands;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.octanne.mi5agent.Mi5AgentBukkit;
import eu.octanne.mi5agent.sanctions.Ban;

public class TBanCommand implements CommandExecutor{
	
	String COMAND_TAG = "§4Ban §8|§r ";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("mi5-agent.commands.tempban")) {
			if(args.length > 2) {
				for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
					if(p.getName().equalsIgnoreCase(args[0])) {
						// GET TIME
						int values[] = {0,0,0,0,0,0};
						String units[] = {"a","mo","j","h","m","s"};
						for(int iUS = 0; iUS < units.length; iUS++) {
							int iUnit = args[1].indexOf(units[iUS]);
							String number = "";
							for(int i = iUnit-1; Character.isDigit(args[1].charAt(i)) && i > -1; i--) {
								number = args[1].charAt(i)+number;
							}
							try {
								values[iUS] = Integer.parseInt(number);
							}catch(NumberFormatException e) {
								values[iUS] = 0;
							}
						}
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.YEAR, values[0]); cal.add(Calendar.MONTH, values[1]);
						cal.add(Calendar.DAY_OF_YEAR, values[3]); cal.add(Calendar.HOUR_OF_DAY, values[0]);
						cal.add(Calendar.MINUTE, values[0]); cal.add(Calendar.SECOND, values[0]);
						
						//GET REASON
						String reason = "";
						for(int i = 2; i < args.length; i++) {
							reason+=" "+args[i];
						} reason = reason.substring(1);
						// Ban PART
						Ban ban = Mi5AgentBukkit.getContainer().applyBan(p.getUniqueId(), sender instanceof Player ? ((Player) sender).getUniqueId() : null, reason, cal);
						sender.sendMessage(COMAND_TAG+"§9"+p.getName()+" §7viens d'être banni pour : §9"+reason+" §8(durée : "+ban.getUntilTime()+"§7)");
						if(p.isOnline()) {
							String[] sDate = ban.getUntilDateToString().split(" ");
							p.getPlayer().kickPlayer("\n§8§lVous avez été Banni" + "\n" +"par §c§l" + ban.getSanctionerName() + "\n" +"§8§lPour §c§l" + ban.getReason() + "\n" 
									+ "§8Jusqu'au §c" + sDate[0]+"§8/§c"+sDate[1]+"§8/§c"+sDate[2]+" §c"+sDate[3]+"§8:§c"+sDate[4]+"§8:§c"+sDate[5]);
						}
						return true;
					}
				}
				sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas un joueur valide.");
				return false;
			}else {
				sender.sendMessage(COMAND_TAG+"§cUsage : /tempban <joueur> <#a #mo #j #h #m #s> <raison>");
				return false;
			}
		}else {
			sender.sendMessage("§cErreur §8| §cCommande reservée à l'administration.");
			return false;
		}
	}
}