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
   private ArrayList<Troncon> troncons;
   
   
	public Chemin(PointItineraire depart, PointItineraire destination, ArrayList<Troncon> troncons, int duree) {
		this.depart = depart;
		this.destination = destination;
		this.troncons = troncons;
		this.duree = duree;
	}

	/**
	 * 
	 * @return The place of destination of the pathway.
	 */
	public PointItineraire getDestination(){
		return destination;
	}

	/**
	 * 
	 * @return the point of depart of the pathway
	 */
	public PointItineraire getDepart(){
		return depart;
	}
	
	/**
	 * 
	 * @return the necessary duration in seconds to cross the pathway
	 */
	public int getDuree(){
		return duree;
	}
	
	/**
	 * 
	 * @return The order list of sections constituting the pathway
	 */
	public ArrayList<Troncon> getTroncons(){
		return troncons;
	}
}
