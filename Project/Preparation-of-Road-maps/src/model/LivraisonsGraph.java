/***********************************************************************
 * Module:  LivraisonsGraph.java
 * Author:  H3404
 ***********************************************************************/

package model;

import java.util.ArrayList;
import java.util.Iterator;

import tsp.Graph;

public class LivraisonsGraph implements Graph {
	private int nbVertices;
	private int maxArcCost;
	private int minArcCost;
	private int[][] cost; 
	private ArrayList<ArrayList<Integer>> succ; 
	
	public LivraisonsGraph(ArrayList<ArrayList<Integer>> succ, int[][] cost)
	{
		this.succ = succ;
		this.cost = cost;
		nbVertices = succ.size();
	
		maxArcCost = 0;
		minArcCost = 0;
		for(int i=0; i<cost.length; i++)
		{
			for(int j=0; j<cost[0].length; j++)
			{
				if(cost[i][j] > maxArcCost)
				{
					maxArcCost = cost[i][j];
				}
				if(cost[i][j] > -1 && cost[i][j] < minArcCost)
				{
					minArcCost = cost[i][j];
				}
			}
		}
	}
	
	@Override
	public void display(){
		for (int i=0; i<nbVertices; i++){
			System.out.print("Vertex "+i+" has "+succ.get(i).size()+" successors: ");
			Iterator<Integer> it = succ.get(i).iterator();
			while (it.hasNext()){
				int j = it.next();
				System.out.print(j + "(cost = ");
				System.out.print(cost[i][j]+") ");
			}
			System.out.println("");
		}
	}

	@Override
	public int getMaxArcCost() {
		return maxArcCost;
	}

	@Override
	public int getMinArcCost() {
		return minArcCost;
	}

	@Override
	public int getNbVertices() {
		return nbVertices;
	}

	@Override
	public int getCost(int i, int j) throws ArrayIndexOutOfBoundsException{
		if ((i<0) || (i>=nbVertices) || (j<0) || (j>=nbVertices))
			throw new ArrayIndexOutOfBoundsException();
		return cost[i][j];
	}

	@Override
	public int[][] getCost(){
		return cost;
	}

	@Override
	public ArrayList<Integer> getSucc(int i) throws ArrayIndexOutOfBoundsException{
		if ((i<0) || (i>=nbVertices))
			throw new ArrayIndexOutOfBoundsException();
		return succ.get(i);
	}
}