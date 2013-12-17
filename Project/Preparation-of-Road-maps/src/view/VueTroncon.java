/***********************************************************************
 * Module:  VueSection.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package view;

import java.util.Observable;
import java.util.Observer;


import model.Troncon;


public class VueTroncon implements Observer {

		private VueIntersection destination;
	    private VueIntersection pointDepart;
	    private boolean cheminLivraison;

	 
	    /**
		 * Constructeur de VueTroncon
		 * @param destination est un objet VueIntersection permettant de connaitre la destination du troncon
		 * @param pointDepart est un objet VueIntersection permettant de connaitre le point de depart du troncon
		 * @param cheminLivraison permet de mettre en evidence dans le dessin si un troncon est dans le chemin de livraison
		 */
	public VueTroncon (VueIntersection destination, VueIntersection pointDepart, boolean cheminLivraison) {
	        this.destination=destination;
	        this.pointDepart=pointDepart;
	        this.cheminLivraison=cheminLivraison;
	 }
	 
	  /**
	   * Builder by default of VueTroncon
	 */
	public VueTroncon(){}
	   
	public VueIntersection getDestination() {
		return destination;
	}

	public VueIntersection getPointDepart() {
		return pointDepart;
	}
	
	public boolean getCheminLivraison() {
		return cheminLivraison;
	}

	public void setCheminLivraison(boolean cheminLivraison) {
		this.cheminLivraison = cheminLivraison;
	}
	
	 /**
	  * update of the pattern to Observe who allows to put has in the daytime the view
	  * If a section becomes a pathway of delivery
	 */
	public void update(Observable obs, Object obj) {
	if (obs instanceof Troncon)
		{
			this.setCheminLivraison(((Troncon) obs).getCheminLivraison());
		}
		
	}
}



