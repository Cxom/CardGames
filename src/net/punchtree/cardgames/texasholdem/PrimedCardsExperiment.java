package net.punchtree.cardgames.texasholdem;

import java.util.HashMap;
import java.util.Map;

import net.punchtree.cardgames.Rank;
import net.punchtree.cardgames.StandardCard;
import net.punchtree.cardgames.StandardDeck;
import net.punchtree.cardgames.Suit;

public class PrimedCardsExperiment {

	private static final int[] primes = {
			  1,   2,   3,   5,
			  7,  11,  13,  17,
			 19,  23,  29,  31,
			 37,  41,  43,  47,
			 53,  59,  61,  67,
			 71,  73,  79,  83,
			 89,  97, 101, 103,
			107, 109, 113, 127,
			131, 137, 139, 149,
			151, 157, 163, 167,
			173, 179, 181, 191,
			193, 197, 199, 211,
			223, 227, 229, 233,
			239, 241, 251, 257
	};
	
	public PrimedCardsExperiment() {
		
	}
	
	private static Map<Rank, Map<Suit, Integer>> primeMapping = new HashMap<>();
	
	public static void main(String[] args) {
		StandardDeck deck = StandardDeck.getNewDeck();
		
		int j = 0;
		for (Rank rank : Rank.values()) {
			Map<Suit, Integer> suitMap = new HashMap<>();
			for (Suit suit : Suit.values()) {
				suitMap.put(suit, primes[j]);
				++j;
			}
			primeMapping.put(rank, suitMap);
		}
		
		StandardCard[][] royalFlushes = new StandardCard[4][5];
		int i = 0;
		for (Suit suit : Suit.values()) {
			royalFlushes[i][0] = new StandardCard(suit, Rank.ACE);
			royalFlushes[i][1] = new StandardCard(suit, Rank.KING);
			royalFlushes[i][2] = new StandardCard(suit, Rank.QUEEN);
			royalFlushes[i][3] = new StandardCard(suit, Rank.JACK);
			royalFlushes[i][4] = new StandardCard(suit, Rank.TEN);
			++i;
		}
		
		long[] flushPrimes = new long[4];
		i = 0;
		for (StandardCard[] rf : royalFlushes) {
			flushPrimes[i] = calculateNum(rf);
			++i;
		}
		
		for (long k : flushPrimes) {
			System.out.println(k);
		}
		
		StandardCard[] testHand1 = {
			new StandardCard(Suit.CLUBS, Rank.ACE),
			new StandardCard(Suit.CLUBS, Rank.TEN),
			new StandardCard(Suit.CLUBS, Rank.KING),
			new StandardCard(Suit.CLUBS, Rank.JACK),
			new StandardCard(Suit.DIAMONDS, Rank.THREE),
			new StandardCard(Suit.SPADES, Rank.FOUR),
			new StandardCard(Suit.CLUBS, Rank.QUEEN)
		};
		long testHand1Sum = calculateNum(testHand1);
		System.out.println(testHand1Sum);
		System.out.println();
		
		for (long k : flushPrimes) {
			System.out.println((double) testHand1Sum / (double) k);
		}
		
	}
	
	static long calculateNum(StandardCard[] cards) {
		long l = 1;
		for (StandardCard card : cards) {
			l *= primeMapping.get(card.rank).get(card.suit);
		}
		return l;
	}
	
}
