package net.punchtree.cardgames.ur;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import net.punchtree.cardgames.CardGame;

public class UrGame implements CardGame {

	static final String TITLE = ChatColor.translateAlternateColorCodes('&', "&e&o&lUR");
	
	final Player player1;
	final Player player2;
	final Set<Player> spectators = new HashSet<>();
	
	private final UrDisplay display;
	
	Player whoseTurn;
	
	enum Token {
		GRAY, BLACK, EMPTY;
	}
	
	// TODO Figure out indexing order and directionality
	Token[][] grid = new Token[3][7];
	
	boolean ended = false;
	private BukkitTask shutdownLaterTask;
	
	public UrGame(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
		
		initializeGame();
		
		whoseTurn = Math.random() < 0.5 ? player1 : player2;
		
		display = new UrDisplay(this);
		display.addPlayer(player1);
		display.addPlayer(player2);
	}
	
	public void addSpectator(Player spectator) {
		if (spectators.add(spectator)) {
			display.addPlayer(spectator);
		}
	}
	
	private void initializeGame() {
		// TODO initialize game
	}

	Token getCurrentTokenColor() {
		return whoseTurn == player1 ? Token.GRAY : Token.BLACK;
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
		if (shutdownLaterTask != null) {
			shutdownLaterTask.cancel();
		}
		display.sendShutdownMessage();
		display.removeAll();
		CardGame.deregisterPlayers(player1, player2);
		CardGame.deregisterPlayers(spectators);
	}
	
}
