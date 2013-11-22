/***********************************************************************
 * Module:  Entrepot.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package model;

import java.util.Date;


public class Entrepot extends PointItineraire {
	
	private Date heureRetour;
	
	public Entrepot() {
	
	}

	/**
	 * 
	 * @return the hour of return planned in the warehouse, having make the tour
	 */
	public Date getHeureRetour() {
		return heureRetour;
	}

	/**
	 * Affect one hour of return e the warehouse for the tour.
	 * @param heureRetour The hour of return planned in the warehouse
	 */
	public void setHeureRetour(Date heureRetour) {
		this.heureRetour = heureRetour;
	}
	
	

}
