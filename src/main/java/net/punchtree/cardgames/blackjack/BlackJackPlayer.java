package net.punchtree.cardgames.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.punchtree.cardgames.Rank;
import net.punchtree.cardgames.StandardCard;

public class BlackJackPlayer {

	public final Player player;
	private StandardCard downCard;
	private StandardCard upCard;
	private final List<StandardCard> hits = new ArrayList<>();
	private final List<Integer> sums = new ArrayList<>();
	
	private boolean set = false;
	
	public BlackJackPlayer(Player player) {
		this.player = player;
	}
	
	public void sendMessage(String message) {
		player.sendMessage(message);
	}
	
	void setHand(StandardCard downCard, StandardCard upCard) {
		this.downCard = downCard;
		this.upCard = upCard;
		sums.add(BlackJackGame.getValue(downCard.rank));
		if (downCard.rank == Rank.ACE) {
			sums.add(11);
		}
		calculateSums(upCard);
	}
	
	void hit(StandardCard additionalCard) {
		hits.add(additionalCard);
		calculateSums(additionalCard);
	}
	
	private void calculateSums(StandardCard additionalCard) {
		if (additionalCard.rank == Rank.ACE) {
			int sumsPreSize = sums.size();
			for (int i = 0; i < sumsPreSize; ++i) {
				sums.add(sums.get(i) + 10);
			}
		}
		for (int i = 0; i < sums.size(); ++i) {
			sums.set(i, sums.get(i) + BlackJackGame.getValue(additionalCard.rank));
		}
		sums.removeIf((sum) -> sum > 21);
	}
	
	void lockIn() {
		set = true;
	}
	
	StandardCard getUpCard() {
		return upCard;
	}
	
	StandardCard getDownCard() {
		return downCard;
	}
	
	List<StandardCard> getHitCards() {
		return hits;
	}
	
	String getSelfHandString() {
		return ChatColor.GRAY + "(" + getDownCard() + ChatColor.RESET + ChatColor.GRAY + ") " + getUpCardsString();
	}
	
	String getPublicHandString() {
		return ChatColor.GRAY + "(" + ChatColor.DARK_GRAY + "█" + ChatColor.GRAY + ") " + getUpCardsString();
	}
	
	private String getUpCardsString() {
		StringBuilder handString = new StringBuilder();
		handString.append(getUpCard());
		getHitCards().forEach(hitCard -> handString.append(" " + hitCard));
		return handString.toString();
	}
	
	boolean isSet() {
		return set;
	}
	
	int getSum() {
		if (isBusted()) {
			return -1;
		}
		return Collections.max(sums);
	}
	
	boolean isBusted() {
		return sums.isEmpty();
	}
	
	
	
	boolean isBlackJack() {
		return (upCard.rank.isFace() && downCard.rank == Rank.ACE)
			|| (upCard.rank == Rank.ACE && downCard.rank.isFace());
	}
	
}
