package net.punchtree.cardgames.connect4;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import net.punchtree.cardgames.CardGame;
import net.punchtree.cardgames.CardGamesPlugin;

public class Connect4Game implements CardGame {

	static final String TITLE = ChatColor.AQUA + "" + ChatColor.ITALIC + "Connect" + ChatColor.DARK_AQUA + "4";
	
	final Player player1;
	final Player player2;
	final Set<Player> spectators = new HashSet<>();
	
	private final Connect4Display display;
	
	Player whoseTurn;
	
	enum Token {
		RED, BLUE, EMPTY;
	}
	
	// right ->, up ^
	Token[][] grid = new Token[7][6]; 
	
	boolean ended = false;
	private BukkitTask shutdownLaterTask;
	
	public Connect4Game(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
		
		initializeGrid();

		whoseTurn = Math.random() < 0.5 ? player1 : player2;
		
		display = new Connect4Display(this);
		display.addPlayer(player1);
		display.addPlayer(player2);
		
		display.refresh();
	}
	
	public void addSpectator(Player spectator) {
		if (spectators.add(spectator)) {
			display.addPlayer(spectator);
		}
	}
	
	private void initializeGrid() {
		for (int i = 0; i < 7; ++i) {
			for(int y = 0; y < 6; ++y) {
				grid[i][y] = Token.EMPTY;
			}
		}
	}
	
	private boolean playAndNextTurn(int index) {
		for (int yIndex = 0; yIndex < 6; ++yIndex) {
			if (grid[index][yIndex] == Token.EMPTY) {
				grid[index][yIndex] = getCurrentTokenColor();
				if (checkForWinCondition()) {
					endWithWin();
				} else {
					nextTurn();
				}
				display.refresh();
				return true;
			}
		}
		return false;
	}
	
	Token getCurrentTokenColor() {
		return whoseTurn == player1 ? Token.RED : Token.BLUE;
	}
	
	private boolean checkForWinCondition() {
		return checkRowsForWinCondition()
				|| checkColumnsForWinCondition()
				|| checkDiagonalsForWinCondition();
	}
	
	private boolean checkRowsForWinCondition() {
		Token token = getCurrentTokenColor();
		for(int y = 0; y < 6; ++y) {
			int amt = 0;
			for(int i = 0; i < 7; ++i) {
				if (grid[i][y] == token) {
					++amt;
					if (amt >= 4) {
						return true;
					}
				} else {
					amt = 0;
				}
			}
		}
		return false;
	}
	
	private boolean checkColumnsForWinCondition() {
		Token token = getCurrentTokenColor();
		for(int i = 0; i < 7; ++i) {
			int amt = 0;
			for(int y = 0; y < 6; ++y) {
				if (grid[i][y] == token) {
					++amt;
					if (amt == 4) {
						return true;
					}
				} else {
					amt = 0;
				}
			}
		}
		return false;
	}
	
	private boolean checkDiagonalsForWinCondition() {
		Token token = getCurrentTokenColor();
		for (int diagSum = 3; diagSum <= 5; ++diagSum) {
			final int neswOffset = 5 - diagSum;
			int nwseAmt = 0;
			int nwseAmt2 = 0;
			int neswAmt = 0;
			int neswAmt2 = 0;
			for (int d = 0; d <= diagSum; ++d) {
				if (grid[d][diagSum-d] == token) {
					++nwseAmt;
					if (nwseAmt == 4) {
						return true;
					}
				} else {
					nwseAmt = 0;
				}
				if (grid[6-d][5-(diagSum-d)] == token) {
					++nwseAmt2;
					if (nwseAmt2 == 4) {
						return true;
					}
				} else {
					nwseAmt2 = 0;
				}
				if (grid[d][d+neswOffset] == token) {
					++neswAmt;
					if (neswAmt == 4) {
						return true;
					}
				} else {
					neswAmt = 0;
				}
				if (grid[6-d][5-(d+neswOffset)] == token) {
					++neswAmt2;
					if (neswAmt2 == 4) {
						return true;
					}
				} else {
					neswAmt2 = 0;
				}
			}
		}
		return false;
	}
	
	private void nextTurn() {
		whoseTurn = whoseTurn == player1 ? player2 : player1;
	}
	
	private void endWithWin() {
		ended = true;
		display.showWinMessage();
		shutdownIn10Sec();
	}
	
	private void shutdownIn10Sec() {
		shutdownLaterTask = new BukkitRunnable() {
			@Override
			public void run() {
				shutdown();
			}
		}.runTaskLaterAsynchronously(CardGamesPlugin.getInstance(), 10 * 20);
	}
	
	@Override
	public void onPlayerChat(Player player, String message) {
		if ("quit".equalsIgnoreCase(message)) {
			onPlayerQuit(player);
			return;
		}
		
		if (ended) {
			return;
		}
		
		if (player != whoseTurn) {
			return;
		}
		
		int index = -1;
		try {
			index = Integer.parseInt(message);
		} catch (NumberFormatException e) {
			return;
		}
		
		if (index < 1 || index > 7) {
			return;
		}
		
		boolean successPlaying = playAndNextTurn(index - 1);
		if (!successPlaying) {
			player.sendMessage(ChatColor.RED + "That column is currently full");
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
		if (shutdownLaterTask != null) {
			shutdownLaterTask.cancel();
		}
		display.sendShutdownMessage();
		display.removeAll();
		CardGame.deregisterPlayers(player1, player2);
		CardGame.deregisterPlayers(spectators);
	}

}
