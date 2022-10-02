package net.punchtree.cardgames;

import java.awt.Image;

import org.bukkit.ChatColor;

import net.punchtree.cardgames.Suit.Color;
import net.punchtree.cardgames.display.CardSprites;

public class StandardCard {

	public final Suit suit;
	public final Rank rank;
	
	public StandardCard(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}
	
	public String toString() {
		String string = (suit.color == Color.RED ? ChatColor.DARK_RED : ChatColor.DARK_GRAY) + ""
				+ suit.getCharacter() + rank.getString();
		return string;
	}
	
	public Image getSprite() {
		return CardSprites.getCardFront(suit, rank);
	}
	
}
