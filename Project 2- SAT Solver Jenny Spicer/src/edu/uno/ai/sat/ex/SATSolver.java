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
			//assignment.setValue(, Value.TRUE);
			return assignment.getValue() == Value.TRUE;
		}
		// If every clause is true, return true. (edge case)
		else if( (assignment.countUnknownClauses() == 0) && (assignment.countFalseClauses() == 0) ) {
			return true;
		}
		// If any clause is empty, return false. (edge case)
//		else if(assignment.countUnknownClauses() > 0) {
//			return false;
//		}
		
		// Base case for false
		else if(assignment.getValue() == Value.FALSE) {
			return false;
		}
		
		// Main code:
		else {
			//check for unit prop
			//////
			
//			// Check for pure symbols, and set them to true values
//			if( !pureSymbols(assignment).isEmpty() ) {
//				ArrayList<Literal> symbols = pureSymbols(assignment);
//				for(Literal literal : symbols) {
//					if(tryValue(assignment, literal.variable, Value.TRUE)) {
//						return true;
//					}
//					else if(tryValue(assignment, literal.variable, Value.FALSE)) {
//						return true;
//					}
//				}
//			}
			
			// Add all variables with unknown values from the problem to an ArrayList
			ArrayList<Variable> unknowns = new ArrayList<>();
			for(Variable variable : assignment.problem.variables){
				if(assignment.getValue(variable) == Value.UNKNOWN){
					unknowns.add(variable);
				}
			}//end for loop
			
			// Keep trying until the assignment is satisfying.
			int index = 0;
			//while(assignment.getValue() != Value.TRUE) {
			//while(index < unknowns.size()) {
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
	
	private boolean unitClause(Assignment assignment, Clause clause) {
		//means clause has exactly one unassigned literal
		//unit clause is a clause with one unknown literal-- marking it as special; unit prop is recognizing unit clause and setting values; using info from unit clauses to make a decision for model 
		//cuts down on time
		//set to true and see if it works, else set to false
		//return assignment.countUnknownLiterals(clause) == 1;
		
		
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
	
	// Get unknown variable from a unit clause
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
	
	
	// TIP: with FALSE V variable, we can just do V variable because FALSE doesnt help
	// remember to check if new unit clauses created from solving current/previous unit clauses
	
	/*
	//challenge:
	//step 1: take clause, return all unit clauses
	//step 2: take unit clauses from list (getter) (could be random or in order)
	//step 3: use step two to solve with tryvalue */
	
	//###########################################################################//
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
		try {
			// To send a unit clause to part three
			return clausesList.get(0);
		}
		catch(Exception e) {
			System.out.println(e.toString());
			return null;
		}
		
	}
	
	// Solve unit clause with tryValue(), part 3/3 of challenge
	private boolean solveUnitClause(Assignment assignment, Clause clause) {
		Literal unknownLit = getUnknown(assignment, clause);
		Variable unknownVar = unknownLit.variable;
		
		if(tryValue(assignment, unknownVar, Value.TRUE)){
			return true;
		}
		else if(tryValue(assignment, unknownVar, Value.FALSE)) {
			return true;
		}
		return false;
	}//end solveUnitClause()
	
	//###########################################################################//
	
	private ArrayList<Literal> pureSymbols(Assignment assignment) {
		ArrayList<Literal> positives = new ArrayList<>();
		ArrayList<Literal> negatives = new ArrayList<>();
		
		ArrayList<Literal> pure_symbols = new ArrayList<>();
		
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
			}
		}//end for loop
		
		// Keep elements that both lists have in common
		pure_symbols.addAll(positives);
		pure_symbols.retainAll(negatives);
		
		return pure_symbols;
	}//end pureSymbols()
	

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
	 * DPLL pseudocode
	 * 
	 * ✓function DPLL-SATISFIABLE?(s) returns true or false
	 * ✓inputs: s, a sentence in propositional logic
	 * 
	 * clauses <- the set of clauses in the CNF representation of s
	 * symbols <- a list of the proposition symbols in s
	 * return DPLL(clauses, symbols, {})
	 * ----------------------------------------
	 * function DPLL(clauses, symbols, model) returns true or false
	 * 
	 * ✓if every clause in clauses is true in model then return true
	 * ✓if some clause in clauses is false in model then return false
	 * P, value <- FIND-PURE-SYMBOL(symbols, clauses, model)
	 * if P is non-null then return DPLL(clauses, symbols - P, model UNION {P=value})
	 * P, value <- FIND-UNIT-CLAUSE(clauses, model)
	 * if P is non-null then return DPLL(clauses, symbols - P, model UNION {P=value})
	 * P <- FIRST(symbols); rest <- REST(symbols)
	 * return DPLL(clauses, rest, model UNION {P=true}) or DPLL(clauses, rest, model UNION {P=false})
	 */
	
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
