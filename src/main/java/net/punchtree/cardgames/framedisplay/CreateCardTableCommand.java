package net.punchtree.cardgames.framedisplay;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

import net.md_5.bungee.api.ChatColor;
import net.punchtree.cardgames.CardGamesPlugin;

public class CreateCardTableCommand implements CommandExecutor {
	
	public static final CardTableCreationListener listener = new CardTableCreationListener();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if ( ! (sender instanceof Player)) return true;
		Player player = (Player) sender;
		
		CardTableCreationSession creationSession = new CardTableCreationSession(player);
		sessionMap.put(player.getUniqueId(), creationSession);
		
		return true;
	}

	public static void clearSessions() {
		sessionMap.clear();
	}
	
	private static final Map<UUID, CardTableCreationSession> sessionMap = new HashMap<>();
	
	private static final int MAX_TABLE_SIZE = 5;
	
	private static class CardTableCreationSession {
		
		private final Player player;
		private Block corner1;
		private Block corner2;
		
		private CardTableCreationSession(Player player) {
			this.player = player;
			player.sendMessage("Right click one corner of the table");
		}
		
		private void onSelectCorner(PlayerInteractEvent event) {
			if (corner1 == null) {
				corner1 = event.getClickedBlock();
				player.sendMessage("Corner 1 set, right click the second corner of the table");
				return;
			}
			corner2 = event.getClickedBlock();
			if (validateTable()) {
				player.sendMessage("Corner 2 set, creating table...");
				finish();
				player.sendMessage("Table created");
			}
		}
		
		private boolean validateTable() {
			// TODO abstract this whole multi-map creation process and the MULTI-MAP concept/object out into library (for selection and tracking)
			// TODO error messages for validation (send to player)
			if (corner1 == null) return false;
			if (corner2 == null) return false;
			if (!corner1.getWorld().equals(corner2.getWorld())) {
				player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "The corners of the table must be in the same world!");
				return false;
			}
			if (corner1.getY() != corner2.getY()) {
				player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "The corners of the table must be on the same plane!");
				return false;
			}
			int xDimension = Math.abs(corner1.getX() - corner2.getX()) + 1;
			int zDimension = Math.abs(corner1.getZ() - corner2.getZ()) + 1;
			if (xDimension > MAX_TABLE_SIZE || zDimension > MAX_TABLE_SIZE) {
				player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "The corners of the table cannot be more than " + MAX_TABLE_SIZE + " blocks apart!");
				return false;
			}			
			// The following lines verify the table is created on solid blocks, but I mean, whatever...
//			int minX = Math.min(corner1.getX(), corner2.getX());
//			int maxX = Math.max(corner1.getX(), corner2.getX());
//			int minZ = Math.min(corner1.getZ(), corner2.getZ());
//			int maxZ = Math.max(corner1.getZ(), corner2.getZ());
//			for (int x = minX; x <= maxX; ++x) {
//				for (int z = minZ; z <= maxZ; ++z) {
//					if (!new Location(corner1.getWorld(), x, corner1.getY(), z).getBlock().getType().isSolid()) {
//						return false;
//					}
//				}
//			}
			
			return true;
		}
		
		private void finish() {
			sessionMap.remove(player.getUniqueId());
			CardTable cardTable = new CardTable(getMinCorner(), getXDim(), getZDim());
			CardGamesPlugin.addCardTable(cardTable);
		}
		
		private Block getMinCorner() {
			return new Location(corner1.getWorld(), Math.min(corner1.getX(), corner2.getX()), corner1.getY(), Math.min(corner1.getZ(), corner2.getZ())).getBlock();
		}

		private int getXDim() {
			return Math.abs(corner1.getX() - corner2.getX()) + 1;
		}
		
		private int getZDim() {
			return Math.abs(corner1.getZ() - corner2.getZ()) + 1;
		}

		private void terminate() {
			sessionMap.remove(player.getUniqueId());
		}
	}
	
	private static class CardTableCreationListener implements Listener {
		
		@EventHandler
		public void onSelectCorner(PlayerInteractEvent event) {
			if (event.getAction() != Action.RIGHT_CLICK_BLOCK 
					|| !sessionMap.containsKey(event.getPlayer().getUniqueId())
					|| event.getHand() != EquipmentSlot.HAND) return;
			event.setCancelled(true);
			sessionMap.get(event.getPlayer().getUniqueId()).onSelectCorner(event);
		}
		
		@EventHandler
		public void onLeave(PlayerQuitEvent event) {
			if (!sessionMap.containsKey(event.getPlayer().getUniqueId())) return;
			sessionMap.get(event.getPlayer().getUniqueId()).terminate();
		}
		
	}
	
}
