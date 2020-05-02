package eu.squadev.mi5agent.bukkit;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import eu.squadev.mi5agent.bukkit.commands.BanCommand;
import eu.squadev.mi5agent.bukkit.commands.MuteCommand;
import eu.squadev.mi5agent.bukkit.commands.SanctionHistoryCommand;
import eu.squadev.mi5agent.bukkit.commands.TempBanCommand;
import eu.squadev.mi5agent.bukkit.commands.TempMuteCommand;
import eu.squadev.mi5agent.bukkit.commands.UnbanCommand;
import eu.squadev.mi5agent.bukkit.commands.UnmuteCommand;
import eu.squadev.mi5agent.bukkit.commands.WarningCommand;

public class Mi5Agent extends JavaPlugin{
	
	private static SanctionManager manager;
	
	private File file = new File("plugins/MI5Agent/config.yml");
	private YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
	
	public void onEnable() {
		initializeFile();
		manager = new SanctionManager(config.getString("folderPath"));
		getCommand("tempban").setExecutor(new TempBanCommand());
		getCommand("tempmute").setExecutor(new TempMuteCommand());
		getCommand("mute").setExecutor(new MuteCommand());
		getCommand("ban").setExecutor(new BanCommand());
		getCommand("unmute").setExecutor(new UnmuteCommand());
		getCommand("unban").setExecutor(new UnbanCommand());
		getCommand("sanctionhistory").setExecutor(new SanctionHistoryCommand());
		getCommand("warn").setExecutor(new WarningCommand());
	}
	
	public void onDisable() {
		
	}
	
	static public Plugin getInstance() {
		return Bukkit.getPluginManager().getPlugin("MI5-Agent");
	}
	
	static public SanctionManager getManager() { return manager;}
	
	private void initializeFile() {
		if(!file.exists()) {
			config.set("folderPath", "plugins/MI5Agent/player/");
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}