/***********************************************************************
 * Module:  Section.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package model;

import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Element;

public class Section extends Observable {
	
	/** Attribute */
	private String street; 
	private Integer speed; 
	private Integer length; 
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
	 * @return Name of the street has which one belongs the section
	 */
	public String getStreet() {
		return street;
	}
	/**
	 * Allocate a street name to the section
	 * @param street nom de la street
	 */
	public void setStreet(String street) {
		street = street;
	}
	/**
	 * 
	 * @return Limit of speed of the section 
	 */
	public int getSpeed() {
		return speed;
	}
	/**
	 * Allocate the limit of speed to the section
	 * @param speed
	 */
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	/**
	 * 
	 * @return The Section of the section
	 */
	public Integer getLength() {
		return length;
	}
	/**
	 * Affect the Section of the section
	 * @param length
	 */
	public void setLength(int length) {
		this.length = length;
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
			street = tronconElement.getAttribute("nomRue");
			length = Integer.parseInt(tronconElement.getAttribute("longueur"));
			if (length < 0)
			{
				ZoneGeo.message = "Fichier incorrect, Longueur Negative";
				logger.log(Level.SEVERE,ZoneGeo.message);
				return idDestination;
			}
			speed = Integer.parseInt(tronconElement.getAttribute("vitesse"));
			if ((speed < 0) || (speed == 0 && length >0))
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
		if ( this.speed > 0)
		{
			return (this.length)/(this.speed) ;
		}
		else
		{
			if ( this.length == 0 && this.speed == 0)
				return 0; 

			return -1; 
		}		  
	}
}
