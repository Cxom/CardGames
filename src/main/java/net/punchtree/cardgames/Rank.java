package net.punchtree.cardgames;

public enum Rank {

	ACE("A"),
	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8"),
	NINE("9"),
	TEN("10"),
	JACK("J"),
	QUEEN("Q"),
	KING("K");
	
	private String string;
	
	private Rank(String string) {
		this.string = string;
	}
	
	public String getString() {
		return string;
	}
	
	public boolean isFace() {
		return this == JACK || this == QUEEN || this == KING;
	}
	
	public boolean isFaceOrAce() {
		return isFace() || this == ACE;
	}
	
}
