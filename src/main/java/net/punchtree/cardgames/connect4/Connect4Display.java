package net.punchtree.cardgames.connect4;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.punchtree.cardgames.connect4.Connect4Game.Token;
import net.punchtree.cardgames.display.ScoreboardDisplay;

public class Connect4Display {

	private static final String GRID_COIN = "█";
	
	// For chat serialization later
	private final Connect4Game game;
	private final ScoreboardDisplay scoreboard;
	
	private final Set<Player> players = new HashSet<>();
	
	public Connect4Display(Connect4Game game) {
		this.game = game;
		
		this.scoreboard = new ScoreboardDisplay(Connect4Game.TITLE);
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
		scoreboard.showMessage(buildGridRows());
	}
	
	private void showChatGrid(Player player) {
		player.sendMessage("----------------");
		for (String row : buildGridRows()) {
			player.sendMessage(row);
		}
		player.sendMessage("----------------");
	}
	
	private String[] buildGridRows() {
		String[] gridRows = new String[8];
		gridRows[0] = getWhoseTurnString();
		gridRows[1] = "1 2 3 4 6 7";
		gridRows[1] = (".1"
				+ ChatColor.BLACK + "l"+ ChatColor.RESET + "2" 
				+ ChatColor.BLACK + "l"+ ChatColor.RESET + "3" 
				+ ChatColor.BLACK + "l"+ ChatColor.RESET + "4" 
				+ ChatColor.BLACK + "l"+ ChatColor.RESET + "5" 
				+ ChatColor.BLACK + "l"+ ChatColor.RESET + "6" 
				+ ChatColor.BLACK + "l"+ ChatColor.RESET + "7"); 
//		(ChatColor.AQUA + "|"+ ChatColor.RESET + "1"
//				+ ChatColor.AQUA + "l" + "2" + "l" 
//				+ ChatColor.RESET + "3" 
//				+ ChatColor.AQUA + "l" + "4" + "l" 
//				+ ChatColor.RESET + "5" 
//				+ ChatColor.AQUA + "l" + "6" + "l"
//				+ ChatColor.RESET + "7"); 
		
		for (int i = 2; i < 8; ++i) {
			gridRows[i] = "";
			for (int k = 0; k < (i-2); ++k) {
				gridRows[i] += ChatColor.RESET;
			}
		}
		for (Token[] tokenColumn : game.grid) {
			for (int y = 0; y < 6; ++y) {
				gridRows[y+2] += getTokenChatColor(tokenColumn[5-y]) + GRID_COIN;
			}
		}
		return gridRows;
	}
	
	private String getWhoseTurnString() {
		ChatColor chatColor = getTokenChatColor(game.getCurrentTokenColor());
		return chatColor + "" + ChatColor.ITALIC + game.whoseTurn.getName() + "'s " + (game.ended ? "won!" : "turn!"); 
	}
	
	private ChatColor getTokenChatColor(Token token) {
		switch (token) {
		case RED:
			return ChatColor.GREEN;
		case BLUE:
			return ChatColor.DARK_AQUA;
		case EMPTY:
			return ChatColor.DARK_GRAY;
		default:
			return ChatColor.BLACK;
		}
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
