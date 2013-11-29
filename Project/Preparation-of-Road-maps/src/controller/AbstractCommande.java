/***********************************************************************
 * Module:  AbstractModel.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package controller;

/**
 * 
 * Implementation of the Design Pattern Commandes, to allow to cancel an action
 */

public abstract class AbstractCommande {
	/**
	 * Make the command
	 */
	public abstract void Do();
	
	/**
	 * Cancel the command
	 */
	public abstract void Undo();
}

