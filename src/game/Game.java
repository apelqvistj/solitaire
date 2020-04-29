package game;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import actions.CardAction;
import actions.DeckSlotAction;
import actions.GameSlotAction;
import actions.WinSlotAction;
import cards.Card;
import cards.Deck;
import slots.*;

// Game is a singleton class
public class Game {
	private static Game game = null;
	private ArrayList<WinSlot> winSlots = new ArrayList<WinSlot>();
	private ArrayList<GameSlot> gameSlots = new ArrayList<GameSlot>();
	private ArrayList<Card> deck;
	private DeckSlot deckSlot = new DeckSlot();
	private DeckSlot drawSlot = new DeckSlot();
	private WinSlot WinSlot1 = new WinSlot();
	private WinSlot WinSlot2 = new WinSlot();
	private WinSlot WinSlot3 = new WinSlot();
	private WinSlot WinSlot4 = new WinSlot();
	private GameSlot gameSlot1 = new GameSlot();
	private GameSlot gameSlot2 = new GameSlot();
	private GameSlot gameSlot3 = new GameSlot();
	private GameSlot gameSlot4 = new GameSlot();
	private GameSlot gameSlot5 = new GameSlot();
	private GameSlot gameSlot6 = new GameSlot();
	private GameSlot gameSlot7 = new GameSlot();
	
	// Public method that returns the instance of the class
	public static Game getGameInstance() {
		if (game == null) {
			game = new Game();
		}
		return game;
	}
	
	// Private constructor
	private Game() {
		addCardsToCardSlots();
		addListenersToCards();
	}
	
	// Adds cards to card slots
	private void addCardsToCardSlots() {
		deck = Deck.getDeck();
		setWinSlots();
		setGameSlots();
		addEmptySpotsToCardSlots();
		int cardPosInSlot;
		
		// Add face up and face down cards to game slots
		for (int i = 0; i < gameSlots.size(); i++) {
			// Take the top card from the deck
			Card topCard = deck.get(deck.size() - 1);
			
			// For every game slot except the first one, add face down cards
			// GameSlot2 has one face down card, GameSlot3 has two, and so on..
			if (i > 0) {
				for (int j = i; j > 0; j--) {
					topCard.setFaceDown(true);
					gameSlots.get(i).getCards().add(topCard);
					cardPosInSlot = gameSlots.get(i).getCards().indexOf(topCard);
					topCard.setBounds(0, cardPosInSlot * 25, 100, 150);
					gameSlots.get(i).add(topCard, new Integer(cardPosInSlot + 1), 0);	
					topCard.setSlot(gameSlots.get(i));
					deck.remove(topCard);
					topCard = deck.get(deck.size() - 1);
				}
			}
			// Add a face up card to every game slot
			topCard.setFaceDown(false);
			gameSlots.get(i).getCards().add(topCard);
			cardPosInSlot = gameSlots.get(i).getCards().indexOf(topCard);
			topCard.setBounds(0, cardPosInSlot * 25, 100, 150);
			gameSlots.get(i).add(topCard, new Integer(cardPosInSlot + 1), 0);
			topCard.setSlot(gameSlots.get(i));
			deck.remove(topCard);
		}
		
		// Add the remaining to the deck slot 
		deck.forEach(card -> card.setFaceDown(true));
		for (Card card: deck) {
			cardPosInSlot = deck.indexOf(card);
			
			card.setBounds(0, 0, 100, 150);
			card.setInactiveBorder();
			deckSlot.add(card, new Integer(cardPosInSlot + 1), 0);	
		}
		
		deckSlot.getCards().addAll(deck);
	}
	
	// Adds clickable empty spots to all game slots, win slots, and the deck slot
	private void addEmptySpotsToCardSlots() {		
		gameSlots.forEach(slot -> addEmptyGameSlot(slot, emptyButtonBuilder()));
		winSlots.forEach(slot -> addEmptyWinSlot(slot, emptyButtonBuilder()));
		addEmptyDeckSlot(emptyButtonBuilder());
	}
	
	// Returns a JButton acting as a clickable empty spot
	private JButton emptyButtonBuilder() {
		JButton emptySpotButton = new JButton();
		
		emptySpotButton.setMinimumSize(new Dimension(100, 150));
		emptySpotButton.setPreferredSize(new Dimension(100, 150));
		emptySpotButton.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
		emptySpotButton.setBounds(0, 0, 100, 150);
		emptySpotButton.setBackground(Color.WHITE);
		
		return emptySpotButton;
	}
	
	// Methods for adding the empty spot to the slot, and adding action listeners to them
	private void addEmptyWinSlot(WinSlot slot, JButton button) {		
		slot.add(button, new Integer(0), 0);
		
		CardAction actionListener = WinSlotAction.getAction();
		button.addActionListener(actionListener);
	}
	
	private void addEmptyDeckSlot(JButton button) {		
		deckSlot.add(button, new Integer(0), 0);
		
		CardAction actionListener = DeckSlotAction.getAction();
		button.addActionListener(actionListener);
	}
	
	private void addEmptyGameSlot(GameSlot slot, JButton button) {
		slot.add(button, new Integer(0), 0);
		
		CardAction actionListener = GameSlotAction.getAction();
		button.addActionListener(actionListener);
	}
	
	// Takes the top card in the deck slot and adds it to the draw slot
	public void drawCardFromDeck() {
		Card topCard = deck.get(deck.size() - 1);
		topCard.setFaceDown(false);
		deck.remove(topCard);
		deckSlot.remove(topCard);
		topCard.setSlot(drawSlot);
		drawSlot.getCards().add(topCard);
		
		int cardPosInSlot = drawSlot.getCards().indexOf(topCard);
		topCard.setBounds(0, 0, 100, 150);
		topCard.setInactiveBorder();
		drawSlot.add(topCard, new Integer(cardPosInSlot), 0);
	}
	
	// Takes all cards in the draw slot and adds them face down to the deck slot in reverse order
	public void moveCardsToDeckSlot() {
		ArrayList<Card> cards = drawSlot.getCards();
		cards.forEach(card -> card.setFaceDown(true));
		
		for (int i = cards.size() - 1; i > -1; i--) {
			deck.add(cards.get(i));
			int cardPosInSlot = deck.indexOf(cards.get(i));
			
			deck.get(cardPosInSlot).setBounds(0, 0, 100, 150);
			deckSlot.add(deck.get(cardPosInSlot), new Integer(cardPosInSlot + 1), 0);
		}
		
		drawSlot.getCards().removeAll(cards);
	}
	
	// Returns true if all win slots contain 13 cards
	Boolean checkWin() {
		int fullSlotCounter = 0;
		
		for(CardSlot winSlot: winSlots) {
			if (winSlot.getCards().size() == 13) {
				fullSlotCounter++;
			}
		}
		
		if (fullSlotCounter == 4) {
			return true;
		} 
		return false;
	}

	// Adds action listeners to all cards in game slots and in the deck slot
	private void addListenersToCards() {
		deckSlot.getCards().forEach(card -> card.addActionListener(DeckSlotAction.getAction()));
		gameSlots.forEach(slot -> {
			slot.getCards().forEach(card -> card.addActionListener(GameSlotAction.getAction()));
		});
		
	}
	
	// Removes all cards from every slot
	private void removeAllCards() {
		winSlots.forEach(slot -> {
			slot.getCards().clear();
			slot.removeAll();
		});
		gameSlots.forEach(slot -> {
			slot.getCards().clear();
			slot.removeAll();
		});
		deckSlot.getCards().clear();
		deckSlot.removeAll();
		drawSlot.getCards().clear();
		drawSlot.removeAll();
	}
	
	// Resets everything and starts a new game
	public void newGame() {
		removeAllCards();
		addCardsToCardSlots();
		addListenersToCards();
		GameInterface.updateCardSlots();
		GameInterface.setSingleEvent(false);
		GameInterface.setStackEvent(false);
		GameInterface.regretButtonToggle(true);
	}
	
	// Getters and setters for the member variables
	public ArrayList<WinSlot> getWinSlots() {
		return winSlots;
	}
	
	public ArrayList<GameSlot> getGameSlots() {
		return gameSlots;
	}
	
	private void setWinSlots() {
		winSlots.clear();
		winSlots.add(WinSlot1);
		winSlots.add(WinSlot2);
		winSlots.add(WinSlot3);
		winSlots.add(WinSlot4);
	}
	
	private void setGameSlots() {
		gameSlots.clear();
		gameSlots.add(gameSlot1);
		gameSlots.add(gameSlot2);
		gameSlots.add(gameSlot3);
		gameSlots.add(gameSlot4);
		gameSlots.add(gameSlot5);
		gameSlots.add(gameSlot6);
		gameSlots.add(gameSlot7);
	}
	
	public ArrayList<Card> getDeck() {
		return deck;
	}
	
	public DeckSlot getDeckSlot() {
		return deckSlot;
	}
	
	public DeckSlot getDrawSlot() {
		return drawSlot;
	}
}
