package eu.octanne.mi5agent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Mi5AgentBukkit extends JavaPlugin implements Listener{
	
	private SanctionContainer container;
	
	@Override
	public void onEnable() {
		container = new SanctionContainer();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public SanctionContainer getContainer() {
		return container;
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		
	}
}
