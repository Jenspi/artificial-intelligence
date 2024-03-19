package edu.uno.ai.sat.ex;

import java.util.ArrayList;
import java.util.Random;

import edu.uno.ai.sat.Assignment;
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
		/* Brute force:
			Begin with every variable’s value unassigned.
			To find a model which satisfies a CNF expression:
				If every clause is true, return true. ✓
				If any clause is empty, return false. ✓
				Choose an unassigned variable V.
				Set V=T. Try to find a model that satisfies.
				Set V=F. Try to find a model that satisfies.
				Return false.
		*/
		
		// If it already has a solution, return true
		// (I don't know if this will run me into issues so it will be commented out for now)
//		if(assignment.getValue() == Value.TRUE) {
//			return true;
//		}
		// If the problem has no variables, it is assumed to have the values true or false. (pre-programmed)
		if(assignment.problem.variables.size() == 0) {
			System.out.println("Variable value: +"+assignment.getValue());//debugging
			return assignment.getValue() == Value.TRUE;
		}
		// If every clause is true, return true. (edge case)
		else if( (assignment.countUnknownClauses() == 0) || (assignment.countFalseClauses() == 0) ) {
			return true;
		}
		// If any clause is empty, return false. (edge case)
		else if(assignment.countUnknownClauses() > 0) {
			return false;
		}
		else {
			// Keep trying until the assignment is satisfying.
			while(assignment.getValue() != Value.TRUE) {
				// Add all variables with unknown values from the problem to an ArrayList
				ArrayList<Variable> unknowns = new ArrayList<>();
				for(Variable variable : assignment.problem.variables){
					if(assignment.getValue(variable) == Value.UNKNOWN){
						unknowns.add(variable);
					}
				}//end for loop
				
				// Choose a variable whose value will be set; here, choose an UNASSIGNED variable (V).
				//Variable variable = chooseVariable(assignment);//old code
				Variable variable = unknowns.get(0);
				
				// Set V=T. Try to find a model that satisfies.
				//assignment.setValue(variable, Value.TRUE);
				tryValue(assignment, variable, Value.TRUE);//tryValue() is used in place of solve()-- tryValue() undoes wrong switches
				
				// Set V=F. Try to find a model that satisfies.
				tryValue(assignment, variable, Value.FALSE);
				
				// Return false
				return false;
			}
			// Return success. (Note, if the problem cannot be solved, this
			// solver will run until it reaches the operations or time limit.)
			return true;
		}
	}//end Solve()
	
	// Set a variable to a value, and if it doesn't work, undo it; Given through PDF.
	private boolean tryValue(Assignment a, Variable var, Value val) {
		// Backup variable's current value
		Value backup = a.getValue(var);
		
		// Now, set the variable to the given value
		a.setValue(var, val);
		
		// Try to solve the problem with this new value
		if( solve(a) ){
			return true;
		}
		else {
			// We failed, so return the variable to the backup (previous) value.
			a.setValue(var, backup);
			return false;
		}
	}// end tryValue()
	
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
	@SuppressWarnings("unused")
	private final Variable chooseVariable(Assignment assignment) {
		// This list will hold all variables whose current value is 'unknown.'
		// this will choose a random variable and assign it a random value-- not good for brute/dpll
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
