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
		// If the problem has no variables, it is assumed to have the values true or false. (pre-programmed)
		if(assignment.problem.variables.size() == 0) {
			return assignment.getValue() == Value.TRUE;
		}
		// If every clause is true, return true. (edge case)
//		else if( (assignment.countUnknownClauses() == 0) || (assignment.countFalseClauses() == 0) ) {
//			return true;
//		}
		// If any clause is empty, return false. (edge case)
//		else if(assignment.countUnknownClauses() > 0) {
//			return false;
//		}
		else {
			// Add all variables with unknown values from the problem to an ArrayList
			ArrayList<Variable> unknowns = new ArrayList<>();
			for(Variable variable : assignment.problem.variables){
				if(assignment.getValue(variable) == Value.UNKNOWN){
					unknowns.add(variable);
				}
			}//end for loop
			
			// Keep trying until the assignment is satisfying.
			int index = 0;
			while(assignment.getValue() != Value.TRUE) {
			//while(index < unknowns.size()) {
				// Choose a variable whose value will be set; here, choose an UNASSIGNED variable (V).
				Variable variable = unknowns.get(index);
				
				// Set V=T. Try to find a model that satisfies.
				if(tryValue(assignment, variable, Value.TRUE)){
					//tryValue() is used in place of solve()-- tryValue() undoes wrong switches
					index++;
				}
				else if(tryValue(assignment, variable, Value.FALSE)) {
					index++;
				}
				else {
					// Will go into this bracket if setting it to FALSE did NOT work
					// Set V=F. Try to find a model that satisfies.
					assignment.setValue(variable, Value.UNKNOWN);//cleaning up after ourselves
					//index++;
				}
			}//end while loop
			
			//return true if assignment is satisfied, false if not satisfied.
			return assignment.getValue() == Value.TRUE;

		}//end (long) else statement
	}//end Solve()
	

	private boolean tryValue(Assignment a, Variable var, Value val) {
		// tryValue's mission: Set a variable to a value, and if it doesn't work, undo it; Given through PDF.
		
		// Backup variable's current value, then set the variable's new value to val
		Value backup = a.getValue(var);
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
	}//end chooseVariable()
}
