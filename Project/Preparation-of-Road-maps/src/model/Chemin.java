/***********************************************************************
 * Module:  Chemin.java
 * Author:  H4304
 ***********************************************************************/

package model;
 
import java.util.ArrayList;

public class Chemin {
   private int duree;
   
   private PointItineraire destination;
   private PointItineraire depart;
   private java.util.ArrayList<Section> troncons;
   
   
	public Chemin(PointItineraire depart, PointItineraire destination, ArrayList<Section> troncons, int duree) {
		this.depart = depart;
		this.destination = destination;
		this.troncons = troncons;
		this.duree = duree;
	}

	/**
	 * 
	 * @return Retourne le point d'arrivee du chemin.
	 */
	public PointItineraire getDestination(){
		return destination;
	}

	/**
	 * 
	 * @return Retourne le point de depart du chemin.
	 */
	public PointItineraire getDepart(){
		return depart;
	}
	
	/**
	 * 
	 * @return Retourne la duree en secondes necessaire pour traverser le chemin.
	 */
	public int getDuree(){
		return duree;
	}
	
	/**
	 * 
	 * @return Retourne la liste ordonnee des troncons constituant le chemin.
	 */
	public ArrayList<Section> getTroncons(){
		return troncons;
	}
}
