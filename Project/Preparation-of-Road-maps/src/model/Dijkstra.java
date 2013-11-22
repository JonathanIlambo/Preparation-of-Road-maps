/***********************************************************************
 * Module:  Dijkstra.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package model;

import java.util.ArrayList;
public class Dijkstra {
	
	
	/**
	 * Apply the algorithm of Djikstraa has a set of points, and returns the list of the couples (pathway, weight)
	 * Of every shorter pathway towards places of destination specify.
	 * @param ensNoeuds List of the identifiers of the points of the graph
	 * @param distance Board of the distances between the various points of the graph
	 * @param debut Identifier of the point of depart of the algorithm
	 * @param fin List of the identifiers of the places of destination towards which to calculate pathways
	 * @return The list of the couples (pathway, weight) calculate, an etant path(way) a list orders of identifiers of points of the graph
	 */
	public  ArrayList<ArrayList<ArrayList<Integer>>> algoDijkstra(ArrayList<Integer> ensNoeuds, int[][] distance, int debut, ArrayList<Integer> fin)
	{
		ArrayList<Integer> noeuds = new ArrayList<Integer>(ensNoeuds);
		int nbPoints = noeuds.size();
		int parcourus[] = new int[nbPoints];
		int precedents[] = new int[nbPoints];
		
		ArrayList<ArrayList<ArrayList<Integer>>> chemin = new ArrayList< ArrayList<ArrayList<Integer>>>();
		
		for (int i=0; i< nbPoints ; i++)
		{
			parcourus[i] = -1;
			precedents[i] = 0;			
		}
		
		parcourus[debut] = 0;
		
		
		// Search of the shortest pathway Ã leave from the beginning
		while(!noeuds.isEmpty())
		{
			int enCours=-1;
			int minimum=-1;
			int pointTest;
			for (int i =0; i< noeuds.size() ; i++)
			{
				pointTest =  noeuds.get(i);
				if ((parcourus[pointTest]<minimum || minimum<0) && parcourus[pointTest]>=0)
				{
					minimum = parcourus[pointTest];
					enCours = pointTest;
				}
			}
			
			// We remove following in nodes
			noeuds.remove(noeuds.indexOf(enCours));
			
			// We browse its successors
			for(int successeur=0; successeur< nbPoints; successeur++)
			{
				if (distance[enCours][successeur] != -1)
				{
					if (parcourus[successeur] > parcourus[enCours] + distance[enCours][successeur] || parcourus[successeur]<0)
					{
						
						parcourus[successeur] = parcourus[enCours] + distance[enCours][successeur];
						precedents[successeur] = enCours;
						if (!noeuds.contains(successeur))
						{
							noeuds.add(successeur);
						}					
					}	
				}
			}
		}
		
		// Generation of the shortest pathway between the beginning and the end
		int compteur = 0;
		while( compteur < fin.size())
		{	
			ArrayList<Integer> cheminEnCours = new ArrayList<Integer>();
			ArrayList<Integer> poidsCheminEnCours = new ArrayList<Integer>();
			poidsCheminEnCours.add(parcourus[fin.get(compteur)]);
			int pointEnCours = fin.get(compteur);
			while (pointEnCours != debut)
			{
				cheminEnCours.add(0, pointEnCours);
				pointEnCours = precedents[pointEnCours];
			}
			cheminEnCours.add(0, pointEnCours);
			ArrayList<ArrayList<Integer>> coupleCheminPoids = new ArrayList<ArrayList<Integer>>();
			coupleCheminPoids.add(cheminEnCours);
			coupleCheminPoids.add(poidsCheminEnCours);
			chemin.add(coupleCheminPoids);
			compteur++;
		}
		return chemin;		
	}
}

