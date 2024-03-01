package com.stephengware.java.games.chess.bot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import com.stephengware.java.games.chess.state.Piece;
import com.stephengware.java.games.chess.state.Player;
import com.stephengware.java.games.chess.state.State;

/**
 * A chess bot which selects its next move at random.
 * 
 * @author Stephen G. Ware
 */
public class MyBot extends Bot {
	/** A random number generator */
	//private final Random random;
	Player me;
	
	/**
	 * Constructs a new chess bot named "jmspicer" and whose random  number
	 * generator (see {@link java.util.Random}) begins with a seed of 0.
	 */
	public MyBot() {
		super("jmspicer");
		//Change value for different outcomes
		//this.random = new Random(0);
	}
	
	

	@Override
	protected State chooseMove(State root) {
		me = root.player;
		/**
		 * Here you are provided with a State whose root represents the current state of the board and must
		 * return one of its child states. Each child state represents a reachable state given the
		 * current board configuration. Thus returning one of these child states is essentially
		 * expressing which move you want to make.
		 */
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// This list will hold all the children nodes of the root.
//		ArrayList<State> children = new ArrayList<>();
//		
//		// Generate all the children nodes of the root (that is, all the
//		// possible next states of the game.  Make sure that we do not exceed
//		// the number of GameTree nodes that we are allowed to generate... 500,000 for this assignment.
//		Iterator<State> iterator = root.next().iterator();
//		while(!root.searchLimitReached() && iterator.hasNext())
//			children.add(iterator.next());
		
//		Iterator<Piece> iterator2 = root.board.iterator();
//		while(!root.searchLimitReached() && iterator.hasNext()) {
//			System.out.println("ITER TO STRING: "+iterator2.next().toString());}
//		
//		ArrayList<State> children = new ArrayList<>();
		// Choose one of the children at random.
		
		
		
//		//debugging/getting player colors:
//		System.out.println("\nUs: "+root.player);//gets my color; shows only my ply per turn
//		System.out.println("white Player: "+Player.BLACK.other());
//		System.out.println("Black Player: "+Player.BLACK);
//		System.out.println("White Player: "+Player.WHITE);
		
		//evaluateState(root);
		//return children.get(random.nextInt(children.size()));
		
		
		//return minimax(root, 2, me);
		return greedyStrategy(root);
	}
	
	protected int evaluateState(State state) {
		/*
		 * a function called ‘evaluateState()’ that takes
		 * in a ‘State’ object as an argument and returns a number that measures ‘board
		 * goodness’ for any given player, with higher scores being “more good.” A first pass of this
		 * might just be returning the difference in material scores (e.g., my perception of how
		 * good this state is would be *my material score* minus *your material score*).
		 * 
		 * You write it from “your perspective” – so if YOU are doing better than your
		 * enemy (regardless of what color you are), then it returns a high score, and if you
		 * are losing, then it returns a low score. If you do it this way, then “high numbers
		 * are always good” for you, but you need to pay attention to who is the owner of
		 * each piece as it will change from game to game (i.e., sometimes the white pieces
		 * are yours, and sometimes the black pieces are yours).
		 */

		// Material scores:
		int pawn=1, knight=3, bishop=3, rook=5, queen=9, king=4;
		int gameoverpts = 100;
		int MSBlack=0, MSWhite=0;
		boolean isBlack;
//		HashMap<Integer, Player> botsMap = new HashMap<>();
		
		//determine color of pieces
		//add scores to corresponding variables
		//find out which color i am at the end
		//use that in an if-else statement for final util score
		
		
		// check what color we are assigned this game (changes game to game).
		//if(state.player.equals(Player.BLACK)) {//might not  need if we're gonna have a switch for both colors
		/*
		if(state.player.equals(Player.BLACK)) {//might not  need if we're gonna have a switch for both colors
			// Player => Black
			isBlack = false;// used for determining util score later
			//have an iterator for all black pieces, assign to MSBLACK
			//System.out.println("Player is assigned Black this game.");//debugging
			//if the opponents king is available to put in check, award +100 points
			if(state.check) {
				if(state.over) {
					MSWhite += gameoverpts;
				}
				MSWhite += king;
			}
			
			
		}
		//else if(state.player.equals(Player.WHITE)) {
		else{
			// Player => White
			isBlack = true;// used for determining util score later
			//System.out.println("Player is assigned White this game.");//debugging
			if(state.check) {
				if(state.over) {
					MSBlack += gameoverpts;
				}
				MSBlack += king;
			}
			
		}
		*/
		
		if(state.check) {
			if(state.over) {
				if(state.player.equals(Player.WHITE)){
					//white in checkmate
					MSBlack += gameoverpts;
				}
				if(state.player.equals(Player.BLACK)){
					//black in checkmate
					MSWhite += gameoverpts;
				}
			}
		}
		
		
		
		
		
//		System.out.println("COLOR: " + state.player);
			
			//could also do one large switch statement checking if piece is black/white and adding to their own variables?
			//then check if im black or white, and check who is winning/losing
			// solution not done ^^^
			// board API file:///Users/jenspi/src/artificial-intelligence/Jenny's%20Assisted%20Chess%20Bot/doc/com/stephengware/java/games/chess/state/Board.html
			//go through all pieces
			//////for(int i=0; i< state.Board.countPieces(playername); i++) {
				//switch statement for each piece, adding to material score for each one present
				//Piece.equals(Knight)
				Iterator<Piece> iterator3 = state.board.iterator();
//				System.out.println("\nEvaluate state debugging while loop starting...");//debugging
				int tempCounter = 0;//debugging
//				int total = 0;//debugging
				while(!state.searchLimitReached() && iterator3.hasNext()) {
//					tempCounter++;//debugging
					// i will return all pieces on the board, regardless of color/player
					// We can then use i.player.equals(color) to determine which piece belongs to which player
					Piece i = iterator3.next();
					
					//Check what pieces belong to who, then add to appropriate material scores
//					System.out.println("i: "+i);
//					System.out.println("i.player: "+i.player);
//					System.out.println("i.player: "+i.player);
					
					//state.board.toString()//ascii
					
					if(i.player.equals(Player.BLACK)) {
//						System.out.printf("PIECE %d: %s is BLACK\n", tempCounter, i);// debugging
						//System.out.printf("PIECE %d: %s is BLACK\n and a %s", tempCounter, i, (i.getClass()));// debugging
						// PIECE BELONGS TO BLACK HERE
						System.out.println("Class: " + i.getClass()+"\nClass toString: "+i.toString());
						switch(i.toString().toUpperCase()) {
							case "P":
								MSBlack += pawn;
								break;
							case "N":
								MSBlack += knight;
								break;
							case "B":
								MSBlack += bishop;
								break;
							case "R":
								MSBlack += rook;
								break;
							case "Q":
								MSBlack += queen;
								break;
							default:
								//debugging... should only hit if black has no pieces on board
								System.out.println("no if's entered for black");
								break;
						}
					}//end Black case
					else {
						switch(i.toString().toUpperCase()) {
						case "P":
							MSWhite += pawn;
							break;
						case "N":
							MSWhite += knight;
							break;
						case "B":
							MSWhite += bishop;
							break;
						case "R":
							MSWhite += rook;
							break;
						case "Q":
							MSWhite += queen;
							break;
						default:
							//debugging... should only hit if black has no pieces on board
							//System.out.println("no if's entered for black");
							break;
						}
					}//end White case
//					System.out.println("New black mat score:"+MSBlack);//debugging
//					System.out.println("New white mat score:"+MSWhite);//debugging
//					
//					total++;//debugging
				}//end while loop
//				System.out.println("Should have this number of pieces: "+ total);//debugging
//				System.out.println("\nEvaluate state debugging while loop DONE.");//debugging
				
//				System.out.println("New black mat score:"+MSBlack);//debugging
//				System.out.println("New white mat score:"+MSWhite);//debugging
				
		// Determine & return utility score
		if(me.equals(Player.BLACK)) {
			// We are black, bot is white
//			System.out.println("util score: "+ (MSBlack - MSWhite));//debugging
			return MSBlack - MSWhite;
		}
		else {
			// We are white, bot is black
//			System.out.println("util score: "+ (MSWhite- MSBlack));//debugging
			return MSWhite - MSBlack;
		}
	}//end evaluateState method
	
//	protected State greedyStrategy(State root) {
//		/*
//		 * Modeled after greedy bot:
//		 * always chooses the move which maximizes its total material score. If
//		 * it has multiple moves that result in the same score, it chooses at random between
//		 * them. It does not “look ahead” at all; it (greedily) chooses only among reachable
//		 * states from the current board configuration
//		 */
//		
//		// Material scores:
//		int pawn=1, knight=3, bishop=3, rook=5, queen=9, king=0;
//		
//		// This list will hold all the frontier children nodes of the root.
//		ArrayList<State> children = new ArrayList<>();
//		
//		//Generate all children nodes of root (no more than 500k) and store them in list
//		//root.setSearchLimit(500000);//not really needed because this approach doesn't look ahead
//		Iterator<State> iterator = root.next().iterator();
//		while(!root.searchLimitReached() && iterator.hasNext()) {
//			children.add(iterator.next());
//		}
//		//choose one with highest material score
//		//if piece score > than current max, return the new max
//		return new State();
//	}
	
	
	protected State greedyStrategy(State root) {
		/*
		 * Modeled after greedy bot:
		 * always chooses the move which maximizes its total material score. If
		 * it has multiple moves that result in the same score, it chooses at random between
		 * them. It does not “look ahead” at all; it (greedily) chooses only among reachable
		 * states from the current board configuration
		 */
		// FOR BEATING RANDOM BOT
		//root
		
		// Material scores:
		int pawn=1, knight=3, bishop=3, rook=5, queen=9, king=0;
		
		// This list will hold all the frontier children nodes of the root.
		//ArrayList<State> children = new ArrayList<>();
		HashMap<State, Integer> children = new HashMap<>();
		
		//Generate all children nodes of root (no more than 500k) and store them in list
		//root.setSearchLimit(500000);//not really needed because this approach doesn't look ahead
		Iterator<State> iterator = root.next().iterator();
		while(!root.searchLimitReached() && iterator.hasNext()) {
//			children.add(iterator.next());
			State nextP = iterator.next();
			//if(! nextP.check) {
				children.put(nextP, evaluateState(nextP));
			//}
			System.out.println("NextP: "+nextP + "; Util: "+evaluateState(nextP));
			
//			if(evaluateState(nextP) != 0) {
//				try {
//					Thread.sleep(900000000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		}
		//choose one with highest material score
		//if piece score > than current max, return the new max
		int maxValueInMap = Collections.max(children.values());
		
		State returnState = null;
        // Iterate through HashMap
        for (Entry<State, Integer> entry : children.entrySet()) {
//        	System.out.println(" Key:"+ entry.getKey() +"; Value: "+ entry.getValue());
//        	System.out.println("Entry get value: "+ entry.getValue());
//        	System.out.println("MaxValueInMap "+ maxValueInMap);
            if ( entry.getValue().equals(maxValueInMap) )  {
                // Print the key with max value
     
            	returnState = entry.getKey();
            }
        }
        //children.get(maxValueInMap);
        //debugging:
//        for (Entry<State, Integer> entry : children.entrySet()) {
//        	System.out.println(entry.getValue());
//        }
        //return new State();
		return returnState;
	}
	
	protected boolean isTerminal(State state) {
		if(state.countDescendants() == 0) {
			return true;
		}
		else {
			return false;
		}
	}//end isTerminal
	
	protected State minimax(State state, int depthLimit, boolean maximizingPlayer) {
		/*
		 * Goal: Minimax with Alpha-Beta pruning and Iterative Deepening Search
		 * Using minmax pseudocode: https://en.wikipedia.org/wiki/Minimax
		 */
		
		int alpha = Integer.MAX_VALUE;
		int beta = Integer.MIN_VALUE;
		int value;
		
//		function minimax(node, depth, maximizingPlayer) is
//	    if depth = 0 or node is a terminal node then
//	        return the heuristic value of node
//	    if maximizingPlayer then
//	        value := −∞
//	        for each child of node do
//	            value := max(value, minimax(child, depth − 1, FALSE))
//	        return value
//	    else (* minimizing player *)
//	        value := +∞
//	        for each child of node do
//	            value := min(value, minimax(child, depth − 1, TRUE))
//	        return value
		
		//stubbed
		return new State();
	}//end minimax
	
}
