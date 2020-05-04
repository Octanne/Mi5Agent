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
		File file = new File(pathFolder+sanction.getPlayerID().toString()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if(sanction instanceof Ban) {
			config.set(sanction.getID()+".type", "ban");
			config.set(sanction.getID()+".sanctionerID", sanction.getSanctionerID().toString());
			config.set(sanction.getID()+".reason", sanction.getReason());
			config.set(sanction.getID()+".date", sanction.getDate().getTimeInMillis());
			config.set(sanction.getID()+".untilDate", ((Ban) sanction).getUntilDate() != null ?
					((Ban) sanction).getUntilDate().getTimeInMillis() : 0);
		}else if(sanction instanceof Mute) {
			config.set(sanction.getID()+".type", "mute");
			config.set(sanction.getID()+".sanctionerID", sanction.getSanctionerID().toString());
			config.set(sanction.getID()+".reason", sanction.getReason());
			config.set(sanction.getID()+".date", sanction.getDate().getTimeInMillis());
			config.set(sanction.getID()+".enable", ((Mute) sanction).hisEnable());
			config.set(sanction.getID()+".untilDate", ((Mute) sanction).getUntilDate() != null ?
					((Mute) sanction).getUntilDate().getTimeInMillis() : 0);
		}else if(sanction instanceof Warning) {
			config.set(sanction.getID()+".type", "warn");
			config.set(sanction.getID()+".sanctionerID", sanction.getSanctionerID().toString());
			config.set(sanction.getID()+".reason", sanction.getReason());
			config.set(sanction.getID()+".date", sanction.getDate().getTimeInMillis());
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
					boolean enable = config.getBoolean(path+".enable");
					sanctions.add(new Mute(playerID, sanctionerID, reason, UUID.fromString(path), date, untilDate, enable));
				}
			}
		}
		return sanctions;
	}

	public boolean unMute(UUID playerID) {
		Mute mute = checkMute(playerID);
		if(mute != null) {
			mute.setEnable(false);
			Mi5AgentBukkit.mutePlayers.remove(playerID);
			saveSanction(mute);
			return true;
		}else return false;
	}
	
	/*
	 * checkBan
	 */
	public Ban checkBan(UUID playerID) {
		if(Bukkit.getOfflinePlayer(playerID).isBanned()) {
			for(Sanction s : getSanctions(playerID)) {
				if(s instanceof Ban) {
					return (Ban) s;
				}
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
				if(((Mute) s).isActive()) {
					return (Mute) s;
				}else return null;
			}
		}
		return null;
	}

	/*
	 * applyBan
	 */
	public Ban applyBan(UUID playerID, @Nullable UUID sanctionerID, String reason, @Nullable Calendar untilDate) {
		OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(playerID);
		if(oPlayer != null) {
			String bannerName = sanctionerID == null ? "Console" : Bukkit.getOfflinePlayer(playerID).getName();
			Bukkit.getBanList(Type.NAME).addBan(oPlayer.getName(), 
				reason, untilDate != null ? untilDate.getTime() : null, bannerName);
			Ban ban = new Ban(playerID, sanctionerID, reason, null, null, untilDate);
			saveSanction(ban);
			return ban;
		}else return null;
	}

	/*
	 * applyMute
	 */
	public Mute applyMute(UUID playerID, @Nullable UUID sanctionerID, String reason, @Nullable Calendar untilDate) {
		OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(playerID);
		if(oPlayer != null) {
			Mute mute = new Mute(playerID, sanctionerID, reason, null, null, untilDate, true);
			Mi5AgentBukkit.mutePlayers.put(playerID, mute);
			saveSanction(mute);
			return mute;
		}else return null;
	}
	
	/*
	 * addWarn
	 */
	public Warning applyWarn(UUID playerID, @Nullable UUID sanctionerID, String reason) {
		OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(playerID);
		if(oPlayer != null) {
			Warning warn = new Warning(playerID, sanctionerID, reason, null, null);
			saveSanction(warn);
			return warn;
		}else return null;
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
