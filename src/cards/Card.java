package cards;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import slots.CardSlot;

// Class for card objects 
// It extends JButton for easy access to ActionListeners and the ability to paint the component
@SuppressWarnings("serial")
public class Card extends JButton {
	private int number;
	private String suit;
	private Boolean faceDown;
	private Boolean isBlack;
	private BufferedImage faceUpImg;
	private BufferedImage faceDownImg;
	private CardSlot slot;
	
	Card(int num, String suit, Boolean isBlack) {
		this.number = num;
		this.suit = suit;
		if (isBlack) {
			this.isBlack = true;
		} else {
			this.isBlack = false;
		}
		this.setInactiveBorder();
	}
	
	// Paints the component with a BufferedImage of the card
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage img;
		if (this.faceDown) {
			img = faceDownImg;
		} else {
			img = faceUpImg;
		}
		
		g.drawImage(img, 0, 0, null);
	}
	
	// Buffers the face up verision of the card
	void bufferFaceUpImage() {
		try {
			faceUpImg = ImageIO.read(this.getClass().getResourceAsStream("/img/"+this.suit+"/"+this.number+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Buffers the face down verision of the card
	void bufferFaceDownImage() {
		try {
			faceDownImg = ImageIO.read(this.getClass().getResourceAsStream("/img/cardBack.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Set a standard black border around the card
	public void setInactiveBorder() {
		this.setBorder(new LineBorder(Color.BLACK));
	}
	
	// Getters and setters for the member variables
	public CardSlot getSlot() {
		return slot;
	}

	public void setSlot(CardSlot slot) {
		this.slot = slot;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getSuit() {
		return suit;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

	public Boolean isFaceDown() {
		return faceDown;
	}

	public void setFaceDown(Boolean faceDown) {
		this.faceDown = faceDown;
	}

	public Boolean isBlack() {
		return isBlack;
	}
	
	
}
