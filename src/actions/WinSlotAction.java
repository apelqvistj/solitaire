package actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;

import cards.Card;
import game.GameInterface;
import slots.CardSlot;
import slots.WinSlot;

// Action listener for cards in the win slots
@SuppressWarnings("serial")
public class WinSlotAction extends CardAction {
	private static WinSlotAction winSlotAction = null;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Card clickedCard = null;
		WinSlot clickedCardSlot = null;
		
		// Try to set clickedCard to the clicked card. If this fails, the slot is empty
		try {
			clickedCard = (Card) e.getSource();
			clickedCardSlot = (WinSlot) clickedCard.getSlot();
		} catch (Exception ex) {
			JButton emptySpot = (JButton) e.getSource();
			clickedCardSlot = (WinSlot) emptySpot.getParent();
		}	
		
		Boolean singleMoveEventInAction = GameInterface.getSingleEvent();
		Boolean stackMoveEventInAction = GameInterface.getStackEvent();
		Card cardInMovement = GameInterface.getCardInMovement();
		ArrayList<Card> cardStackInMovement = GameInterface.getCardStackInMovement();
		CardSlot previousCardSlot = GameInterface.getPreviousCardSlot();
		
		// If a single move event is in action, check if the move is legal and move the card to the slot if it is.
		// Otherwise, reset the border and set singleMoveEventInAction to false
		if (singleMoveEventInAction) {
			if (clickedCardSlot.moveIsLegal(cardInMovement)) {
				moveCard(cardInMovement, previousCardSlot, clickedCardSlot);
			} else {
				cardInMovement.setInactiveBorder();
				GameInterface.setSingleEvent(false);
			}
		} else if (stackMoveEventInAction) {
			// Can't move a stack to a win slot
			cardStackInMovement.forEach(card -> card.setInactiveBorder());
			GameInterface.setStackEvent(false);
		} else if (!clickedCardSlot.getCards().isEmpty()){
			// If no move event is in action, and the slot is not empty, select the card
			selectCard(clickedCard, clickedCardSlot);
		}
		
	}
	
	// Get method returns the instance of this class
	public static CardAction getAction() {
		if (winSlotAction == null) {
			winSlotAction = new WinSlotAction();
		}
		return winSlotAction;
	}
}

