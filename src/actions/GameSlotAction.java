package actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;

import cards.Card;
import game.Game;
import game.GameInterface;
import slots.CardSlot;
import slots.GameSlot;

// Action listener for the game slot cards
@SuppressWarnings("serial")
public class GameSlotAction extends CardAction {
	private static GameSlotAction gameSlotAction = null;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Game game = Game.getGameInstance();
		Card clickedCard = null;
		GameSlot clickedCardSlot = null;
		
		// Try to set clickedCard to the clicked card. If this fails, the slot is empty
		try {
			clickedCard = (Card) e.getSource();
			clickedCardSlot = (GameSlot) clickedCard.getSlot();
		} catch (Exception ex) {
			JButton emptySpot = (JButton) e.getSource();
			clickedCardSlot = (GameSlot) emptySpot.getParent();
		}
		
		Boolean singleMoveEventInAction = GameInterface.getSingleEvent();
		Boolean stackMoveEventInAction = GameInterface.getStackEvent();
		Card cardInMovement = GameInterface.getCardInMovement();
		ArrayList<Card> cardStackInMovement = GameInterface.getCardStackInMovement();
		CardSlot previousCardSlot = GameInterface.getPreviousCardSlot();
		
		// If a card already has been clicked and the user is clicking on another card to move it there
		if (singleMoveEventInAction) {	
			if (clickedCard == cardInMovement) {
				// User double clicked on a card, this moves the card to a win slot if possible
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
						// If no win slots has a legal move
						if (i == 3) {
							cardInMovement.setInactiveBorder();
							GameInterface.setSingleEvent(false);
						}
					}
				}
				return;
			}
			
			// Try to move card to clicked card slot
			if (clickedCardSlot.moveIsLegal(cardInMovement)) {
				// Move card to clicked card slot
				moveCard(cardInMovement, previousCardSlot, clickedCardSlot);
			} else {
				// Move was not legal, reset the cards border and set singleMoveEventInAction to false
				cardInMovement.setInactiveBorder();
				GameInterface.setSingleEvent(false);
			}
			return;
		}
		
		// If a card stack has been selected and the user is clicking on another card to move the stack
		if (stackMoveEventInAction) {
			if (clickedCardSlot.moveIsLegal(cardStackInMovement.get(0))) {
				moveCardStack(cardStackInMovement, previousCardSlot, clickedCardSlot);
				GameInterface.checkIfAutoWinPossible();
			} else {
				cardStackInMovement.forEach(card -> card.setInactiveBorder());
				GameInterface.setStackEvent(false);
			}
			return;
		}
		
		// If the user clicks an empty spot with no selected card(s), nothing happens
		if (clickedCardSlot.getCards().isEmpty()) {
			return;
		}
		
		// If the user clicks on a face down card on the top of the card slot, flip it
		if (clickedCard.isFaceDown() && (clickedCardSlot.getTopCard() == clickedCard)) {
			
				clickedCardSlot.flipCard();
				
				GameInterface.updateCardSlots();
				GameInterface.regretButtonToggle(true);
				GameInterface.checkIfAutoWinPossible();
				return;
		} 
		
		// If the user clicks on a face up card that is not the top card, try to move the cards under it
		if (!clickedCard.isFaceDown() && (clickedCardSlot.getTopCard() != clickedCard)) {
				
				ArrayList<Card> grabbedCards = clickedCardSlot.grabCardStack(clickedCard);
				selectCardStack(grabbedCards, clickedCardSlot);
				return;
		} 
		
		if (clickedCardSlot.getTopCard() == clickedCard) {
			selectCard(clickedCard, clickedCardSlot);
			return;
		}							
	}
	
	// Get method returns the instance of this class
	public static CardAction getAction() {
		if (gameSlotAction == null) {
			gameSlotAction = new GameSlotAction();
		}
		return gameSlotAction;
	}
	
}


