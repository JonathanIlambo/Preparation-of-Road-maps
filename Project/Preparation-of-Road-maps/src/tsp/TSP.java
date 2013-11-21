package tsp;
import static choco.Choco.allDifferent;
import static choco.Choco.eq;
import static choco.Choco.makeIntVar;
import static choco.Choco.makeIntVarArray;
import static choco.Choco.nth;
import static choco.Choco.sum;
import choco.Options;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;

/**
 * @author Teacher
 *
 */

public class TSP {

	private int[] next;
	private int[] pos;
	private int totalCost;
	private SolutionState state;

	public TSP() {
		state = SolutionState.NO_SOLUTION_FOUND;
	}

	/**
	 * Searches for a tour in <code>graph</code> lower than <code>bound</code> and tries to prove its optimality in less than <code>timeLimit</code> milliseconds.
	 * @param timeLimit a limit of CPU time in milliseconds
	 * @param bound an upper bound on the total cost of the tour
	 * @param graph a graph
	 * @return NO_SOLUTION_FOUND, if no tour lower than <code>bound</code> has been found within <code>timeLimit</code> milliseconds,<br>
	 * SOLUTION_FOUND, if a tour lower than <code>bound</code> has been found, but its optimality has not been proven within <code>timeLimit</code> milliseconds,<br> 
	 * OPTIMAL_SOLUTION_FOUND, if a tour lower than <code>bound</code> has been found, and its optimality has been proven,<br>
	 * or INCONSISTENT, if there does not exist a tour lower than <code>bound</code>.
	 */
	public SolutionState solve(int timeLimit, int bound, Graph graph) {
		int n = graph.getNbVertices();
		int minCost = graph.getMinArcCost();
		int maxCost = graph.getMaxArcCost();
		int[][] cost = graph.getCost();
		next = new int[n];
		pos = new int[n];

		// Declare Choco variables:		
		// xPos[i] = ith visited vertex
		IntegerVariable[] xPos = makeIntVarArray("Pos ", n, 0, n - 1);
		// xCost[i] = cost of arc (i,xNext[i])
		IntegerVariable[] xCost = makeIntVarArray("Cost ", n, minCost, maxCost);
		// xNext[i] = vertex visited after i
		IntegerVariable[] xNext = new IntegerVariable[n];
		for (int i = 0; i < n; i++)
			xNext[i] = makeIntVar("Next " + i, graph.getSucc(i));
		// xTotalCost = total cost of the solution
		IntegerVariable xTotalCost = makeIntVar("Total cost ", n*minCost, bound - 1, Options.V_OBJECTIVE);

		Model m = new CPModel();
		// Add constraints to the model m
		m.addConstraint(eq(xPos[0], 0)); 							// The tour starts from the depot 0
		for (int i = 0; i < n; i++) {
			m.addConstraint(nth(xNext[i], cost[i], xCost[i]));		// xCost[i] = cost[i][xNext[i]]
			if (i < n - 1)
				m.addConstraint(nth(xPos[i], xNext, xPos[i + 1]));	// xPos[i+1] = xNext[xPos[i]]
		}
		m.addConstraint(nth(xPos[n - 1], xNext, xPos[0])); 			// The tour ends on the depot 0: xNext[xPos[n-1]] = xPos[0]
		m.addConstraint(allDifferent(xPos));						// Each vertex occurs once in xPos
		m.addConstraint(allDifferent(xNext));						// Each vertex occurs once in xNext
		m.addConstraint(eq(xTotalCost, sum(xCost)));				// xTotalCost = sum { xCost[i] | i in 0..n_1 }

		// Solve the problem
		Solver s = new CPSolver();
		s.read(m);
		s.setTimeLimit(timeLimit);
		Boolean res = s.minimize(false);
		if (res == null) state = SolutionState.NO_SOLUTION_FOUND;
		else if (!res) state = SolutionState.INCONSISTENT;
		else { // Store the solution in class attributes
			for (int i = 0; i < graph.getNbVertices(); i++) {
				next[i] = s.getVar(xNext[i]).getVal();
				pos[i] = s.getVar(xPos[i]).getVal();
			}
			totalCost = s.getVar(xTotalCost).getVal();
			if (s.isEncounteredLimit()) 
				state = SolutionState.SOLUTION_FOUND;
			else 
				state = SolutionState.OPTIMAL_SOLUTION_FOUND;
		}
		return state;
	}

	/**
	 * @return an array <code>next</code> such that <code>next[i]</code> gives the vertex visited just after <code>i</code> in the last computed solution
	 * @throws NullPointerException If <code>this.getSolutionState()</code> not in <code>{SOLUTION_FOUND, OPTIMAL_SOLUTION_FOUND}</code>
	 */
	public int[] getNext() throws NullPointerException {
		if ((state == SolutionState.NO_SOLUTION_FOUND) || (state == SolutionState.INCONSISTENT))
			throw new NullPointerException();
		return next;
	}

	/**
	 * @return an array <code>pos</code> such that <code>pos[i]</code> gives the <code>i</code>th visited vertex in the last computed solution
	 * @throws NullPointerException If <code>this.getSolutionState()</code> not in <code>{SOLUTION_FOUND, OPTIMAL_SOLUTION_FOUND}</code>
	 */
	public int[] getPos() throws NullPointerException{
		if ((state == SolutionState.NO_SOLUTION_FOUND) || (state == SolutionState.INCONSISTENT))
			throw new NullPointerException();
		return pos;
	}

	/**
	 * @return the total cost of the last computed solution (0 if no solution computed)
	 */
	public int getTotalCost() {
		return totalCost;
	}

	/**
	 * @return NO_SOLUTION_FOUND if no solution has been found during the last call to solve,<br>
	 * SOLUTION_FOUND if a solution has been found during the last call to solve but its optimality has not been proven,<br>
	 * OPTIMAL_SOLUTION_FOUND if a solution has been found during the last call to solve, and its optimality proven,<br>
	 * INCONSISTENT if the last call to solve has proven that the problem does not have a solution. 
	 */
	public SolutionState getSolutionState() {
		return state;
	}

}

