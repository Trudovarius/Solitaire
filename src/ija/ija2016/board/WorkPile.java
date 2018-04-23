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

import ija.ija2016.Game;
import ija.ija2016.board.*;
import ija.ija2016.cards.*;
import java.util.Stack;

/**
 * Class for heap pile.
 * @param int size          - number of card currently in the stack
 * @param Stack<Card> cards - here are the cards stored
 * @param int index         - index of the workpile in the Game.work[] array
 */
public class WorkPile {
	private int size;
	private Stack<Card> cards;
	private int index;

	/**
	 * Constructs the object.
	 *
	 * @param int index         - index of the workpile in the Game.work[] array
	 */
	public WorkPile (int index) {
		this.size = 0;
		this.cards = new Stack<Card>();
		this.index = index;
	}

	/**
	 * Places the cards.
	 * According to index. (In the end, the first workpile has 1 card turned faceup
	 * ,second has 2 cards, the one on top turned faceup.....
	 * , seventh has 7 cards, the one on top turned faceup, others facedown)
	 *
	 * @param CardPack cards - 52 cards inside a cardpack and shuffled
	 * @param int index      - index of the workpile in the Game.work[] array
	 */
	public void init(CardPack cards, int index) {
		for (int i = 1; i <= index; i++) {
			Card c = cards.get();
			if (i == index)
				c.setFaceUp();
			else
				c.setFaceDown();
			this.size++;
			this.cards.push(c);
		}
	}
	
	/**
	 * Returns a string representation of the object.
	 * Probably never used XDDD
	 *
	 * @return All cards inside a workpile as String
	 */
	@Override
	public String toString() {
		String ret = "";
		Iterator<Card> iter = this.cards.iterator();
		int i = 1;
		while (iter.hasNext()) {
			Card c = iter.next();
			if (c.isFaceUp())
				ret += i++ +"  - "+c.toString()+" FaceUP\n";
			else
				ret += i++ +"  - "+c.toString()+" FaceDOWN\n";
		}
		return ret;
	}
	
	/**
	 * Return a card from top. If a facedown card is beneath turn it faceup and add 5 score
	 *
	 * @param  Game g - game that contains this workpile (to increase score)
	 *
	 * @return  Null if empty, else a card from the top
	 */
	public Card get(Game g) {
		Card c;
		if (this.size > 0) {
			this.size--;
			c =  this.cards.pop();
			if (!this.cards.isEmpty()) // Turn over Tableau card +5
				if (!this.cards.peek().isFaceUp()) {
					this.cards.peek().setFaceUp();
					g.incScore(5);
				}
		} else {
			c = null;
		}
		return c;
	}
	
	/**
	 * Push card into pile, no matter what, increase size.
	 *
	 * @param Card c - card to push into pile
	 */
	public void push(Card c) {
		this.cards.push(c);
		this.size++;
	}
	
	/**
	 * Get card from pile, decrease size.
	 * Checks if the pile is not empty,
	 *
	 * @return Card if pile is not empty, false if pile is empty
	 */
	public Card get() {
		if (this.size > 0) {
			this.size--;
			return this.cards.pop();
		}
		return null;
	}
	
	/**
	 * Insert a card into this pile.
	 * The cards is inserted only if the value on the top of the workpile is by one greater
	 * then the card c.
	 * Only Kings can be placed on empty piles.
	 *
	 * @param Card c - the card to put
	 *
	 * @return True if the card c met the conditions, False if not and the put failed.
	 */
	public boolean put(Card c) {
		if (this.size == 0) {
			if (c.getValue() == 13) {
				this.size++;
				this.cards.push(c);
				return true;
			} else 
				return false;
		} else {
			if (!this.cards.peek().isSimilarColor(c) && this.cards.peek().getValue() == (c.getValue() + 1) ) {
				this.size++;
				this.cards.push(c);
				return true;
			} else 
				return false;
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
		return null;
	}
	
	/**
	 * Gets all cards.
	 *
	 * @return Stack<Card>   Stack of All cards from the pile.
	 */
	public Stack<Card> getAllCards() {
		Stack<Card> cards = new Stack<Card>();
		
		Iterator<Card> iter = this.cards.iterator();
		while (iter.hasNext()) {
			cards.push(iter.next());
		}
		Stack<Card> ret = new Stack<Card>();
		for (int i = cards.size(); i >0; i--) {
			ret.push(cards.pop());
		}
		return ret;
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
	 * Gets the n cards.
	 *
	 * @param Game g   - the game (not used in this function but passed to another)
	 * @param int N    - number of the cards
	 *
	 * @return     The n cards.
	 */
	public Stack<Card> getNCards(Game g, int N) {
		Stack<Card> cards = new Stack<Card>();
		for (int i = 0; i < N; i++) {
			cards.push(this.get(g));
		}
		return cards;
	}
	
	/**
	 * Determines if empty.
	 *
	 * @return    True if empty, False otherwise.
	 */
	public boolean isEmpty() {
		if (this.size > 0)
			return false;
		else
			return true;
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
	 * Get a card from this pile from the given index.
	 * The stack bound are not controlled, programmer has to do it.
	 *
	 * @param int index - get the card from this index
	 *
	 * @return Card from the given index
	 */
	public Card get (int index) {
		return this.cards.get(index);
	}
	
	/**
	 * Gets the size.
	 *
	 * @return int - The size.
	 */
	public int getSize () {
		return this.size;
	}
}
