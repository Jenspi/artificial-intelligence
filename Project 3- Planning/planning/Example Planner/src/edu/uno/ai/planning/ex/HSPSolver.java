package edu.uno.ai.planning.ex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

import edu.uno.ai.SearchBudget;
import edu.uno.ai.planning.Plan;
import edu.uno.ai.planning.Step;
import edu.uno.ai.planning.ss.StateSpaceNode;
import edu.uno.ai.planning.ss.StateSpaceProblem;
import edu.uno.ai.planning.ss.StateSpaceSearch;

/**
 * A planner that uses simple breadth first search through the space of states.
 * 
 * @author Stephen G. Ware
 */
public class HSPSolver extends StateSpaceSearch {

	/** The queue which will hold the frontier (states not yet visited) */
	private final Queue<StateSpaceNode> queue = new LinkedList<>();
	
	/**
	 * Constructs a new state space search object.
	 * 
	 * @param problem the problem to solve
	 * @param budget the search budget, which constrains how many states may be
	 * visited and how much time the search can take
	 */
	public HSPSolver(StateSpaceProblem problem, SearchBudget budget) {
		super(problem, budget);
	}

	@Override
	public Plan solve() {
		// Start with only the root node (initial state) in the queue.
		queue.add(root);
		// Search until the queue is empty (no more states to consider).
		while(!queue.isEmpty()) {
			// Pop a state off the frontier.
			StateSpaceNode current = queue.poll();
			// Check if it is a solution.
			if(problem.isSolution(current.plan))
				return current.plan;
			// Consider every possible step...
			for(Step step : problem.steps) {
				// If it's precondition is met in the current state...
				if(step.precondition.isTrue(current.state)) {
					// Add the state results from that step to the frontier.
					// use heuristic as state's priority # for priority queue
					queue.offer(current.expand(step));
				}
			}
		}
		// If the queue is empty and we never found a solution, the problem
		// cannot be solved. Return null.
		return null;
	}
	
	/*
	 * Input: The current state.
	 * Every literal has a cost, initially ∞.
	 * Every literal that is true in the current state has a cost of 0.
	 * //Note: The cost of a conjunction is the sum of the costs of its conjuncts.
	 * Do this until the costs of the literals stop changing:
	 * 		For every step S:
	 * 			For every literal E in the effect of S:
	 * 				Let the cost of E be the minimum of:
	 * 				1. The current cost of E.
	 * 				2. The cost of S’s precondition + 1.
	 * Return the cost of the problem’s goal. // the sum of its conjuncts
	 */
	
	public int HSPHeuristic(StateSpaceNode current_state) {
		HashMap<StateSpaceNode, Integer> statesMap = new HashMap<StateSpaceNode, Integer>();
		Integer cost = Integer.MAX_VALUE;
		
		// Assign all to the HashMap with their values
		for(Step step : problem.steps) {
			// Every literal has a cost, initially ∞.
			statesMap.put(current_state, cost);
			
			if(step.precondition.isTrue(current_state.state)) {
				// Every literal that is TRUE in the current state has a cost of 0.
				statesMap.put(current_state, 0);
			}
		}
		
		// Return the cost of the problem’s goal (the sum of its conjuncts).
		int problemCost = 0;
		for (Entry<StateSpaceNode, Integer> entry : statesMap.entrySet()) {
			problemCost += entry.getValue();
		}
		    
		return problemCost;
		//return 0;
	}
}
