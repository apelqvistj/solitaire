package actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;

import cards.Card;
import game.GameInterface;
import slots.CardSlot;
import slots.DeckSlot;
import slots.GameSlot;
import slots.WinSlot;

// Action listener for the 'Regret Move' button
@SuppressWarnings("serial")
public class RegretAction extends AbstractAction {
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// lastMove contains the card(s) that were last moved
		ArrayList<Card> lastMove = GameInterface.getLastMove();
		CardSlot previousCardSlot = GameInterface.getLastMovePrevCardSlot();
		CardSlot currentCardSlot = lastMove.get(0).getSlot();
		
		
		for (int i = 0; i < lastMove.size(); i++) {
			// Remove the card(s) from the current card slot
			currentCardSlot.getCards().remove(lastMove.get(i));
			currentCardSlot.remove(lastMove.get(i));
			
			// Add the card(s) to the previous card slot
			previousCardSlot.getCards().add(lastMove.get(i));
			lastMove.get(i).setSlot(previousCardSlot);
			
			int cardPosInSlot = previousCardSlot.getCards().indexOf(lastMove.get(i));
			
			// If the previous card slot was a game slot, the bounds need to be different because of the cascading positions of the cards
			if (previousCardSlot instanceof GameSlot) {
				lastMove.get(i).setBounds(0, cardPosInSlot * 25, 100, 150);
			} else {
				lastMove.get(i).setBounds(0, 0, 100, 150);
			}
			previousCardSlot.add(lastMove.get(i), new Integer(cardPosInSlot + 1), 0);
			
			// Add and remove action listeners to the card(s) depending on the card slots
			if (previousCardSlot instanceof DeckSlot) lastMove.get(i).addActionListener(DrawSlotAction.getAction());
			if (previousCardSlot instanceof GameSlot) lastMove.get(i).addActionListener(GameSlotAction.getAction());
			if (previousCardSlot instanceof WinSlot) lastMove.get(i).addActionListener(WinSlotAction.getAction());
			
			if (currentCardSlot instanceof DeckSlot) lastMove.get(i).removeActionListener(DrawSlotAction.getAction());
			if (currentCardSlot instanceof GameSlot) lastMove.get(i).removeActionListener(GameSlotAction.getAction());
			if (currentCardSlot instanceof WinSlot) lastMove.get(i).removeActionListener(WinSlotAction.getAction());
						
		}
		
		GameInterface.regretButtonToggle(true);
		GameInterface.setSingleEvent(false);
		GameInterface.setStackEvent(false);
		GameInterface.updateCardSlots();
	}
}
