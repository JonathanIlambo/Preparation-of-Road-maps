/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


import model.Intersection;


public class VueIntersection implements Observer {

	private int id;
 	private int x;
    private int y;
    private int ancienX;
    private int ancienY;
    private ArrayList<VueTroncon> lesTronc;
    private int etat;
    private boolean respectPlageHoraire; 
    
    /**
	 * Constructeur de VueIntersection
	 * @param id correspond a l'id de l'intersection
	 * @param x correspond a la coordonnee x dans le dessin
	 * @param y correspond a la coordonnee y dans le dessin
	 * @param ancienX correspond au x d'origine dans le fichier xml
	 * @param ancienY correspond au y d'origine dans le fichier xml
	 * @param etat correspond a l'etat de l'intersection (livraison, entrepot...)
	 * @param respectPH permet de mettre en evidence dans le dessin si une livraison n'est pas dans la plage horaire
	 */
    public VueIntersection (int id, int x, int y, int ancienX, int ancienY, int etat, boolean respectPH) {
	       this.id=id;
	       this.x = x;
	       this.y =y;
	       this.ancienX=ancienX;
	       this.ancienY=ancienY;
	       this.lesTronc= new ArrayList<VueTroncon>();
	       this.etat=etat;
	       this.respectPlageHoraire=respectPH;
	 }
    
    /**
   	 * Constructeur par defaut de VueIntersection
   	 */
    public VueIntersection(){}
    

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getAncienX() {
		return ancienX;
	}

	public int getAncienY() {
		return ancienY;
	}
	

	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public boolean getRespectPlageHoraire() {
		return respectPlageHoraire;
	}

	public void setRespectPlageHoraire(boolean respectPlageHoraire) {
		this.respectPlageHoraire = respectPlageHoraire;
	}
	
	public ArrayList<VueTroncon> getLesTroncons() {
		return lesTronc;
	}

	public void addLesTroncons(VueTroncon unTr) {
		this.lesTronc.add(unTr);
	}
	
	
	 /**
	  * methode update du pattern Observer qui permet de mettre a jour la vue
	  * si l'etat d'une intersection change
	 */
	public void update(Observable obs, Object obj) {
		if (obs instanceof Intersection)
		{
			this.setEtat(((Intersection) obs).getEtat());
			this.setRespectPlageHoraire(((Intersection) obs).getRespectPlageHoraire());
		}
		
	}


}

