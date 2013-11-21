/***********************************************************************
 * Module:  PlageHoraire.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Element;

public class PlageHoraire {
	private int id;
   private Date heureDebut;
   private Date heureFin;
   private ArrayList<PointItineraire> points;
   
   static private Logger logger = Logger.getLogger("logger");  
   
   public PlageHoraire() {
	   this.points = new ArrayList<PointItineraire>(); 
	}
   
   public PlageHoraire(Date heureDebut, Date heureFin)
   {
	   this.points = new ArrayList<PointItineraire>(); 
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
   }
   
   /**
    * Ajoute un point d'itineraire a la plage horaire.
    * @param pt point d'itineraire a ajouter a la plage
    */
   public void addPointItineraire(PointItineraire pt) {
      this.points.add(pt);
   }
   
   
   /**
    * 
    * @return Retourne l'identifiant de la plage horaire
    */
   public int getId() {
	return id;
}

   /**
    * 
    * @param id identifiant de la plage horaire
    */
public void setId(int id) {
	this.id = id;
}

   
   /**
    * 
    * @return Retourne l'heure de commencement de la plage horaire
    */
   public Date getHeureDebut() {
		return heureDebut;
   }
   
   /**
    * 
    * @return Retourne l'heure de terminaison de la plage horaire
    */
   public Date getHeureFin() {
		return heureFin;
   }

   /**
    * 
    * @return Retourne la liste des points d'itineraire contenus dans la plage horaire
    */
	public ArrayList<PointItineraire> getPoints() {
		return points;
	}
	
	/**
	 * Retourne le point d'itineraire localise a l'intersection identifiee par l'id donne.
	 * @param idIntersection identifiant de l'intersection de laquelle on souhaite obtenir le point de livraison
	 * @return Retourne le point d'itineraire si existant, <code>null</code> sinon
	 */
	public PointItineraire matchPtItineraire_Livraison(int idIntersection) {
		for (int i = 0; i < this.points.size() ; i++) {
			if (this.points.get(i).getIntersection().getId() == idIntersection) {
				return this.points.get(i);
			}
		}
		
		return null;
	}
	
	/**
	 * Cree et ajoute une livraison a a plage horaire, associee a une intersection.
	 * @param ptIntersection identifiant de l'intersection ou doit etre localisee la livraison
	 * @return OK
	 */
	public Integer ajouterLivraison(Intersection ptIntersection) {
		Livraison nouvelleLivraison = new Livraison(this, ptIntersection);
		this.points.add(nouvelleLivraison);
		ptIntersection.setEtat(this.id);
		return AbstractModel.OK;
	}
	
	/**
	 * Supprime une livraison associee a l'intersection identifiee par l'id donne
	 * @param idIntersection identifiant de l'intersection ou est localisee la livraison a supprimer
	 * @return OK si la suppression s'est bien deroulee,<br>ERR sinon
	 */
	public Integer supprimerLivraison(int idIntersection) {
		for (int i = 0 ; i < this.points.size() ; i++) {
			if (this.points.get(i).getIntersection().getId() == idIntersection) {
				this.points.get(i).getIntersection().setEtat(-1);
				this.points.remove(i);
				return AbstractModel.OK;
			}
		}		
		return AbstractModel.ERR;
	}
	
	/**
	 * Genere la plage horaire a partir de donnees XML
	 * @param plageHoraireElement
	 * @return OK si la generation s'est deroulee correctement,<br> ERR sinon
	 */
	@SuppressWarnings("deprecation")
	public int genererPlage (Element plageHoraireElement)
	{
		int debutHeure, debutMinute, finHeure, finMinute; 
		ZoneGeo.message = "Debut de la creation d une Plage Horaire";
		logger.log(Level.INFO,ZoneGeo.message); 
		try
		{
			debutHeure = Integer.parseInt(plageHoraireElement.getAttribute("debutHeure"));
			debutMinute = Integer.parseInt(plageHoraireElement.getAttribute("debutMinute"));
			finHeure = Integer.parseInt(plageHoraireElement.getAttribute("finHeure"));
			finMinute = Integer.parseInt(plageHoraireElement.getAttribute("finMinute"));
			if ( debutHeure > finHeure)
			{
				ZoneGeo.message = "Probleme de plage Horaire"; 
				logger.log(Level.SEVERE,ZoneGeo.message); 
				return AbstractModel.ERR;
			}
			else
			{
				if (( debutHeure == finHeure) && (debutMinute > finMinute))
				{
					ZoneGeo.message = "Probleme de plage Horaire"; 
					logger.log(Level.SEVERE,ZoneGeo.message); 
					return AbstractModel.ERR;
				}
			}
			
			heureDebut = new Date();
			heureDebut.setHours(debutHeure); 
			heureDebut.setMinutes(debutMinute); 
			heureFin = new Date();
			heureFin.setHours(finHeure); 
			heureFin.setMinutes(finMinute); 
			
			return AbstractModel.OK;
		}
		catch(Exception ex)
		{
			ZoneGeo.message = ex.getMessage(); 
			logger.log(Level.SEVERE,ZoneGeo.message);
			return AbstractModel.ERR; 
		}
	}
}
