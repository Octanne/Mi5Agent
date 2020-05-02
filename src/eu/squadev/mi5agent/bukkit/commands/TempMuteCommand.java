package eu.squadev.mi5agent.bukkit.commands;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import eu.squadev.mi5agent.bukkit.Mi5Agent;
import eu.squadev.mi5agent.sanctions.MuteTempory;

public class TempMuteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(sender.hasPermission("mi5agent.tempmute")) {
			if(args.length > 1) {
				int timeCode[] = {0,0,0};
				int nbrArgs = 1;
				boolean noReason = false;
				for(int nbr = 1; nbr < args.length; nbr++) {
					nbrArgs=nbr;
					if(args[nbr].endsWith("d")) {
						try{
							timeCode[0]+=Integer.parseInt(args[nbr].replaceFirst("d", ""));
						}catch(NumberFormatException e) {
							break;
						}
					}else if(args[nbr].endsWith("h")) {
						try{
							timeCode[1]+=Integer.parseInt(args[nbr].replaceFirst("h", ""));
						}catch(NumberFormatException e) {
							break;
						}
					}else if(args[nbr].endsWith("m")) {
						try{
							timeCode[2]+=Integer.parseInt(args[nbr].replaceFirst("m", ""));
						}catch(NumberFormatException e) {
							break;
						}
					}else {
						break;
					}
				}
				if(nbrArgs == args.length-1) {noReason = true;}
				if(timeCode[0] < 1 && timeCode[1] < 1 && timeCode[2] < 1) { 
					sender.sendMessage("§cErreur: /tempmute <pseudo> <1d|1h|1m> [motif...]");
					return false;
				}else {
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, timeCode[0]);
					cal.add(Calendar.HOUR_OF_DAY, timeCode[1]);
					cal.add(Calendar.MINUTE, timeCode[2]);

					String reason = "non précisé";
					if(!noReason) {
						reason = "";
						for(int nbrArg = nbrArgs; nbrArg < args.length; nbrArg++) {
							if(nbrArg == args.length -1) {
								reason=reason+args[nbrArg];
							}else {
								reason=reason+args[nbrArg]+" ";
							}
						}
					}
					Mi5Agent.getManager().mute(args[0], reason, sender.getName(), cal);
					String sDate[] = ((MuteTempory) Mi5Agent.getManager().getLastMute(args[0])).getUntilDateToString().split(" ");
					Bukkit.broadcastMessage("§8§l[MI5Agent] §a"+args[0]+" §8a été mute pour§c "+reason+" §8jusqu'au §c"
						+ sDate[0]+"§8/§c"+sDate[1]+"§8/§c"+sDate[2]+" §c"+sDate[3]+"§8:§c"+sDate[4]+"§8:§c"+sDate[5]);
					return true;
				}
			}else {
				sender.sendMessage("§cErreur: /tempmute <pseudo> <1d|1h|1m> [motif...]");
				return false;
			}	
		}else {
			sender.sendMessage("Tu n'as pas la permission d'exécuter cette commande.");
			return false;
		}
	}
}
