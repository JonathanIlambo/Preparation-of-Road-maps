/***********************************************************************
 * Module:  FeuilleDeRoute.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package model;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import tsp.*;

public class FeuilleDeRoute extends AbstractModel {

   private ZoneGeo zoneGeo;

 private ArrayList<PlageHoraire> plagesHoraires; // { ordered }
   
   private ArrayList<Chemin> itineraireTSP;
   private ArrayList<Chemin> tousChemins;
   private LivraisonsGraph graph;
   private int iteratorLivraisonID = 0;
   private int[][] graph_cost;
   private TSP tsp;
   private int[] ordreTSP;
   private final int TIME = 100000; // limite a la resolution TSP
   private final long TEMPS_ARRET = 15*60000; // 15 min pour décharger livraison
   
   private static File xmlPlagesHoraires; 
   private static Document document;
   static private Logger logger = Logger.getLogger("logger");
   
   public FeuilleDeRoute(File xml) 
   {
	   tousChemins = new ArrayList<Chemin>();
	   itineraireTSP = new ArrayList<Chemin>();
	   plagesHoraires = new ArrayList<PlageHoraire>();
	   tsp = new TSP();
	   FeuilleDeRoute.xmlPlagesHoraires = xml;
	   genererPlagesHoraires(); 
	   this.zoneGeo = new ZoneGeo();
   }

/**
 * Ouvre le fichier XML decrivant les differentes plages horaires
 * @return TRUE si le fichier a ete ouvert correctement,<br> FALSE sinon
 */
   public static boolean ouvertureFichier()
   {
		try
		{
			// creation d'une fabrique de documents
			DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
			
			// creation d'un constructeur de documents
			DocumentBuilder constructeur = fabrique.newDocumentBuilder();

			document = constructeur.parse(xmlPlagesHoraires);
					
		}
		catch(ParserConfigurationException pce){
			ZoneGeo.message = "Erreur de configuration du parseur DOM"; 
			logger.log(Level.SEVERE,ZoneGeo.message);
			logger.log(Level.SEVERE,pce.getMessage());
			return false; 
			
		}
		catch(SAXException se){
			ZoneGeo.message = "Erreur lors du parsing du document"; 
			logger.log(Level.SEVERE,ZoneGeo.message);
			logger.log(Level.SEVERE,se.getMessage()); 
			return false; 
		
		}
		catch(IOException ioe){
			ZoneGeo.message = "Erreur d'entree/sortie"; 
			logger.log(Level.SEVERE,ZoneGeo.message);
			logger.log(Level.SEVERE,ioe.getMessage());
			return false; 
		}		
		return true ; 
	}


   public int creerZone(File fic) {
	   this.zoneGeo = new ZoneGeo();
	   zoneGeo.setXml(fic); 
	   return zoneGeo.genererZoneGeo();
	}
   
   
   /**
    * Genere les plages horaires a partir du fichier XML les decrivant
    * @return OK si la generation s'est deroulee correctement,<br> ERR sinon
    */
   public int genererPlagesHoraires ()
   {
	   logger.log(Level.INFO,"DEBUT DU CHARGEMENT DES PLAGES HORAIRES");
	   if (ouvertureFichier() == true)
	   {
		   
		   String tag = "Reseau"; 
		   try
		   {
			    // Tous les tags du fichier xml
		        NodeList elements = document.getElementsByTagName("*");
		        
		        for (int i=0 ; i < elements.getLength() ; i++)
		        {
		        	if ( (elements.item(i).getNodeName().equals("Reseau")) || (elements.item(i).getNodeName().equals("plageshoraires")) || (elements.item(i).getNodeName().equals("plagehoraire")))
		        	{		        		 
		        	}
		        	else
		        	{
		        		ZoneGeo.message = "Fichier incorrect, Certains tags sont non valides ou manquants"; 
		        		logger.log(Level.SEVERE,ZoneGeo.message);
		        		return AbstractModel.ERR;
		        	}
		        }
			   // Recuperation de la racine 
			   Element racine = document.getDocumentElement();
			   
			   if (racine.getNodeName().equals(tag)) 
			   {
				   tag="plageshoraires";
				   
		 		   NodeList listPlageHoraire = racine.getElementsByTagName(tag);
		 		   
		 		   if ( listPlageHoraire.getLength() != 1)
		 		   {		 			   
		 			   ZoneGeo.message = "Manque Balise <plagesHoraires> ou plusieurs balises <plagesHoraires>"; 
 					   logger.log(Level.SEVERE,ZoneGeo.message);
 					   return AbstractModel.ERR; 
		 		   }
		 		   
		 		   tag="plagehoraire";
		 		   listPlageHoraire = racine.getElementsByTagName(tag);
		 		   
		 		   //plagesHoraires = new ArrayList<PlageHoraire>(); 
		 			
				   for (int i=0 ; i <listPlageHoraire.getLength() ; i++)
				   {
						Element plageHoraireElement = (Element) listPlageHoraire.item(i);
						
						PlageHoraire plageHoraire = new PlageHoraire();
						plageHoraire.setId(i+1);
						
						if (plageHoraire.genererPlage(plageHoraireElement) != AbstractModel.OK){
							ZoneGeo.message = "Probleme a la creation d une Plage Horaire"; 
							logger.log(Level.SEVERE,ZoneGeo.message);
			                return AbstractModel.ERR;
			            }	
						
						// TEST
						if (plagesHoraires.isEmpty() == false && plagesHoraires.size() >1)
						{
							PlageHoraire tmpPlage = plagesHoraires.get(plagesHoraires.size()-1);
							
						    if (tmpPlage.getHeureFin().compareTo(plageHoraire.getHeureDebut()) > 0)
						    {
						    	ZoneGeo.message = "Probleme avec les dates de plage horaire"; 
								logger.log(Level.SEVERE,ZoneGeo.message);
								return AbstractModel.ERR;						    	
						    }
						}						
						plagesHoraires.add(plageHoraire);						
					}
					logger.log(Level.INFO,"PLAGES HORAIRES CREEES AVEC SUCCES");
			   }
			   else
			   {
				    ZoneGeo.message = "PROBLEME AU NIVEAU DE LA RACINE"; 
					logger.log(Level.SEVERE,ZoneGeo.message);
					return AbstractModel.ERR;
			   }				
			   return AbstractModel.OK;
			   
		   }
		   catch(Exception ex)
		   {
			   return AbstractModel.ERR; 
		   }
	   }
	   else
	   {
		   return AbstractModel.ERR;
	   }
		 
	}

 
   public int genererFeuille()
   {  
	  boolean noLivraison = true;
	  for(int i=0; i<plagesHoraires.size(); i++){
		  if(!plagesHoraires.get(i).getPoints().isEmpty()){
			  noLivraison = false;
			  break;
		  }
	  }
	  if(noLivraison){ return AbstractModel.ERR; }
	  
	  // ----------------------------------------------------------------------
	  // 		INSERTION DE L'ENTREPOT
	  // ----------------------------------------------------------------------
      Date heureFinEntrepot;
	  Date heureDebEntrepot;
	  
	  // Ajout plage horaire antï¿½rieure aux autres = dï¿½part Entrepot
	  heureFinEntrepot = plagesHoraires.get(0).getHeureDebut();
	  Calendar cal = Calendar.getInstance();
	  cal.setTime(heureFinEntrepot);
	  cal.add(Calendar.HOUR_OF_DAY, -1);
	  heureDebEntrepot = cal.getTime();
	  PlageHoraire debut = new PlageHoraire(heureDebEntrepot, heureFinEntrepot);
	  debut.addPointItineraire((PointItineraire)zoneGeo.getEntrepot());
	  plagesHoraires.add(0, debut);
	  
	  // Ajout plage horaire postï¿½rieure aux autres = retour Entrepot
	  heureDebEntrepot = plagesHoraires.get(0).getHeureFin();
	  cal.setTime(heureDebEntrepot);
	  cal.add(Calendar.HOUR_OF_DAY, 1);
	  heureFinEntrepot = cal.getTime();
	  PlageHoraire fin = new PlageHoraire(heureDebEntrepot, heureFinEntrepot);
	  fin.addPointItineraire((PointItineraire)zoneGeo.getEntrepot());
	  plagesHoraires.add(fin);
	  
	  
	  // ------------------------------------------------------------------------
	  //		AFFECTATION DES ID AUX POINTS D'ITINERAIRE
	  //		+ INITIALISATION graphe: 	- liste successeurs
	  //									- couts
	  // ------------------------------------------------------------------------
	  ArrayList<ArrayList<Integer>> succ = new ArrayList<ArrayList<Integer>>();
	  ArrayList<PointItineraire> points;
	  int nbMax_succ = 0;
	  for(int i=0; i<plagesHoraires.size()-1; i++)
	  {
		  points = plagesHoraires.get(i).getPoints();
		  nbMax_succ += points.size();
		  
		  for(int j=0; j<points.size(); j++)
		  {
			  points.get(j).setId(iteratorLivraisonID++);
			  succ.add(new ArrayList<Integer>());
		  }
	  }
	  graph_cost = new int[succ.size()][nbMax_succ*2];
	  
	  
	  // ------------------------------------------------------------------------
	  // 		CALCUL DES CHEMINS
	  // ------------------------------------------------------------------------
	  ArrayList<ArrayList<ArrayList<Integer>>> chemins;
	  PlageHoraire plageFocused;
	  PlageHoraire plageSuivante = null;
	  PointItineraire pointFocused;
	  ArrayList<PointItineraire> pointsDestination = new ArrayList<PointItineraire>();
	  
	  int i=0;
	  while(i<plagesHoraires.size()-1)
	  {
		while(plagesHoraires.get(i).getPoints().isEmpty()) {i++;}
		plageFocused = plagesHoraires.get(i++);
		while(plagesHoraires.get(i).getPoints().isEmpty()) {i++;}
		plageSuivante = plagesHoraires.get(i);
		
		for(int j=0; j<plageFocused.getPoints().size(); j++)
		{
			pointFocused = plageFocused.getPoints().get(j);
			
			// destinations internes ï¿½ la plage (privï¿½ du point actuel)
			pointsDestination.clear();
			pointsDestination.addAll(plageFocused.getPoints());
			pointsDestination.remove(j);
			
			// destinations externes ï¿½ la plage ( = vers la plage suivante)
			pointsDestination.addAll(plageSuivante.getPoints());

			// Liste des successeurs pour graphe TSP
			for(int k=0; k<pointsDestination.size(); k++)
			{
				succ.get(pointFocused.getId()).add(pointsDestination.get(k).getId());
			}
			
			chemins = pointFocused.calculerPlusCourtCheminVers(zoneGeo.getIntersections(), pointsDestination, zoneGeo.getDistances());
			
			for(int k=0; k<chemins.size(); k++)
			{
				int dureeSecondes = chemins.get(k).get(1).get(0);
				graph_cost[pointFocused.getId()][pointsDestination.get(k).getId()] = dureeSecondes;
				ArrayList<Integer> listeIntersections = chemins.get(k).get(0);
				
				ArrayList<Section> troncons = new ArrayList<Section>();
				for(int l=0; l<listeIntersections.size()-1; l++)
				{
					troncons.add(zoneGeo.getTroncon(listeIntersections.get(l), listeIntersections.get(l+1)));
				}
				
				PointItineraire arrivee = plageFocused.matchPtItineraire_Livraison(listeIntersections.get(listeIntersections.size()-1));
				if(arrivee == null){
					arrivee = plageSuivante.matchPtItineraire_Livraison(listeIntersections.get(listeIntersections.size()-1));
				}
				
				tousChemins.add(new Chemin(pointFocused, arrivee, troncons, dureeSecondes));
			}
		}
	  }
		
	  
		// -----------------------------------------------------------------------
		// 			RESOLUTION TSP
		// -----------------------------------------------------------------------
		graph = new LivraisonsGraph(succ, graph_cost);
		tsp.solve(TIME, graph.getNbVertices()*graph.getMaxArcCost()+1, graph);
		if (tsp.getSolutionState() != SolutionState.INCONSISTENT && tsp.getSolutionState() != SolutionState.NO_SOLUTION_FOUND)
		{
			ordreTSP = tsp.getPos();

			Chemin chemin;
			for(int k=0; k<ordreTSP.length-1; k++)
			{
				chemin = this.getChemin(ordreTSP[k], ordreTSP[k+1]);
				for(int j=0; j<chemin.getTroncons().size(); j++){
					chemin.getTroncons().get(j).setCheminLivraison(true);
				}
				itineraireTSP.add(chemin);
			}

			// + retour entrepot
			chemin = this.getChemin(ordreTSP[ordreTSP.length-1], ordreTSP[0]);
			for(int j=0; j<chemin.getTroncons().size(); j++){
				chemin.getTroncons().get(j).setCheminLivraison(true);
			}
			itineraireTSP.add(chemin);

			this.etablirHoraires();
			return AbstractModel.OK;
		}
		else
		{
			System.out.println("No solution found after 100 seconds...");	
			return AbstractModel.SOLUTION_NOT_FOUND;
		}
		
		
   }
	 

   public void viderZone() {
	   for(int i=0; i<plagesHoraires.size(); i++){
		   plagesHoraires.get(i).getPoints().clear();
	   }
	   
	   if(itineraireTSP.size() > 0){
		   // nettoyage plages horaires fictives "entrepot"
		   plagesHoraires.remove(0);
		   plagesHoraires.remove(plagesHoraires.size()-1);
	   }

	   this.itineraireTSP.clear();
	   this.tousChemins.clear();
	   
	   
	   for(int i=0; i<zoneGeo.getIntersections().size(); i++){
		   zoneGeo.getIntersections().get(i).getTronconSortant().clear();
	   }
	   zoneGeo.getIntersections().clear();
	   this.zoneGeo = null;

	   iteratorLivraisonID = 0;
	   tsp = new TSP();
   }


   public boolean intersectionIsLivraison(int idIntersection) {
	   for(int i=0; i<plagesHoraires.size(); i++){
		   ArrayList<PointItineraire> points = plagesHoraires.get(i).getPoints();
		   for(int j=0; j<points.size(); j++){
			   if(points.get(j).getIntersection().getId() == idIntersection){
				   return true;
			   }
		   }
	   }
	   return false;
   }


   public boolean intersectionIsEntrepot(int idIntersection) {
	   return zoneGeo.getEntrepot().getIntersection().getId() == idIntersection;
   }


   public int ajouterLivraisonAvantGen(int idIntersection, int indexPlageH) {
	   for(int i=0; i<zoneGeo.getIntersections().size(); i++){
		   if(zoneGeo.getIntersections().get(i).getId() == idIntersection){
			   return plagesHoraires.get(indexPlageH).ajouterLivraison(zoneGeo.getIntersections().get(i));
		   }
	   }
	   return AbstractModel.ERR;

   }


   public int supprimerLivraisonAvantGen(int idIntersection) {
	   for (int i = 0 ; i < this.plagesHoraires.size() ; i++) 
	   {
		   for (int j = 0; j < this.plagesHoraires.get(i).getPoints().size() ; j++)
		   {
			   if (this.plagesHoraires.get(i).getPoints().get(j).getIntersection().getId() == idIntersection) 
			   {
				   return this.plagesHoraires.get(i).supprimerLivraison(idIntersection);  
			   }			   
		   }		   
	   }
	   return AbstractModel.ERR;

   }


   public int ajouterLivraisonApresGen(int idIntersectionLivraison, int idIntersectionLivraisonPrecedente,
		   int indexPlageH) {

	   // decalage indices P.H. car ajout d'une plage horaire "0" (pour le depart entrepot) lors de la gï¿½nï¿½ration FdR
	   indexPlageH++;
	   if(indexPlageH >= plagesHoraires.size()-1 || indexPlageH <= 0 ) { return AbstractModel.ERR; }

	   ajouterLivraisonAvantGen(idIntersectionLivraison, indexPlageH);

	   PointItineraire liv = plagesHoraires.get(indexPlageH).getPoints().get(plagesHoraires.get(indexPlageH).getPoints().size()-1);
	   liv.setId(iteratorLivraisonID++);
	   PointItineraire livPrec = null;
	   for(int i=0; i<plagesHoraires.size(); i++){
		   for(int j=0; j<plagesHoraires.get(i).getPoints().size(); j++)
		   {
			   if(plagesHoraires.get(i).getPoints().get(j).getIntersection().getId() == idIntersectionLivraisonPrecedente){
				   livPrec = plagesHoraires.get(i).getPoints().get(j);
				   break;
			   }
		   }
	   }
	   if(livPrec == null) { return AbstractModel.ERR; }

	   int indexCheminToAdd = 0;
	   PointItineraire livNext = null;
	   for(int i=0; i<itineraireTSP.size(); i++)
	   {
		   if(itineraireTSP.get(i).getDepart().getId() == livPrec.getId())
		   {
			   indexCheminToAdd = i;
			   Chemin cheminToRemove = itineraireTSP.get(i);
			   livNext = cheminToRemove.getDestination();
			   for(int j=0; j<cheminToRemove.getTroncons().size(); j++){
				   cheminToRemove.getTroncons().get(j).setCheminLivraison(false);
			   }
			   itineraireTSP.remove(i);
			   break;
		   }
	   }
	   
	   Chemin livPrecToLiv = creerChemin(livPrec, liv);
	   for(int j=0; j<livPrecToLiv.getTroncons().size(); j++){
		   livPrecToLiv.getTroncons().get(j).setCheminLivraison(true);
	   }  

	   Chemin livToLivNext = creerChemin(liv, livNext);
	   for(int j=0; j<livToLivNext.getTroncons().size(); j++){
		   livToLivNext.getTroncons().get(j).setCheminLivraison(true);
	   }

	   itineraireTSP.add(indexCheminToAdd++, livPrecToLiv);
	   itineraireTSP.add(indexCheminToAdd, livToLivNext);

	   this.etablirHoraires();

	   return AbstractModel.OK;
   }


   public int supprimerLivraisonApresGen(int idIntersectionLivraison) {
	   int idLivraison = -1;
	   for(int i=0; i<plagesHoraires.size(); i++)
	   {
		   for(int j=0; j<plagesHoraires.get(i).getPoints().size(); j++)
		   {
			   if(plagesHoraires.get(i).getPoints().get(j).getIntersection().getId() == idIntersectionLivraison){
				   idLivraison = plagesHoraires.get(i).getPoints().get(j).getId();
			   }
		   }
	   }
	   if(idLivraison == -1) { return AbstractModel.ERR; }

	   supprimerLivraisonAvantGen(idIntersectionLivraison);

	   int indexCheminToAdd = -1;
	   PointItineraire depart = null;
	   PointItineraire arrivee = null;
	   for(int i=0; i<itineraireTSP.size(); i++)
	   {
		   if(itineraireTSP.get(i).getDestination().getId() == idLivraison)
		   {
			   indexCheminToAdd = i;
			   Chemin cheminToRemove1 = itineraireTSP.get(i);
			   Chemin cheminToRemove2 = itineraireTSP.get(i+1);
			   depart = cheminToRemove1.getDepart();
			   arrivee = cheminToRemove2.getDestination();

			   for(int j=0; j<cheminToRemove1.getTroncons().size(); j++){
				   cheminToRemove1.getTroncons().get(j).setCheminLivraison(false);
			   }
			   for(int j=0; j<cheminToRemove2.getTroncons().size(); j++){
				   cheminToRemove2.getTroncons().get(j).setCheminLivraison(false);
			   }

			   itineraireTSP.remove(i);
			   itineraireTSP.remove(i);
			   break;
		   }
	   }
	   if(indexCheminToAdd == -1) { return AbstractModel.ERR; }

	   Chemin cheminTransversal = creerChemin(depart, arrivee);
	   for(int j=0; j<cheminTransversal.getTroncons().size(); j++){
		   cheminTransversal.getTroncons().get(j).setCheminLivraison(true);
	   }
	   itineraireTSP.add(indexCheminToAdd, cheminTransversal);

	   this.etablirHoraires();

	   return cheminTransversal.getDepart().getIntersection().getId();
   }


   public int nbLivraisons() {
	   int nb = 0;
	   for(int i=0; i<plagesHoraires.size(); i++){
		   nb += plagesHoraires.get(i).getPoints().size();
	   }
	   if(itineraireTSP.size() > 0){
		   nb -= 2; // prise en compte de l'entrepot (depart et retour) si generation deja effectuée
	   }
	   return nb;
   }


   public int getPHLivraison(int idIntersectionLivraison) {
	   for(int i=0; i<plagesHoraires.size(); i++)
	   {
		   ArrayList<PointItineraire> points = plagesHoraires.get(i).getPoints();
		   for(int j=0; j<points.size(); j++)
		   {
			   if(points.get(j).getIntersection().getId() == idIntersectionLivraison)
			   {
				   // prise en compte decalage dans liste plages horaraires (ajout plage "0" pour entrepot)
				   return itineraireTSP.size() > 0 ? i-1: i;
			   }
		   }
	   }
	   return AbstractModel.ERR;
   }


   public ArrayList<Intersection> getIntersections() {
	   return zoneGeo.getIntersections();
   }
   
   
   public ArrayList<Chemin> getItineraire() {
	   return this.itineraireTSP;
   }

   
   public ArrayList<PlageHoraire> getPlagesHoraires() {
	   return plagesHoraires;
   }	
   
   
   public int nbLivraisonsPourPlage(int indexPlageH){
	   if(itineraireTSP.size() > 0){
		   indexPlageH++; // prendre en compte l'ajout de PH "0" (pour entrepot) apres génération
	   }
	   return plagesHoraires.get(indexPlageH).getPoints().size();
   }

   /**
    * 
    * @return Retourne la zone geographique associee a la feuille de route
    */
   public ZoneGeo getZoneGeo() {
		return zoneGeo;
	}

/**
 * Affecte une zone geographique a la feuille de route
 * @param zoneGeo
 */
	public void setZoneGeo(ZoneGeo zoneGeo) {
		this.zoneGeo = zoneGeo;
	}

/**
 * 
 * @return Retourne le fichier XML decrivant les plages horaires
 */
	public static File getXml() {
		return xmlPlagesHoraires;
	}

/**
 * Affecte le fichier XML decrivant les plages horaires a la feuille de route
 * @param xml
 */
	public static void setXml(File xml) {
		FeuilleDeRoute.xmlPlagesHoraires = xml;
	}




   /**
    * Works out the deliveries hours, and checks whether the time slots are respected or not
    */
   private void etablirHoraires()
   {
	   Chemin chemin;

	   // Test si la plage horaire est respectï¿½e
	   int itSurItineraireTSP = 0;
	   long horaireCalcule;
	   long echeancePlageHoraire;
	   long debutPlageHoraire;


	   // heure de dï¿½part entrepot adaptï¿½e pour arriver a l'heure a la 1ere livraison
	   horaireCalcule = plagesHoraires.get(1).getHeureDebut().getTime() - itineraireTSP.get(0).getDuree()*1000 - TEMPS_ARRET;
	   itineraireTSP.get(0).getDepart().setHeurePassage(new Date(horaireCalcule + TEMPS_ARRET));

	   for(int i=1; i<plagesHoraires.size()-1; i++)
	   {
		   echeancePlageHoraire = plagesHoraires.get(i).getHeureFin().getTime();
		   debutPlageHoraire = plagesHoraires.get(i).getHeureDebut().getTime();

		   int nbPointsDansPlage = 0;
		   ArrayList<PointItineraire> points = plagesHoraires.get(i).getPoints();
		   while(nbPointsDansPlage < points.size())
		   {
			   chemin = itineraireTSP.get(itSurItineraireTSP++);
			   horaireCalcule += chemin.getDuree()*1000 + TEMPS_ARRET;
			   if(horaireCalcule < debutPlageHoraire){
				   horaireCalcule = debutPlageHoraire;
			   }
			   chemin.getDestination().setHeurePassage(new Date(horaireCalcule));

			   if(horaireCalcule > echeancePlageHoraire){
				   chemin.getDestination().getIntersection().setRespectPlageHoraire(false);
			   }
			   else{
				   chemin.getDestination().getIntersection().setRespectPlageHoraire(true);
			   }

			   nbPointsDansPlage++;
		   }
	   }

	   // + horaire retour entrepot
	   chemin = itineraireTSP.get(itineraireTSP.size()-1);
	   long horaireRetour = chemin.getDepart().getHeurePassage().getTime();
	   horaireRetour += chemin.getDuree()*1000 + TEMPS_ARRET;
	   ((Entrepot)chemin.getDestination()).setHeureRetour(new Date(horaireRetour));
   }


   /**
    * Creates a <code>Chemin</code> from its ending points
    * @param depart the itinerary point from where the path starts
    * @param arrivee the itinerary point to where the path is heading
    * @return Returns the path created
    */
   private Chemin creerChemin(PointItineraire depart, PointItineraire arrivee){
	   ArrayList<PointItineraire> pointsDestination = new ArrayList<PointItineraire>();
	   pointsDestination.add(arrivee);
	   ArrayList<ArrayList<ArrayList<Integer>>> chemins = depart.calculerPlusCourtCheminVers(zoneGeo.getIntersections(), pointsDestination, zoneGeo.getDistances());

	   int dureeSecondes = chemins.get(0).get(1).get(0);
	   ArrayList<Integer> listeIntersections = chemins.get(0).get(0);

	   ArrayList<Section> troncons = new ArrayList<Section>();
	   for(int l=0; l<listeIntersections.size()-1; l++)
	   {
		   troncons.add(zoneGeo.getTroncon(listeIntersections.get(l), listeIntersections.get(l+1)));
	   }

	   return new Chemin(depart, arrivee, troncons, dureeSecondes);
   }


   /**
    * Gets a <code>Chemin</code> in the list <code>tousChemins</code>
    * @param idDepart the ID of the <code>PointItineraire</code> from where the path starts
    * @param idArrivee the ID of the <code>PointItineraire</code> to where the path is heading
    * @return Returns the <code>Chemin</code> if found in the list,<br>
    * <code>null</code> otherwise
    */
   private Chemin getChemin(int idDepart, int idArrivee)
   {
	   for(int i=0; i<tousChemins.size(); i++)
	   {
		   if(tousChemins.get(i).getDepart().getId() == idDepart && tousChemins.get(i).getDestination().getId() == idArrivee)
		   {
			   return tousChemins.get(i);
		   }
	   }
	   return null;
   }




}