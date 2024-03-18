package edu.uno.ai.sat.ex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.uno.ai.sat.Assignment;
import edu.uno.ai.sat.Clause;
import edu.uno.ai.sat.Literal;
import edu.uno.ai.sat.Solver;
import edu.uno.ai.sat.Value;
import edu.uno.ai.sat.Variable;

/**
 * <i>Project 2: SAT Solver</i>
 * @author Jenny Spicer
 */
public class SATSolver extends Solver {

	private final Random random = new Random(0);
	
	/**
	 * Constructs a new random SAT solver with the name being my UNO ID name, jmspicer.
	 */
	public SATSolver() {
		super("jmspicer");
	}

	/**
	 * //TODO: add description
	 * 
	 * @param assignment the assignment being worked on
	 * @return a boolean, representing if it is solved
	 */
	@Override
	public boolean solve(Assignment assignment) {
		// If the problem has no variables, it is assumed to have the values true or false.
		// edge case where there are 0 variables:
		if(assignment.problem.variables.size() == 0)
			return assignment.getValue() == Value.TRUE;
		else {
			// Keep trying until the assignment is satisfying.
			while(assignment.getValue() != Value.TRUE) {
				// Choose a variable whose value will be set.
				Variable variable = chooseVariable(assignment);
				// Choose 'true' or 'false' at random.
				Value value;
				if(random.nextBoolean())//randomly is true or false
					value = Value.TRUE;
				else {
					value = Value.FALSE;
				}
				// Assign the chosen value to the chosen variable.
				assignment.setValue(variable, value);
			}
			// Return success. (Note, if the problem cannot be solved, this
			// solver will run until it reaches the operations or time limit.)
			return true;
		}
	}
	
	/*
	 * WALKSAT pseudocode (checks satisfiability by randomly flipping the values of variables)
	 * 
	 * function WALKSAT(clauses, p, max_flips) returns a satisfying model or failure
	 * inputs: clauses, a set of clauses in propositional logic
	 * 			p, the probability of choosing to so a "random walk" move, typically around 0.5
	 * 			max_flips, number of flips allowed before giving up
	 * 
	 * model <- a random assignment of true/false to the symbols in clauses
	 * for i = 1 to max_flips, do
	 * 		if model satisfies clauses then return model
	 * 		clause <- a randomly selected clause from clauses that is false in model
	 * 		with probability p, flip the value in model of a randomly selected symbol from clause
	 * 		else flip whichever symbol in clause maximizes the number of satisfied clauses
	 * return failure
	 */
	
	/**
	 * Randomly choose a variable from the problem whose value will be set. If
	 * any variables have the value 'unknown,' choose one of those first;
	 * otherwise choose any variable.
	 * 
	 * @param assignment the assignment being worked on
	 * @return a variable, chosen randomly
	 */
	private final Variable chooseVariable(Assignment assignment) {
		// This list will hold all variables whose current value is 'unknown.'
		ArrayList<Variable> unknown = new ArrayList<>();
		// Loop through all the variables in the problem and find ones whose
		// current value is 'unknown.'
		for(Variable variable : assignment.problem.variables)
			if(assignment.getValue(variable) == Value.UNKNOWN)
				unknown.add(variable);
		// If any variables are 'unknown,' choose one of them randomly.
		if(unknown.size() > 0)
			return unknown.get(random.nextInt(unknown.size()));
		// Otherwise, choose any variable from the problem at random.
		else
			return assignment.problem.variables.get(random.nextInt(assignment.problem.variables.size()));
	}
	
	//---------------------------- HELPERS ----------------------------//
	
//	/**
//	 * Finds out if any given variable is a pure symbol.
//	 * Example: (and (or A B) (or (not A) B) [or (not A) (not B)]); // TODO: ADD explanation   .
//	 * 
//	 * @param literals	List of one or more Literal
//	 * @see clauseList	clauseList helper method for getting a list of Literals from a given Clause
//	 * @param variable	Variable to check
//	 * @return a boolean; true if pure, otherwise false.
//	 */
//	public boolean pureCheck(ArrayList<Literal> literals, Variable variable) {
//		//stubbed
//		return true;
//	}
//	
//	/**
//	 * This is the list of each literal that shows up in a specific clause; could be used to help find unit clauses.
//	 * Example: (or (not A) B); There are two literals: the (not A) literal, and the B literal.
//	 * 
//	 * @param clause	A Clause looks like this: (or (not A) B)
//	 * @return an ArrayList containing one or more Literal.
//	 */
//	public ArrayList<Literal> clauseList(Clause clause){
//		//ArrayList<Literal> list = new ArrayList<Literal>();
//		
//		//stubbed
//		return new ArrayList<Literal>();
//	}
//	
//	/**
//	 * This is how you know if the literal is a “positive” literal or a “negative” literal, i.e.,
//	 * whether the literal is negated or not. This also can be used to help find pure symbols!
//	 * Example: (or (not A) B); The ‘A’ literal here would have a valence of ‘false’ because it is negated.
//	 * The ‘B’ literal here would have a valence of ‘true’ because it is not negated.
//	 * 
//	 * @param clause	A Clause looks like this: (or (not A) B)
//	 * @return an ArrayList containing one or more Literal.
//	 */
//	public boolean valence(Literal literal) {
//		//stubbed
//		return true;
//	}
}
