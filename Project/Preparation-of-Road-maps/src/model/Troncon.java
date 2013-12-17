/***********************************************************************
 * Module:  Troncon.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package model;

import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Element;

public class Troncon extends Observable {
	
	/** Attribute */
	private String rue; 
	private Integer vitesse; 
	private Integer longueur; 
	private Intersection destination;
	private Intersection depart;
	private boolean cheminLivraison = false; 
	
	private static final Logger logger = Logger.getLogger("logger");
	
	/**
	 * Affect a point of depart to the section
	 * @param depart intersection of depart
	 */
	public void setDepart(Intersection depart) {
		this.depart = depart;
	}
	/**
	 * 
	 * @return Name of the rue has which one belongs the section
	 */
	public String getRue() {
		return rue;
	}
	/**
	 * Allocate a rue name to the section
	 * @param street nom de la rue
	 */
	public void setRue(String street) {
		street = street;
	}
	/**
	 * 
	 * @return Limit of vitesse of the section 
	 */
	public int getVitesse() {
		return vitesse;
	}
	/**
	 * Allocate the limit of vitesse to the section
	 * @param vitesse
	 */
	public void setVitesse(Integer vitesse) {
		this.vitesse = vitesse;
	}
	/**
	 * 
	 * @return The Troncon of the section
	 */
	public Integer getLongueur() {
		return longueur;
	}
	/**
	 * Affect the Troncon of the section
	 * @param longueur
	 */
	public void setLongueur(int longueur) {
		this.longueur = longueur;
	}
	/**
	 * 
	 * @return The intersection of depart of the section
	 */
	public Intersection getDepart() {
		return depart;
	}
	/**
	 * 
	 * @return The intersection of arrival of the section
	 */
	public Intersection getDestination() {
		return destination;
	}
	/**
	 * Affect the intersection of arrival of the section
	 * @param destination
	 */
	public void setDestination(Intersection destination) {
		this.destination = destination;
	}
	/**
	 * 
	 * @return TRUE if the section is crossbar by the route of the road map, < br > FALSE otherwise
	 */
	public boolean getCheminLivraison() {
		return cheminLivraison;
	}
	/**
	 * Basic salary if the section is crossbar or not by the route of the road map
	 * @param cheminLivraison TRUE if the section is crossbar, FALSE otherwise
	 */
	public void setCheminLivraison(boolean cheminLivraison) {
		this.cheminLivraison = cheminLivraison;
		setChanged();
		notifyObservers(this.cheminLivraison);	
	}
	/**
	 * Generate the section has to leave data XML
	 * @param tronconElement
	 * @return
	 */
	public int genererTroncon (Element tronconElement)
	{
		int idDestination = -1; 
		try
		{
			/** Recovery of the attributes d an intersection */
			rue = tronconElement.getAttribute("nomRue");
			longueur = Integer.parseInt(tronconElement.getAttribute("longueur"));
			if (longueur < 0)
			{
				ZoneGeo.message = "Fichier incorrect, Longueur Negative";
				logger.log(Level.SEVERE,ZoneGeo.message);
				return idDestination;
			}
			vitesse = Integer.parseInt(tronconElement.getAttribute("vitesse"));
			if ((vitesse < 0) || (vitesse == 0 && longueur >0))
			{
				ZoneGeo.message = "Fichier incorrect, Vitesse Negative OU Nulle";
				logger.log(Level.SEVERE,ZoneGeo.message);
				return idDestination;
			}
			destination = null ; 
			// TODO Est ce qu un troncon a toujours une destination ? 
			idDestination = Integer.parseInt(tronconElement.getAttribute("destination"));
			if (idDestination < 0)
			{
				ZoneGeo.message = "File incorrect, Identifiant Destination Negatif";
				logger.log(Level.SEVERE,ZoneGeo.message);
				return -1;
			}
			return idDestination ; 			
		}
		catch(Exception ex)
		{
			ZoneGeo.message = ex.getMessage();
			logger.log(Level.SEVERE,ZoneGeo.message);
			return idDestination; 
		}		
	}
	
	/**
	 * Calculate the duration in seconds put to cross the section 
	 * @return The duration of crossing in seconds
	 */
	public int duration ()
	{
		if ( this.vitesse > 0)
		{
			return (this.longueur)/(this.vitesse) ;
		}
		else
		{
			if ( this.longueur == 0 && this.vitesse == 0)
				return 0; 

			return -1; 
		}		  
	}
}
