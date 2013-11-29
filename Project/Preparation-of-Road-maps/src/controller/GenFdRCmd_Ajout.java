/***********************************************************************
 * Module:  GenFdRCmd_Ajout.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package controller;

import model.AbstractModel;

/**
 * Implement the designer pattern commands : 
 * Command which addition a delivery in a not calculated tour
 */
public class GenFdRCmd_Ajout extends AbstractCommande{
	// Identifier of the intersection where has to be add a new delivery
	private int lidIntersection;
	// Index of the time slot in which the new delivery is planned
	private int lindexPH;
	// Conductor of the command: the model
	private AbstractModel modele;
	
	/**
	 * Builder of the class: create a new command
	 * @param leModele Conductor of the command: the model which will be affected
	 * @param idIntersection Identifier of the intersection where must be added a new delivery
	 * @param indexPH Index of the time slot in which the new delivery is planned
	 */
	public GenFdRCmd_Ajout(AbstractModel leModele,int idIntersection, int indexPH) {
		modele=leModele;
		lidIntersection=idIntersection;
		lindexPH=indexPH;
	}

	/**
	 * Make the command
	 */
	@Override
	public void Do(){
		modele.ajouterLivraisonAvantGen(lidIntersection, lindexPH);
	}

	/**
	 * Cancel the command
	 */
	@Override
	public void Undo() {
		modele.supprimerLivraisonAvantGen(lidIntersection);
	}

}
