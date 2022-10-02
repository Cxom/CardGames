package net.punchtree.cardgames;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.entity.Player;

public interface CardGame {
	
	default public void onPlayerChat(Player player, String message) {}

	public void onPlayerQuit(Player player);
	
	public void shutdown();
	
	static void deregisterPlayers(Player... players) {
		CardGamesPlugin.getInstance().removeGame(Arrays.asList(players));
	}
	
	static void deregisterPlayers(Collection<Player> players) {
		CardGamesPlugin.getInstance().removeGame(players);
	}
	
}
