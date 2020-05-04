package eu.octanne.mi5agent;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.octanne.mi5agent.commands.BanCommand;
import eu.octanne.mi5agent.commands.MuteCommand;
import eu.octanne.mi5agent.commands.TBanCommand;
import eu.octanne.mi5agent.commands.TMuteCommand;
import eu.octanne.mi5agent.commands.UBanCommand;
import eu.octanne.mi5agent.commands.UMuteCommand;
import eu.octanne.mi5agent.sanctions.Ban;
import eu.octanne.mi5agent.sanctions.Mute;

public class Mi5AgentBukkit extends JavaPlugin implements Listener{
	
	static private HashMap<UUID, Mute> mutePlayers = new HashMap<UUID, Mute>();
	
	static private SanctionContainer container;
	
	@Override
	public void onEnable() {
		container = new SanctionContainer();
		
		// Commands
		getCommand("tempban").setExecutor(new TBanCommand());
		getCommand("ban").setExecutor(new BanCommand());
		getCommand("tempmute").setExecutor(new TMuteCommand());
		getCommand("mute").setExecutor(new MuteCommand());
		getCommand("unmute").setExecutor(new UMuteCommand());
		getCommand("unban").setExecutor(new UBanCommand());
	}
	
	@Override
	public void onDisable() {
		
	}
	
	static public SanctionContainer getContainer() {
		return container;
	}
	
	static public boolean isMute(Player p) {
		if(mutePlayers.containsKey(p.getUniqueId())) {
			if(mutePlayers.get(p.getUniqueId()).isActive()) {
				return true;
			}else return false;
		}else return false;
	}
	
	@EventHandler
	public void onPlayerLogin(AsyncPlayerPreLoginEvent e) {
		Ban ban = container.checkBan(e.getUniqueId());
		if(ban != null) {
			e.setLoginResult(Result.KICK_BANNED);
			String[] sDate = ban.getUntilDateToString().split(" ");
			if(ban.getUntilDate() != null) {
				e.setKickMessage("\n§8§lVous avez été Banni" + "\n" +"par §c§l" + ban.getSanctionerName() + "\n" +"§8§lPour §c§l" + ban.getReason() + "\n" 
						+ "§8Jusqu'au §c" + sDate[0]+"§8/§c"+sDate[1]+"§8/§c"+sDate[2]+" §c"+sDate[3]+"§8:§c"+sDate[4]+"§8:§c"+sDate[5]);
			}else {
				e.setKickMessage("\n§8§lVous avez été Banni" + "\n" +"par §c§l" + ban.getSanctionerName() + "\n" +"§8§lPour §c§l" + ban.getReason());
			}
		}else {
			Mute mute = container.checkMute(e.getUniqueId());
			if(mute != null) mutePlayers.put(e.getUniqueId(), mute);
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		mutePlayers.remove(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if(isMute(e.getPlayer())) {
			Mute mute = mutePlayers.get(e.getPlayer().getUniqueId());
			// Modify Message TODO
			e.getPlayer().sendMessage("§6Mute §8| §cTu as été mute pour §c"+mute.getReason());
			e.setCancelled(true);
		}
	} 
}
