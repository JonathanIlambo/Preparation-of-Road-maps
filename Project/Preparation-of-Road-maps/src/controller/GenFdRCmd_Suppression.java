/***********************************************************************
 * Module:  AbstractModel.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package controller;

import model.AbstractModel;

/**
 * Implement the DP commands : 
 * Command which deletes a delivery in a not calculated tour
 */
public class GenFdRCmd_Suppression extends AbstractCommande{

	// Conductor of the command: the model
	private AbstractModel modele;
	// Identifier of the intersection corresponding to the delivery has to delete
	private int lidLivraison;
	//Index of the time slot in which the delivery was planned
	private int lindexPH;
	
	/**
	 * Builder of the class: constructed a new command
	 * @param leModele Conductor of the command: the model which will be affects
	 * @param idLivraison Identifier of the corresponding intersection has the delivery to be deleted
	 * @param indexPH Index of the time slot in which was planned the deleted delivery
	 */
	public GenFdRCmd_Suppression(AbstractModel leModele, int idLivraison, int indexPH) {
		modele=leModele;
		lidLivraison=idLivraison;
		lindexPH=indexPH;
	}

	/**
	 * Make the command
	 */
	@Override
	public void Do() {
		modele.supprimerLivraisonAvantGen(lidLivraison);
	}

	/**
	 * Cancel the command
	 */
	@Override
	public void Undo() {
		modele.ajouterLivraisonAvantGen(lidLivraison, lindexPH);
	}

}
