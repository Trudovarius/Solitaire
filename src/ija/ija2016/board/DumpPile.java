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

/**
 * Class for dump pile.
 * @param int size          - number of card currently in the stack
 * @param Stack<Card> cards - here are the cards stored
 */
public class DumpPile {
	private int size;
	private Stack<Card> cards;

	/**
	 * Constructs the object. Initializes the values.
	 */
	public DumpPile () {
		this.size = 0;
		this.cards = new Stack<Card>();
	}
	
	/**
	 * Puts a card inside the stack and increments the size.
	 *
	 * @param Card c  - the card to be put
	 */
	public void put(Card c) {
		this.size++;
		this.cards.push(c);
	}
	
	/**
	 * Gets the size.
	 *
	 * @return int - the number of cards inside
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Get the card from the top and decrease size
	 *
	 * @return Card  - the card from the top
	 */
	public Card get() {
		if (this.size > 0) {
			this.size--;
			return this.cards.pop();
		}
		return null;
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
		return this.cards.pop();
	}
	
	/**
	 * Put the card into stack with no restrictions
	 *
	 * @param Card c - the card that is pushed into the stack
	 */
	public void push(Card c) {
		this.size++;
		this.cards.push(c);
	}
}
