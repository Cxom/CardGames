package net.punchtree.cardgames.ur;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.punchtree.cardgames.display.ScoreboardDisplay;
import net.punchtree.cardgames.ur.UrGame.Token;

public class UrDisplay {

	private static final String UNOCCUPIED_SQUARE = "█";
	private static final String OCCUPIED_SQUARE = "⬤";
	
	private final UrGame game;
	private final ScoreboardDisplay scoreboard;
	
	private final Set<Player> players = new HashSet<>();
	
	public UrDisplay(UrGame game) {
		this.game = game;
		
		this.scoreboard = new ScoreboardDisplay(UrGame.TITLE);
	}
	
	public void addPlayer(Player player) {
		players.add(player);
		scoreboard.addPlayer(player);
	}
	
	public void removePlayer(Player player) {
		scoreboard.removePlayer(player);
	}
	
	public void removeAll() {
		scoreboard.removeAll();
	}
	
	public void refresh() {
		displayGrid();
	}
	
	private void displayGrid() {
		// TODO build grid display
	}
	
	private ChatColor getTokenChatColor(Token token) {
		switch (token) {
		case GRAY:
			return ChatColor.GRAY;
		case BLACK:
			return ChatColor.DARK_GRAY;
		case EMPTY:
			return ChatColor.BLUE;
		default:
			return ChatColor.BLACK;
		}
	}
	
	private void showChatGrid(Player player) {
		player.sendMessage("----------------");
		for (String row : buildGridRows()) {
			player.sendMessage(row);
		}
		player.sendMessage("----------------");
	}
	
	private String[] buildGridRows() {
		String[] gridRows = new String[5];
		gridRows[0] = getWhoseTurnString();
		
		return gridRows;
	}
	
	private String getWhoseTurnString() {
		ChatColor chatColor = getTokenChatColor(game.getCurrentTokenColor());
		return chatColor + "" + ChatColor.ITALIC + game.whoseTurn.getName() + "'s " + (game.ended ? "won!" : "turn!"); 
	}
	
	public void showWinMessage() {
		ChatColor winnerChatColor = getTokenChatColor(game.getCurrentTokenColor());
		players.forEach(player -> {
			showChatGrid(player);
			player.sendMessage(winnerChatColor + game.whoseTurn.getName() + " has won, Congrats!");
		});
	}
	
	public void sendPlayerLeftMessage(Player playerLeft) {
		players.forEach(player -> {
			player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + playerLeft.getName() + " left, ending game");
		});
	}
	
	public void sendShutdownMessage() {
		players.forEach(player -> {
			player.sendMessage(ChatColor.DARK_GRAY + "Game ended");
		});
	}
}
