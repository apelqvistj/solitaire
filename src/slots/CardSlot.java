package slots;

import java.util.ArrayList;

import javax.swing.JLayeredPane;

import cards.Card;

// Abstract superclass for the card slots
// It extends JLayeredPane to be able to contain components and display them in layers
@SuppressWarnings("serial")
public abstract class CardSlot extends JLayeredPane {
	private ArrayList<Card> cards = new ArrayList<Card>();
	
	
	public abstract Boolean moveIsLegal(Card card);
	
	// Returns true if slot is empty
	public Boolean isEmpty() {
		if (cards.isEmpty()) {
			return true;
		}
		return false;
	}
	
	// Returns the top card in the slot
	public Card getTopCard() {
		return cards.get(cards.size() - 1);
	}
	
	// Returns all cards in the slot
	public ArrayList<Card> getCards() {
		return cards;
	}
	
}
