/***********************************************************************
 * Module:  PointItineraire.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/
package model;
import java.util.ArrayList;
import java.util.Date;

public class PointItineraire {
   private int id;
   private Date heurePassage;
   private PlageHoraire plageHoraire;
   private Intersection intersection;
   
     
   public PointItineraire() {
}

public PointItineraire(PlageHoraire plageHoraire, Intersection intersection) {
	this.plageHoraire = plageHoraire;
	this.intersection = intersection;
}

public Intersection getIntersection() {
	return intersection;
}

/**
 * Calculate the shortest pathways towards points data.
 * @param intersections List of the intersections of the graph
 * @param pointsDestination List of the points of route towards which to calculate the various pathways
 * @param distance Board of the distances between the intersections
 * @return The list of the couples (pathway, duration), a pathway being an orderly list of identifiers of points of route, and duration expressing itself in seconds
 */
   public ArrayList<ArrayList<ArrayList<Integer>>> calculerPlusCourtCheminVers(ArrayList<Intersection> intersections, ArrayList<PointItineraire> pointsDestination, int[][] distance) 
   {
	   // Creation of the arraylist nodes
	   ArrayList<Integer> noeuds = new ArrayList<Integer>();
	   for (int i = 0; i < intersections.size() ; i++ )
	   {
		   noeuds.add(intersections.get(i).getId());
	   }
	   
	   // Recovery of id of the starting point
	   int debut = this.getIntersection().getId();
	   
	   // Creation of the list of id of places of destination
	   ArrayList<Integer> fin = new ArrayList<Integer>();
	   for (int i = 0; i < pointsDestination.size() ; i++ )
	   {
		   fin.add(pointsDestination.get(i).getIntersection().getId());
	   } 
	   
	   // Call of the function Dijkstra
	   Dijkstra d = new Dijkstra();
	   
	   return d.algoDijkstra(noeuds, distance, debut,fin);

   }

    public int getId() {
            return id;
    }

    public void setId(int id) {
            this.id = id;
    }

    public Date getHeurePassage() {
            return heurePassage;
    }

    public void setHeurePassage(Date heurePassage) {
            this.heurePassage = heurePassage;
    }

    public PlageHoraire getPlageHoraire() {
            return plageHoraire;
    }

    public void setPlageHoraire(PlageHoraire plageHoraire) {
            this.plageHoraire = plageHoraire;
    }

    public void setIntersection(Intersection intersection) {
            this.intersection = intersection;
    }     

}