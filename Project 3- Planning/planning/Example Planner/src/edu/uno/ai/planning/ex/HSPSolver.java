package edu.uno.ai.planning.ex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

import edu.uno.ai.SearchBudget;
import edu.uno.ai.logic.Conjunction;
import edu.uno.ai.logic.Literal;
import edu.uno.ai.logic.Proposition;
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
public class HSPSolver extends StateSpaceSearch implements Comparable<StateSpaceNode>{

	/** The queue which will hold the frontier (states not yet visited) */
	// private final Queue<StateSpaceNode> queue = new LinkedList<>();
	private final Queue<StateSpaceNode> queue = new PriorityQueue<>();
	
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
		Double priorityNumber; // priority => f(x) = g(x) steps taken + h(x) steps til goal
		
		// Start with only the root node (initial state) in the queue.
		queue.add(root);
		// Search until the queue is empty (no more states to consider).
		while(!queue.isEmpty()) {
			// Pop a state off the frontier.
			StateSpaceNode current = queue.poll();
			priorityNumber = HSPHeuristic(current)+0;
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
	
	// in all of the domains and problems for this project,
	// you will only encounter two kinds of propositions: a literal by itself (i.e., an instance of the Literal class) or
	// a conjunction of literals (i.e., this proposition *isn’t* an instance of the Literal class, but is instead an instance of the Conjunction class). 
	private static List<Literal> getLiterals(Proposition proposition){
		ArrayList<Literal> list = new ArrayList<>();
		if(proposition instanceof Literal) {
			list.add((Literal) proposition);
		}
		else {
			for(Proposition conjunct : ((Conjunction) proposition).arguments) {
				list.add((Literal) conjunct);
			}
		}
		return list;
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
	
	public Double HSPHeuristic(StateSpaceNode current_state) {
		HashMap<StateSpaceNode, Double> statesMap = new HashMap<StateSpaceNode, Double>();
		//ArrayList<Literal> literals = current_state.
		Double cost = Double.POSITIVE_INFINITY;
		
		// Assign all to the HashMap with their values
		for(Step step : problem.steps) {
			// Every literal has a cost, initially ∞.
			statesMap.put(current_state, cost);
			
			if(step.precondition.isTrue(current_state.state)) {
				// Every literal that is TRUE in the current state has a cost of 0.
				statesMap.put(current_state, 0.0);
			}
		}
		
		// Return the cost of the problem’s goal (calculate the sum of its conjuncts).
		Double problemCost = 0.0;
		for (Entry<StateSpaceNode, Double> entry : statesMap.entrySet()) {
			problemCost += entry.getValue();
		}
		    
		return problemCost;
		//return 0;
	}
}
