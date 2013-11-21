/***********************************************************************
 * Module:  Dijkstra.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package model;

import java.util.ArrayList;
public class Dijkstra {
	
	
	/**
	 * Applique l'agorithme de Djikstraa a un ensemble de points, et retourne la liste des couples (chemin, poids)
	 * de chaque plus court chemin vers les points d'arrivee spécifies.
	 * @param ensNoeuds liste des identifiants des points du graphe
	 * @param distance tableau des distances entre les differents points du graphe
	 * @param debut identifiant du point de depart de l'algorithme
	 * @param fin liste des identifiants des points d'arrivee vers lesquels calculer les chemins
	 * @return Retourne la liste des couples (chemin, poids) calcules, un chemin etant une liste ordonne d'identifiants de points du graphe
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
		
		
		// Recherche des plus courts chemin Ã  partir de debut
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
			
			// On retire suivant Ã  noeuds
			noeuds.remove(noeuds.indexOf(enCours));
			
			// On parcourt ses successeurs
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
		
		// GÃ©nÃ©ration du plus court chemin entre debut et fin
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

