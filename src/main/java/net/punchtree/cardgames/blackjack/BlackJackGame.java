package net.punchtree.cardgames.blackjack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.punchtree.cardgames.CardGame;
import net.punchtree.cardgames.CardGamesPlugin;
import net.punchtree.cardgames.Rank;
import net.punchtree.cardgames.StandardDeck;

public class BlackJackGame implements CardGame {

	static final String PREFIX = ChatColor.DARK_GRAY + "[B" + ChatColor.GOLD + "J" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " ";
	
	private final StandardDeck deck;
	final Map<Player, BlackJackPlayer> playersMap = new HashMap<>();
	
	private final BlackJackTextDisplay display;
	
	public BlackJackGame(Set<Player> players) {
		players.forEach((player) -> {
			this.playersMap.put(player, new BlackJackPlayer(player));
		});
		this.deck = StandardDeck.getNewDeck();
		
		this.display = new BlackJackTextDisplay(this);
		
		deck.shuffle();
		deal();
		
		display.showInstructions();
		display.showPlayersTheirHands();
		display.showPlayersOthersHands();
	}
	
	private void deal() {
		for (BlackJackPlayer bjp : playersMap.values()) {
			bjp.setHand(deck.pop(), deck.pop());
		}
	}
	
	private void hit(BlackJackPlayer bjp) {
		if (bjp.isSet()) {
			bjp.sendMessage(PREFIX + ChatColor.RED + "You're already locked in!");
		}
		bjp.hit(deck.pop());
		
		display.showPlayerHit(bjp);
		display.showPlayersTheirHands();
		display.showPlayersOthersHands();
		
		if (bjp.isBusted()) {
			bjp.lockIn();
		}
		checkForAllLockedIn();
	}
	
	private void set(BlackJackPlayer bjp) {
		bjp.lockIn();
		display.showPlayerSet(bjp);
		checkForAllLockedIn();
	}
	
	private void playerQuit(BlackJackPlayer leavingBjp) {
		playersMap.values().forEach((bjp) -> {
			// TODO change prefix to game specific
			bjp.sendMessage(CardGamesPlugin.CHAT_PREFIX_ERROR + leavingBjp + " quit!");
		});
		playersMap.remove(leavingBjp.player);
		shutdown();
	}
	
	@Override
	public void onPlayerChat(Player player, String message) {
		BlackJackPlayer bjp = playersMap.get(player);
		if ("hit".equalsIgnoreCase(message) || message.toLowerCase().startsWith("hit me")) {
			hit(bjp);
		} else if ("set".equalsIgnoreCase(message) || "stay".equalsIgnoreCase(message)) {
			set(bjp);
		} else if ("quit".equalsIgnoreCase(message) || "leave".equalsIgnoreCase(message)) {
			playerQuit(bjp);
		}
	}
	
	private void checkForAllLockedIn() {
		for (BlackJackPlayer bjp : playersMap.values()) {
			if (!bjp.isSet()) {
				return;
			}
		}
		endHand();
	}
	
	private void endHand() {
		List<BlackJackPlayer> winners = calculateWinners();
		
		display.showWinner(winners);
		
		shutdown();
	}
	
	private List<BlackJackPlayer> calculateWinners() {
		int winningHandSum = 0;
		List<BlackJackPlayer> winners = new ArrayList<>();
		for (BlackJackPlayer bjp : playersMap.values()) {
			if (bjp.getSum() > winningHandSum) {
				winners.clear();
				winningHandSum = bjp.getSum();
				
			}
			if (bjp.getSum() == winningHandSum) {
				winners.add(bjp);
			}
		}
		return winners;
	}
	
	@Override
	public void onPlayerQuit(Player player) {
		BlackJackPlayer bjp = playersMap.get(player);
		playerQuit(bjp);
	}
	
	@Override
	public void shutdown() {
		CardGame.deregisterPlayers(playersMap.keySet());
		display.showShutdown();
		playersMap.clear();
	}
	
	static int getValue(Rank rank) {
		switch (rank) {
			case KING:
			case QUEEN:
			case JACK:
				return 10;
			default:
				return rank.ordinal() + 1;
		}
	}

}
