package game;

import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Dimension;

import cards.Card;
import slots.CardSlot;
import slots.GameSlot;
import slots.WinSlot;
import actions.NewGameAction;
import actions.RegretAction;

// Class for the game window
public class GameInterface {
	private static Game game;
	
	private static JFrame frame;
	private static JLabel totalWinLabel;
	private static JButton newGameButton;
	private static JButton regretButton;
	
	private static Boolean singleMoveEventInAction = false;
	private static Boolean stackMoveEventInAction = false;
	private static Card cardInMovement;
	private static ArrayList<Card> cardStackInMovement;
	private static CardSlot previousCardSlot;
	private static ArrayList<Card> lastMove;
	private static CardSlot lastMovePrevCardSlot;
	
	private static NewGameAction newGameAction = new NewGameAction();
	private static RegretAction regretAction = new RegretAction();
	
	private static Timer timer;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					game = Game.getGameInstance();
					new GameInterface();
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		});
	}

	/**
	 * Create the application.
	 */
	private GameInterface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {				
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(1000, 900));
		frame.setVisible(true);
		
		JPanel topPanel = new JPanel();
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);
		GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.columnWidths = new int[]{10, 100, 5, 100, 250, 100, 5, 100, 5, 100, 5, 100, 10};
		gbl_topPanel.rowHeights = new int[]{20, 150};
		topPanel.setLayout(gbl_topPanel);
		
		totalWinLabel = new JLabel("Total Wins: 0");
		GridBagConstraints gbc_totalWinLabel = new GridBagConstraints();
		gbc_totalWinLabel.fill = GridBagConstraints.BOTH;
		gbc_totalWinLabel.insets = new Insets(10, 0, 10, 0);
		gbc_totalWinLabel.gridx = 11;
		gbc_totalWinLabel.gridy = 0;
		topPanel.add(totalWinLabel, gbc_totalWinLabel);
		
		newGameButton = new JButton("New Game");
		newGameButton.addActionListener(newGameAction);
		GridBagConstraints gbc_newGameButton = new GridBagConstraints();
		gbc_newGameButton.fill = GridBagConstraints.BOTH;
		gbc_newGameButton.insets = new Insets(10, 0, 10, 0);
		gbc_newGameButton.gridx = 1;
		gbc_newGameButton.gridy = 0;
		topPanel.add(newGameButton, gbc_newGameButton);
		
		regretButton = new JButton("Regret move");
		regretButton.addActionListener(regretAction);
		regretButton.setEnabled(false);
		GridBagConstraints gbc_regretButton = new GridBagConstraints();
		gbc_regretButton.fill = GridBagConstraints.BOTH;
		gbc_regretButton.insets = new Insets(10, 0, 10, 0);
		gbc_regretButton.gridx = 3;
		gbc_regretButton.gridy = 0;
		topPanel.add(regretButton, gbc_regretButton);
		
		JLayeredPane deckSlot = game.getDeckSlot();
		GridBagConstraints gbc_DeckSlot = new GridBagConstraints();
		gbc_DeckSlot.fill = GridBagConstraints.BOTH;
		gbc_DeckSlot.gridx = 1;
		gbc_DeckSlot.gridy = 1;
		topPanel.add(deckSlot, gbc_DeckSlot);
		
		JLayeredPane drawSlot = game.getDrawSlot();
		GridBagConstraints gbc_DrawSlot = new GridBagConstraints();
		gbc_DrawSlot.fill = GridBagConstraints.BOTH;
		gbc_DrawSlot.gridx = 3;
		gbc_DrawSlot.gridy = 1;
		topPanel.add(drawSlot, gbc_DrawSlot);
		
		for (int i = 0; i < game.getWinSlots().size(); i++) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridy = 1;
			gbc.gridx = 5 + (i * 2);
			topPanel.add(game.getWinSlots().get(i), gbc);
		}		
		
		JPanel mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[]{70, 100, 10, 100, 10, 100, 10, 100, 10, 100, 10, 100, 10, 100, 70};
		gbl_mainPanel.rowHeights = new int[]{30, 700};
		gbl_mainPanel.rowWeights = new double[]{0.0, 1.0};
		mainPanel.setLayout(gbl_mainPanel);
		
		for (int i = 0; i < game.getGameSlots().size(); i++) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridy = 1;
			gbc.gridx = 1 + (i * 2);
			mainPanel.add(game.getGameSlots().get(i), gbc);
		}
		
		frame.pack();
	}

	// Updates the card slot graphics
	public static void updateCardSlots() {
		frame.revalidate();
		frame.repaint();
		
	}
	
	// Checks if the deck and draw slot are empty, and if there are any face down cards
	public static void checkIfAutoWinPossible() {
		
		if (!game.getDeck().isEmpty()) return;
		if (!game.getDrawSlot().getCards().isEmpty()) return;
		
		for (int i = 0; i < game.getGameSlots().size(); i++) {
			ArrayList<Card> cardsInGameSlot = game.getGameSlots().get(i).getCards();
			
			for (int j = 0; j < cardsInGameSlot.size(); j++) {
				if (cardsInGameSlot.get(j).isFaceDown()) return;
			}
		}
		
		// If deck and draw slots are empty, and no face down cards exist
		moveAllCardsToWinSlots();
		incrementWinCounter();
	}
	
	// Increments the number of wins in the win label
	private static void incrementWinCounter() {
		String winLabelText = totalWinLabel.getText();
		int winAmount = Integer.parseInt(winLabelText.substring(winLabelText.lastIndexOf(' ') + 1));
		winAmount++;
		totalWinLabel.setText("Total Wins: "+winAmount);
	}
	
	// Move all cards in game slots to the win slots
	private static void moveAllCardsToWinSlots() {
		ArrayList<GameSlot> gameSlots = game.getGameSlots();
		ArrayList<WinSlot> winSlots = game.getWinSlots();
		
		// Use a swing timer to move one card from a game slot to a win slot every 50 milliseconds
		timer = new Timer(50, new ActionListener() {
			int slotCounter = 0;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Card topCard = null;
				// Get the top card if the slot contains any cards
				if (!gameSlots.get(slotCounter).getCards().isEmpty()) {
					topCard = gameSlots.get(slotCounter).getTopCard();
				}
				
				// Check if the top card has a legal moves to any win slot
				for (int i = 0; i < winSlots.size(); i++) {
					if (winSlots.get(i).moveIsLegal(topCard)) {
						gameSlots.get(slotCounter).getCards().remove(topCard);
						gameSlots.get(slotCounter).remove(topCard);
						winSlots.get(i).getCards().add(topCard);
						topCard.setBounds(0, 0, 100, 150);
						int cardPosInStack = winSlots.get(i).getCards().indexOf(topCard);
						winSlots.get(i).add(topCard, new Integer(cardPosInStack + 1), 0);
						
						GameInterface.updateCardSlots();
					}
				}
				
				slotCounter++;
				if (slotCounter == 7) slotCounter = 0;
				
				// Iterate until no cards remain in the game slots
				if (game.checkWin() == true) {
					timer.stop();
					GameInterface.regretButtonToggle(true);
				}
				
			}
		});
		timer.start();
	}
	
	// Toggles the Regret Move button
	public static void regretButtonToggle (Boolean regretButtonDisabled) {
		if (regretButtonDisabled) {
			regretButton.setEnabled(false);
		} else {
			regretButton.setEnabled(true);
		}
	}
	
	// Getters and setters for the member variables
	public static Boolean getSingleEvent() {
		return singleMoveEventInAction;
	}
	
	public static Boolean getStackEvent() {
		return stackMoveEventInAction;
	}
	
	public static void setSingleEvent(Boolean singleMoveEventInAction) {
		GameInterface.singleMoveEventInAction = singleMoveEventInAction;
	}

	public static void setStackEvent(Boolean stackMoveEventInAction) {
		GameInterface.stackMoveEventInAction = stackMoveEventInAction;
	}
	public static Card getCardInMovement() {
		return cardInMovement;
	}

	public static void setCardInMovement(Card cardInMovement) {
		GameInterface.cardInMovement = cardInMovement;
	}

	public static ArrayList<Card> getCardStackInMovement() {
		return cardStackInMovement;
	}

	public static void setCardStackInMovement(ArrayList<Card> cardStackInMovement) {
		GameInterface.cardStackInMovement = cardStackInMovement;
	}
	
	public static CardSlot getPreviousCardSlot() {
		return previousCardSlot;
	}

	public static void setPreviousCardSlot(CardSlot previousCardSlot) {
		GameInterface.previousCardSlot = previousCardSlot;
	}
	
	public static void setLastMove (Card card, ArrayList<Card> cards) {
		ArrayList<Card> lastMove = new ArrayList<Card>();
		if (card == null) {
			lastMove.addAll(cards);
		} else {
			lastMove.add(card);
		}
		GameInterface.lastMove = lastMove;
	}
	
	public static ArrayList<Card> getLastMove() {
		return lastMove;
	}
	
	
	public static CardSlot getLastMovePrevCardSlot() {
		return lastMovePrevCardSlot;
	}

	public static void setLastMovePrevCardSlot(CardSlot lastMovePrevCardSlot) {
		GameInterface.lastMovePrevCardSlot = lastMovePrevCardSlot;
	}
}
