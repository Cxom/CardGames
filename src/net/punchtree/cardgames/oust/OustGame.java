package net.punchtree.cardgames.oust;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import net.punchtree.cardgames.connect4.Connect4Display;

public class OustGame {

	private static final int GRID_SIZE = 10;
	
	static final String TITLE = ChatColor.translateAlternateColorCodes('&', "&2&o&lOU&a&o&lST");
	
	final Player player1;
	final Player player2;
	final Set<Player> spectators = new HashSet<>();
	
	int player1PieceCount = 0;
	int player2PieceCount = 0;
	
	private final Connect4Display display;
	
	Player whoseTurn;
	
	enum Token {
		RED, BLUE, EMPTY;
	}
	
	// right ->, up ^
	Token[][] grid = new Token[GRID_SIZE][GRID_SIZE]; 
	
	boolean ended = false;
	private BukkitTask shutdownLaterTask;
	
	public OustGame(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
		
		initializeGrid();

		whoseTurn = Math.random() < 0.5 ? player1 : player2;
		
		display = new OustDisplay(this);
		display.addPlayer(player1);
		display.addPlayer(player2);
		
		display.refresh();
	}
	
	public void addSpectator(Player spectator) {
		if (spectators.add(spectator)) {
			display.addPlayer(spectator);
		}
	}
	
	Token getCurrentTokenColor() {
		return whoseTurn == player1 ? Token.RED : Token.BLUE;
	}
	
	private void initializeGrid() {
		for (int i = 0; i < GRID_SIZE; ++i) {
			for(int y = 0; y < GRID_SIZE; ++y) {
				grid[i][y] = Token.EMPTY;
			}
		}
	}
	
	private boolean playAndNextTurn(int row, int column) {
		if (grid[row][column] != Token.EMPTY) {
			return false;
		}
		if (isNonCapturingMove(row, column)) {
			grid[row][column] = getCurrentTokenColor();
			endOrNextTurn();
			display.refresh();
			return true;
		}
		return false;
	}
	
	private boolean isNonCapturingMove(int row, int column) {
		// TODO logic
		return true;
	}
	
	private void endOrNextTurn() {
		if (checkForWinCondition()) {
			endWithWin();
		} else {
			nextTurn();
		}
	}
	
	private boolean checkForWinCondition() {
		return checkRowsForWinCondition()
				|| checkColumnsForWinCondition()
				|| checkDiagonalsForWinCondition();
	}
	
}
