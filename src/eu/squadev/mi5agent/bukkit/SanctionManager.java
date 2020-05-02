package eu.squadev.mi5agent.bukkit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import eu.squadev.mi5agent.sanctions.Ban;
import eu.squadev.mi5agent.sanctions.BanTempory;
import eu.squadev.mi5agent.sanctions.Mute;
import eu.squadev.mi5agent.sanctions.MuteTempory;
import eu.squadev.mi5agent.sanctions.Warning;


public class SanctionManager implements Listener {

	private String path = "plugins/Mi5Agent/";

	public SanctionManager(String path) {
		this.path = path;
		Bukkit.getPluginManager().registerEvents(this, Mi5Agent.getInstance());
	}

	public void openCasier(String playerName, Player executorPlayer) {
		
	}

	public boolean unBan(String playerName) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		if(hasEverBeenBan(playerName)) {
			config.set("LastestBan", null);
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}else return false;
	}
	public boolean unMute(String playerName) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		if(hasEverBeenMute(playerName)) {
			config.set("LastestMute", null);
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}else return false;
	}

	public void ban(String playerName, String reason, String executorName) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		Ban ban = new Ban(playerName, executorName);
		ban.setReason(reason);
		if(Bukkit.getPlayer(playerName) != null) {
			if(ban.getType().equalsIgnoreCase("Ban")){
				Bukkit.getPlayer(playerName).kickPlayer("§8§lVous avez été Banni" + "\n" +"par §c§l" + ban.getExecutorName() + "\n" +"§8§lPour §c§l" + ban.getReason());
			}
		}

		if(hasEverBeenBan(playerName)) {
			Ban lastBan = getLastBan(playerName);
			int banNumber = 0;
			for(int nbrBan = 0; config.isConfigurationSection("Ban-History.Ban-"+nbrBan); nbrBan++) {banNumber++;}

			config.set("Ban-History.Ban-"+banNumber+".playerName", lastBan.getPlayerName());
			config.set("Ban-History.Ban-"+banNumber+".executorName", lastBan.getExecutorName());
			config.set("Ban-History.Ban-"+banNumber+".reason", lastBan.getReason());
			config.set("Ban-History.Ban-"+banNumber+".date", lastBan.getCreationDateToString());
			if(lastBan.getType().equalsIgnoreCase("BanTempory")) config.set("Ban-History.Ban-"+banNumber+".untilDate", ((BanTempory) lastBan).getUntilDateToString());

			config.set("LastestBan.playerName", ban.getPlayerName());
			config.set("LastestBan.executorName", ban.getExecutorName());
			config.set("LastestBan.reason", ban.getReason());
			config.set("LastestBan.date", ban.getCreationDateToString());

			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			config.set("LastestBan.playerName", ban.getPlayerName());
			config.set("LastestBan.executorName", ban.getExecutorName());
			config.set("LastestBan.reason", ban.getReason());
			config.set("LastestBan.date", ban.getCreationDateToString());

			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void ban(String playerName, String reason, String executorName, Calendar untilDate) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		BanTempory ban = new BanTempory(playerName, executorName, untilDate);
		ban.setReason(reason);
		if(Bukkit.getPlayer(playerName) != null) {
			if(ban.getType().equalsIgnoreCase("Ban")){
				Bukkit.getPlayer(playerName).kickPlayer("§8§lVous avez été Banni" + "\n" +"par §c§l" + ban.getExecutorName() + "\n" +"§8§lPour §c§l" + ban.getReason());
			}else {
				String[] sDate = ((BanTempory) ban).getUntilDateToString().split(" ");
				Bukkit.getPlayer(playerName).kickPlayer("§8§lVous avez été Banni" + "\n" +"par §c§l" + ban.getExecutorName() + "\n" +"§8§lPour §c§l" + ban.getReason() + "\n" 
						+ "§8Jusqu'au §c" + sDate[0]+"§8/§c"+sDate[1]+"§8/§c"+sDate[2]+" §c"+sDate[3]+"§8:§c"+sDate[4]+"§8:§c"+sDate[5]);
			}
		}

		if(hasEverBeenBan(playerName)) {
			Ban lastBan = getLastBan(playerName);
			int banNumber = 0;
			for(int nbrBan = 1; config.isConfigurationSection("Ban-History.Ban-"+nbrBan); nbrBan++) {banNumber++;}

			config.set("Ban-History.Ban-"+banNumber+".playerName", lastBan.getPlayerName());
			config.set("Ban-History.Ban-"+banNumber+".executorName", lastBan.getExecutorName());
			config.set("Ban-History.Ban-"+banNumber+".reason", lastBan.getReason());
			config.set("Ban-History.Ban-"+banNumber+".date", lastBan.getCreationDateToString());
			if(lastBan.getType().equalsIgnoreCase("BanTempory")) config.set("Ban-History.Ban-"+banNumber+".untilDate", ((BanTempory) lastBan).getUntilDateToString());

			config.set("LastestBan.playerName", ban.getPlayerName());
			config.set("LastestBan.executorName", ban.getExecutorName());
			config.set("LastestBan.reason", ban.getReason());
			config.set("LastestBan.date", ban.getCreationDateToString());
			config.set("LastestBan.untilDate", ban.getUntilDateToString());

			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			config.set("LastestBan.playerName", ban.getPlayerName());
			config.set("LastestBan.executorName", ban.getExecutorName());
			config.set("LastestBan.reason", ban.getReason());
			config.set("LastestBan.date", ban.getCreationDateToString());
			config.set("LastestBan.untilDate", ban.getUntilDateToString());

			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void mute(String playerName, String reason, String executorName) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		Mute mute = new Mute(playerName, executorName);
		mute.setReason(reason);

		if(hasEverBeenMute(playerName)) {
			Mute lastMute = getLastMute(playerName);
			int muteNumber = 0;
			for(int nbrMute = 1; config.isConfigurationSection("Mute-History.Mute-"+nbrMute); nbrMute++) {muteNumber++;}

			config.set("Mute-History.Mute-"+muteNumber+".playerName", lastMute.getPlayerName());
			config.set("Mute-History.Mute-"+muteNumber+".executorName", lastMute.getExecutorName());
			config.set("Mute-History.Mute-"+muteNumber+".reason", lastMute.getReason());
			config.set("Mute-History.Mute-"+muteNumber+".date", lastMute.getCreationDateToString());
			if(lastMute.getType().equalsIgnoreCase("MuteTempory")) config.set("Mute-History.Mute-"+muteNumber+".untilDate", ((MuteTempory) lastMute).getUntilDateToString());

			config.set("LastestMute.playerName", mute.getPlayerName());
			config.set("LastestMute.executorName", mute.getExecutorName());
			config.set("LastestMute.reason", mute.getReason());
			config.set("LastestMute.date", mute.getCreationDateToString());

			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			config.set("LastestMute.playerName", mute.getPlayerName());
			config.set("LastestMute.executorName", mute.getExecutorName());
			config.set("LastestMute.reason", mute.getReason());
			config.set("LastestMute.date", mute.getCreationDateToString());

			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void mute(String playerName, String reason, String executorName, Calendar untilDate) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		MuteTempory mute = new MuteTempory(playerName, executorName, untilDate);
		mute.setReason(reason);

		if(hasEverBeenMute(playerName)) {
			Mute lastMute = getLastMute(playerName);
			int muteNumber = 0;
			for(int nbrMute = 1; config.isConfigurationSection("Mute-History.Mute-"+nbrMute); nbrMute++) {muteNumber++;}

			config.set("Mute-History.Mute-"+muteNumber+".playerName", lastMute.getPlayerName());
			config.set("Mute-History.Mute-"+muteNumber+".executorName", lastMute.getExecutorName());
			config.set("Mute-History.Mute-"+muteNumber+".reason", lastMute.getReason());
			config.set("Mute-History.Mute-"+muteNumber+".date", lastMute.getCreationDateToString());
			if(lastMute.getType().equalsIgnoreCase("MuteTempory")) config.set("Mute-History.Mute-"+muteNumber+".untilDate", ((MuteTempory) lastMute).getUntilDateToString());

			config.set("LastestMute.playerName", mute.getPlayerName());
			config.set("LastestMute.executorName", mute.getExecutorName());
			config.set("LastestMute.reason", mute.getReason());
			config.set("LastestMute.date", mute.getCreationDateToString());
			config.set("LastestMute.untilDate", mute.getUntilDateToString());

			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			config.set("LastestMute.playerName", mute.getPlayerName());
			config.set("LastestMute.executorName", mute.getExecutorName());
			config.set("LastestMute.reason", mute.getReason());
			config.set("LastestMute.date", mute.getCreationDateToString());
			config.set("LastestMute.untilDate", mute.getUntilDateToString());

			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void warn(String playerName, String reason, String executorName) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		Warning warning = new Warning(playerName, executorName);
		warning.setReason(reason);

		if(hasEverBeenWarn(playerName)) {
			Warning lastWarn = getLastWarn(playerName);
			int warnNumber = 0;
			for(int nbrWarn = 1; config.isConfigurationSection("Warn-History.Warn-"+nbrWarn); nbrWarn++) {warnNumber++;}

			config.set("Warn-History.Warn-"+warnNumber+".playerName", lastWarn.getPlayerName());
			config.set("Warn-History.Warn-"+warnNumber+".executorName", lastWarn.getExecutorName());
			config.set("Warn-History.Warn-"+warnNumber+".reason", lastWarn.getReason());
			config.set("Warn-History.Warn-"+warnNumber+".date", lastWarn.getCreationDateToString());

			config.set("LastestWarn.playerName", warning.getPlayerName());
			config.set("LastestWarn.executorName", warning.getExecutorName());
			config.set("LastestWarn.reason", warning.getReason());
			config.set("LastestWarn.date", warning.getCreationDateToString());

			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			config.set("LastestWarn.playerName", warning.getPlayerName());
			config.set("LastestWarn.executorName", warning.getExecutorName());
			config.set("LastestWarn.reason", warning.getReason());
			config.set("LastestWarn.date", warning.getCreationDateToString());

			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean hasEverBeenBan(String playerName) {
		if(getLastBan(playerName) != null) {
			return true;
		}else {
			return false;
		}
	}
	public boolean hasEverBeenMute(String playerName) {
		if(getLastMute(playerName) != null) {
			return true;
		}else {
			return false;
		}
	}
	public boolean hasEverBeenWarn(String playerName) {
		if(getLastWarn(playerName) != null) {
			return true;
		}else {
			return false;
		}
	}

	public boolean isMute(String playerName) {
		if(getLastMute(playerName) != null) {
			if(getLastMute(playerName).getType().equalsIgnoreCase("MuteTempory")) {
				if(((MuteTempory)getLastMute(playerName)).isActive()) {
					return true;
				}else return false;
			}else return true;
		}else return false;
	}
	public boolean isBan(String playerName) {
		if(getLastBan(playerName) != null) {
			if(getLastBan(playerName).getType().equalsIgnoreCase("BanTempory")){
				if(((BanTempory)getLastBan(playerName)).isActive()) {
					return true;
				}else return false;
			}else return false;
		}else return false;
	}

	public Ban getLastBan(String playerName) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		if(config.isConfigurationSection("LastestBan")) {
			if(config.isSet("LastestBan.untilDate")) {
				BanTempory ban = new BanTempory(playerName, config.getString("LastestBan.executorName"), config.getString("LastestBan.untilDate"));
				ban.setReason(config.getString("LastestBan.reason"));
				ban.setDate(config.getString("LastestBan.date"));
				return ban;
			}else {
				Ban ban = new Ban(playerName, config.getString("LastestBan.executorName"));
				ban.setReason(config.getString("LastestBan.reason"));
				ban.setDate(config.getString("LastestBan.date"));
				return ban;
			}

		}else return null;
	}
	public Mute getLastMute(String playerName) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		if(config.isConfigurationSection("LastestMute")) {
			if(config.isSet("LastestMute.untilDate")) {
				MuteTempory mute = new MuteTempory(playerName, config.getString("LastestMute.executorName"), config.getString("LastestMute.untilDate"));
				mute.setReason(config.getString("LastestMute.reason"));
				mute.setDate(config.getString("LastestMute.date"));
				return mute;
			}else {
				Mute mute = new Mute(playerName, config.getString("LastestMute.executorName"));
				mute.setReason(config.getString("LastestMute.reason"));
				mute.setDate(config.getString("LastestMute.date"));
				return mute;
			}

		}else return null;
	}
	public Warning getLastWarn(String playerName) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if(config.isConfigurationSection("LastestWarn")) {
			Warning warn = new Warning(playerName, config.getString("LastestWarn.executorName"));
			warn.setReason(config.getString("LastestWarn.reason"));
			warn.setDate(config.getString("LastestWarn.date"));
			return warn;
		}else return null;
	}

	public ArrayList<Warning> getWarns(String playerName) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		ArrayList<Warning> warns = new ArrayList<Warning>();

		if(config.isConfigurationSection("LastestWarn") || config.isConfigurationSection("Warn-History")) {
			for(int warnNumber = 1; config.isConfigurationSection("Warn-History.Warn-"+warnNumber); warnNumber++) {
				Warning warn = new Warning(config.getString("Warn-History.Warn-"+warnNumber+".playerName"), config.getString("Warn-History.Warn-"+warnNumber+".executorName"));
				warn.setReason(config.getString("Warn-History.Warn-"+warnNumber+".reason"));
				warn.setDate(config.getString("Warn-History.Warn-"+warnNumber+".date"));
				warns.add(warn);
			}
			if(getLastMute(playerName) != null) {
				warns.add(getLastWarn(playerName));
			}
			return warns;
		}else return null;
	}
	public ArrayList<Mute> getMutes(String playerName) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		ArrayList<Mute> mutes = new ArrayList<Mute>();

		if(config.isConfigurationSection("LastestMute") || config.isConfigurationSection("Mute-History")) {
			for(int muteNumber = 1; config.isConfigurationSection("Mute-History.Mute-"+muteNumber); muteNumber++) {
				Mute mute = new Mute(config.getString("Mute-History.Mute-"+muteNumber+".playerName"), config.getString("Mute-History.Mute-"+muteNumber+".executorName"));
				mute.setReason(config.getString("Mute-History.Mute-"+muteNumber+".reason"));
				mute.setDate(config.getString("Mute-History.Mute-"+muteNumber+".date"));
				if(config.isSet("Mute-History.Mute-"+muteNumber+".untilDate")) ((MuteTempory) mute).setUntilDate(config.getString("Mute-History.Mute-"+muteNumber+".untilDate"));
				mutes.add(mute);
			}
			if(getLastMute(playerName) != null) {
				mutes.add(getLastMute(playerName));
			}
			return mutes;
		}else {
			return null;
		}
	}
	public ArrayList<Ban> getBans(String playerName) {
		File file = new File(path+playerName+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		ArrayList<Ban> bans = new ArrayList<Ban>();

		if(config.isConfigurationSection("LastestBan") || config.isConfigurationSection("Ban-History")) {
			for(int banNumber = 1; config.isConfigurationSection("Ban-History.Ban-"+banNumber); banNumber++) {
				Ban ban = new Ban(config.getString("Ban-History.Ban-"+banNumber+".playerName"), config.getString("Ban-History.Ban-"+banNumber+".executorName"));
				ban.setReason(config.getString("Ban-History.Ban-"+banNumber+".reason"));
				ban.setDate(config.getString("Ban-History.Ban-"+banNumber+".date"));
				if(config.isSet("Ban-History.Ban-"+banNumber+".untilDate")) ((BanTempory) ban).setUntilDate(config.getString("Ban-History.Ban-"+banNumber+".untilDate"));
				bans.add(ban);
			}
			if(getLastBan(playerName) != null) {
				bans.add(getLastBan(playerName));
			}
			return bans;
		}else {
			return null;
		}
	}

	@EventHandler
	public void onJoinPlayer(PlayerLoginEvent e) {
		if(isBan(e.getPlayer().getName())) {
			e.setResult(Result.KICK_BANNED);
			Player p = e.getPlayer();
			Ban ban = getLastBan(p.getName());
			if(ban.getType().equalsIgnoreCase("Ban")){
				e.setKickMessage("\n§8§lVous avez été Banni" + "\n" +"par §c§l" + ban.getExecutorName() + "\n" +"§8§lPour §c§l" + ban.getReason());
			}else{
				String[] sDate = ((BanTempory) ban).getUntilDateToString().split(" ");
				e.setKickMessage("\n§8§lVous avez été Banni" + "\n" +"par §c§l" + ban.getExecutorName() + "\n" +"§8§lPour §c§l" + ban.getReason() + "\n" 
						+ "§8Jusqu'au §c" + sDate[0]+"§8/§c"+sDate[1]+"§8/§c"+sDate[2]+" §c"+sDate[3]+"§8:§c"+sDate[4]+"§8:§c"+sDate[5]);
			}
		}
	}

	@EventHandler
	public void onQuitPlayer(PlayerQuitEvent e) {
		if(isBan(e.getPlayer().getName())) {
			e.setQuitMessage(null);
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(isMute(e.getPlayer().getName())) {
			Player p = e.getPlayer();
			Mute mute = getLastMute(p.getName());
			e.setCancelled(true);
			if(mute.getType().equalsIgnoreCase("Mute")){
				p.sendMessage("§8Vous avez été mute par §c" + mute.getExecutorName() + " §8pour (§c" + mute.getReason() + "§8)");
			}else {
				String[] sDate = ((MuteTempory) mute).getUntilDateToString().split(" ");
				sDate = getFormatDate(sDate);
				p.sendMessage("§8Vous avez été mute par §c" + mute.getExecutorName() + " §8pour (§c" + mute.getReason() + "§8) "
						+ "§8Jusqu'au §c" + sDate[0]+"§8/§c"+sDate[1]+"§8/§c"+sDate[2]+" §c"+sDate[3]+"§8:§c"+sDate[4]+"§8:§c"+sDate[5]);
			}
		}
	}
	
	public String[] getFormatDate(String sDate[]) {
		sDate[1] = ""+(1+Integer.parseInt(sDate[1]));
		for(int nbr = 0; nbr <= sDate.length-1; nbr++) {
			if(Integer.parseInt(sDate[nbr]) < 10) {
				sDate[nbr]= "0"+sDate[nbr];
			}
		}
		return sDate;
	}
}