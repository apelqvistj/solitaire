package slots;

import java.util.ArrayList;

import cards.Card;

// Class for the game slots
@SuppressWarnings("serial")
public class GameSlot extends CardSlot {
	
	@Override
	public Boolean moveIsLegal(Card card) {
		// Check if there are cards in the slot
		if (super.getCards().isEmpty()) {
			// If none, check if card is a king
			if (card.getNumber() == 13) {
				return true;
			}
		}
			
		// Check if the top card is face down
		if (super.getTopCard().isFaceDown()) {
			return false;
		}
		
		// Check if the top card is of opposite color
		if (super.getTopCard().isBlack() != card.isBlack()) {
			// Check if the top cards value is one higher
			if (super.getTopCard().getNumber() == card.getNumber() + 1) {
				return true;
			}
		}
		
		return false;
			
	}
	
	// Sets the top card to face up
	public void flipCard() {
		super.getTopCard().setFaceDown(false);
	}
	
	// Method that is called when the user tries to select a card stack
	public ArrayList<Card> grabCardStack(Card topCard) {
		ArrayList<Card> grabbedCards = new ArrayList<Card>();
		
		//Get the position in the card slot of the clicked card
		int topCardIndex = this.getCards().indexOf(topCard);
		
		// Iterate through the cards that are on top of the clicked card
		for (int i = topCardIndex; i < this.getCards().size(); i++) {
			Card thisCard = this.getCards().get(i);
			// If the card in the iteration is the last card in the array, break the loop
			if (thisCard == this.getTopCard()) {
				grabbedCards.add(thisCard);
				break;
			}
			
			// Compare this card to the next card in the array, if it is a legal stack: add it to the grabbedCards arraylist
			Card nextCard = this.getCards().get(i + 1);
			
			if (thisCard.isBlack() == nextCard.isBlack()) {
				return null;
			}
			
			if (thisCard.getNumber() != nextCard.getNumber() + 1) {
				return null;
			}
			
			grabbedCards.add(thisCard);
			
		}
		
		return grabbedCards;
	}
}
