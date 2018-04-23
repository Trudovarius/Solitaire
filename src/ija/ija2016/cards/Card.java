/*
 *
 * Hra Solitaire pre predmet IJA (Seminář Java) VUT FIT 2017
 * 
 * @author Rudolf Kučera     - xkucer91
 * @author Richard Činčura   - xcincu00
 *
 */

package ija.ija2016.cards;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class for card.
 * 
 * @param int value      - value of the card 
 * @param Color c        - color of the card
 * @param boolean faceUp - if true the card is faceup, if false the card is facedown
 */
public class Card {
	private int value;
	private Color c;
	private boolean faceUp;

	/**
	 * This enum describes the Color of a card.
	 * The colors are CLUBS, DIAMOND, HEARTS, SPADES.
	 */
	public static enum Color {
		CLUBS, DIAMONDS, HEARTS, SPADES;
		
		@Override
		public String toString() {
		    switch(this) {
		      case CLUBS: return "C";
		      case DIAMONDS: return "D";
		      case HEARTS: return "H";
		      case SPADES: return "S";
		      default: throw new IllegalArgumentException();
		    }
		}
	}
	
	@Override
	public String toString()  {
		switch(this.value) {
			case 1:
				return "A(" + this.c.toString() + ")";
			case 11:
				return "J(" + this.c.toString() + ")";
			case 12:
				return "Q(" + this.c.toString() + ")";
			case 13:
				return "K(" + this.c.toString() + ")";
			default:
				return this.value + "(" + this.c.toString() + ")";
		}
	}
	
	/**
	 * Constructs the object.
	 *
	 * @param      c      The color
	 * @param      value  The value
	 */
	public Card(Card.Color c, int value) {
		this.value = value;
		this.c = c;
		this.faceUp = false;
	}
	
	/**
	 * Constructs the object and get the values from a string.
	 *
	 * @param String c  -  the format is V(C)[1|0] V- value  C - color 1-faceup 0-facedown
	 */
	public Card(String c) {
		String[] tmp = c.split("[(]");
		switch (tmp[0]) {
			case "A": this.value = 1; break;
			case "J": this.value = 11; break;
			case "Q": this.value = 12; break;
			case "K": this.value = 13; break;
			default: this.value = Integer.parseInt(tmp[0]);
		}
		tmp = tmp[1].split("[)]");
		switch (tmp[0]) {
			case "C": this.c = Color.CLUBS; break;
			case "D": this.c = Color.DIAMONDS; break;
			case "H": this.c = Color.HEARTS; break;
			case "S": this.c = Color.SPADES; break;
		}
		if (Integer.parseInt(tmp[1]) == 1) {
			this.faceUp = true;
		} else
			this.faceUp = false;
	}
	
	@Override
	public boolean equals(Object c) {
	    if (!(c instanceof Card)) {
	        return false;
	    }

	    Card that = (Card) c;

	    // Custom equality check here.
	    return this.c.equals(that.c)
	        && this.value == that.value;
	}
	
	@Override
	public int hashCode() {
	    int hashCode = 1;

	    hashCode = hashCode * 37 + this.c.hashCode();
	    hashCode = hashCode * 37 + this.value;

	    return hashCode;
	}
	
	/**
	 * Gets the image of a card.
	 * Images are located ine the folder img/
	 *
	 * @return BufferedImage image of a card (of its value, or the back if its facedown)
	 */
	public BufferedImage getImg() {
		BufferedImage img = null;
		// ak je karta otocena cislom dolu
		if (!this.faceUp) {
			try {
				img = ImageIO.read(new File("img/back.jpg"));
			} catch (IOException ex) {
				System.out.printf("Error loading images");
				System.exit(1);
			}
		} else {
			String name = "";
			switch (this.value) {
				case 1:  name += "a" 		+ this.c.toString(); break;
				case 11: name += "j" 		+ this.c.toString(); break;
				case 12: name += "q" 		+ this.c.toString(); break;
				case 13: name += "k" 		+ this.c.toString(); break;
				default: name += this.value + this.c.toString(); break;
			}
			try {
				img = ImageIO.read(new File("img/"+name+".jpg"));
			} catch (IOException ex) {
				System.out.printf("Error loading images");
				System.exit(1);
			}			
		}
		return img;
	}
	
	/**
	 * Sets the face up.
	 */
	public void setFaceUp() {
		this.faceUp = true;
	}
	
	/**
	 * Sets the face down.
	 */
	public void setFaceDown() {
		this.faceUp = false;
	}
	
	/**
	 * Determines if face up.
	 *
	 * @return     True if face up, False otherwise.
	 */
	public boolean isFaceUp() {
		return this.faceUp;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return     The value.
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * Gets the color.
	 *
	 * @return     The color.
	 */
	public Color getColor() {
		return this.c;
	}
	
	/**
	 * Determines if similar color.
	 *
	 * @param      c     { parameter_description }
	 *
	 * @return     True if similar color, False otherwise.
	 */
	public boolean isSimilarColor(Card c) {
		if (this.c == Color.CLUBS || this.c == Color.SPADES) {
			if (c.c == Color.CLUBS || c.c == Color.SPADES) 
				return true;
			else
				return false;
		} else {
			if (c.c == Color.HEARTS || c.c == Color.DIAMONDS)
				return true;
			else 
				return false;
		}
	}
}
