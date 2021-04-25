package net.punchtree.cardgames;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.punchtree.cardgames.prototyping.CardTableTest;

public class CardGamesPlugin extends JavaPlugin {

	Map<UUID, CardGame> activePlayers = new HashMap<>();
	
	private static CardGamesPlugin pluginInstance;
	public static CardGamesPlugin getInstance() {
		return pluginInstance;
	}
	
	public static final String CHAT_PREFIX = ChatColor.BOLD + "[" 
											+ ChatColor.RESET + "" + ChatColor.RED + "CG"
											+ ChatColor.RESET + ChatColor.BOLD + "]"
											+ ChatColor.RESET + " ";
	
	public static final String CHAT_PREFIX_ERROR = CHAT_PREFIX + ChatColor.RED;
	
	
	@Override
	public void onEnable() {
		getCommand("cg").setExecutor(new CardGamesCommandExecutor());
		getCommand("cardtabletest").setExecutor(new CardTableTest());
		
		Bukkit.getPluginManager().registerEvents(new PlayerGameListeners(), this);
		
		pluginInstance = this;
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void addGame(Collection<Player> players, CardGame game) {
		for (Player player : players) {
			CardGame existingGame = activePlayers.get(player.getUniqueId());
			if (existingGame != null) {
				existingGame.onPlayerQuit(player);
			}
			activePlayers.put(player.getUniqueId(), game);
		}
	}
	
	public void removeGame(Collection<Player> players) {
		for (Player player : players) {
			activePlayers.remove(player.getUniqueId());
		}
	}
	
	private boolean isInAGame(Player player) {
		return activePlayers.containsKey(player.getUniqueId());
	}
	
	private class PlayerGameListeners implements Listener {

		@EventHandler
		public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
			Player player = event.getPlayer();
			CardGame cg = activePlayers.get(player.getUniqueId());
			if (cg != null) {
				cg.onPlayerChat(player, event.getMessage());
			}
		}
		
		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent event) {
			Player player = event.getPlayer();
			CardGame cg = activePlayers.get(player.getUniqueId());
			if (cg != null) {
				cg.onPlayerQuit(player);
			}
		}
		
	}
	
}
