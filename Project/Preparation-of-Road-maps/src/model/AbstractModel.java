/***********************************************************************
 * Module:  AbstractModel.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package model;

import java.io.File;
import java.util.ArrayList;

/**
 * Class abstract which defined interfaces it (methods and constants)
 * Between the model and the controller
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
	 * @return TRUE if the intersection corresponds has a delivery/warehouse, <br> FALSE otherwise
	 * @see intersectionIsEntrepot
	 */
	public abstract boolean intersectionIsLivraison(int idIntersection);
	
	/**
	 * Allows to know if an intersection corresponds has the location of the warehouse
	 * @param idIntersection The identifier of the intersection has to make out a will(to test)
	 * @return TRUE if the intersection corresponds has the warehouse, <br> FALSE otherwise
	 */
	public abstract boolean intersectionIsEntrepot(int idIntersection);
	
	/**
	 * Allows to add a delivery in a not calculated tour
	 * @param idIntersection Identifier of the intersection which corresponds has the delivery
	 * 							That we wish to add
	 * @param indexPlageH Index of the time slot (in the list of time slots) in which
	 * 						The new delivery is planned
	 * @return OK If the operation took place correctly, <br> ERR otherwise
	 * @see getPlagesHoraires
	 */
	public abstract int ajouterLivraisonAvantGen(int idIntersection, int indexPlageH);
	
	/**
	 * Allows to delete a delivery in a tour not calculee
	 * @param idIntersection Identifier of the corresponding intersection has the delivery has to delete
	 * @return OK If the operation took place correctly, <br> ERR otherwise
	 */
	public abstract int supprimerLivraisonAvantGen(int idIntersection);
	
	/**
	 * Allows to add a delivery in an already calculated tour. The route is put has in the daytime as a consequence(accordingly), and passing times are recalculated.
	 * @param idIntersectionLivraison Identifier of the intersection which corresponds has the delivery
	 * 									That we wish to add
	 * @param idIntersectionLivraisonPrecedente Identifier of the intersection which corresponds has the delivery
	 * 									Which has to precede the added delivery
	 * @param indexPlageH Index of the time slot (in the list of time slots) in which
	 * 						The new delivery is planned
	 * @return OK If the operation is deroulee correctly, <br> ERR otherwise
	 * @see getPlagesHoraires
	 */
	public abstract int ajouterLivraisonApresGen(int idIntersectionLivraison, int idIntersectionLivraisonPrecedente, int indexPlageH);
	
	/**
	 * Allows to delete a delivery in an already calculated tour. The route is put has in the daytime as a consequence, and passing times are recalculate.
	 * @param idIntersectionLivraison Identifier of the intersection which corresponds in 
	 * 			The delivery deleted
	 * @return The id Of the intersection of the delivery which precedes the deleted delivery
	 */
	public abstract int supprimerLivraisonApresGen(int idIntersectionLivraison);

	/**
	 * Launch the calculation to generate the route of the tour of the deliverer and the passing times.
	 * @return OK If the calculation is end,
	 * <br>SOLUTION_NOT_FOUND If there is no solution, or if no solution was trouvee within the limits of time <code TIME> < / code >, < br >
	 * ERR If there is no delivery in the whole of time slots
	 */
	public abstract int genererFeuille();
	
	/**
	 * Ask for the number of deliveries planned in the current(in class) route
	 * @return Number of deliveries contained in the whole of time slots
	 */
	public abstract int nbLivraisons(); 
	
	/**
	 * Allows to obtain the index, in the list of time slots, time slot of a delivery
	 * @param idIntersectionLivraison Id of the intersection of the delivery
	 * @return The index of the time slot in the list of time slots
	 */
	public abstract int getPHLivraison(int idIntersectionLivraison);
	
	/**
	 * Allows to reach has the route calculate, in it current state
	 * @return List ordered by the component pathways the tour
	 * @see Chemin
	 */
	public abstract ArrayList<Chemin> getItineraire();
	
	/**
	 * Return the number of deliveries at present contained in a given time slot
	 * @param indexPlageH Index of the time slot in the list of time slots
	 * @return the number of deliveries contents to the beach(range)
	 */
	public abstract int nbLivraisonsPourPlage(int indexPlageH);
	
} //end  class AbstractModel
