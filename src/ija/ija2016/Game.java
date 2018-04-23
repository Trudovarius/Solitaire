/*
 *
 * Hra Solitaire pre predmet IJA (Seminář Java) VUT FIT 2017
 * 
 * @author Rudolf Kučera     - xkucer91
 * @author Richard Činčura   - xcincu00
 *
 */

package ija.ija2016;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import ija.ija2016.Move.pile;
import ija.ija2016.board.*;
import ija.ija2016.cards.*;
import java.util.Stack;

import javax.swing.JOptionPane;

import ija.ija2016.board.*;
import ija.ija2016.cards.*;

/**
 * Class for game.
 * Every instance of this class represents a solitaire board with all the cards and piles and moves.
 * 
 * @param HeapPile heap      - represents the pile from where the cards are drawn
 * @param DumpPile dump      - here are stored the cards drawn from heap
 * 
 * @param TargetPile[] target- 4 target piles
 * @param WorkPile[] work    - 7 work piles
 * 
 * @param int Score          - actual score of the game
 * @param int Time           - time passed since start of the game
 * 
 * @param Stack<Move> moves  - here are stored the moves for undo to be undone
 * @param Move m             - here is the actual move, after every move is
 */
public class Game {
	public HeapPile heap;
	public DumpPile dump;
	
	public TargetPile[] target;
	public WorkPile[] work;
	
	private int Score;
	private int Time;
	
	private Stack<Move> moves;
	
	public Move m;
	
	/**
	 * Creates a new Game from scratch.
	 * Initializes the variables and fills them with default data.
	 * 
	 * CardPack is created, shuffled and then placed on the deck.
	 * @see CardPack
	 */
	public Game() {
		this.Score = this.Time = 0;
		// vytvorenie vsetkych poli
		this.heap = new HeapPile();
		this.dump = new DumpPile();
		this.target = new TargetPile[4];
		this.work = new WorkPile[7];
		this.m = new Move();
		this.moves = new Stack<Move>();
		
		// vygenerovanie balicku kariet a zamiesanie
		CardPack cards = new CardPack();
		cards.shuffle();
		
		// vlozenie kariet na svoje miesta
		for (int i = 0; i < 7; i++) {
			this.work[i] = new WorkPile(i);
			this.work[i].init(cards, i+1);
		}
		for (int i = 0; i < 4; i++) {
			this.target[i] = new TargetPile();
		}
		this.heap.init(cards);
		
	}
	
	/**
	 * Creates a new Game from a file.
	 * 
	 * This constructor opens the given file, reads it and then fills all the variables
	 * according to the file.
	 *
	 * @param String filename - the name of the file
	 */
	public Game (String filename) {
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader("save/"+filename);
			br = new BufferedReader(fr);

			br = new BufferedReader(new FileReader("save/"+filename));
			
			// load TIME
			String line = br.readLine();
			if (GUI.isNumeric(line))
				this.Time = Integer.parseInt(line);
			
			// load SCORE
			line = br.readLine();
			if (GUI.isNumeric(line))
				this.Score = Integer.parseInt(line);
			
			// load HEAP
			line = br.readLine();
			String[] splited = line.split("\\s+");
			int size = splited.length;
			this.heap = new HeapPile();
			for (int i = 0; i < size; i++) {
				if (splited[i].length() != 0) {
					Card c = new Card(splited[i]);
					this.heap.push(c);
				} else size--;
			}
			
			// load DUMP
			line = br.readLine();
			splited = line.split("\\s+");
			size = splited.length;
			this.dump = new DumpPile();
			for (int i = 0; i < size; i++) {
				if (splited[i].length() != 0) {
					Card c = new Card(splited[i]);
					this.dump.push(c);
				} else size--;
			}
			
			// load TARGET
			this.target = new TargetPile[4];
			for (int i = 0; i < 4; i++) {
				line = br.readLine();
				splited = line.split("\\s+");
				size = splited.length;
				this.target[i] = new TargetPile();
				for (int j = 0; j < size; j++) {
					if (splited[j].length() != 0) {
						Card c = new Card(splited[j]);
						this.target[i].put(c);
						this.target[i].c = c.getColor();
					} else size--;
				}
			}
			// load WORK
			this.work = new WorkPile[7];
			for (int i = 0; i < 7; i++) {
				line = br.readLine();
				splited = line.split("\\s+");
				size = splited.length;
				this.work[i] = new WorkPile(i);
				for (int j = 0; j < size; j++) {
					if (splited[j].length() != 0) {
						Card c = new Card(splited[j]);
						this.work[i].push(c);
						
					} else size--;
				}
			}
			this.m = new Move();
			this.moves = new Stack<Move>();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * This function is called every second by a timer and increases Time by 1.
	 * @see GUI.startUp()
	 */
    public void incTime() {
    	this.Time++;
    }
    
    /**
     * Adds a move to the stack Game.moves.
     * Moves stored here can be undone by using Game.undo()
     * @see Game.undo()
     * 
     * Only 5 moves can be stored here. Last move is deleted if there are more.
     *
     * @param Move m   - an already done move
     */
    public void addMove(Move m) {
    	// vlozi tah do zasobniku
    	this.moves.push(m);
    	int s = this.moves.size();
    	// ak je ulozenych viacej ako 5 tahov, posledny zmaz
    	if (s > 5) {
    		this.moves.remove(0);
    	}
    }
    
    /**
     * Increase the score of the game.
     * Minimum score is 0.
     *
     * @param int i   - this number will be added to the score
     */
    public void incScore(int i) {
    	this.Score += i;
    	if (this.Score < 0)
    		this.Score = 0;
    }
	
	/**
	 * Gets the time passed since the start of the game.
	 *
	 * @return int - the amount of seconds passed since the start of the game
	 */
	public int getTime() {
		return this.Time;
	}
	
	/**
	 * Gets the score.
	 *
	 * @return int The score.
	 */
	public int getScore() {
		return this.Score;
	}
	
	/**
	 * This function returns the card on the top from a workpile at the given index.
	 *
	 * @param int index - index of the workpile in the array Game.work[]
	 *
	 * @return Card from the top
	 */
	public Card popWorkPile(int index) {
		return this.work[index].get(this);
	}
	
	/**
	 * This function returns all the cards from a workpile given by the index 'index'.
	 *
	 * @param  int index - index of the workpile in the array Game.work[]
	 *
	 * @return Stack<Card> - stack of cards from a workpile
	 */
	public Stack<Card> getAllCards(int index) {
		Stack<Card> cards = this.work[index].getAllCards();
		Stack<Card> ret = new Stack<Card>();

		Iterator<Card> iter = cards.iterator();
		while (iter.hasNext()) {
			ret.push(iter.next());
		}
		return cards;
	}
	
	/**
	 * This function checks if the player won the game.
	 * 
	 * The game is winned by having each king on the top of each target pile
	 *
	 * @return true if win, else false
	 */
	public boolean win() {
		boolean win = false;
		for (int i = 0; i < 4; i++) {
			if (!this.target[i].isEmpty())
				if (this.target[i].peek().getValue() == 13)
					win = true;
				else {
					win = false;
					break;
				}
			else
				win = false;
		}
		return win;
	}
	
	/**
	 * Sets Game.Time to 0
	 */
	public void resetTime() {
		this.Time = 0;
	}
	
	/**
	 * This function pops a Move from the (Stack<Move>) Game.moves stack and reverses it.
	 * 
	 */
	public void undo() {
		try {
			if (this.moves.size() > 0) {
				Move m = this.moves.pop();
	        	Stack<Card> cards = new Stack<Card>();
	        	
				for (int i = 0; i < m.count; i++) {
					// zoberem kartu odkial som ju predtym daval
					Card c;
					switch (m.to) {
						case w1: c = this.work[0].undo();  break;
						case w2: c = this.work[1].undo(); break;
						case w3: c = this.work[2].undo(); break;
						case w4: c = this.work[3].undo(); break;
						case w5: c = this.work[4].undo(); break;
						case w6: c = this.work[5].undo(); break;
						case w7: c = this.work[6].undo(); break;
						case t1: c = this.target[0].undo(); break;
						case t2: c = this.target[1].undo(); break;
						case t3: c = this.target[2].undo(); break;
						case t4: c = this.target[3].undo(); break;
						case d : c = this.dump.undo(); c.setFaceDown(); break;
						case h : c = this.heap.undo(); c.setFaceUp(); break;	
						default: c = null;
					}
					cards.push(c);
				}
				if (m.to == pile.h) {
					for (int i = cards.size(); i  > 0; i--) {
						Card card = cards.get(i-1);
						cards.remove(i-1);
						cards.push(card);
					}
				}
				for (int i = 0; i < m.count; i++) {
					switch (m.from) {
						case w1: if (i == 0 && !this.work[0].isEmpty()) {this.work[0].peek().setFaceDown(); m.Score +=5; } this.work[0].push(cards.pop()); break;
						case w2: if (i == 0 && !this.work[1].isEmpty()) {this.work[1].peek().setFaceDown(); m.Score +=5; }this.work[1].push(cards.pop()); break;
						case w3: if (i == 0 && !this.work[2].isEmpty()) {this.work[2].peek().setFaceDown(); m.Score +=5; }this.work[2].push(cards.pop()); break;
						case w4: if (i == 0 && !this.work[3].isEmpty()) {this.work[3].peek().setFaceDown(); m.Score +=5; }this.work[3].push(cards.pop()); break;
						case w5: if (i == 0 && !this.work[4].isEmpty()) {this.work[4].peek().setFaceDown(); m.Score +=5; }this.work[4].push(cards.pop()); break;
						case w6: if (i == 0 && !this.work[5].isEmpty()) {this.work[5].peek().setFaceDown(); m.Score +=5; }this.work[5].push(cards.pop()); break;
						case w7: if (i == 0 && !this.work[6].isEmpty()) {this.work[6].peek().setFaceDown(); m.Score +=5; }this.work[6].push(cards.pop()); break;
						case t1: this.target[0].push(cards.pop()); break;
						case t2: this.target[1].push(cards.pop()); break;
						case t3: this.target[2].push(cards.pop()); break;
						case t4: this.target[3].push(cards.pop()); break;
						case d : this.dump.push(cards.pop()); break;
						case h : this.heap.push(cards.pop()); break;
					}
				}
				this.incScore(-m.Score);
			}
		} catch (NullPointerException e) {
			
		}
	}

	/**
	 * This function opens a file, sends it to function Game.writeGame() that writes the game down and then
	 * shows a MessageDialog with the name of the file. (mostly savegame-N)
	 * @see Game.writeGame()
	 */
	public void save() {
		try{
			int i = new File("save/").listFiles().length;
		    PrintWriter writer = new PrintWriter("save/savegame-" + i, "UTF-8");
		    this.writeGame(writer);
		    JOptionPane.showMessageDialog(null,
    			    "You can now find your game under the name savegame-"+i+" int the folder save.",
    			    "Game saved!",
    			    JOptionPane.PLAIN_MESSAGE);
		    writer.close();
		} catch (IOException e) {
		   System.out.println("Error saving your game! The game is not saved.");
		}
	}
	
	/**
	 * Writes a game.
	 * Basicly converts the game to text.
	 * 
	 * The format of the text in the file is this:
	 * 1. line - Time
	 * 2. line - Score
	 * 3. line - HEAP  (cards on the heap pile) (cards are of format A(S)1 >> A-value S-color 1-isFaceUp)
	 * 4. line - DUMP
	 * 5. line - TARGET 1
	 * |
	 * 8. line - TARGET 4
	 * 9. line - WORK 1
	 * |
	 * 15.line - WORK 7
	 *
	 * @param PrintWriter writer - the opened file that will contain the text
	 */
	public void writeGame(PrintWriter writer) {
		writer.println(this.Time);
		writer.println(this.Score);
		Iterator<Card> iter = this.heap.getIter();
		String line = "";
		// vypis heapu
		while (iter.hasNext()) {
			Card c = iter.next();
			if (c.isFaceUp())
				line += c.toString() + "1 ";
			else
				line += c.toString() + "0 ";
		}
		writer.println(line);
		line = "";
		iter = this.dump.getIter();
		// vypis dumpu
		while (iter.hasNext()) {
			Card c = iter.next();
			if (c.isFaceUp())
				line += c.toString() + "1 ";
			else
				line += c.toString() + "0 ";
		}		
		// vypis vsetkych targetov
		for (int i = 0; i < 4 ; i++) {
			writer.println(line);
			line = "";
			iter = this.target[i].getIter();
			while (iter.hasNext()) {
				Card c = iter.next();
				if (c.isFaceUp())
					line += c.toString() + "1 ";
				else
					line += c.toString() + "0 ";
			}
		}
		// vypis workov
		for (int i = 0; i < 7 ; i++) {
			writer.println(line);
			line = "";
			iter = this.work[i].getIter();
			while (iter.hasNext()) {
				Card c = iter.next();
				if (c.isFaceUp())
					line += c.toString() + "1 ";
				else
					line += c.toString() + "0 ";
			}
		}
		writer.println(line);
	}
}
