/*
 *
 * Hra Solitaire pre predmet IJA (Seminář Java) VUT FIT 2017
 * 
 * @author Rudolf Kučera     - xkucer91
 * @author Richard Činčura   - xcincu00
 *
 */

package ija.ija2016.board;
import java.util.Iterator;
import java.util.Stack;
import ija.ija2016.board.*;
import ija.ija2016.cards.*;
import ija.ija2016.cards.Card.Color;

/**
 * Class for target pile.
 * @param int size          - number of card currently in the stack
 * @param Stack<Card> cards - here are the cards stored
 * @param Color c           - only cards of this color can be stored here
 * @param boolean empty     - is this pile empty
 */
public class TargetPile {
	private int size;
	private Stack<Card> cards;
	public Color c;
	private boolean empty;
	
	/**
	 * Constructs the object.
	 */
	public TargetPile () {
		this.size = 0;
		this.cards = new Stack<Card>();
		this.empty = true;
	}
	
	/**
	 * Return a card from top of the stack but DO NOT REMOVE.
	 *
	 * @return Card  - the card from the top
	 */
	public Card peek() {
		if (this.size > 0)
			return this.cards.peek();
		else
			return null;
	}
	
	/**
	 * Puts a card into the pile.
	 * On empty target piles only ACE cards can be placed.
	 * When placing the ace, the TargetPile.c (color of pile) is set.
	 * 
	 * If not empty, only cards that have value greater by 1 and same color can be placed in the stack.
	 *
	 * @param  Card c - the card that shall be put into the stack
	 *
	 * @return True if the card was placed, False if it did not met the conditions.
	 */
	public boolean put(Card c) {
		//do prazdneho pola sa moze ukladat eso lubovolnej farby
		if (this.empty) {
			if (c.getValue() == 1) { // je to eso?
				this.c = c.getColor();// nastavi farbu
				this.size++;
				this.cards.push(c);
				this.empty = false;
				return true;
			} else {
				return false;
			}
		} else {
			if (c.getColor() == this.c) { // sedi farba?
				if (this.cards.peek().getValue() == c.getValue()-1) { // sedi cislo??
					this.size++;
					this.cards.push(c);
					this.empty = false;
					return true;
				} else 
					return false;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Put the card into stack with no restrictions
	 *
	 * @param Card c - the card that is pushed into the stack
	 */
	public void push(Card c) {
		this.cards.push(c);
		this.size++;
	}
	
	/**
	 * Determines if empty.
	 *
	 * @return     True if empty, False otherwise.
	 */
	public boolean isEmpty() {
		if (this.size == 0)
			return true;
		else
			return false;
	}

	/**
	 * Gets a card from top.
	 * If removing the last card (ACE), the TargetPile.empty is set to true.
	 *
	 * @return  If empty null, else Card from the top.
	 */
	public Card get() {
		if (this.size > 0) {
			if (this.size == 1) {
				this.size--;
				this.empty = true;
				return this.cards.pop();				
			} else {
				this.size--;
				this.empty = false;
				return this.cards.pop();
			}
		} else
			return null;
	}
	
	/**
	 * Gets the iterator.
	 *
	 * @return Iterator<Card>  The iterator.
	 */
	public Iterator<Card> getIter() {
		return this.cards.iterator();
	}
	
	/**
	 * Get the card from the top and decrease size. USED ONLY BY UNDO
	 *
	 * @return Card  - the card from the top
	 */
	public Card undo() {
		this.size--;
		if (this.size == 0)
			this.empty = true;
		return this.cards.pop();
	}
}
