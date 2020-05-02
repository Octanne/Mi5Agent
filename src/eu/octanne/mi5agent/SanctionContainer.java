package eu.octanne.mi5agent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.octanne.mi5agent.sanctions.Ban;
import eu.octanne.mi5agent.sanctions.Mute;
import eu.octanne.mi5agent.sanctions.Sanction;
import eu.octanne.mi5agent.sanctions.Warning;

public class SanctionContainer {

	String pathFolder = "plugins/Mi-5Agent/data/";

	private void saveSanction(Sanction sanction) {
		File file = new File(pathFolder+sanction.getID().toString()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if(sanction instanceof Ban) {
			config.set(".type", "ban");
			config.set(".sanctionerID", sanction.getSanctionerID());
			config.set(".reason", sanction.getReason());
			config.set(".date", sanction.getDate());
			config.set(".untilDate", ((Ban) sanction).getUntilDate() != null ?
					((Ban) sanction).getUntilDate().getTimeInMillis() : 0);
		}else if(sanction instanceof Mute) {
			config.set(".type", "mute");
			config.set(".sanctionerID", sanction.getSanctionerID());
			config.set(".reason", sanction.getReason());
			config.set(".date", sanction.getDate());
			config.set(".untilDate", ((Mute) sanction).getUntilDate() != null ?
					((Mute) sanction).getUntilDate().getTimeInMillis() : 0);
		}else if(sanction instanceof Warning) {
			config.set(".type", "ban");
			config.set(".sanctionerID", sanction.getSanctionerID());
			config.set(".reason", sanction.getReason());
			config.set(".date", sanction.getDate());
		}
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * getSanctions
	 */
	public List<Sanction> getSanctions(UUID playerID){
		File file = new File(pathFolder+playerID.toString()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		List<Sanction> sanctions = new ArrayList<>();
		for(String path : config.getKeys(false)) {
			String type = config.getString(path+".type", "null");
			if(!type.equalsIgnoreCase("null")) {
				String strID = config.getString(path+".sanctionerID", null);
				UUID sanctionerID = strID != null ? UUID.fromString(strID) : null;
				String reason = config.getString(path+".reason", "aucune raison renseignée");
				Calendar date = Calendar.getInstance();
				date.setTimeInMillis(config.getLong(path+".date", 0));
				if(type.equalsIgnoreCase("warn")) {
					sanctions.add(new Warning(playerID, sanctionerID, reason, UUID.fromString(path), date));
				}else if(type.equalsIgnoreCase("ban")) {
					Calendar untilDate = Calendar.getInstance();
					untilDate.setTimeInMillis(config.getLong(path+".untilDate", 0));
					if(untilDate.getTimeInMillis() == 0) untilDate = null;
					sanctions.add(new Ban(playerID, sanctionerID, reason, UUID.fromString(path), date, untilDate));
				}else if(type.equalsIgnoreCase("mute")) {
					Calendar untilDate = Calendar.getInstance();
					untilDate.setTimeInMillis(config.getLong(path+".untilDate", 0));
					if(untilDate.getTimeInMillis() == 0) untilDate = null;
					sanctions.add(new Mute(playerID, sanctionerID, reason, UUID.fromString(path), date, untilDate));
				}
			}
		}
		return sanctions;
	}

	/*
	 * checkBan
	 */
	public Ban checkBan(UUID playerID) {
		if(Bukkit.getOfflinePlayer(playerID).isBanned()) {
			for(Sanction s : getSanctions(playerID)) {
				if(s instanceof Ban) return (Ban) s;
			}
			return null;
		}else {
			return null;
		}
	}

	/*
	 * checkMute
	 */
	public Mute checkMute(UUID playerID) {
		for(Sanction s : getSanctions(playerID)) {
			if(s instanceof Mute) {
				if(((Mute) s).getUntilDate().after(Calendar.getInstance())) {
					return (Mute) s;
				}else return null;
			}
		}
		return null;
	}

	/*
	 * applyBan
	 */
	public boolean applyBan(UUID playerID, @Nullable UUID sanctionerID, String reason, @Nullable Calendar untilDate) {
		OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(playerID);
		if(oPlayer != null) {
			String bannerName = sanctionerID == null ? "Console" : Bukkit.getOfflinePlayer(playerID).getName();
			Bukkit.getBanList(Type.NAME).addBan(oPlayer.getName(), 
				reason, untilDate != null ? untilDate.getTime() : null, bannerName);
			saveSanction(new Ban(playerID, sanctionerID, reason, null, null, untilDate));
			if(oPlayer.isOnline()) {
				Bukkit.broadcastMessage("§1Ban §8| §r"+oPlayer.getName()+" banni par "+bannerName+" pour "+reason);
			}
			return true;
		}else return false;
	}

	/*
	 * applyMute
	 */
	public boolean applyMute(UUID playerID, @Nullable UUID sanctionerID, String reason, @Nullable Calendar untilDate) {
		OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(playerID);
		if(oPlayer != null) {
			String bannerName = sanctionerID == null ? "Console" : Bukkit.getOfflinePlayer(playerID).getName();
			saveSanction(new Mute(playerID, sanctionerID, reason, null, null, untilDate));
			if(oPlayer.isOnline()) {
				Bukkit.broadcastMessage("§1Mute §8| §r"+oPlayer.getName()+" mute par "+bannerName+" pour "+reason);
			}
			return true;
		}else return false;
	}
	
	/*
	 * addWarn
	 */
	public boolean addWarn(UUID playerID, @Nullable UUID sanctionerID, String reason) {
		OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(playerID);
		if(oPlayer != null) {
			String bannerName = sanctionerID == null ? "Console" : Bukkit.getOfflinePlayer(playerID).getName();
			saveSanction(new Warning(playerID, sanctionerID, reason, null, null));
			if(oPlayer.isOnline()) {
				Bukkit.broadcastMessage("§1Warn §8| §r"+oPlayer.getName()+" warn par "+bannerName+" pour "+reason);
			}
			return true;
		}else return false;
	}
	
	/*
	 * removeSanction
	 */
	public boolean removeSanction(UUID playerID, UUID sanID) {
		File file = new File(pathFolder+sanID.toString()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if(config.isSet(sanID.toString())) {
			config.set(sanID.toString(), null);
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}else return false;
	}
}
