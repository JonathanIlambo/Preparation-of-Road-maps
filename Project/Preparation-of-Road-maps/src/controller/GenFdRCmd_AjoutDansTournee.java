/***********************************************************************
 * Module:  GenFdRCmd_AjoutDansTournee.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package controller;

import model.AbstractModel;

/**
 * Implement the Desing Pattern commands : 
 * Command which adds a delivery in an already calculated tour
 */
public class GenFdRCmd_AjoutDansTournee extends AbstractCommande{

	// Identifier of the intersection where to add the delivery
	private int lidIntersection;
	// Identifier of the corresponding intersection has the delivery after which we have to make the addition
	private int lidLivraisonPrec;
	// Index of the time slot where is planned the added delivery
	private int lindexPH;
	// Conductor of the command(order): the model
	private AbstractModel modele;
	
	/**
	 * Builder of the class: create a new command
	 * @param leModele Conductor of the command: the model which will be affects
	 * @param idIntersection Identifier of the intersection where to add the delivery
	 * @param idLivraisonPrec Identifier of the intersection corresponding to the delivery after which 
	 * 							We have to make the addition
	 * @param indexPH Index of the time slot or is planned the added delivery
	 */
	public GenFdRCmd_AjoutDansTournee(AbstractModel leModele, int idIntersection,
			int idLivraisonPrec, int indexPH) {
		modele=leModele;
		lidIntersection=idIntersection;
		lidLivraisonPrec=idLivraisonPrec;
		lindexPH=indexPH;
	}

	/**
	 * Make the command
	 */
	@Override
	public void Do() {
		modele.ajouterLivraisonApresGen(lidIntersection, lidLivraisonPrec, lindexPH);
	}

	/**
	 * Cancel the command
	 */
	@Override
	public void Undo() {
		modele.supprimerLivraisonApresGen(lidIntersection);
	}

}

