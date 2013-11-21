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
	 * @return Retourne l'heure de retour prevue à l'entrepot, apres avoir effectue la tournee.
	 */
	public Date getHeureRetour() {
		return heureRetour;
	}

	/**
	 * Affecte une heure de retour e l'entrepot pour la tournee.
	 * @param heureRetour heure de retour prevue à l'entrepot
	 */
	public void setHeureRetour(Date heureRetour) {
		this.heureRetour = heureRetour;
	}
	
	

}
