package slots;

import cards.Card;

// Class for the win slots
@SuppressWarnings("serial")
public class WinSlot extends CardSlot {
		
	@Override
	public Boolean moveIsLegal(Card card) {
		if (card == null) return false;
		
		// Check if there are cards in the slot
		if (super.isEmpty()) {
			// If none, check if card is an ace
			if (card.getNumber() == 1) {
				return true;
			}
			return false;
		}
		
		// Check if card is of same suit
		if (super.getTopCard().getSuit() == card.getSuit()) {
			// Check if top card is one lower
			if (super.getTopCard().getNumber() == card.getNumber() - 1) {
				return true;
			}
		}
		return false;
	}
}
