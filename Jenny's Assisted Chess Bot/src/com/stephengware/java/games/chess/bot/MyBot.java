package com.stephengware.java.games.chess.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import com.stephengware.java.games.chess.bot.Bot;
import com.stephengware.java.games.chess.state.Bishop;
import com.stephengware.java.games.chess.state.Board;
import com.stephengware.java.games.chess.state.King;
import com.stephengware.java.games.chess.state.Knight;
import com.stephengware.java.games.chess.state.Pawn;
import com.stephengware.java.games.chess.state.Piece;
import com.stephengware.java.games.chess.state.Player;
import com.stephengware.java.games.chess.state.Queen;
import com.stephengware.java.games.chess.state.Rook;
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
		
		
		/*
		//debugging/getting player colors:
		System.out.println("\nPlayer: "+root.player);//gets my color; shows only my ply per turn
		System.out.println("white Player: "+Player.BLACK.other());
		System.out.println("Black Player: "+Player.BLACK);
		System.out.println("White Player: "+Player.WHITE);*/
		
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
		boolean isBlack;
//		HashMap<Integer, Player> botsMap = new HashMap<>();
		
		//determine color of pieces
		//add scores to corresponding variables
		//find out which color i am at the end
		//use that in an if-else statement for final util score
		
		
		// check what color we are assigned this game (changes game to game).
		if(state.player.equals(Player.BLACK)) {//might not  need if we're gonna have a switch for both colors
			// Player => Black
			isBlack = true;// used for determining util score later
			//have an iterator for all black pieces, assign to MSBLACK
			//System.out.println("Player is assigned Black this game.");//debugging
			
		}
		//else if(state.player.equals(Player.WHITE)) {
		else{
			// Player => White
			isBlack = false;// used for determining util score later
			//System.out.println("Player is assigned White this game.");//debugging
			
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
//				System.out.println("\nEvaluate state debugging while loop starting...");//debugging
//				int tempCounter = 0;//debugging
//				int total = 0;//debugging
				while(!state.searchLimitReached() && iterator3.hasNext()) {
//					tempCounter++;//debugging
					// i will return all pieces on the board, regardless of color/player
					// We can then use i.player.equals(color) to determine which piece belongs to which player
					Piece i = iterator3.next();
					
					//Check what pieces belong to who, then add to appropriate material scores
					if(i.player.equals(Player.BLACK)) {
						//System.out.printf("PIECE %d: %s is BLACK\n", tempCounter, i);// debugging
						// PIECE BELONGS TO BLACK HERE
						if(i instanceof Pawn) {
							MSBlack += pawn;
						}
						else if(i instanceof Knight) {
							MSBlack += knight;
						}
						else if(i instanceof Bishop) {
							MSBlack += bishop;
						}
						else if(i instanceof Rook) {
							MSBlack += rook;
						}
						else if(i instanceof Queen) {
							MSBlack += queen;
						}
						else {
							//debugging... should only hit if black has no pieces on board
							System.out.println("no if's entered for black");
						}
					}//end Black case
					else {
						//System.out.printf("PIECE %d: %s is WHITE\n", tempCounter, i);// debugging
						// PIECE BELONGS TO WHITE HERE
						if(i instanceof Pawn) {
							MSWhite += pawn;
						}
						else if(i instanceof Knight) {
							MSWhite += knight;
						}
						else if(i instanceof Bishop) {
							MSWhite += bishop;
						}
						else if(i instanceof Rook) {
							MSWhite += rook;
						}
						else if(i instanceof Queen) {
							MSWhite += queen;
						}
						else {
							//debugging... should only hit if white has no pieces on board
							System.out.println("no if's entered for white");
						}
					}//end White case
//					System.out.println("New black mat score:"+MSBlack);//debugging
//					System.out.println("New white mat score:"+MSWhite);//debugging
					
//					total++;//debugging
				}//end while loop
//				System.out.println("Should have this number of pieces: "+ total);//debugging
//				System.out.println("\nEvaluate state debugging while loop DONE.");//debugging
					
		// Determine & return utility score
		if(isBlack) {
			// We are black, bot is white
			System.out.println("util score: "+ (MSBlack - MSWhite));//debugging
			return MSBlack - MSWhite;
		}
		else {
			// We are black, bot is white
			System.out.println("util score: "+ (MSWhite- MSBlack));//debugging
			return MSWhite - MSBlack;
		}
	}//end evaluateState method
	
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
