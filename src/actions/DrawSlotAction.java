package actions;

import java.awt.event.ActionEvent;

import cards.Card;
import game.Game;
import game.GameInterface;
import slots.CardSlot;

// Action listener for when the user clicks on the "draw" slot, to the side of the deck
@SuppressWarnings("serial")
class DrawSlotAction extends CardAction {
	private static DrawSlotAction drawSlotAction = null;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Game game = Game.getGameInstance();
		Card card = (Card) e.getSource();
		Boolean singleMoveEventInAction = GameInterface.getSingleEvent();
		Card cardInMovement = GameInterface.getCardInMovement();
		CardSlot previousCardSlot = GameInterface.getPreviousCardSlot();
			
		// If singleMoveEventInAction is true, the user has clicked on a card before clicking on this card
		if (singleMoveEventInAction) {
			if (card == cardInMovement) {
				// User double clicked on this card, this moves the card to a win slot if possible
				
				if (cardInMovement.getNumber() == 1) {
					
					// If card is an ace, move to first empty win slot
					for (int i = 0; i < 4; i++) {
						if (game.getWinSlots().get(i).isEmpty()) {
							moveCard(cardInMovement, previousCardSlot, game.getWinSlots().get(i));
							break;
						}
					}
				} else {
					
					// If card is not an ace, move to win slot if move is legal
					for (int i = 0; i < 4; i++) {
						if (game.getWinSlots().get(i).moveIsLegal(cardInMovement)) {
							moveCard(cardInMovement, previousCardSlot, game.getWinSlots().get(i));
							break;
						}
						if (i == 3) {
							// If no legal move exists, reset the card border and set singleMoveEventInAction to false
							cardInMovement.setInactiveBorder();
							GameInterface.setSingleEvent(false);
						}
					}
				}	
				return;
			} else {
				// The user clicked on a card that is not this card before clicking on this card. This is not allowed.
				cardInMovement.setInactiveBorder();
				GameInterface.setSingleEvent(false);
				return;
			}
		}
		// No card was clicked before clicking this card, so the card gets selected
		selectCard(card, game.getDrawSlot());
	}
	
	// Get method returns the instance of this class
	static CardAction getAction() {
		if (drawSlotAction == null) {
			drawSlotAction = new DrawSlotAction();
		}
		return drawSlotAction;
	}

}
