package actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import cards.Card;
import game.Game;
import game.GameInterface;

// Action listener for when the user clicks on the deck of cards
@SuppressWarnings("serial")
public class DeckSlotAction extends CardAction {
	private CardAction drawSlotAction = DrawSlotAction.getAction();
	private static CardAction deckSlotAction = null;
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		Game game = Game.getGameInstance();
		Card cardInMovement = GameInterface.getCardInMovement();
		ArrayList<Card> cardStackInMovement = GameInterface.getCardStackInMovement();
		
		// Reset borders if a card or card stack is selected
		if (cardInMovement != null) cardInMovement.setInactiveBorder();
		if (cardStackInMovement != null) cardStackInMovement.forEach(card -> card.setInactiveBorder());
		
		if (game.getDeck().isEmpty()) {
			// Move the cards in the draw slot to the deck slot and removes/adds the correct action listeners
			game.moveCardsToDeckSlot();
			game.getDeck().forEach(card -> card.removeActionListener(DrawSlotAction.getAction()));
			game.getDeck().forEach(card -> card.addActionListener(getAction()));
			GameInterface.updateCardSlots();
			return;
		}
		
		// Draw a card from the deck, add the correct action listener and remove this action listener
		game.drawCardFromDeck();
		game.getDrawSlot().getTopCard().addActionListener(drawSlotAction);
		game.getDrawSlot().getTopCard().removeActionListener(this);
		GameInterface.setSingleEvent(false);
		GameInterface.setStackEvent(false);
		GameInterface.regretButtonToggle(true);
		GameInterface.updateCardSlots();
	}
	
	// Get method that returns the one instance of this class
	public static CardAction getAction() {
		if (deckSlotAction == null) {
			deckSlotAction = new DeckSlotAction();
		}
		return deckSlotAction;
	}

}
