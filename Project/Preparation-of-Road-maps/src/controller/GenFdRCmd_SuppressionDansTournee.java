/***********************************************************************
 * Module:  AbstractModel.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package controller;

import model.AbstractModel;

/**
 * Command which deletes a delivery in a tour already calculated tour
 */
public class GenFdRCmd_SuppressionDansTournee extends AbstractCommande{

	// Identifier of the intersection corresponding to the delivery to be deleted
	private int lidLivraison;
	// Identifier of the corresponding intersection has the delivery which precedes the deleted delivery
	private int lidLivraisonPrec;
	// Index of the time slot in which was planned the deleted delivery
	private int lindexPH;
	// Conductor of the order: the modele
	private AbstractModel modele;
	
	/**
	 * Builder of the class: constructed a new command
	 * @param leModele Conductor of the command: the model which will be affects
	 * @param idLivraison Identifier of the delivery to be deleted
	 * @param indexPH Index of the time slot in which was planned the deleted delivery
	 */
	public GenFdRCmd_SuppressionDansTournee(AbstractModel leModele, int idLivraison,
			int indexPH) {
		lidLivraison=idLivraison;
		lindexPH=indexPH;
		modele=leModele;
	}

	/**
	 * Make the command
	 */
	@Override
	public void Do() {
		lidLivraisonPrec=modele.supprimerLivraisonApresGen(lidLivraison);
	}

	/**
	 * Cancel the command
	 */
	@Override
	public void Undo() {
		modele.ajouterLivraisonApresGen(lidLivraison, lidLivraisonPrec, lindexPH);
	}

}
