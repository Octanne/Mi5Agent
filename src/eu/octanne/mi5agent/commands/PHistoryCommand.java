package eu.octanne.mi5agent.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import eu.octanne.mi5agent.Mi5AgentBukkit;
import eu.octanne.mi5agent.Utils;
import eu.octanne.mi5agent.sanctions.Sanction;

public class PHistoryCommand implements CommandExecutor, Listener{

	static public HashMap<UUID, SanctionViewer> viewers = new HashMap<UUID, SanctionViewer>();

	String COMAND_TAG = "§7History §8|§r ";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("mi5-agent.commands.phistory")) {
				if(args.length > 0) {
					for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
						if(p.getName().equalsIgnoreCase(args[0])) {
							openViewer((Player) sender, 1);
							return true;
						}
					}
					sender.sendMessage(COMAND_TAG+"§cErreur : §9"+args[0]+" §cn'est pas un joueur valide.");
					return false;
				}else {
					sender.sendMessage(COMAND_TAG+"§cUsage : /phistory <joueur>");
					return false;
				}
			}else {
				sender.sendMessage("§cErreur §8| §cCommande reservée à l'administration.");
				return false;
			}
		}else {
			sender.sendMessage("§cErreur §8| §cCommande reservée au client.");
			return false;
		}
	}

	private void openViewer(Player p, int page) {
		// Update
		if(viewers.containsKey(p.getUniqueId())) {
			SanctionViewer view = viewers.get(p.getUniqueId());
			// SET SANCTIONS
			for(int i = 9; i < view.sanctions.size() && i < 27; i++) {
				view.inv.clear(i);
			}
			for(int i = 9*page; i < view.sanctions.size() && i < 27; i++) {
				Sanction san = view.sanctions.get(i-9);
				view.inv.setItem(i, Utils.createItemStack(san.getClass().getName(), Material.STAINED_GLASS_PANE, 1, new ArrayList<>(), 1, false));
			}
		}
		// Create
		else {
			Inventory inv = Bukkit.createInventory(null, 27, "§7Historique de §b"+p.getName());
			List<Sanction> sanctions = Mi5AgentBukkit.getContainer().getSanctions(p.getUniqueId());
			
			viewers.put(p.getUniqueId(), new SanctionViewer(inv, sanctions));

			for(int i = 0; i < 9; i++) {
				inv.setItem(i, Utils.createItemStack(" ", Material.STAINED_GLASS_PANE, 1, new ArrayList<>(), 1, false));
			}
			//INFO
			ArrayList<String> loreINFO = new ArrayList<>();
			
			inv.setItem(4, Utils.createItemSkull(p.getName(), loreINFO, SkullType.PLAYER, p.getName(), false));

			//PAGE SELECTOR
			ArrayList<String> lorePAGE = new ArrayList<>();

			inv.setItem(1, Utils.createItemSkull("§7Page précédente", lorePAGE, SkullType.PLAYER, "MHF_ArrowLeft", false));
			inv.setItem(7, Utils.createItemSkull("§7Page suivante", lorePAGE, SkullType.PLAYER, "MHF_ArrowRight", false));
			
			// SET SANCTIONS
			for(int i = 9; i < sanctions.size() && i < 27; i++) {
				Sanction san = sanctions.get(i-9);
				inv.setItem(i, Utils.createItemStack(san.getClass().getName(), Material.STAINED_GLASS_PANE, 1, new ArrayList<>(), 1, false));
			}
			p.openInventory(inv);
		}
	}

	@EventHandler
	public void closeInventory(InventoryCloseEvent e) {
		if(e.getInventory().getName().contains("§7Historique de §b"))viewers.remove(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void lockInventory(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getClickedInventory().getName().contains("§7Historique de §b")) e.setCancelled(true);

	}

	@EventHandler
	public void quitServer(PlayerQuitEvent e) {
		viewers.remove(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void playerKick(PlayerKickEvent e) {
		viewers.remove(e.getPlayer().getUniqueId());
	}

	class SanctionViewer {
		public Inventory inv;
		public List<Sanction> sanctions;
		public int pageNumber = 1;
		
		public void finalize() {
			inv = null;
			sanctions = null;
		}

		public SanctionViewer(Inventory inv, List<Sanction> sanctions) {
			this.sanctions = sanctions;
			this.inv = inv;
		}
	}
}