package tsp;

import java.util.ArrayList;

/**
 * @author Teacher
 *
 */
public interface Graph {

	
	/**
	 * Displays <code>this</code> on the standard output
	 */
	public abstract void display();

	/**
	 * @return the maximal cost of an arc of <code>this</code>
	 */
	public abstract int getMaxArcCost();

	/**
	 * @return the minimal cost of an arc of <code>this</code>
	 */
	public abstract int getMinArcCost();

	/**
	 * @return the number of vertices of <code>this</code>
	 */
	public abstract int getNbVertices();

	/**
	 * @param i a vertex such that <code>0 <= i < this.getNbVertices()</code>
	 * @param j a vertex such that <code>0 <= j < this.getNbVertices()</code>
	 * @return the cost of <code>(i,j)</code> if <code>(i,j)</code> is an arc of <code>this</code>, 
	 * <code>this.getMaxArcCost()+1</code> otherwise
	 * @throws ArrayIndexOutOfBoundsException If <code>i<0</code> or <code>i>=this.getNbVertices()</code> 
	 * or <code>j<0</code> or <code>j>=this.getNbVertices()</code>
	 */
	public abstract int getCost(int i, int j)
			throws ArrayIndexOutOfBoundsException;

	/**
	 * @return the <code>cost</code> matrix of <code>this</code>: for all vertices <code>i</code> and <code>j</code>,
	 * if <code>(i,j)</code> is an arc of <code>this</code>, then <code>cost[i][j]</code> = cost of <code>(i,j)</code>, 
	 * otherwise <code>cost[i][j] = this.getMaxArcCost()+1</code>
	 */
	public abstract int[][] getCost();

	/**
	 * @param i a vertex such that <code>0 <= i < this.getNbVertices()</code>
	 * @return the list of successor vertices of <code>i</code> in <code>this</code>
	 * @throws ArrayIndexOutOfBoundsException If <code>i<0</code> or <code>i>=this.getNbVertices()</code>
	 */
	public abstract ArrayList<Integer> getSucc(int i)
			throws ArrayIndexOutOfBoundsException;

}