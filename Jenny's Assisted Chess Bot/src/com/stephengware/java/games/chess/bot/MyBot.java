package com.stephengware.java.games.chess.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import com.stephengware.java.games.chess.bot.Bot;
import com.stephengware.java.games.chess.state.Board;
import com.stephengware.java.games.chess.state.King;
import com.stephengware.java.games.chess.state.Knight;
import com.stephengware.java.games.chess.state.Pawn;
import com.stephengware.java.games.chess.state.Piece;
import com.stephengware.java.games.chess.state.Player;
import com.stephengware.java.games.chess.state.State;

/**
 * A chess bot which selects its next move at random.
 * 
 * @author Stephen G. Ware
 */
public class MyBot extends Bot {
	private final String playername;
	/** A random number generator */
	private final Random random;
	
	/**
	 * Constructs a new chess bot named "jmspicer" and whose random  number
	 * generator (see {@link java.util.Random}) begins with a seed of 0.
	 */
	public MyBot() {
		super("jmspicer");
		playername = "jmspicer";
		//Change value for different outcomes
		this.random = new Random(0);
	}

	@Override
	protected State chooseMove(State root) {
		/**
		 * Here you are provided with a State whose root represents the current state of the board and must
		 * return one of its child states. Each child state represents a reachable state given the
		 * current board configuration. Thus returning one of these child states is essentially
		 * expressing which move you want to make.
		 */
		// This list will hold all the children nodes of the root.
		ArrayList<State> children = new ArrayList<>();
		
		// Generate all the children nodes of the root (that is, all the
		// possible next states of the game.  Make sure that we do not exceed
		// the number of GameTree nodes that we are allowed to generate... 500,000 for this assignment.
		Iterator<State> iterator = root.next().iterator();
		while(!root.searchLimitReached() && iterator.hasNext())
			children.add(iterator.next());
		
//		Iterator<Piece> iterator2 = root.board.iterator();
//		while(!root.searchLimitReached() && iterator.hasNext()) {
//			System.out.println("ITER TO STRING: "+iterator2.next().toString());}
//		
//		ArrayList<State> children = new ArrayList<>();
		// Choose one of the children at random.
		System.out.println("\nPlayer: "+root.player);//gets my color; shows only my ply per turn
		System.out.println("white Player: "+Player.BLACK.other());
		System.out.println("Black Player: "+Player.BLACK);
		System.out.println("White Player: "+Player.WHITE);
		evaluateState(root);
		return children.get(random.nextInt(children.size()));
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
		int pawn=1, knight=3, bishop=3, rook=5, queen=9;
		int MSBlack=0, MSWhite=0;
		String color;
//		HashMap<Integer, Player> botsMap = new HashMap<>();
		
		//determine color of pieces
		//add scores to corresponding variables
		//find out which color i am at the end
		//use that in an if-else statement for final util score
		if(state.player.equals(Player.BLACK)) {//dont need if we're gonna have a switch for both colors
			//need this so we can determine which color we get assigned
			//System.out.println("Player is assigned Black this game.");
			
		}
		else if(state.player.equals(Player.WHITE)) {
			//System.out.println("Player is assigned White this game.");
		}
			
			//could also do one large switch statement checking if piece is black/white and adding to their own variables?
			//then check if im black or white, and check who is winning/losing
			// solution not done ^^^
			// board API file:///Users/jenspi/src/artificial-intelligence/Jenny's%20Assisted%20Chess%20Bot/doc/com/stephengware/java/games/chess/state/Board.html
			//go through all pieces
			//////for(int i=0; i< state.Board.countPieces(playername); i++) {
				//switch statement for each piece, adding to material score for each one present
				//Piece.equals(Knight)
				Iterator<Piece> iterator3 = state.board.iterator();
				System.out.println("\nEvaluate state debugging...");
				while(!state.searchLimitReached() && iterator3.hasNext()) {
					Piece i = iterator3.next();
					//check color
//					if(Player.BLACK) {
//						
//					}
//					
//					
//					//check pieces
//					System.out.println("Current Piece Class: "+ i.getClass());
//					//prints something like     class com.stephengware.java.games.chess.state.Knight
//					//if(i.getClass().){
//						
//					//}
//					
//					//switch(i.toString()) {
//					if(i instanceof Pawn) {
//						materialScore_Me += pawn;
//					case (i.equals(new Pawn(null, i, i))):
//						
//					default:
//						materialScore_Me +=999999;
//					}//end 
					
					color = i.toString(); 
					
						//System.out.println("White: "+i);
						System.out.println("i.player: "+i.player);
					
					
				}//end while loop
					
			//}//end if for if(state.player.equals(playername))
//		else {
//			//...
//		}//end if-else statement for if(state.player.equals(playername))
		//stubbed
		//return my score minus yours
				//return materialScore_Me - materialScore_Bot;
		return 0;
	}
	
	protected State greedyStrategy(State root) {
		/*
		 * Modeled after greedy bot:
		 * always chooses the move which maximizes its total material score. If
		 * it has multiple moves that result in the same score, it chooses at random between
		 * them. It does not “look ahead” at all; it (greedily) chooses only among reachable
		 * states from the current board configuration
		 */
		
		// Material scores:
		int pawn=1, knight=3, bishop=3, rook=5, queen=9;
		
		// This list will hold all the frontier children nodes of the root.
		ArrayList<State> children = new ArrayList<>();
		
		//Generate all children nodes of root (no more than 500k) and store them in list
		//root.setSearchLimit(500000);//not really needed because this approach doesn't look ahead
		Iterator<State> iterator = root.next().iterator();
		while(!root.searchLimitReached() && iterator.hasNext()) {
			children.add(iterator.next());
		}
		//choose one with highest material score
		//if piece score > than current max, return the new max
		return new State();
	}
	
	protected State minimax(State root) {
		//Goal: Minimax with Alpha-Beta pruning and Iterative Deepening Search
		//stubbed
		return new State();
	}
	
}
