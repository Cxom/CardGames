package net.punchtree.cardgames.texasholdem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.punchtree.cardgames.CardGame;
import net.punchtree.cardgames.StandardCard;
import net.punchtree.cardgames.StandardDeck;
import net.punchtree.cardgames.util.CircleList;

public class TexasHoldemGame implements CardGame {
	
	static final String TITLE = ChatColor.DARK_GREEN + "Texas Hold'em";
	
	private final StandardDeck deck;
	private final CircleList<TexasHoldemPlayer> players = new CircleList<>();
	private final Map<Player, TexasHoldemPlayer> playersMap = new HashMap<>();
	
	private final TexasHoldemTextDisplay display;
	
	private TexasHoldemPlayer dealer;

	private TexasHoldemPlayer whoseTurn;
	
	private StandardCard[] flop;
	private StandardCard turn;
	private StandardCard river;
	
	
	public TexasHoldemGame(List<Player> players) {
		Collections.shuffle(players);
		
		players.forEach((player) -> {
			TexasHoldemPlayer thp = new TexasHoldemPlayer(player);
			this.playersMap.put(player, thp);
			this.players.add(thp);
		});
		
		this.deck = StandardDeck.getNewDeck();
		
		givePeopleSomeChips();
		choseAFirstDealer();
		
		deck.shuffle();
		deal();
		
		display.showInstructions();
		display.showPlayersTheirHands();
	}
	
	private void givePeopleSomeChips() {
		final int initialChips = 10;
		for (TexasHoldemPlayer thp : players) {
			thp.setChips(initialChips);
		}
	}
	
	private void choseAFirstDealer() {
		this.dealer = players.current();
		
		List<Integer> valuesList = new ArrayList<Integer>(map.values());
		int randomIndex = new Random().nextInt(valuesList.size());
		Integer randomValue = valuesList.get(randomIndex);
	}
	
	private void deal() {
		for (TexasHoldemPlayer thp : playersMap.values()) {
			thp.setHand(deck.pop(), deck.pop());
		}
	}
	
	private void nextTurn() {
		int whoseTurnIndex = players.indexOf(whoseTurn);
		if (whoseTurnIndex + 1 == players.size()) {
			whoseTurn = players.get(0);
		} else {
			whoseTurn = players.get(whoseTurnIndex + 1);
		}
		// TODO make sure whoseTurn is advanced if the player leaves the game on their turn
	}
	
	private void check() {
		
	}
	
	private void fold() {
		whoseTurn.setFolded(true);
		
		TexasHoldemPlayer potentialPotWinner = getOnePlayerUnfolded();
		if (potentialPotWinner != null) {
			endPot(potentialPotWinner);
		}
	}
	
	private TexasHoldemPlayer getOnePlayerUnfolded() {
		int notFolded = 0;
		TexasHoldemPlayer notFoldedPlayer = null;
		for (TexasHoldemPlayer thp : playersMap.values()) {
			if (! thp.isFolded()) {
				++notFolded;
				notFoldedPlayer = thp;
			}
		}
		if (notFolded == 1) {
			return notFoldedPlayer;
		} else {
			return null;
		}
	}
	
	private void showdown() {
		// Calculate winner
		// endPot(potWinner);
	}
	
	private void endPot(TexasHoldemPlayer potWinner) {
		
	}
	
	@Override
	public void onPlayerChat(Player player, String message) {
		if ("quit".equalsIgnoreCase(message)) {
			onPlayerQuit(player);
			return;
		}
		
		if ( ! isPlayersTurn(player)) {
			return;
		}
		
		if ("check".equalsIgnoreCase(message)) {
			check();
			return;
		}
		
		if ("fold".equalsIgnoreCase(message)) {
			fold();
			return;
		}
	}

	private boolean isPlayersTurn(Player player) {
		return player != null && player.equals(whoseTurn.player);
	}
	
	@Override
	public void onPlayerQuit(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

}
