package cards;

import java.util.ArrayList;
import java.util.Collections;

// Deck is a singleton class
public class Deck {
	private static Deck deck = null;
	private static ArrayList<Card> cards;
	
	// The private constructor
	// Creates 52 Card objects, puts them in the cards ArrayList, and shuffles them
	private Deck() {
		cards = new ArrayList<Card>();
		Card newCard = null;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 1; j < 14; j++) {
				switch (i) {
				case 0:
					newCard = new Card(j, "clubs", true);
					break;
				case 1:
					newCard = new Card(j, "spades", true);
					break;
				case 2:
					newCard = new Card(j, "diamonds", false);
					break;
				case 3:
					newCard = new Card(j, "hearts", false);
					break;
				}
				newCard.bufferFaceUpImage();
				newCard.bufferFaceDownImage();
				cards.add(newCard);
			}
		}
		Collections.shuffle(cards);
	}
	
	// Public get method returns the cards ArrayList created in the constructor
	public static ArrayList<Card> getDeck() {
		if (deck == null) {
			deck = new Deck();
		}
		return cards;
	}
	
	// Public method for resetting the deck
	public static void resetDeck() {
		deck = new Deck();
	}
}
