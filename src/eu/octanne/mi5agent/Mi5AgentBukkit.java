package eu.octanne.mi5agent;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.octanne.mi5agent.commands.BanCommand;
import eu.octanne.mi5agent.commands.MuteCommand;
import eu.octanne.mi5agent.commands.PHisctoryCommand;
import eu.octanne.mi5agent.commands.TBanCommand;
import eu.octanne.mi5agent.commands.TMuteCommand;
import eu.octanne.mi5agent.commands.UBanCommand;
import eu.octanne.mi5agent.commands.UMuteCommand;
import eu.octanne.mi5agent.commands.VanishCommand;
import eu.octanne.mi5agent.sanctions.Ban;
import eu.octanne.mi5agent.sanctions.Mute;

public class Mi5AgentBukkit extends JavaPlugin implements Listener{

	static public HashMap<UUID, Mute> mutePlayers = new HashMap<UUID, Mute>();

	static private SanctionContainer container;

	@Override
	public void onEnable() {
		//Load Para
		if(!getConfig().isSet("anti-cheat.reach"))getConfig().set("anti-cheat.reach", 3.1);
		if(!getConfig().isSet("anti-cheat.cps"))getConfig().set("anti-cheat.cps", 18);
		saveConfig();
		
		container = new SanctionContainer();

		// Commands
		getCommand("tempban").setExecutor(new TBanCommand());
		getCommand("ban").setExecutor(new BanCommand());
		getCommand("tempmute").setExecutor(new TMuteCommand());
		getCommand("mute").setExecutor(new MuteCommand());
		getCommand("unmute").setExecutor(new UMuteCommand());
		getCommand("unban").setExecutor(new UBanCommand());
		getCommand("vanish").setExecutor(new VanishCommand());
		getCommand("phistory").setExecutor(new PHisctoryCommand());

		for(Player p : Bukkit.getOnlinePlayers()) {
			Mute mute = container.checkMute(p.getUniqueId());
			if(mute != null) mutePlayers.put(p.getUniqueId(), mute);
		}

		Bukkit.getPluginManager().registerEvents(this, this);
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
			if(ban.getUntilDate() != null) {
				String[] sDate = ban.getUntilDateToString().split(" ");
				e.setKickMessage("\n§7Vous avez été banni :" 
						+ "\n\n" +"par §c" + ban.getSanctionerName() + "\n\n" 
						+"§7MOTIF :\n"+ "§c" + ban.getReason() + "\n\n" 
						+ "§7Jusqu'au : §c" + sDate[0]+"§8/§c"+sDate[1]+"§8/§c"+sDate[2]+" §c"+sDate[3]+"§8:§c"+sDate[4]+"§8:§c"+sDate[5]);
			}else {
				e.setKickMessage("\n§7Vous avez été banni :" 
						+ "\n\n" +"par §c" + ban.getSanctionerName() + "\n\n" 
						+"§7MOTIF :\n" + "§c" + ban.getReason());
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
			e.getPlayer().sendMessage("§cMute §8| §7Tu as été mute pour §9"+mute.getReason()+"§7, durée : §9"+mute.getUntilTime());
			e.setCancelled(true);
		}
	}

	public class CPSProfile {
		private Calendar endTime;

		private int accurateCount = 0;
		private int totalCount = 0;

		public CPSProfile() {
			this.endTime = Calendar.getInstance();
			endTime.add(Calendar.SECOND, 10); // TODO
		}

		public int getAccurate() {
			return accurateCount/2;
		}

		public int getTotal() {
			return totalCount/2;
		}

		public void addClick() {
			totalCount++;
		}

		public void addAccurate() {
			totalCount++;
			accurateCount++;
		}

		public boolean isEnd() {
			return endTime.before(Calendar.getInstance());
		}
	}

	static HashMap<UUID, CPSProfile> cpsProfile = new HashMap<UUID, CPSProfile>();

	@EventHandler
	public void onAttackBlock(PlayerInteractEvent e) {
		if(e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) || e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)){
			if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				if(cpsProfile.containsKey(e.getPlayer().getUniqueId())) {
					CPSProfile profile = cpsProfile.get(e.getPlayer().getUniqueId());
					if(profile.isEnd()) {
						if(profile.getTotal() >= getConfig().getDouble("anti-cheat.cps", 18)) {
							for(Player p : Bukkit.getOnlinePlayers()) {
								if(p.hasPermission("mi5-agent.anti-cheat.warning"))p.sendMessage("§7[§cMi5-Agent§7] §cAttention §9"+e.getPlayer().getName()+" §c: §eAuto-Click§c, "
										+ "§6CPS : §7(§e"+profile.getTotal()+"§8/§9"+profile.getAccurate()+"§7)");
							}
						}CPSProfile nProfile = new CPSProfile();
						nProfile.addClick();
						cpsProfile.put(e.getPlayer().getUniqueId(), nProfile);
					}else {
						profile.addClick();
					}
				}else {
					CPSProfile profile = new CPSProfile();
					profile.addClick();
					cpsProfile.put(e.getPlayer().getUniqueId(), profile);
				}
			}
		}
	}

	@EventHandler
	public void onAttackPlayer(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && (((HumanEntity) e.getDamager()).getGameMode().equals(GameMode.SURVIVAL) 
				|| ((HumanEntity) e.getDamager()).getGameMode().equals(GameMode.ADVENTURE))){
			if(cpsProfile.containsKey(e.getDamager().getUniqueId())) {
				CPSProfile profile = cpsProfile.get(e.getDamager().getUniqueId());
				if(profile.isEnd()) {
					if(profile.getTotal() >= getConfig().getDouble("anti-cheat.cps", 18)) {
						for(Player p : Bukkit.getOnlinePlayers()) {
							if(p.hasPermission("mi5-agent.anti-cheat.warning"))p.sendMessage("§7[§cMi5-Agent§7] §cAttention §9"+e.getDamager().getName()+" §c: §eAuto-Click§c, "
									+ "§6CPS : §7(§e"+profile.getTotal()+"§8/§9"+profile.getAccurate()+"§7)");
						}
					}
					CPSProfile nProfile = new CPSProfile();
					nProfile.addAccurate();
					cpsProfile.put(e.getDamager().getUniqueId(), nProfile);
				}else {
					profile.addAccurate();
				}
			}else {
				CPSProfile profile = new CPSProfile();
				profile.addAccurate();
				cpsProfile.put(e.getDamager().getUniqueId(), profile);
			}
			
			//Reach
			double distance = e.getDamager().getLocation().distance(e.getEntity().getLocation());
			Bukkit.getLogger().info(e.getDamager().getName()+" Reach : "+distance);
			if(distance > getConfig().getDouble("anti-cheat.reach", 3.1)) {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(p.hasPermission("mi5-agent.anti-cheat.warning"))p.sendMessage("§7[§cMi5-Agent§7] §cAttention §9"+e.getDamager().getName()+" §c: §9Reach§c, §6Distance §c: "
							+ distance);
				}
			}
		}
	}
}
