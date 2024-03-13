package com.stephengware.java.games.chess.bot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import com.stephengware.java.games.chess.state.Piece;
import com.stephengware.java.games.chess.state.Player;
import com.stephengware.java.games.chess.state.State;

/**
 * A chess bot which selects its next move at random.
 * 
 * @author Stephen G. Ware
 */
public class MyBot extends Bot {
	Player me;
	
	/**
	 * Constructs a new chess bot named "jmspicer" and whose random  number
	 * generator (see {@link java.util.Random}) begins with a seed of 0.
	 */
	public MyBot() {
		super("jmspicer");
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
//		}//debugging code for slowing game down
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
		int gameoverpts = 10000;
		int MSBlack=0, MSWhite=0;
		
		// Checking for checkmate:
		if(state.check) {
			if(state.over) {
				if(state.player.equals(Player.WHITE)){
					// white in checkmate
					MSBlack += gameoverpts;
				}
				if(state.player.equals(Player.BLACK)){
					// black in checkmate
					MSWhite += gameoverpts;
				}
			}
		}//end checkmate check
		
		Iterator<Piece> iterator3 = state.board.iterator();
		while(!state.searchLimitReached() && iterator3.hasNext()) {
			Piece i = iterator3.next();
					
			if(i.player.equals(Player.BLACK)) {
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
							//System.out.println("no if's entered for black");
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
				}
			}//end while loop
		
		// Determine & return utility score
		if(me.equals(Player.BLACK)) {
			// We are black, bot is white
			return MSBlack - MSWhite;
		}
		else {
			// We are white, bot is black
			return MSWhite - MSBlack;
		}
	}//end evaluateState method
	
	protected State greedyStrategy(State root) {
		/*
		 * FOR BEATING RANDOM BOT
		 * Modeled after greedy bot:
		 * always chooses the move which maximizes its total material score. If
		 * it has multiple moves that result in the same score, it chooses at random between
		 * them. It does not “look ahead” at all; it (greedily) chooses only among reachable
		 * states from the current board configuration
		 */
		
		// Material scores:
		int pawn=1, knight=3, bishop=3, rook=5, queen=9, king=0;
		
		// This list will hold all the frontier children nodes of the root.
		HashMap<State, Integer> children = new HashMap<>();
		
		//Generate all children nodes of root (no more than 500k) and store them in list
		Iterator<State> iterator = root.next().iterator();
		while(!root.searchLimitReached() && iterator.hasNext()) {
			State nextP = iterator.next();
			children.put(nextP, evaluateState(nextP));
		}
		
		// choose state with highest material score (chooses random with greedy strategy)
		// if piece score > than current max, assign the new max state to be current state
		int maxValueInMap = Collections.max(children.values());
		State returnState = null;
		
        // Iterate through HashMap for largest value
        for (Entry<State, Integer> entry : children.entrySet()) {
            if ( entry.getValue().equals(maxValueInMap) )  {
                // Assign largest valued state
            	returnState = entry.getKey();
            }
        }
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
	
	//UNFINISHED
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
