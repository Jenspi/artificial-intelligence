package com.stephengware.java.games.chess.bot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.stephengware.java.games.chess.bot.Bot;
import com.stephengware.java.games.chess.state.State;

/**
 * A chess bot which selects its next move at random.
 * 
 * @author Stephen G. Ware
 */
public class MyBot extends Bot {

	/** A random number generator */
	private final Random random;
	
	/**
	 * Constructs a new chess bot named "jmspicer" and whose random  number
	 * generator (see {@link java.util.Random}) begins with a seed of 0.
	 */
	public MyBot() {
		super("jmspicer");
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
//		// This list will hold all the children nodes of the root.
//		ArrayList<State> children = new ArrayList<>();
//		
//		// Generate all the children nodes of the root (that is, all the
//		// possible next states of the game.  Make sure that we do not exceed
//		// the number of GameTree nodes that we are allowed to generate... 500,000 for this assignment.
//		Iterator<State> iterator = root.next().iterator();
//		while(!root.searchLimitReached() && iterator.hasNext())
//			children.add(iterator.next());
		
		ArrayList<State> children = new ArrayList<>();
		// Choose one of the children at random.
		return children.get(random.nextInt(children.size()));
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
		
		// This list will hold all the children nodes of the root.
		ArrayList<State> children = new ArrayList<>();
		
		//Generate all children nodes of root (no more than 500k) and store them in list
		//root.setSearchLimit(500000);
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
