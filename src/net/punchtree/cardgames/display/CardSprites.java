package net.punchtree.cardgames.display;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.punchtree.cardgames.Rank;
import net.punchtree.cardgames.Suit;

public class CardSprites {

	private static final String CARD_SPRITE_SHEET_FILENAME = "/playing-card-sprite-sheet-v2.png";
	public static final BufferedImage CARD_SPRITE_SHEET;
	private static final Map<String, Image> CARD_SPRITES = new HashMap<>();
	public static final int CARD_WIDTH = 20;
	public static final int CARD_HEIGHT = 32;
	public static final int MIN_PIXEL_MARGIN_TO_READ_RANK = 7;
	static {
		BufferedImage cardSpriteSheet = null;
		try {
			cardSpriteSheet = ImageIO.read(CardSprites.class.getResourceAsStream(CARD_SPRITE_SHEET_FILENAME));
			if (cardSpriteSheet == null) {
				System.out.println("Something's wrong");
				throw new IOException("Sprite sheet not read correctly");
			}
			for ( int i = 0; i < 13; ++i ) {
				BufferedImage cardSprite = cardSpriteSheet.getSubimage(CARD_WIDTH * i, CARD_HEIGHT * 0, CARD_WIDTH, CARD_HEIGHT);
				CARD_SPRITES.put(getKeyString(Suit.HEARTS, Rank.values()[i]), cardSprite);
			}
			for ( int i = 0; i < 13; ++i ) {
				BufferedImage cardSprite = cardSpriteSheet.getSubimage(CARD_WIDTH * i, CARD_HEIGHT * 1, CARD_WIDTH, CARD_HEIGHT);
				CARD_SPRITES.put(getKeyString(Suit.DIAMONDS, Rank.values()[i]), cardSprite);
			}
			for ( int i = 0; i < 13; ++i ) {
				BufferedImage cardSprite = cardSpriteSheet.getSubimage(CARD_WIDTH * i, CARD_HEIGHT * 2, CARD_WIDTH, CARD_HEIGHT);
				CARD_SPRITES.put(getKeyString(Suit.CLUBS, Rank.values()[i]), cardSprite);
			}
			for ( int i = 0; i < 13; ++i ) {
				BufferedImage cardSprite = cardSpriteSheet.getSubimage(CARD_WIDTH * i, CARD_HEIGHT * 3, CARD_WIDTH, CARD_HEIGHT);
				CARD_SPRITES.put(getKeyString(Suit.SPADES, Rank.values()[i]), cardSprite);
			}
			for ( int i = 0; i < 13; ++i ) {
				BufferedImage cardSprite = cardSpriteSheet.getSubimage(CARD_WIDTH * i, CARD_HEIGHT * 4, CARD_WIDTH, CARD_HEIGHT);
				CARD_SPRITES.put(String.format("card-backing-%d", i + 1), cardSprite);
			}
		} catch (IOException e) {
			System.out.println("Something went wrong");
			e.printStackTrace();
		}
		CARD_SPRITE_SHEET = cardSpriteSheet;
	}
	
	private static String getKeyString(Suit suit, Rank rank) {
		return suit.getCharacter() + rank.getString();
	}
	
	public static Image getCardFront(Suit suit, Rank rank) {
		return CARD_SPRITES.get(getKeyString(suit, rank));
	}
	
	public static Image getCardBacking() {
		return CARD_SPRITES.get("card-backing-6");
	}
	
}
