package net.punchtree.cardgames;

public enum Suit {
	
	CLUBS('♣', Color.BLACK),
	DIAMONDS('♦', Color.RED),
	HEARTS('♥', Color.RED),
	SPADES('♠', Color.BLACK);
	
	public enum Color {
		RED, BLACK
	}
	
	private final char character;
	public final Color color;
	
	private Suit(char character, Color color) {
		this.character = character;
		this.color = color;
	}
	
	public char getCharacter() {
		return character;
	}
	
}
