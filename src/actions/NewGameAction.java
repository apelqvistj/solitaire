package actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import cards.Deck;
import game.Game;

// Action listener for the 'New Game' button
@SuppressWarnings("serial")
public class NewGameAction extends AbstractAction {
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Game game = Game.getGameInstance();
		// Reset the deck and start a new game
		Deck.resetDeck();
		game.newGame();
	}

}
