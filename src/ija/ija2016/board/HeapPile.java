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
 * Class for heap pile.
 * @param int size          - number of card currently in the stack
 * @param Stack<Card> cards - here are the cards stored
 */
public class HeapPile {
	private int size;
	private Stack<Card> cards;
	
	/**
	 * Constructs the object.
	 */
	public HeapPile () {
		this.size = 0;
		this.cards = new Stack<Card>();
	}
	
	/**
	 * When creating a game, 28 cards are placed into the workpiles.
	 * The remaining 24 cards are  
	 *
	 * @param      cards  The cards
	 */
	public void init(CardPack cards) {
		int size = cards.count();
		for (int i = 0; i < size; i++) {
			Card c = cards.get();
			c.setFaceDown();
			this.cards.push(c);
			this.size++;
		}

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
	 * Get the card from the top and decrease size, check if size is not 0
	 *
	 * @return Card  - the card from the top or null if pile is empty
	 */
	public Card get() {
		if (this.size > 0) {
			this.size--;
			return this.cards.pop();
		}
		return null;
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
	 * Put the card into stack
	 *
	 * @param Card c - the card that is placed into the stack
	 */
	public void put(Card c) {
		this.size++;
		this.cards.push(c);
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
		this.cards.push(c);
		this.size++;
	}
	
	/**
	 * Gets the iterator.
	 *
	 * @return Iterator<Card>  The iterator.
	 */
	public Iterator<Card> getIter() {
		return this.cards.iterator();
	}
}
