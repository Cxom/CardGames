package net.punchtree.cardgames.texasholdem;

import net.punchtree.cardgames.display.ScoreboardDisplay;

public class TexasHoldemTextDisplay {

	private final TexasHoldemGame game;
	private final ScoreboardDisplay scoreboard;
	
	public TexasHoldemTextDisplay(TexasHoldemGame game) {
		this.game = game;
		
		this.scoreboard = new ScoreboardDisplay(TexasHoldemGame.TITLE);
	}
	
	void showInstructions() {
		
	}
	
	void showPlayersTheirHands() {
		
	}
	
}
