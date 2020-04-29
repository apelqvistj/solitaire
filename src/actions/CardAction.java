package actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.border.LineBorder;

import cards.Card;
import game.GameInterface;
import slots.CardSlot;
import slots.DeckSlot;
import slots.GameSlot;
import slots.WinSlot;

// Superclass for all action listeners
@SuppressWarnings("serial")
public abstract class CardAction extends AbstractAction {
	
	@Override
	public abstract void actionPerformed(ActionEvent arg0);
	
	// Method for selecting a single card
	// Changes the cards border and sets the necessary variables in GameInterface
	void selectCard(Card card, CardSlot previousCardSlot) {
		card.setBorder(new LineBorder(Color.CYAN, 2));
		GameInterface.setCardInMovement(card);
		GameInterface.setPreviousCardSlot(previousCardSlot);
		GameInterface.setSingleEvent(true);
	}
	
	// Method for moving the selected card to a new card slot
	void moveCard(Card cardInMovement, CardSlot previousCardSlot, CardSlot newCardSlot) {
		// Reset the card border and remove it from the previous card slot
		cardInMovement.setInactiveBorder();
		previousCardSlot.getCards().remove(cardInMovement);
		previousCardSlot.remove(cardInMovement);
		
		// Remove the previous action listener
		if (previousCardSlot instanceof GameSlot) cardInMovement.removeActionListener(GameSlotAction.getAction());
		if (previousCardSlot instanceof WinSlot) cardInMovement.removeActionListener(WinSlotAction.getAction());
		if (previousCardSlot instanceof DeckSlot) cardInMovement.removeActionListener(DrawSlotAction.getAction());
		
		// Add the card to the new card slot
		int cardPosInSlot = newCardSlot.getComponentCount();
		if (newCardSlot instanceof GameSlot) {
			cardInMovement.setBounds(0, (cardPosInSlot - 1) * 25, 100, 150);
			newCardSlot.add(cardInMovement, new Integer(cardPosInSlot), 0);
			cardInMovement.addActionListener(GameSlotAction.getAction());
		}
		
		if (newCardSlot instanceof WinSlot) {
			cardInMovement.setBounds(0, 0, 100, 150);
			newCardSlot.add(cardInMovement, new Integer(cardPosInSlot), 0);
			cardInMovement.addActionListener(WinSlotAction.getAction());
		}
		newCardSlot.getCards().add(cardInMovement);
		cardInMovement.setSlot(newCardSlot);
		
		// Set the necessary variables in GameInterface, check if a win is possible, and update the graphics.
		GameInterface.setLastMove(cardInMovement, null);
		GameInterface.setLastMovePrevCardSlot(previousCardSlot);
		GameInterface.setSingleEvent(false);
		GameInterface.regretButtonToggle(false);
		GameInterface.updateCardSlots();
		GameInterface.checkIfAutoWinPossible();
	}
	
	// Method for selecting a stack of cards
	// Changes the border of the whole stack, treating it as a unit
	void selectCardStack (ArrayList<Card> cards, CardSlot previousCardSlot) {
		if (cards != null) {
			cards.get(0).setBorder(BorderFactory.createMatteBorder(2, 2, 0, 2, Color.CYAN));
			for (int i = 1; i < cards.size(); i++) {
				cards.get(i).setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, Color.CYAN));
			}
			GameInterface.setCardStackInMovement(cards);
			GameInterface.setPreviousCardSlot(previousCardSlot);
			GameInterface.setStackEvent(true);
		}
	}
	
	// Method for moving a card stack to a new card slot
	void moveCardStack(ArrayList<Card> cardStackInMovement, CardSlot previousCardSlot, CardSlot newCardSlot) {
		// Reset the border and remove the cards from the previous card slot
		cardStackInMovement.forEach(card -> card.setInactiveBorder());
		cardStackInMovement.forEach(card -> previousCardSlot.remove(card));
		previousCardSlot.getCards().removeAll(cardStackInMovement);
		
		// Add the cards to the new card slot
		int newCardSlotSize = newCardSlot.getCards().size();
		cardStackInMovement.forEach(card -> {
			int cardPosInSlot = newCardSlotSize + cardStackInMovement.indexOf(card);
			card.setBounds(0, (cardPosInSlot) * 25, 100, 150);
			newCardSlot.add(card, new Integer(cardPosInSlot), 0);
			newCardSlot.getCards().add(card);
		});
		cardStackInMovement.forEach(card -> card.setSlot(newCardSlot));
		
		// Set the necessary variables in GameInterface
		GameInterface.setLastMove(null, cardStackInMovement);
		GameInterface.setLastMovePrevCardSlot(previousCardSlot);
		GameInterface.setStackEvent(false);
		GameInterface.regretButtonToggle(false);
		GameInterface.updateCardSlots();
	}
}
