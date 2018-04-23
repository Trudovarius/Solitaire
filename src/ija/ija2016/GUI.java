/*
 *
 * Hra Solitaire pre predmet IJA (Seminář Java) VUT FIT 2017
 * 
 * @author Rudolf Kučera     - xkucer91
 * @author Richard Činčura   - xcincu00
 *
 */

package ija.ija2016;
import javax.swing.*;
import javax.swing.border.Border;

import ija.ija2016.Move.pile;
import ija.ija2016.board.*;
import ija.ija2016.cards.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.event.*;

import javafx.event.ActionEvent;

/**
 * Class for GUI.
 * 
 * This class creates the graphic interface of the application.
 * 
 * 
 * @param int gamesCount - number of games that are running
 * @param Game[] games   - the games are stored here
 * @param JPanel[] piles - the JPanel with the cardboards of every game
 * @param JLabel[] scores- the JLabel with the Scores of every game
 * @param JLabel[] times - the JLabel with the Times of every game
 * @param JFrame start   - the first window displayed
 * @param JFrame game    - the window with the games
 */
public class GUI {	
	private int gamesCount;
	private Game[] games;
	private JPanel[] piles;
	private JLabel[] scores;
	private JLabel[] times;
	
	private JFrame start;
	private JFrame game;
	

	/**
	 * Constructs the object.
	 * Initializes the parameter and calls StartUp() function 
	 * @see GUI.StartUp()
	 */
	public GUI() {
		this.start = null;
		this.game = null;
		this.gamesCount = 0;
		this.games = new Game[4];
		this.piles = new JPanel[4];
		this.scores = new JLabel[4];
		this.times = new JLabel[4];
		this.StartUp();
	}
	
	/**
	 * StartUP().
	 * Displays the first window with the basic menu.
	 * Allows user to start a new game or load one.
	 * After starting or loading a game CreateGameGUI() is called.
	 * @see GUI.CreateGameGUI()
     * 
	 */
	public void StartUp() {
		// vytvoreni okna aplikace
		this.start = new JFrame("Solitaire");
		// nastaveni velikosti
		start.pack();
		start.setSize(500, 300);
		start.getContentPane().setBackground(Color.WHITE);
		this.centreWindow(start);

		Container layout = start.getContentPane();	    

		JLabel label = new JLabel("xkucer91_xcincu00");
		label.setPreferredSize(new Dimension(500, 100));
		layout.add(label,BorderLayout.PAGE_START);
		label.setBorder(BorderFactory.createEmptyBorder(0, 185, 10, 20));

		JPanel menu = new JPanel(new BorderLayout());
		menu.setSize(new Dimension(500, 50));
		menu.setBackground(Color.WHITE);
		menu.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		
		JButton StartButton = new JButton("Start Game");
		StartButton.setPreferredSize(new Dimension(500, 20));
		menu.add(StartButton, BorderLayout.PAGE_START);
		
		JButton LoadButton = new JButton("Load Game");
		menu.add(LoadButton, BorderLayout.PAGE_END);
		LoadButton.setPreferredSize(new Dimension(500, 20));
		
		StartButton.addActionListener((java.awt.event.ActionEvent ev) -> {
			StartButton.setEnabled(false);
			new Thread(() -> {
				start.dispose();
				this.createGameGUI(null);
				SwingUtilities.invokeLater(() -> {
					StartButton.setEnabled(true);
				});
			}).start();
		});

		LoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (gamesCount < 4) 
					printLoadGame();
				else
					JOptionPane.showMessageDialog(start,
		    			    "Only 4 games can run at the same time.",
		    			    "Maximum amount of games reached",
		    			    JOptionPane.PLAIN_MESSAGE);
			}
		});
		
	    JPanel footer = new JPanel();
	    footer.setPreferredSize(new Dimension(500, 100));
	    footer.setBackground(Color.WHITE);
		
	    // Vytvorenie buttonu kt. ukonci aplikaci
		JButton QuitButton = new JButton("Quit");
		QuitButton.setPreferredSize(new Dimension(500, 20));
		footer.add(QuitButton, BorderLayout.PAGE_START);
		// po kliknuti sa skoci applikace
		QuitButton.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				start.dispose();
				System.exit(0);
			}
		});
		
		layout.add(footer,BorderLayout.PAGE_END);	    
	    layout.add(menu, BorderLayout.CENTER);
	    
		// implicitni operace pri zavreni okna
		start.setDefaultCloseOperation(
		JFrame.EXIT_ON_CLOSE);
		// otevreni okna
		start.setVisible(true);
		
		/**
         * Timer
         * This timer is refreshing the time and score values of every game running.
         * 
         */
		Timer Time = new Timer(1000, new ActionListener(){
		    @Override
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	for (int i = 0; i < gamesCount; i++) {
		    		if (times[i] != null && games[i] != null) {
				    	games[i].incTime();
				    	times[i].setText("Time: " + games[i].getTime() + "s");
				    	scores[i].setText("Score: " + games[i].getScore());
		    		}
		    	}
		    }
		});
		Time.start();
	}
	
	/**
	 * Creates a game gui.
	 * The function has one parameter. To load a game, an object of type Game is created 
	 * from a savefile and then sent as loadGame parameter of this function.
	 * To start a new game, call this function with loadGame set to null.
	 * 
	 * The upper text and buttons are generated and added in function GUI.printHeader()
	 * @see GUI.printHeader()
	 * 
	 * If loadGame is null - a brand new object of type Game will be created.
	 * Calls function printGame() to draw a JPanel and adds it to the frame.
	 * @see GUI.printGame()
	 *
	 * @param Game loadGame - loaded game from a file or null if creating a new game
	 */
	public void createGameGUI(Game loadGame) {
		int index = this.gamesCount++;
		if (loadGame != null)
			this.games[index] = loadGame;
		else {
			this.games[index] = new Game();
			this.games[index].resetTime();
		}
		// vytvoreni okna aplikace
		this.game = new JFrame("Solitaire");

		Container layout = game.getContentPane();
		
		// horne menu
		this.printHeader(layout, game);
		
		// miesto pre hry
		JPanel content = new JPanel(new FlowLayout());
		content.setSize(1200,700);
		for (int i = 0; i < this.gamesCount; i++) {
			content.add(this.printGame(games[i], i, game));
		}
		
		game.add(content, BorderLayout.CENTER);
		
		
		// implicitni operace pri zavreni okna
		game.setDefaultCloseOperation(
		JFrame.EXIT_ON_CLOSE);
		// nastaveni velikosti a umistneni do stredu obrazovky
		game.pack();
		game.setSize(1300, 700);
		game.getContentPane().setBackground(Color.WHITE);
		this.centreWindow(game);
		// otevreni okna
		game.setVisible(true);
	}
	
	/**
	 * Create header and add it to the window.
	 * 
	 * Adds text and buttons to the layout. Also creates the ActionListeners for buttons.
	 * The 'Start New Game' button closes the actual window and then calls createGameGUI() function
	 * which then creates another window, but this time with +1 game/cardboard
	 * @see GUI.createGameGUI()
	 * 
	 * The 'Load Game' button opens a new window with a list of saved games that can be opened.
	 * @see GUI.printLoadGame()
	 * 
	 * The 'Quit' button quits the app.
	 *
	 * @param Container layout - Layout of the window, to this layout is the header added.
	 * @param JFrame game      - Reference to the game window (used by the buttons quit and start to close window)
	 */
	public void printHeader(Container layout, JFrame game) {
		// horne menu
		JPanel header = new JPanel(new FlowLayout());
		header.setPreferredSize(new Dimension(1200, 30));
		
		JLabel label = new JLabel("xkucer91_xcincu00");
		label.setPreferredSize(new Dimension(200, 20));
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));	
		header.add(label);
		
		JButton headerStart = new JButton("Start New Game");
		headerStart.setPreferredSize(new Dimension(200, 20));
		headerStart.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (gamesCount < 4) {
					game.dispose();
					createGameGUI(null);
				}
				else
					JOptionPane.showMessageDialog(game,
		    			    "Only 4 games can run at the same time.",
		    			    "Maximum amount of games reached",
		    			    JOptionPane.PLAIN_MESSAGE);
			}
		});
		header.add(headerStart);
		
		JButton headerLoad = new JButton("Load Game");
		headerLoad.setPreferredSize(new Dimension(200, 20));
		header.add(headerLoad);
		
		headerLoad.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (gamesCount < 4) 
					printLoadGame();
				else
					JOptionPane.showMessageDialog(game,
		    			    "Only 4 games can run at the same time.",
		    			    "Maximum amount of games reached",
		    			    JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		 // Vytvorenie buttonu kt. ukonci aplikaci
		JButton headerQuit = new JButton("Quit");
		headerQuit.setPreferredSize(new Dimension(200, 20));
		header.add(headerQuit);
		// po kliknuti sa skoci applikace
		headerQuit.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				game.dispose();
				System.exit(0);
			}
		});
		
		layout.add(header, BorderLayout.PAGE_START);
	}
	
	/**
     * Creates the board for the game.
     * The variable g and GUI.games[index] are the same game.
     * Calls functions printStats and printPiles adds them to a JPanel and then 
     * returns it.
     * @see GUI.printStats()
     * @see GUI.printPiles()
     *
     * @param Game g        - the game that is generated/printed
     * @param int index     - the index of the game (in array GUI.games[] )
     * @see GUI
     * @param JFrame frame  - Reference to the game window
     *
     * @return A JPanel that contains all the cards, piles, score and time of the game passed as the g param.
     */
	public JPanel printGame(Game g, int index, JFrame frame) {
		JPanel game = new JPanel(new BorderLayout());
		switch (this.gamesCount) {
		case 1:
			game.setPreferredSize(new Dimension(1200, 600));
			break;
		default:
			game.setPreferredSize(new Dimension(600, 300));
			break;
		}
		// statistiky hry na spodu (PAGE_END)
		this.printStats(game, g, index);
		
		this.piles[index] = this.printPiles(game, g, index);
		
		game.add(this.piles[index], BorderLayout.CENTER);
		
		game.setBorder(BorderFactory.createLineBorder(Color.black));
		
		return game;
	}
	
    /**
     * This function generates a JPanel that contains statistics panel.
     * Time and Score are in the corners and in the midle is the local menu
     * for every game. Buttons to save game, undo the last move and cancel the game.
     * 
     * The 'Save Game' button creates a file (save/savegame-N) where all the game data are
     * and can be loaded again.
     * @see Game.save()
     * 
     * The 'Undo' button undoes the last move. It can be used max 5 times and moves are not saved
     * with the game.
     * @see Game.undo()
     * 
     * The 'Close Game' button closes the actual game.
     * @see GUI.removeGame()
     *   
     * @param Game g        - from here are the stats (score, time)
     * @param int index     - the index of the game (in array GUI.games[] )
     * @param JPanel game   - here are the stats added
     */
	public void printStats(JPanel game, Game g, int index) {
		JPanel stats = new JPanel(new BorderLayout());
		stats.setPreferredSize(new Dimension(500, 30));
		stats.setName(""+index);
		
		// Cas 
		times[index] = new JLabel("Time: " + g.getTime() + "s");
		times[index].setPreferredSize(new Dimension(100, 30));
		stats.add(times[index], BorderLayout.LINE_END);
		times[index].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Skore
		scores[index] = new JLabel("Score: " + g.getScore());
		scores[index].setPreferredSize(new Dimension(100, 30));
		stats.add(scores[index], BorderLayout.LINE_START);
		scores[index].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    game.add(stats, BorderLayout.PAGE_END);
	    
	    JPanel buttons = new JPanel();
	    buttons.setPreferredSize(new Dimension(400, 20));
	    
	    JButton save = new JButton("Save Game");
	    save.setPreferredSize(new Dimension(100, 20));
	    buttons.add(save);
	    
	    JButton undo = new JButton("Undo");
	    undo.setPreferredSize(new Dimension(80, 20));
	    buttons.add(undo);
	    
	    JButton exit = new JButton("Close Game");
	    exit.setPreferredSize(new Dimension(120, 20));
	    exit.setName(""+index);
	    buttons.add(exit);
	    
	    undo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	games[index].undo();							
				reDraw(game, games[index], index);
            }
    	});
	    
	    save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	games[index].save();
				reDraw(game, g, index);	
            }
    	});
	    
	    exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	removeGame(Integer.parseInt(e.getComponent().getName()));
            }
    	});
	    
	    stats.add(buttons, BorderLayout.CENTER);
	}
	
    /**
     * This function draws/prints all the piles and cards in the game g.
     * Every card is a JLabel with ImageIcon inside it and every pile is a JPanel that contains
     * the cards.
     * 
     * Here is the main logic of the game.
     * To perform a move, click on the card/cards you want to move and click on a pile to move them there.
     * 
     * There is a mouse click listener created for all of the cards displayed, that sets all variables that are needed.
     * Every move is an object.
     * @see Move
     * 
     * On the first click, the variable Move.from is set. (Selected card is highlighted by a RED BORDER)
     * On the second click, the variable Move.to is set. And then the move is interpreted and the variables reset.
     * 
     * After every move function reDraw is called to show the changes.
     * @see GUI.reDraw()
     *
     * @param JPanel game   - here are the stats added
     * @param Game g        - from here are the stats (score, time)
     * @param int index     - the index of the game (in array GUI.games[] )
     *
     * @return JPanel that contains all the piles and cards of a game
     */
	public JPanel printPiles(JPanel game, Game g, int index) {
		// container na vsetky polia
		JPanel panel = new JPanel(new FlowLayout());
		panel.setPreferredSize(new Dimension(game.getHeight(), game.getWidth()));
		panel.setBackground(Color.WHITE);
		
		int width, height;
		if (this.gamesCount == 1) {
			width = 150;
			height = 200;
		} else {
			width = 70;
			height = 100;			
		}
		
		// kopka 24 kariet
		JPanel heap = new JPanel();
		heap.setPreferredSize(new Dimension(width, height));
		heap.setBackground(Color.BLACK);
		panel.add(heap);
		
		// pridanie obrazku vrchnej karty
		if (g.heap.getSize() > 0) {
			ImageIcon heapImg = new ImageIcon(g.heap.peek().getImg());
			if (gamesCount > 1) {  // zmensenie obrazku pri viac ako 1 hre
				Image image = heapImg.getImage();
				Image newimg = image.getScaledInstance(60, 80,  java.awt.Image.SCALE_SMOOTH); 
				heapImg = new ImageIcon(newimg);
			}
			JLabel heapCard = new JLabel(heapImg);
			heapCard.setOpaque(true);
			heap.add(heapCard);
			heap.setName("h");
			heapCard.addMouseListener(new MouseAdapter() {
		                @Override
		                public void mouseClicked(MouseEvent e) {
		                	Card c = g.heap.get();
		                	c.setFaceUp();
		                	g.dump.put(c);
		                	Move m = new Move();
		                	m.from = pile.h; m.to = pile.d; m.count = 1;
		                	games[index].addMove(m);
		                	reDraw(game, g, index);
		                }
			});
		} else {
			heap.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) { // po kliknuti na prazdnu kopku sa vratia vsetky karty z dump do heap -100score
            		Stack<Card> cards = new Stack<Card>();
            		int dSize = games[index].dump.getSize();
                	for (int i = 0; i < dSize; i++) {
                		Card c = games[index].dump.get();
                		c.setFaceDown();
                		games[index].heap.put(c);
                	}
            		Move m = new Move();
                	m.from = pile.d; m.to = pile.h; m.count = dSize;
                	games[index].addMove(m);
                	games[index].incScore(-100);
                	reDraw(game, games[index], index);
                }				
			});
			
		}		
		
		// sem idu karty z heapu
		JPanel dump = new JPanel();
		dump.setPreferredSize(new Dimension(width, height));
		dump.setBackground(Color.BLACK);
		dump.setName("d");
		panel.add(dump);
		if (games[index].dump.getSize() > 0) {
			ImageIcon dumpImg = new ImageIcon(games[index].dump.peek().getImg());
			if (gamesCount > 1) {
				Image image = dumpImg.getImage(); // transform it 
				Image newimg = image.getScaledInstance(60, 80,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				dumpImg = new ImageIcon(newimg);
			}
			JLabel dumpCard = new JLabel(dumpImg);
			dumpCard.setOpaque(true);
			dump.add(dumpCard);
			dumpCard.addMouseListener(new MouseAdapter() {
		                @Override
		                public void mouseClicked(MouseEvent e) {
		                	if (games[index].m.from == null) {
			                	games[index].m.count = 1;
			                	games[index].m.setFrom(e.getComponent().getParent().getName());
			                	Border border = BorderFactory.createLineBorder(Color.RED, 5);
			                	((JComponent) e.getComponent()).setBorder(border);
		                	}
		                }
			});
		}
		
	    // prazdne pole
		JPanel empty = new JPanel();
		empty.setPreferredSize(new Dimension(width, height));
		empty.setBackground(Color.WHITE);
		panel.add(empty);
		
		// 4 cielove polia
		for (int i = 0; i < 4; i++) {
			JPanel target = new JPanel();
			target.setPreferredSize(new Dimension(width, height));
			target.setBackground(Color.BLACK);
			panel.add(target);
			target.setName("t"+(i+1));
			
			target.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	if (games[index].m.from == null) {
	                	games[index].m.setFrom(e.getComponent().getName());
	                	Border border = BorderFactory.createLineBorder(Color.RED, 5);
	                	((JComponent) e.getComponent()).setBorder(border);
                	} else {
	                	games[index].m.setTo(e.getComponent().getName());
	                	// vykonat tah, vymaz premenne, znovu vykresli okno
						games[index].m.move(games[index]);
						
						reDraw(game, g, index);		                	
                	}
                }
			});
			
			if (!games[index].target[i].isEmpty()) {
				ImageIcon targImg = new ImageIcon(g.target[i].peek().getImg());
				if (gamesCount > 1) {
					Image image = targImg.getImage(); // transform it 
					Image newimg = image.getScaledInstance(60, 80,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
					targImg = new ImageIcon(newimg);
				}
				JLabel targCard = new JLabel(targImg);
				targCard.setVerticalAlignment(JLabel.TOP);
				targCard.setHorizontalAlignment(JLabel.CENTER);
				targCard.setOpaque(true);
				targCard.setName("t"+(i+1));
				target.add(targCard);
				

				targCard.addMouseListener(new MouseAdapter() {
	                @Override
	                public void mouseClicked(MouseEvent e) {
	                	if (games[index].m.from == null) {
		                	games[index].m.setFrom(e.getComponent().getParent().getName());
		                	Border border = BorderFactory.createLineBorder(Color.RED, 5);
		                	((JComponent) e.getComponent()).setBorder(border);
	                	} else {
		                	games[index].m.setTo(e.getComponent().getParent().getName());
		                	// vykonat tah, vymaz premenne, znovu vykresli okno
							games[index].m.move(games[index]);
							
							reDraw(game, g, index);
	                	}
	                }
				});
			}
			
		}
		
		// 7 pracovnych poli
		for (int i = 0; i < 7; i++) {
			JLayeredPane work = new JLayeredPane();
			work.setPreferredSize(new Dimension(width, height+100));
			work.setBackground(Color.BLACK);
			
			work.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	// klikam prvykrat
                	if (games[index].m.from != null) {
	                	games[index].m.setTo(e.getComponent().getName());	                	
	                	// vykonat tah, vymaz premenne, znovu vykresli okno
						games[index].m.move(games[index]);
						
						reDraw(game, g, index);
                	}
                }
            });
			
			// pridam pole na hraciu plochu a nastavim ID na w(1-7)
			panel.add(work);
			work.setName("w"+(i+1));
			
			Stack<Card> cards = new Stack<Card>();
			cards = g.getAllCards(i);
			int offset = 0;
			for (int j = cards.size(); j > 0; j--) {
				ImageIcon img = new ImageIcon(cards.get(j-1).getImg());
				if (gamesCount > 1) {
					Image image = img.getImage(); // transform it 
					Image newimg = image.getScaledInstance(60, 80,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
					img = new ImageIcon(newimg);
				}
				JLabel card = new JLabel(img);
				card.setVerticalAlignment(JLabel.TOP);
				card.setHorizontalAlignment(JLabel.CENTER);
				card.setOpaque(true);
				if (gamesCount > 1) {
					card.setBounds(0, offset*10, 75, 100);
				} else {
					card.setBounds(0, offset*20, 75, 100);
				}
				card.setName(""+j);
				work.add(card, new Integer(offset));
				
				offset += 1;
				
				
				card.addMouseListener(new MouseAdapter() {
	                @Override
	                public void mouseClicked(MouseEvent e) {
	                	// klikam prvykrat
	                	if (games[index].m.from == null) {
		                	if (isNumeric(e.getComponent().getName())) {
		                		int XD = Integer.parseInt(e.getComponent().getName());
		                		if (XD > 1) {
		                			games[index].m.count = XD;
		                		}
		                	}
		                	games[index].m.setFrom(e.getComponent().getParent().getName());
		                	Border border = BorderFactory.createLineBorder(Color.RED, 5);
		                	((JComponent) e.getComponent()).setBorder(border);
	                	} else { // klikam druhy krat
		                	games[index].m.setTo(e.getComponent().getParent().getName());		                	
		                	// vykonat tah, vymaz premenne, znovu vykresli okno
							games[index].m.move(games[index]);
							reDraw(game, g, index);
	                	}
	                }
	            });
			}
		}
		
		return panel;
	}
	
    /**
     * This function opens a new window.
     * The window contains buttons that represent the files in the folder 'save'.
     * Every button has a MouseClickListener that when clicked calls function loadFile('filename') with the name of the file
     * as parameter.
     * @see GUI.loadFile()
     * 
     */
	public void printLoadGame() {
		JFrame load = new JFrame("Load Game");
		load.pack();
		load.setSize(500, 300);
		load.getContentPane().setBackground(Color.WHITE);
		centreWindow(load);
		load.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		load.setVisible(true);
		
		JLabel title = new JLabel("Saved Games:");
		title.setPreferredSize(new Dimension(500, 50));
		JPanel files = new JPanel();
		
		// ziska vsetky savegame-N subory
		File[] listOfFiles = new File("save/").listFiles();
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	JButton file = new JButton(listOfFiles[i].getName());
	    	file.setPreferredSize(new Dimension(500, 20));
	    	file.setName(listOfFiles[i].getName());
	    	files.add(file);
	    	
	    	file.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	load.dispose();
                	if (start != null)
                		start.dispose();
                	if (game != null) 
                		game.dispose();
                	Game loadGame = loadFile(e.getComponent().getName());
                	createGameGUI(loadGame);
                }
	    	});
	    }
	    
	    load.add(title,BorderLayout.PAGE_START);
	    load.add(files,BorderLayout.CENTER);
	}
	
    /**
     * Loads a file.
     * Creates a new variable of type Game, calls a constructor with 1 String as parameter
     * and then returns it. 
     * @see Game(String filename)
     *
     * @param String filename - Name of the file with the saved game
     *
     * @return Object of type Game that represents previously saved game.
     */
	public Game loadFile(String filename) {
		Game g = new Game(filename);
		return g;
	}
	
    /**
     * This function removes the JPanel containing the game with index 'index' and
     * then creates another one that is actual.
     * 
     * This function is called after every move.
     * 
     * The function win() is called to check, if the game is completed and then a message
     * pops up to announce the end of the game.
     * @see Game.win()
     *
     * @param JPanel game   - here are the stats added
     * @param Game g        - from here are the stats (score, time)
     * @param int index     - the index of the game (in array GUI.games[] )
     */
	public void reDraw(JPanel game, Game g, int index) {
		game.remove(piles[index]);
    	piles[index] = printPiles(game,games[index],index);
    	game.add(piles[index]);
    	if (g.win()) {
    		JOptionPane.showMessageDialog(game,
    			    "Congratulations you are winner!!!  Score: " + g.getScore() + " Time: " + g.getTime(),
    			    "You win",
    			    JOptionPane.PLAIN_MESSAGE);
    	}
	}
	
    /**
     * Determines if string is a number.
     *
     * @param String str    - this is the verified string
     *
     * @return     True if numeric, False otherwise.
     */
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");
	}
	
    /**
     * Places the given window in the midle of the screen.
     *
     * @param Window frame - this window will be placed in the center of the screen
     */
	public static void centreWindow(Window frame) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
	    frame.setLocation(x, y);
	}
	
    /**
     * Removes a game from the array GUI.games[]
     * @see GUI
     * 
     * The game at the index given is removed.
     * The other games are then shifted as needed so that there are no inactive games 
     * between the active games in the array GUI.games[].
     * 
     * Then every JPanel with all the boards and stats for every game is removed and then 
     * added again with the right game and array indexes.
     *
     * @param int index - index of the game in the array GUI.games[]
     */
	public void removeGame(int index) {
		Container prnt = this.piles[index].getParent().getParent();
		switch (index+1) {
			case 1:
				for (int i = 0; i < this.gamesCount-1; i++)
					this.games[i] = this.games[i+1];
				this.gamesCount--;
				prnt.remove(this.piles[index]);
				if (this.gamesCount == 0) {
					this.game.dispose();
					this.StartUp();
				}
				break;
			case 2:
				for (int i = 1; i < this.gamesCount-1; i++)
					this.games[i] = this.games[i+1];
				prnt.remove(this.piles[index]);
				this.gamesCount--;
				break;
			case 3:
				for (int i = 2; i < this.gamesCount-1; i++)
					this.games[i] = this.games[i+1];
				prnt.remove(this.piles[index]);
				this.gamesCount--;
				break;
			case 4:
				for (int i = 3; i < this.gamesCount-1; i++)
					this.games[i] = this.games[i+1];
				prnt.remove(this.piles[index]);
				this.gamesCount--;
				break;
			default:
				break;
		}
		prnt.removeAll();
		for (int i = 0; i < this.gamesCount; i++) {
			prnt.add(this.printGame(games[i], i, game));
		}
		this.game.validate();
		this.game.repaint();
	}
}
