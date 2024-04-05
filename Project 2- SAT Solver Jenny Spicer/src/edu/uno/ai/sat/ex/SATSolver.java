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
		
		//Simplify the model using unit propagation.
	    //Simplify the model using pure symbols.
		
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
		// If any clause is empty, return false. (BASE case)
//		else if(assignment.countUnknownClauses() > 0) {
//			return false;
//		}
		
		// Base case for false
		else if(assignment.getValue() == Value.FALSE) {
			return false;
		}
		
		// Main code where recursive mumbo jumbo continues:
		else {
			// SIMPLIFY MODEL USING UNIT PROPAGATION
//			if(unitClause(assignment, assignment.problem.)) {
			ArrayList<Clause> unit_clauses_list = assignmentToUnitClauses(assignment);
			Clause currentClause = pickUnitClause(unit_clauses_list);
			
			// Check if there are any unit clauses, and if so, solve.
			if( currentClause != null ) {
				// currentClause is null when there are no unit clauses. This code is for when there IS at least one unit clause.
				return solveUnitClause(assignment, currentClause);
			}
//			}
			
			// SIMPLIFY MODEL USING PURE SYMBOL PROPAGATION
//			// TODO CHECK FOR PURE SYMBOLS (could be before or after unit prop--
			//different cases but same format as checking for unit clauses except checking for
			//pure symbols then propagating those:
			
			//COMMENTED OUT TEMPORARILY
			ArrayList<Literal> pure_symbols_list = pureSymbols(assignment);//list of pure symbols
			Literal currentPureSymbol = pickPureSymbol(pure_symbols_list);//pick first pure symbol in list
			
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
			
			
			int index = 0;
			
			// Choose a variable whose value will be set; here, choose an UNASSIGNED variable (V).
			if(unknowns.size() > 0) {
				Variable variable = unknowns.get(index);
				
				
				
				// Set V=T. Try to find a model that satisfies.
				if(tryValue(assignment, variable, Value.TRUE)){
					return true;
					//tryValue() is used in place of solve()-- tryValue() undoes wrong switches
					//index++;
					
				}
				else if(tryValue(assignment, variable, Value.FALSE)) {
					//index++;
					return true;
				}
//				else {
//					// Will go into this bracket if setting it to FALSE did NOT work
//					// Set V=F. Try to find a model that satisfies.
//					assignment.setValue(variable, Value.UNKNOWN);//cleaning up after ourselves
//					//index++;
//				}
			}
//			else {
//				
//			}
			//}//end while loop
			
			//return true if assignment is satisfied, false if not satisfied.
			return assignment.getValue() == Value.TRUE;

		}//end (long) else statement
	}//end Solve()
	
	// Checks if the clause in the assignment is a unit clause
	private boolean unitClause(Assignment assignment, Clause clause) {
		//means clause has exactly one unassigned literal
		//unit clause is a clause with one unknown literal-- marking it as special; unit prop is recognizing unit clause and setting values; using info from unit clauses to make a decision for model 
		//cuts down on time
		//set to true and see if it works, else set to false
		//return assignment.countUnknownLiterals(clause) == 1;
		
		/*
		 * A Unit Clause requires two things:
		 * 1. Exactly 1 unknown literal,
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
		int unknowns_count = 0;
		for(Literal literal : clause.literals) {
			if(assignment.getValue(literal) == Value.UNKNOWN) {
				//NOT SURE IF THIS WORKS-- trying to go through the literals in one clause,
				//seeing if there is exactly one unknown literal, then setting it to true later.
				unknowns_count++;
			}
		}
		
		if(unknowns_count == 1) {
			return true;
		}
		else {
			return false;
		}
	
	}
	
	// TIP: with FALSE V variable, we can just do V variable because FALSE doesnt help
	// remember to check if new unit clauses created from solving current/previous unit clauses
	/*
	//challenge:
	//step 1: take clause, return all unit clauses
	//step 2: take unit clauses from list (getter) (could be random or in order)
	//step 3: use step two to solve with tryvalue */
	
	////###################### DEALING WITH UNIT CLAUSES #################################//
	
	// Return all unit clauses from an assignment, part 1/3 of challenge
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
	
	// Pick a unit clause from our list that was returned from assignmentToUnitClauses(), part 2/3 of challenge
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
			// Would like/possibly need checking
			Literal unknown_literal = null;
				for(Literal literal : unitClause.literals) {
					if(assignment.getValue(literal) == Value.UNKNOWN) {
						unknown_literal = literal;
					}
				}
				return unknown_literal;
		}
	
	// Solve unit clause with tryValue(), part 3/3 of challenge
	private boolean solveUnitClause(Assignment assignment, Clause clause) {
		Literal unknownLit = getUnknown(assignment, clause);
		Variable unknownVar = unknownLit.variable;
		if(unknownLit.valence){
			// Positive valence, so return TRUE to make the model for this clause TRUE
			//assignment.setValue(unknownVar, Value.TRUE);
			return tryValue(assignment, unknownVar, Value.TRUE);
			
		}
		else{
			// Negative valence, so return FALSE to make the model for this clause TRUE (!FALSE = TRUE)
			//assignment.setValue(unknownVar, Value.FALSE);
			return tryValue(assignment, unknownVar, Value.FALSE);
			
		}
	}//end solveUnitClause()
	
	//###################### DEALING WITH PURE SYMBOLS #################################//
	
	private ArrayList<Literal> pureSymbols(Assignment assignment) {
		ArrayList<Literal> positives = new ArrayList<>();
		ArrayList<Literal> negatives = new ArrayList<>();
		
		Literal pos = null;
		Literal neg = null;
		
		//TODO:
		//do this with variables instead of literals
		//check all that arent in common
		//only consider unit clause pure symbols-- skip literals from clauses with values, only look at the ones with unknown clause values
		
		// Skip variables from clauses that have a value of TRUE/FALSE; because of the {TRUE V A V !B} and {B} and {B V A} thing-- B is technically pure here
//		System.out.println("Variables :"+variable.toString());
//		System.out.println("Clause: "+ clause.toString());
//		System.out.println("Assignment: "+ assignment.toString());
		
		for(Variable variable : assignment.problem.variables){
//			Variable loneVar = assignment.problem.variables.get(0);
//			Literal firstLiteral = loneVar.literals.get(0);
			for(int i=0; i<variable.literals.size(); i++) {
				Literal current_literal = variable.literals.get(i);
				if(current_literal.valence) {
					//positive valence
					positives.add(current_literal);
				}
				else {
					//negative valence
					negatives.add(current_literal);
				}
				System.out.println("Variables :" + variable.toString());
				System.out.println("Clauses: " + assignment.problem.clauses.toString());
				System.out.println("Variables to literals: " + variable.literals);
			}
		}//end for loop
		
		// Keep elements that both lists have in common
		ArrayList<Literal> impure_symbols = new ArrayList<>();
		impure_symbols.addAll(positives);
		impure_symbols.retainAll(negatives);//intersection
		
		// Now keep all that are NOT in common
		ArrayList<Literal> pure_symbols = new ArrayList<>();
		pure_symbols.addAll(positives);
		pure_symbols.addAll(negatives);
		pure_symbols.removeAll(impure_symbols);//remove intersection
		
		
		return pure_symbols;
	}//end pureSymbols()
	
	private Literal pickPureSymbol(ArrayList<Literal> pureSymbolsList) {
		if(!pureSymbolsList.isEmpty()) {
			return pureSymbolsList.get(0);
		}
		else {
			// Empty list
			return null;
		}
	}
	
	private boolean propagateSymbols(Assignment assignment, Literal literal) {
		//set all pure symbols to true
		// literal passing in is a pure literal
		
		//solve pure symbols
		Variable literal_to_variable = literal.variable;
		if(literal.valence){
			// Positive valence, so return TRUE to make the model for this clause TRUE
			return tryValue(assignment, literal_to_variable, Value.TRUE);
			
		}
		else{
			// Negative valence, so return FALSE to make the model for this clause TRUE (!FALSE = TRUE)
			return tryValue(assignment, literal_to_variable, Value.FALSE);
			
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
