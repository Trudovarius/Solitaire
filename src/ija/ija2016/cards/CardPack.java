/*
 *
 * Hra Solitaire pre predmet IJA (Seminář Java) VUT FIT 2017
 * 
 * @author Rudolf Kučera     - xkucer91
 * @author Richard Činčura   - xcincu00
 *
 */

package ija.ija2016.cards;
import java.util.Stack;
import java.util.Iterator;
import java.util.Random;

/**
 * Class for card pack.
 * @param Stack<Card> cards  -  stack for 52 cards
 * @param int count          -  count of cards still in the pack, initialy 52
 */
public class CardPack {
	private Stack<Card> cards;
	private int count;

	/**
	 * Constructs the object.
	 * Generates all the cards.
	 */
	public CardPack() {
		this.cards = new Stack<Card>();
		for (int i = 1; i < 14; i++) this.cards.push(new Card(Card.Color.CLUBS, i));
		for (int i = 1; i < 14; i++) this.cards.push(new Card(Card.Color.DIAMONDS, i));
		for (int i = 1; i < 14; i++) this.cards.push(new Card(Card.Color.HEARTS, i));
		for (int i = 1; i < 14; i++) this.cards.push(new Card(Card.Color.SPADES, i));
		this.count = 52;
	}
	
	/**
	 * Shuffle the cards.
	 * Random generator generates a number between 1-52.
	 * The card on the generated index is removed and push on top of the stack.
	 * This is repeated 256 times
	 */
	public void shuffle() {
		Random RNGesus = new Random();
		for(int i = 1; i <= 256; i++) {
			int index = RNGesus.nextInt(51) + 1;
			Card card = this.cards.get(index);
			this.cards.remove(index);
			this.cards.push(card);
		}
	}
	
	// zaradom vypise obsah balicka kariet,  len na testovacie ucely
	@Override
	public String toString() {
		String ret = "";
		Iterator<Card> iter = this.cards.iterator();
		int i = 1;
		while (iter.hasNext()) {
			ret += i++ +"  - "+iter.next().toString() + "\n";
		}
		return ret;
	}
	
	/**
	 * Returns the amount of cards still in the pack
	 *
	 * @return  amount of cards still in the pack
	 */
	public int count() {
		return this.count;
	}
	
	/**
	 * Get a card from pack, decrease count
	 *
	 * @return  a card from the top of the CardPack.cards stack.
	 */
	public Card get() {
		this.count--;
		return this.cards.pop();		
	}
	
	
}
