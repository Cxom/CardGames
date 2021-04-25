package net.punchtree.cardgames.saythesame;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.punchtree.cardgames.CardGame;

public class SayTheSameGame implements CardGame {

	static final String PREFIX = ChatColor.RED + "[SayTheSame] " + ChatColor.RESET;
	
	final Player player1;
	final Player player2;
	final Set<Player> spectators = new HashSet<>();
	
	String seed1;
	String seed2;
	
	String player1Guess;
	String player2Guess;
	
	private final SayTheSameDisplay display;
	
	public SayTheSameGame(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
		
		initializeRandomSeeds();
		
		display.showInstructions();
		display.showSeeds();
	}
	
	public void initializeRandomSeeds() {
		
	}
	
	@Override
	public void onPlayerChat(Player player, String message) {
		if ("quit".equalsIgnoreCase(message)) {
			onPlayerQuit(player);
			return;
		}
	}
	
	@Override
	public void onPlayerQuit(Player player) {
		if (spectators.remove(player)) {
			display.removePlayer(player);
			return;
		}
		display.sendPlayerLeftMessage(player);
		shutdown();
	}
	
	@Override
	public void shutdown() {
		display.sendShutdownMessage();
		display.removeAll();
		CardGame.deregisterPlayers(player1, player2);
		CardGame.deregisterPlayers(spectators);
	}
	
}
