package net.punchtree.cardgames.texasholdem;

import org.bukkit.entity.Player;

import net.punchtree.cardgames.StandardCard;

public class TexasHoldemPlayer {

	public final Player player;
	private StandardCard card1;
	private StandardCard card2;
	
	private boolean folded = false;
	
	private int chips = 0;
	
	public TexasHoldemPlayer(Player player) {
		this.player = player;
	}
	
	public void sendMessage(String message) {
		player.sendMessage(message);
	}
	
	void setHand(StandardCard card1, StandardCard card2) {
		this.card1 = card1;
		this.card2 = card2;
	}
	
	void setFolded(boolean folded) {
		this.folded = folded;
	}
	
	boolean isFolded() {
		return folded;
	}
	
	void setChips(int chips) {
		this.chips = chips;
	}
	
	int getChips() {
		return chips;
	}
	
}
