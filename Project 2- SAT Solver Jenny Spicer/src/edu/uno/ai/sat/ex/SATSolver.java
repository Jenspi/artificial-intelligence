package edu.uno.ai.sat.ex;

import java.util.ArrayList;
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
		
		// If the problem has no variables, it is assumed to have the values true or false. (pre-programmed)
		if(assignment.problem.variables.size() == 0) {
			return assignment.getValue() == Value.TRUE;
		}
		
		if(assignment.problem.variables.size() == 1) {
			Variable loneVar = assignment.problem.variables.get(0);
			Literal firstLiteral = loneVar.literals.get(0);
			if(firstLiteral.valence) {
				assignment.setValue(loneVar, Value.TRUE);
			}
			else {
				assignment.setValue(loneVar, Value.FALSE);
			}
			return assignment.getValue() == Value.TRUE;
		}
		
		// If every clause is true, return true. (BASE case)
		else if( (assignment.countUnknownClauses() == 0) && (assignment.countFalseClauses() == 0) ) {
			return true;
		}
		
		// Base case for false
		else if(assignment.getValue() == Value.FALSE) {
			return false;
		}
		
		// Main code where recursive mumbo jumbo continues:
		else {
			// SIMPLIFY MODEL USING UNIT PROPAGATION
			ArrayList<Clause> unit_clauses_list = assignmentToUnitClauses(assignment);
			Clause currentClause = pickUnitClause(unit_clauses_list);
			
			// Check if there are any unit clauses, and if so, solve.
			if( currentClause != null ) {
				// currentClause is null when there are no unit clauses. This code is for when there IS at least one unit clause.
				return solveUnitClause(assignment, currentClause);
			}

			// TODO SIMPLIFY MODEL USING PURE SYMBOL PROPAGATION
			/* CHECK FOR PURE SYMBOLS (could be before or after unit prop--
			 * different cases but same format as checking for unit clauses except checking for
			 * pure symbols then propagating those: */
			
			// COMMENTED OUT TEMPORARILY-- This slows my code down dramatically and cuts down cases solved
			
  			ArrayList<Variable> pure_symbols_list = pureSymbols(assignment);//list of pure symbols
			Variable currentPureSymbol = pickPureSymbol(pure_symbols_list);//pick first pure symbol in list
			
			// Check if there are any pure symbols, and if so, solve.
			if( currentPureSymbol != null ) {
				return propagateSymbols(assignment, currentPureSymbol);
			}

			
			// Add all variables with unknown values from the problem to an ArrayList
			ArrayList<Variable> unknowns = new ArrayList<>();
			for(Variable variable : assignment.problem.variables){
				if(assignment.getValue(variable) == Value.UNKNOWN){
					unknowns.add(variable);
				}
			}//end for loop
			
			// Choose a variable whose value will be set; here, choose an UNASSIGNED variable (V).
			int index = 0;
			if(unknowns.size() > 0) {
				Variable variable = unknowns.get(index);
				// Set V=T. Try to find a model that satisfies.
				// tryValue() is used in place of solve()-- tryValue() undoes wrong switches
				if(tryValue(assignment, variable, Value.TRUE)){
					return true;
				}
				else if(tryValue(assignment, variable, Value.FALSE)) {
					return true;
				}
			}
			
			//return true if assignment is satisfied, false if not satisfied.
			return assignment.getValue() == Value.TRUE;

		}//end (long) else statement that controls the recursion
	}//end Solve()
	
	// Checks if the clause in the assignment is a unit clause; cuts down on time spent solving case
	private boolean unitClause(Assignment assignment, Clause clause) {
		/*
		 * A Unit Clause requires two things:
		 * 1. Exactly 1 unknown literal, -- marking it as special; unit prop is recognizing unit clause and setting values; using info from unit clauses to make a decision for model
		 * and
		 * 2. Value of clause must be UNKNOWN...
		 * 
		 * A clause would be UNKNOWN if A V B V C was T V ? V ?
		 * A clause would be TRUE if A V B V C was T V T V ? or T V F V ?
		 * A clause would be FALSE if A V B V C was F V F V F
		 */
		
		// Case 2: Value of clause must be UNKNOWN
		if( assignment.getValue(clause) != Value.UNKNOWN ) {
			return false;
		}
		
		// Case 1: Exactly 1 unknown literal
		return assignment.countUnknownLiterals(clause) == 1;
	
	}
	
	
	
	////###################### DEALING WITH UNIT CLAUSES #################################//
	
	// Return all unit clauses from an assignment, part 1/3 of unit clause propagation
	private ArrayList<Clause> assignmentToUnitClauses(Assignment assignment){
		ArrayList<Clause> unit_clauses = new ArrayList<>();
		
		for(Clause clause : assignment.problem.clauses) {
			if( unitClause(assignment, clause) ) {
				// If the clause is a unit clause, add to list
				unit_clauses.add(clause);
			}
		}
		
		// returns a list of unit clauses, to be used for step 2 (below)
		return unit_clauses;
	}
	
	// Pick a unit clause from our list that was returned from assignmentToUnitClauses(), part 2/3 of unit clause propagation
	private Clause pickUnitClause(ArrayList<Clause> clausesList) {
		if(!clausesList.isEmpty()) {
			// To send a unit clause to part three
			return clausesList.get(0);
		}
		else {
			// Empty list; there are no unit clauses at the moment
			return null;
		}
	}
	
	// Get unknown variable from a unit clause; ONLY TO BE USED WITH **UNIT** CLAUSES!
		private Literal getUnknown(Assignment assignment, Clause unitClause) {
			// Would like/possibly need checking to make sure it's not used on a normal clause
			Literal unknown_literal = null;
				for(Literal literal : unitClause.literals) {
					if(assignment.getValue(literal) == Value.UNKNOWN) {
						unknown_literal = literal;
					}
				}
				return unknown_literal;
		}
	
	// Solve unit clause with tryValue(), part 3/3 of unit clause propagation
	private boolean solveUnitClause(Assignment assignment, Clause clause) {
		Literal unknownLit = getUnknown(assignment, clause);
		Variable unknownVar = unknownLit.variable;
		if(unknownLit.valence){
			// Positive valence, so return TRUE to make the model for this clause TRUE
			return tryValue(assignment, unknownVar, Value.TRUE);
			
		}
		else{
			// Negative valence, so return FALSE to make the model for this clause TRUE (!FALSE = TRUE)
			return tryValue(assignment, unknownVar, Value.FALSE);
			
		}
	}//end solveUnitClause()
	
	
	
	//###################### DEALING WITH PURE SYMBOLS #################################//
	
	private ArrayList<Variable> pureSymbols(Assignment assignment) {
//		ArrayList<Literal> positives = new ArrayList<>();
//		ArrayList<Literal> negatives = new ArrayList<>();
		ArrayList<Variable> pure_symbols = new ArrayList<>();
		
//		Literal pos = null;
//		Literal neg = null;
//		Literal current_literal = null;
		
		//TODO:
		//do this with variables instead of literals
		//check all that arent in common
		//only consider unit clause pure symbols-- skip literals from clauses with values, only look at the ones with unknown clause values
		
		// Skip variables from clauses that have a value of TRUE/FALSE; because of the {TRUE V A V !B} and {B} and {B V A} thing-- B is technically pure here
		
		// For loop for going through each variable
		for(Variable variable : assignment.problem.variables){
//			Variable loneVar = assignment.problem.variables.get(0);
//			Literal firstLiteral = loneVar.literals.get(0);
			Literal pos = null;
			Literal neg = null;
			Literal current_literal = null;
			Literal nullLit = null;
			
			// For loop for going through variables list
			for(int i=0; i<variable.literals.size(); i++) {
				current_literal = variable.literals.get(i);
				//if( assignment.getValue(current_literal.clause) == Value.UNKNOWN) {
					
					if(current_literal.valence) {
						pos = current_literal;
					}
					else {
						neg = current_literal;
					}
					
					// Debugging:
	//				System.out.println("\n\nVariables :" + variable.toString());
	//				System.out.println("Clauses: " + assignment.problem.clauses.toString());
	//				System.out.println("Variables to literals: " + variable.literals);
				}
				
				if( pos == null || neg == null ) {
					// there is not a positive and a negative
					
					// Debugging:
	//				System.out.println("pos: "+pos);
	//				System.out.println("neg: "+ neg);
	//				System.out.println("Pure symbol detected: "+current_literal);
					pure_symbols.add(current_literal.variable);
				}
			//}
		}//end for loop

		return pure_symbols;
	}//end pureSymbols()
	
	private Variable pickPureSymbol(ArrayList<Variable> pureSymbolsList) {
		if(!pureSymbolsList.isEmpty()) {
			return pureSymbolsList.get(0);
		}
		else {
			// Empty list
			return null;
		}
	}
	
	private boolean propagateSymbols(Assignment assignment, Variable pure_variable) {
		// set all pure symbols to true
		
		//solve pure symbols
		//Variable literal_to_variable = pure_literal.variable;
		if(pure_variable.literals.get(0).valence){
			// Positive valence, so return TRUE to make the model for this clause TRUE
			return tryValue(assignment, pure_variable, Value.TRUE);
			
		}
		else{
			// Negative valence, so return FALSE to make the model for this clause TRUE (!FALSE = TRUE)
			return tryValue(assignment, pure_variable, Value.FALSE);
			
		}
	}
	
	
	
	//###################### TRYVALUE AND CHOOSEVARIABLE METHODS #################################//

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
