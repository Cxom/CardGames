package net.punchtree.cardgames.display;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.punchtree.cardgames.CardGame;

public class ScoreboardTesting implements CardGame {

	private Player tester;
	
	private ScoreboardDisplay display;
	
	public ScoreboardTesting(Player tester) {
		this.tester = tester;
		
		display = new ScoreboardDisplay(ChatColor.RED + "Scoreboard Testing");
		display.addPlayer(tester);
	}
	
	@Override
	public void onPlayerChat(Player player, String message) {
		if (player != tester) return;
		
		if ("quit".equalsIgnoreCase(message)) {
			onPlayerQuit(player);
			return;
		}
		
		display.showWrappedMessage(message);
	}

	@Override
	public void onPlayerQuit(Player player) {
		shutdown();
	}

	@Override
	public void shutdown() {
		display.removeAll();
		CardGame.deregisterPlayers(tester);
		tester = null;
	}

}
