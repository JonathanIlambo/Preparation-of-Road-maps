/***********************************************************************
 * Module:  Intersection.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Intersection extends Observable {
	
	/** Attribut */
	private int id; 
	private int x;
	private int y;
	private ArrayList<Troncon> tronconSortant;
	private ZoneGeo zoneGeo;
	private Integer etat;
	private Boolean respectPlageHoraire; 
	
	static private Logger logger = Logger.getLogger("logger");
	
	//Constructeur 	
	public Intersection() {
		this.setEtat(-1);
		this.setRespectPlageHoraire(true); 
	}
	
	/**
	 * 
	 * @return l'identifiant de l'intersection
	 */
	public int getId() {
		return id;
	}
	/**
	 * Affecte l'identifiant de l'intersection
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 
	 * @return l'abscisse geographique de l'intersection
	 */
	public int getX() {
		return x;
	}
	/**
	 * Affecte l'abscisse geographique de l'intersection
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * 
	 * @return l'ordonnee geographique de l'intersection
	 */
	public int getY() {
		return y;
	}
	/**
	 * Affecte l'ordonnee geographique de l'intersection
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * 
	 * @return la liste des troncons ayant cette intersection pour point de depart
	 */
	public ArrayList<Troncon> getTronconSortant() {
		return tronconSortant;
	}
	/**
	 * Affecte la liste des troncons ayant cette intersection pour point de depart
	 * @param tronconSortant
	 */
	public void setTronconSortant(ArrayList<Troncon> tronconSortant) {
		this.tronconSortant = tronconSortant;
	}
	/**
	 * 
	 * @return retourne la zone geographique dans laquelle se trouve l'intersection
	 */
	public ZoneGeo getZoneGeo() {
		return zoneGeo;
	}
	/**
	 * Affecte la zone geographique dans laquelle se trouve l'intersection
	 * @param zoneGeo
	 */
	public void setZoneGeo(ZoneGeo zoneGeo) {
		this.zoneGeo = zoneGeo;
	}
	
	/**
	 * 
	 * @return -1 si l'intersection n'est pas un point de l'itineraire de la feuille de route,<br>
	 * l'identifiant de la plage horaire a laquelle appartient le point d'itineraire associe a l'intersection sinon
	 */
	public Integer getEtat() {
		
		return etat;
	}
	/**
	 * Affecte un idenfifiant de plage horaire a l'intersection, signifiant que l'intersection est un point de l'itineraire
	 * de la feuille de route
	 * @param etat identifiant d'une plage horaire, ou -1 si l'intersection n'est pas associee a un point d'itineraire
	 */
	public void setEtat(Integer etat) {
		this.etat = etat;
		setChanged();
		notifyObservers(this.etat);	
	}
	
	/**
	 * 
	 * @return TRUE si le point d'itineraire associe a l'intersection respecte sa plage horaire,<br> FALSE sinon
	 */
	public Boolean getRespectPlageHoraire() {
		return this.respectPlageHoraire;
	}
	/**
	 * Fixe si le point d'itineraire associe a l'intersection respecte ou non sa plage horaire
	 * @param respectPlageHoraire
	 */
	public void setRespectPlageHoraire(boolean respectPlageHoraire) {
		this.respectPlageHoraire = respectPlageHoraire;
		setChanged();
		notifyObservers(this.respectPlageHoraire);	
		
	}
	/**
	 * Genere l'intersection a partir de donnees XML decrivant l'intersection
	 * @param intersectionElement element XML
	 * @param tronconIdDestination troncons ayant pour point de depart l'intersection
	 * @return
	 */
	public int genererIntersection (Element intersectionElement, Map<Troncon,Integer> tronconIdDestination)
	{
		String tag = "TronconSortant";
		
		try
		{
			// Récupération des attributs d une intersection 
			id = Integer.parseInt(intersectionElement.getAttribute("id"));
			if (id < 0)
			{
				ZoneGeo.message = "Fichier incorrect, Identifiant Intersection Negatif"; 
				logger.log(Level.SEVERE,ZoneGeo.message);
				return AbstractModel.PARSER_ERROR_INTERSECTION;
			}
			x = Integer.parseInt(intersectionElement.getAttribute("x"));
			if (x < 0)
			{
				ZoneGeo.message = "Fichier incorrect, Abscisse Intersection Negatif"; 
				logger.log(Level.SEVERE,ZoneGeo.message);
				return AbstractModel.PARSER_ERROR_INTERSECTION;
			}
	    	y = Integer.parseInt(intersectionElement.getAttribute("y"));
	    	if (y < 0)
			{
	    		ZoneGeo.message = "Fichier incorrect, Ordonnee Intersection Negative"; 
				logger.log(Level.SEVERE,ZoneGeo.message);
				return AbstractModel.PARSER_ERROR_INTERSECTION;
			}
	    	
	    	NodeList tronconsSortants = intersectionElement.getElementsByTagName(tag);
	    	
	    	// TODO Verifier si ce test est nécessaire 
	    	if (tronconsSortants.getLength() < 0) {
	    		return AbstractModel.PARSER_ERROR_INTERSECTION;
	    	}
	    	
	    	tronconSortant = new ArrayList<Troncon>(); 
	    	
	    	for (int i=0 ; i<tronconsSortants.getLength() ; i++)
	    	{
	    		Element tronconElement = (Element) tronconsSortants.item(i);
	    		Troncon troncon = new Troncon();
	    		troncon.setDepart(this); 
	    		
	    		int idDestination = 0; 
	    		
	    		idDestination = troncon.genererTroncon(tronconElement);
	    		
	    		if ( idDestination == -1)
	    		{
	    			return AbstractModel.PARSER_ERROR_INTERSECTION;	    			
	    		}
			
	    		tronconSortant.add(troncon);
	    		
	    		tronconIdDestination.put(troncon, idDestination); 
	    	}
		}
		catch(Exception ex)
		{
			ZoneGeo.message = ex.getMessage();
			logger.log(Level.SEVERE,ZoneGeo.message);
			return AbstractModel.PARSER_ERROR_INTERSECTION; 
		}

		return AbstractModel.OK;
	}
	
}

