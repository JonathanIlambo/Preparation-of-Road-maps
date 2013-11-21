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
 * Calcule les plus courts chemins vers des points donnees.
 * @param intersections liste des intersections du graphe
 * @param pointsDestination liste des points d'itineraire vers lesquels calculer les differents chemins
 * @param distance tableau des distances entre les intersections
 * @return Retourne la liste des couples (chemin, duree), un chemin etant une liste ordonnee d'identifiants de points d'itineraire, et duree s'exprimant en secondes
 */
   public ArrayList<ArrayList<ArrayList<Integer>>> calculerPlusCourtCheminVers(ArrayList<Intersection> intersections, ArrayList<PointItineraire> pointsDestination, int[][] distance) 
   {
	   // Création de l'arraylist noeuds
	   ArrayList<Integer> noeuds = new ArrayList<Integer>();
	   for (int i = 0; i < intersections.size() ; i++ )
	   {
		   noeuds.add(intersections.get(i).getId());
	   }
	   
	   // Récupération de l'id du point de départ
	   int debut = this.getIntersection().getId();
	   
	   // Création de la liste des id des points de destination
	   ArrayList<Integer> fin = new ArrayList<Integer>();
	   for (int i = 0; i < pointsDestination.size() ; i++ )
	   {
		   fin.add(pointsDestination.get(i).getIntersection().getId());
	   } 
	   
	   // Appel de la fonction Dijkstra
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