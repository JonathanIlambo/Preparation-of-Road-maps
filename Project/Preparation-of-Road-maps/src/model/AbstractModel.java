/***********************************************************************
 * Module:  AbstractModel.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package model;

import java.io.File;
import java.util.ArrayList;

/**
 * Class abstract which defined interfaces it (methods and constants)
 * Between the modele and the controller
 */
public abstract class AbstractModel {
	// ---------- Constants
	/** Return: the method is executee correctly	*/
	public static final int OK = 0;
	/** Return: error (without more precision) during the execution	*/
	public static final int ERR =-1;
	/** Error: no found tour	*/
	public static final int SOLUTION_NOT_FOUND = -2;
	/** Error: file xml of the plan invalidates */
	public static final int PARSER_ERROR_FILE = -3;
	/** Error: file xml of the plan invalidates: incorrect intersection */
	public static final int PARSER_ERROR_INTERSECTION = -4;
	
	/**
	 * To create a set of intersections, of sections, etc. has to leave a plan xml
	 * @param fic File xml corresponding to the plan of the zone
	 * @return OK If everything is good pass, PARSER_ERROR_FILE or PARSER_ERROR_INTERSECTION otherwise
	 */
	public abstract int creerZone(File fic);
	
	/**
	 * Empty completely a geopgraphique zone of all its intersections, deliveries, etc.
	 */
	public abstract void viderZone();
	
	/**
	 * Allows to obtain all the intersections contained in the geographical zone
	 * @return A list of the intersections
	 * @see Intersection
	 */
	public abstract ArrayList<Intersection> getIntersections();
	
	/**
	 * Allows to obtain a list of the available time slots
	 * @return An orderly list of the available time slots
	 * @see PlageHoraire
	 */
	public abstract ArrayList<PlageHoraire> getPlagesHoraires();
	
	/**
	 * Allows to know if a delivery is planned has an intersection
	 * @param idIntersection The identifier of the intersection has to make out a will(to test)
	 * @return TRUE if the intersection corresponds has a delivery / warehouse, < br > FALSE otherwise
	 * @see intersectionIsEntrepot
	 */
	public abstract boolean intersectionIsLivraison(int idIntersection);
	
	/**
	 * Allows to know if an intersection corresponds has the location of the warehouse
	 * @param idIntersection The identifier of the intersection has to make out a will(to test)
	 * @return TRUE if the intersection corresponds has the warehouse, < br > FALSE otherwise
	 */
	public abstract boolean intersectionIsEntrepot(int idIntersection);
	
	/**
	 * Permet d'ajouter une livraison dans une tournee non calculee
	 * @param idIntersection identifiant de l'intersection qui correspond a la livraison
	 * 							que l'on souhaite ajouter
	 * @param indexPlageH index de la plage horaire (dans la liste des plages horaires) dans laquelle
	 * 						la nouvelle livraison est prevue
	 * @return OK si l'operation s'est deroulee correctement,<br> ERR sinon
	 * @see getPlagesHoraires
	 */
	public abstract int ajouterLivraisonAvantGen(int idIntersection, int indexPlageH);
	
	/**
	 * Permet de supprimer une livraison dans une tournee non calculee
	 * @param idIntersection identifiant de l'intersection correspondant a la livraison a supprimer
	 * @return OK si l'operation s'est deroulee correctement,<br> ERR sinon
	 */
	public abstract int supprimerLivraisonAvantGen(int idIntersection);
	
	/**
	 * Permet d'ajouter une livraison dans une tournee deja calculee. L'itineraire est mis a jour en consequence, et les heures de passage sont recalcules.
	 * @param idIntersectionLivraison identifiant de l'intersection qui correspond a la livraison
	 * 									que l'on souhaite ajouter
	 * @param idIntersectionLivraisonPrecedente identifiant de l'intersection qui correspond a la livraison
	 * 									qui doit preceder la livraison ajoutee
	 * @param indexPlageH index de la plage horaire (dans la liste des plages horaires) dans laquelle
	 * 						la nouvelle livraison est prevue
	 * @return OK si l'operation s'est deroulee correctement,<br> ERR sinon
	 * @see getPlagesHoraires
	 */
	public abstract int ajouterLivraisonApresGen(int idIntersectionLivraison, int idIntersectionLivraisonPrecedente, int indexPlageH);
	
	/**
	 * Permet de supprimer une livraison dans une tournee deja calculee. L'itineraire est mis a jour en consequence, et les heures de passage sont recalcules.
	 * @param idIntersectionLivraison identifiant de l'intersection qui correspond a 
	 * 			la livraison a supprimee
	 * @return l'id de l'intersection de la livraison qui précède la livraison supprimée
	 */
	public abstract int supprimerLivraisonApresGen(int idIntersectionLivraison);

	/**
	 * Lance le calcul pour generer l'itineraire de la tournee du livreur et les heures de passage.
	 * @return OK si le calcul s'est termine,
	 * <br>SOLUTION_NOT_FOUND si il n'y a pas de solution, ou si aucune solution n'a ete trouvee dans la limite de temps <code>TIME</code>,<br>
	 * ERR si il n'y a aucune livraison dans l'ensemble des plages horaires
	 */
	public abstract int genererFeuille();
	
	/**
	 * Demande le nombre de livraisons prevues dans l'itineraire en cours
	 * @return nombre de livraisons contenues dans l'ensemble des plages horaires
	 */
	public abstract int nbLivraisons(); 
	
	/**
	 * permet d'obtenir l'index, dans la liste des plages horaires, de la plage horaire d'une livraison
	 * @param idIntersectionLivraison l'id de l'intersection de la livraison
	 * @return l'index de la plage horaire dans la liste des plages horaires
	 */
	public abstract int getPHLivraison(int idIntersectionLivraison);
	
	/**
	 * permet d'acceder a l'itineraire calcule, dans son etat courant
	 * @return liste ordonnees des chemins composants la tournee
	 * @see Chemin
	 */
	public abstract ArrayList<Chemin> getItineraire();
	
	/**
	 * Retourne le nombre de livraisons actuellement contenus dans une plage horaire donnée
	 * @param indexPlageH index de la plage horaire dans la liste des plages horaires
	 * @return retourne le nombre de livraisons contenus dans la plage
	 */
	public abstract int nbLivraisonsPourPlage(int indexPlageH);
	
} //end  class AbstractModel
