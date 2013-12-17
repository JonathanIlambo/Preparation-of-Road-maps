/***********************************************************************
 * Module:  ZoneGeo.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/
package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ZoneGeo {
	
	/** Attribute */  
	private int id; 
	private ArrayList<Intersection> intersections;
	private ArrayList<Troncon> lstTroncons; 
	private Entrepot entrepot;
	private int[][] distances; 
	
	static private Logger logger = Logger.getLogger("logger");
	static public String message="";
    
    private static File xml; 
	private static Document document;
	
	/** List containing all the identifiers of the intersections */
	private ArrayList<Integer> lstDestination; 
	
	
	public ZoneGeo() {
		intersections = new ArrayList<Intersection>();
		lstTroncons = new ArrayList<Troncon>();
		lstDestination = new ArrayList<Integer>(); 
	}

	/**
	 * 
	 * @return The identifier of the geographical zone
	 */
	public int getId() {
		return id;
	}
	/**
	 * Affect an identifier has the geographical zone
	 * @param id identifiant
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 
	 * @return the list of all the intersections contained in the geographical zone
	 */
	public ArrayList<Intersection> getIntersections() {
		return intersections;
	}
	/**
	 * Affect the list of all the intersections contained in the geographical zone
	 * @param intersections list of intersections
	 */
	public void setIntersections(ArrayList<Intersection> intersections) {
		this.intersections = intersections;
	} 
		
	/**
	 * 
	 * @return The warehouse associates has the geographical zone
	 */
	public Entrepot getEntrepot() {
		return entrepot;
	}
	/**
	 * Affect the warehouse associate has the geographical zone
	 * @param entrepot
	 */
	public void setEntrepot(Entrepot entrepot) {
		this.entrepot = entrepot;
	}
	/**
	 * 
	 * @return The board of the distances between the various intersections contained in the geographical zone
	 */
	public int[][] getDistances() {
		return distances;
	}

	/**
	 * Affecte le tableau des distances entre les differentes intersections contenues dans la zone geographique
	 * @param distances
	 */
	public void setDistances(int[][] distances) {
		this.distances = distances;
	}

	/**
	 *
	 * @return Retourne le fichier XML decrivant les differentes intersections et liaisons de la zone geographique
	 */
	public static File getXml() {
		return xml;
	}

	/**
	 * Affecte le fichier XML decrivant les differentes intersection et liaisons de la zone geographique
	 * @param xml fichier XML
	 */
	public void setXml(File xml) {
		ZoneGeo.xml = xml;
	}

	/**
	 * Genere la zone geographique, c'est a dire l'ensemble des intersections et des troncons qui y sont contenus,
	 * a partir des donnees du fichier XML
	 * @return OK si la generation s'est bien deroulee,<br> ERR sinon
	 * 
	 */
	public int genererZoneGeo ()
	{
		if ( ouvertureFichier() == AbstractModel.OK )
		{
			String info =""; 
			Map<Troncon,Integer> tronconIdDestination = new HashMap<Troncon, Integer>();
			
			String tag = "Reseau"; 
			
			info = "Debut de la Generation de la Zone Geographique"; 
			logger.log(Level.INFO,info);
			try
			{
				// Tous les tags du fichier xml
		        NodeList elements = document.getElementsByTagName("*");
		        
		        for (int i=0 ; i < elements.getLength() ; i++)
		        {
		        	if ( (elements.item(i).getNodeName().equals("Reseau")) || (elements.item(i).getNodeName().equals("Entrepot")) || (elements.item(i).getNodeName().equals("Noeuds")) || (elements.item(i).getNodeName().equals("Noeud")) || (elements.item(i).getNodeName().equals("TronconSortant")))
		        	{		        		 
		        	}
		        	else
		        	{
		        		ZoneGeo.message = "Fichier incorrect, Certains tags sont non valides ou manquants"; 
		        		logger.log(Level.SEVERE,ZoneGeo.message);
		        		return AbstractModel.PARSER_ERROR_FILE;
		        	}
		        }
		        
				// Recuperation de la racine 
				Element racine = document.getDocumentElement();

				if (racine.getNodeName().equals(tag)) 
				{
					// Recuperation de l entrêpot
					tag = "Entrepot"; 
					NodeList listeEntrepot = racine.getElementsByTagName(tag);
					
					if (listeEntrepot.getLength() != 1)
					{
						message = "Fichier incorrect, Entrepot non valide"; 
						logger.log(Level.SEVERE,message);
						return AbstractModel.PARSER_ERROR_INTERSECTION;
					}
					
					tag="id"; 
					Element entrepotParse = (Element) listeEntrepot.item(0);
			        int idEntrepot = Integer.parseInt(entrepotParse.getAttribute(tag));
			        
			        entrepot = new Entrepot();
			        entrepot.setId(idEntrepot);
			        info = " ENTREPOT " + entrepot.getId();
			        logger.log(Level.INFO,info);
			            
			        // Recuperation des noeuds 
			        tag="Noeuds"; 
		 			NodeList noeuds = racine.getElementsByTagName(tag);
		 			
		 			if (noeuds.getLength() != 1)
		 			{
		 				message = "Fichier incorrect, Balise Noeuds inexistant";
		 				logger.log(Level.SEVERE,message);
		 				return AbstractModel.PARSER_ERROR_FILE;
		 			}
		 			
		 			tag="Noeud"; 
		 			noeuds = racine.getElementsByTagName(tag);
		 			
					for (int i=0 ; i <noeuds.getLength() ; i++)
					{ 
						Element intersectionElement = (Element) noeuds.item(i);
						
						Intersection intersection = new Intersection();
						
						if (intersection.genererIntersection(intersectionElement,tronconIdDestination)!= AbstractModel.OK)
						{
							message = "Fichier incorrect, Intersection invalide";
							logger.log(Level.SEVERE,message);
							// Suppression de toutes les intersections creees 
							intersections.removeAll(intersections);
							lstDestination.removeAll(lstDestination); 
							
			                return AbstractModel.PARSER_ERROR_INTERSECTION;
			            }
						
						if (intersections.isEmpty() != true)
						{
							Intersection tmpIntersection = intersections.get(intersections.size() -1);
							if ( tmpIntersection.getId() > intersection.getId() )
							{
								message = "Fichier incorrect, Identifiant Intersection non range";
								logger.log(Level.SEVERE,message);
								// Suppression de toutes les intersections creees 
								intersections.removeAll(intersections);
								return AbstractModel.PARSER_ERROR_INTERSECTION;
							}							
						}
						
						lstDestination.add(intersection.getId()); 
						intersections.add(intersection);
					}
					
					// On met l etat de l entrepot a 0 
					int indexEntrepot = lstDestination.indexOf(idEntrepot); 
					
					if ( indexEntrepot == -1)
					{
						intersections.removeAll(intersections);
						lstDestination.removeAll(lstDestination);
						message = "Fichier incorrect, Intersection Entrepot n existe pas";
						logger.log(Level.SEVERE,message);
						return AbstractModel.PARSER_ERROR_INTERSECTION;
					}
					else
					{
						intersections.get(indexEntrepot).setEtat(0);
						entrepot.setIntersection(intersections.get(indexEntrepot));
					}
					
					info = "Intersections Creees"; 
					logger.log(Level.INFO,info);		 			
				}
				else
				{
					message = "Fichier incorrect, Racine invalide"; 
					logger.log(Level.SEVERE,message);
					return AbstractModel.PARSER_ERROR_FILE;	
				}
			}
			catch(Exception ex)
			{
				message = ex.getMessage();
				logger.log(Level.SEVERE,message);
				return AbstractModel.PARSER_ERROR_INTERSECTION;			
			}			 
				
			for (int j=0 ; j<intersections.size() ; j++)
			{
				for (int k=0 ; k < intersections.get(j).getTronconSortant().size() ; k++)
				{
					int destination = -1 ; 
					
					if ( tronconIdDestination.get(intersections.get(j).getTronconSortant().get(k)) != null)
					{	 
						destination = tronconIdDestination.get(intersections.get(j).getTronconSortant().get(k));
						
						if ( lstDestination.contains(destination))
						{
							int index = lstDestination.indexOf(destination); 
							intersections.get(j).getTronconSortant().get(k).setDestination(intersections.get(index));					
						}
						else
						{							
							intersections.removeAll(intersections); 
							lstTroncons.removeAll(lstTroncons); 
							lstDestination.removeAll(lstDestination); 
							message = "Fichier incorrect, destination troncon invalide"; 
							logger.log(Level.SEVERE,message);
							return AbstractModel.PARSER_ERROR_INTERSECTION;		
						}						 							
					}
					
					// Ajout du troncon dans la liste contenant tous les troncons 
					lstTroncons.add(intersections.get(j).getTronconSortant().get(k)); 
				}			
			}
			
			AfficherIntersection(); 
			ZoneGeo.message = "Fichier charge"; 
			logger.log(Level.INFO,ZoneGeo.message);
			
			// Calcule des distances 
			calculerDistance(); 
			
			return AbstractModel.OK;			
		}
		else
		{
			return AbstractModel.PARSER_ERROR_FILE; 
		}		      
	}
	
	public static Document getDocument() {
		return document;
	}

	public static void setDocument(Document document) {
		ZoneGeo.document = document;
	}

	/**
	 * Calcule et remplit le tableau des distances entre les differentes intersections de la zone geographique
	 */
	private void calculerDistance()
	{
		logger.log(Level.INFO,"CALCUL DES DISTANCES");
		// Initialisation du tableau distance
		int taille = getIntersections().size(); 
		distances = new int[taille][taille]; 
		for (int i=0 ; i < taille ; i++)
		{
			for (int j=0 ; j < taille ; j++)
			{
				distances[i][j] = -1 ; 
			}
		}
		
		// Remplissage du tableau de durée 
		for (int i=0 ; i < this.getIntersections().size() ; i++)
		{
			for (int j=0 ; j < this.getIntersections().get(i).getTronconSortant().size() ; j++)
			{
				int index = lstDestination.indexOf(getIntersections().get(i).getTronconSortant().get(j).getDestination().getId()); 
				distances[i][index] = getIntersections().get(i).getTronconSortant().get(j).duration();				
			}
		}
		logger.log(Level.INFO,"Fin Calcul des Distances");
	}
	
	/**
	 * Ouvre le fichier XML decrivant les differentes intersections et liaisons de la zone geographique
	 * @return OK si le fichier a ete ouvert correctement
	 */
	private static int ouvertureFichier()
	{
		try{
			// creation d'une fabrique de documents
			DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
			
			// creation d'un constructeur de documents
			DocumentBuilder constructeur = fabrique.newDocumentBuilder();
			
			if (getXml() != null)
			document = constructeur.parse(getXml());
			
		}
		catch(ParserConfigurationException pce){
			message = pce.getMessage(); 
			logger.log(Level.SEVERE,message);
			return AbstractModel.PARSER_ERROR_FILE; 
			
		}
		catch(SAXException se){
			message = se.getMessage(); 
			logger.log(Level.SEVERE,message);
			return AbstractModel.PARSER_ERROR_FILE; 
		
		}
		catch(IOException ioe){
			message = ioe.getMessage(); 
			logger.log(Level.SEVERE,message);
			return AbstractModel.PARSER_ERROR_FILE; 
		}
		return AbstractModel.OK; 
	}
	
	/**
	 * Methode de DEBUG. Affichage console de l'ensemble des intersections constituant la zone geographique
	 */
	private void AfficherIntersection ()
	{
		for (int i=0 ; i < intersections.size() ; i++)
		{
			logger.log(Level.INFO,"INTERSECTION"); 
			logger.log(Level.INFO,"-----> X : " + intersections.get(i).getX() + " Y : " + intersections.get(i).getY() + " ID : " + intersections.get(i).getId() );
			logger.log(Level.INFO,"-----> TRONCON"); 
			for(int j=0; j < intersections.get(i).getTronconSortant().size(); j++)
			{					
				//logger.log(Level.INFO," nomRue :  " + intersections.get(i).getTronconSortant().get(j).getStreet() + " Destination : "+ intersections.get(i).getTronconSortant().get(j).getDestination().getId() + " VITESSE : " + intersections.get(i).getTronconSortant().get(j).getSpeed()) + " LONGUEUR : " + intersections.get(i).getTronconSortant().get(j).getLength()); 
			}
		}		
	}
	
	public Troncon getTroncon(int depart, int arrivee)
	{
		for(int i=0; i<intersections.size(); i++)
		{
			if(intersections.get(i).getId() == depart)
			{
				ArrayList<Troncon> troncons = intersections.get(i).getTronconSortant();
				for(int j=0; j<troncons.size(); j++){
					if(troncons.get(j).getDestination().getId() == arrivee){
						return troncons.get(j);
					}
				}
			}
		}
		return null;		
	}
}

