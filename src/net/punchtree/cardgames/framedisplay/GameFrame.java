package net.punchtree.cardgames.framedisplay;

import org.bukkit.entity.ItemFrame;

import net.punchtree.cardgames.CardGame;

public class GameFrame {

	private final CardGame game;
	
	private final ItemFrame frame;
	
	private final int xIndex;
	private final int yIndex;
	
	public GameFrame(CardGame game, ItemFrame frame, int xIndex, int yIndex) {
		this.game = game;
		this.frame = frame;
		this.xIndex = xIndex;
		this.yIndex = yIndex;
	}
	
	
	
}
