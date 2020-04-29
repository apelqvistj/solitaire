package slots;

import cards.Card;

// Class for the deck slot and the draw slot
@SuppressWarnings("serial")
public class DeckSlot extends CardSlot {
	
	// The user is not allowed to move cards to the deck slot or draw slot
	@Override
	public Boolean moveIsLegal (Card card) {
		return false;
	}
		
}
