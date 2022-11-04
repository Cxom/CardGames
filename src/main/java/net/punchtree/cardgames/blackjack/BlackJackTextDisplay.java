package net.punchtree.cardgames.blackjack;

import org.bukkit.ChatColor;

import java.util.List;

public class BlackJackTextDisplay {

	private final BlackJackGame game;
	
	public BlackJackTextDisplay(BlackJackGame blackJackGame) {
		this.game = blackJackGame;
	}

	void showInstructions() {
		game.playersMap.values().forEach(bjp -> bjp.sendMessage(BlackJackGame.PREFIX + ChatColor.GRAY + "(First card in your hand is hidden from other players)"));
	}
	
	void showPlayersTheirHands() {
		for(BlackJackPlayer bjp : game.playersMap.values()) {
			bjp.sendMessage(BlackJackGame.PREFIX + ChatColor.WHITE + "Your hand: " + bjp.getSelfHandString());
		}
	}
	
	void showPlayersOthersHands() {
		for(BlackJackPlayer bjp : game.playersMap.values()) {
			for (BlackJackPlayer otherBjp : game.playersMap.values()) {
				if(bjp == otherBjp) {
					continue;
				}
				bjp.sendMessage(BlackJackGame.PREFIX + ChatColor.GRAY + otherBjp.player.getName() + ": " + otherBjp.getPublicHandString());
			}
		}
	}
	
	void showPlayersRevealedHands() {
		for(BlackJackPlayer bjp : game.playersMap.values()) {
			for (BlackJackPlayer otherBjp : game.playersMap.values()) {
				bjp.sendMessage(String.format("%s: %s(%s) %s",
													BlackJackGame.PREFIX + ChatColor.GRAY + otherBjp.player.getName(),
													ChatColor.DARK_GRAY,
												    bustBlackjackOrSum(otherBjp) + ChatColor.RESET + ChatColor.DARK_GRAY,
												    otherBjp.getSelfHandString()));
			}
		}
	}
	
	private String bustBlackjackOrSum(BlackJackPlayer bjp) {
		if (bjp.isBusted()) {
			return ChatColor.ITALIC + "BUST!";
		} else if (bjp.isBlackJack()) {
			return ChatColor.GOLD + "" + ChatColor.ITALIC + "BlackJack!";
		} else {
			return String.valueOf(bjp.getSum());	
		}
	}
	
	void showPlayerHit(BlackJackPlayer bjp) {
		sendAll(ChatColor.DARK_GRAY + "-------------------------");
		sendAll(BlackJackGame.PREFIX + ChatColor.RED + ChatColor.ITALIC + bjp.player.getName() + " hit!");
	}
	
	void showPlayerSet(BlackJackPlayer bjp) {
		sendAll(BlackJackGame.PREFIX + ChatColor.GRAY + ChatColor.ITALIC + bjp.player.getName() + "'s locked in!");
	}
	
	void showWinner(List<BlackJackPlayer> winners) {
		String winnersString = buildWinnersString(winners);
		sendAll(ChatColor.RED + "-------------------------");
		sendAll(BlackJackGame.PREFIX + ChatColor.RED + ChatColor.ITALIC + winnersString);
		showPlayersRevealedHands();
		sendAll(ChatColor.RED + "-------------------------");
	}
	
	private String buildWinnersString(List<BlackJackPlayer> winners) {
		if (winners.isEmpty()) {
			return "Everyone Busted!";
		} else {
			int winningHandSum = winners.get(0).getSum();
			StringBuilder winnersString = new StringBuilder();
			winnersString.append(winners.get(0).player.getName());
			for (int i = 1; i < winners.size(); ++i) {
				winnersString.append(" and " + winners.get(i).player.getName());
			}
			winnersString.append(winners.size() == 1 ? " won with " : " tied with ").append(winningHandSum).append("!");
			return winnersString.toString();
		}
	}
	
	void showShutdown() {
		sendAll(BlackJackGame.PREFIX + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Game shutdown");
	}
	
	private void sendAll(String message) {
		game.playersMap.values().forEach(bjp -> bjp.sendMessage(message));
	}

	void showPlayerQuit(BlackJackPlayer leavingBjp, BlackJackGame blackJackGame) {
		sendAll(BlackJackGame.PREFIX + ChatColor.RED + leavingBjp.player.getName() + " quit!");
	}

	void showAlreadyStood(BlackJackPlayer bjp) {
		bjp.sendMessage(BlackJackGame.PREFIX + ChatColor.RED + "You're already locked in!");
	}
}
