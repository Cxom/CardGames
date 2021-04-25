package net.punchtree.cardgames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StandardDeck extends LinkedList<StandardCard> {

	private static final long serialVersionUID = 1L;

	public static final int STANDARD_DECK_SIZE = 52;
	
	private StandardDeck(List<StandardCard> cards) {
		super(cards);
	}
	
	public static StandardDeck getNewDeck() {
		List<StandardCard> cards = new ArrayList<StandardCard>(STANDARD_DECK_SIZE);
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				cards.add(new StandardCard(suit, rank));
			}
		}
		return new StandardDeck(cards);
	}
	
	public StandardDeck shuffle() {
		Collections.shuffle(this);
		return this;
	}
	
}
