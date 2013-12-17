/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import model.Intersection;
import model.Troncon;


@SuppressWarnings("serial")
public class PanelPlan extends JPanel {
	private ArrayList<VueIntersection> lesInters;
	private ArrayList<VueTroncon> lesTronconsVue;
	
	/**
	 * Constructeur, initialise les listes
	 */
	public PanelPlan() {
		this.lesInters = new ArrayList<VueIntersection>();
		this.lesTronconsVue = new ArrayList<VueTroncon>();
		this.setBorder(new BevelBorder(0,Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK));
		
		}
	
	/**
	 * Calcul de mise a l'echelle en prenant en compte la taille de la fenetre
	 */
	public void modifEchelle() {
		int w;
		int h;
		double deltaX;
		double deltaY;
		int tailleFenetreX;
		int tailleFenetreY;
		int maxX;
		int minX;
		int maxY;
		int minY;
		int xx;
		int yy;
		int x;
		int y;
		int tailleUtilisableX;
		int tailleUtilisableY;
		
		w=this.getWidth();
		h=this.getHeight();
		tailleFenetreX=w;
		tailleFenetreY=h;
		deltaX = tailleFenetreX * 0.05;
		deltaY = tailleFenetreY * 0.05;
		tailleUtilisableX = tailleFenetreX - (int)deltaX;
		tailleUtilisableY = tailleFenetreY - (int)deltaY;
		
		
		//calcul des max et min
		maxX=lesInters.get(0).getAncienX();
		maxY=lesInters.get(0).getAncienY();
		for (VueIntersection intersec : lesInters)
		{
			if (maxX<intersec.getAncienX())
			{
				maxX=intersec.getAncienX();
			}
			
			if (maxY<intersec.getAncienY())
			{
				maxY=intersec.getAncienY();
			}
			
		}
		
		minX=lesInters.get(0).getAncienX();
		minY=lesInters.get(0).getAncienY();
		for (VueIntersection intersec : lesInters)
		{
			if (minX>intersec.getAncienX())
			{
				minX=intersec.getAncienX();
			}
			
			if (minY>intersec.getAncienY())
			{
				minY=intersec.getAncienY();
			}
		}
		
		
		//mise a l'echelle des coordonnees
		int maxMinX=maxX-minX;
		int maxMinY=maxY-minY;
		float coefX =  (float)tailleUtilisableX/maxMinX;
		float coefY =  (float)tailleUtilisableY/maxMinY;
		
		for (VueIntersection uneIntersec : lesInters)
		{
			//mise a l'echelle des coordonnees
			xx=(int) (((uneIntersec.getAncienX()-minX)*coefX));
			yy=(int) (((uneIntersec.getAncienY()-minY)*coefY));
			x=(int) (xx+deltaX/2);
			y=(int) (yy+deltaY/2);
			
			uneIntersec.setX(x);
			uneIntersec.setY(y);
		}
		
	}
	
	/**
	 * Instanciation des objets VueIntersection et vueTroncon
	 * @param Liste d'intersections instanciEes avec le fichier xml
	 */
	public void CreerAllVueIntersecTroncon(ArrayList<Intersection> lesIntersec) {
		int w;
		int h;
		double deltaX;
		double deltaY;
		int tailleFenetreX;
		int tailleFenetreY;
		int maxX;
		int minX;
		int maxY;
		int minY;
		int xx;
		int yy;
		int x;
		int y;
		int ancienX;
		int ancienY;
		int tailleUtilisableX;
		int tailleUtilisableY;
		VueIntersection VueIntDest;
		VueIntersection VueIntDep;
		int i=0;
		
		ArrayList<Troncon> lesTroncons1 = new ArrayList<Troncon>();
		
		if (lesInters.size()>0)
		{
			lesInters.clear();
		}
		
			w=this.getWidth();
			h=this.getHeight();
			tailleFenetreX=w;
			tailleFenetreY=h;
			deltaX = tailleFenetreX * 0.05;
			deltaY = tailleFenetreY * 0.05;
			tailleUtilisableX = tailleFenetreX - (int)deltaX;
			tailleUtilisableY = tailleFenetreY - (int)deltaY;
			
			
			//calcul des max et min
			maxX=lesIntersec.get(0).getX();
			maxY=lesIntersec.get(0).getY();
			for (Intersection intersec : lesIntersec)
			{
				if (maxX<intersec.getX())
				{
					maxX=intersec.getX();
				}
				
				if (maxY<intersec.getY())
				{
					maxY=intersec.getY();
				}
			}
			
			minX=lesIntersec.get(0).getX();
			minY=lesIntersec.get(0).getY();
			for (Intersection intersec : lesIntersec)
			{
				if (minX>intersec.getX())
				{
					minX=intersec.getX();
				}
				
				if (minY>intersec.getY())
				{
					minY=intersec.getY();
				}
			}
			
			
			//mise a l'echelle des coordonnees
			int maxMinX=maxX-minX;
			int maxMinY=maxY-minY;
			float coefX =  (float)tailleUtilisableX/maxMinX;
			float coefY =  (float)tailleUtilisableY/maxMinY;
		
		
		//Instancie les VueIntersections
		for (Intersection uneIntersec : lesIntersec)
		{
			//mise a l'echelle des coordonnees
			xx=(int) (((uneIntersec.getX()-minX)*coefX));
			yy=(int) (((uneIntersec.getY()-minY)*coefY));
			x=(int) (xx+deltaX/2);
			y=(int) (yy+deltaY/2);
			ancienX=uneIntersec.getX();
			ancienY=uneIntersec.getY();
			
			VueIntersection uneInter = new VueIntersection(uneIntersec.getId(), x, y,ancienX, ancienY, uneIntersec.getEtat(), uneIntersec.getRespectPlageHoraire());
			uneIntersec.addObserver(uneInter);
			lesInters.add(uneInter);
		}
		
		//Instancie les vueTroncon et ajoute a la vueIntersection
		for (Intersection uneInt : lesIntersec)
		{
			lesTroncons1=uneInt.getTronconSortant();
			for (Troncon tr : lesTroncons1)
			{
				VueIntDest = lesInters.get(tr.getDestination().getId());
				VueIntDep = lesInters.get(uneInt.getId());

				VueTroncon unTroncon = new VueTroncon(VueIntDest, VueIntDep,false);
				tr.addObserver(unTroncon);
				lesInters.get(i).addLesTroncons(unTroncon);
				
			}
			i=i+1;
			
		}
		
		
		repaint();	
		
	}
	
	/**
	 * RafraIchissement du plan de la vue
	 */
	public void refresh() {
		repaint();
	}
	
	/**
	 * Recuperation de l'id de l'intersection cliquee
	 * @param x correspondant aux coordonnees du clic
	 * @param y correspondant aux coordonnees du clic
	 * @return retourne l'identifiant de l'intersection cliquee
	 */
	public int EstClique(int x, int y) {
		for (VueIntersection uneVueIntersec : lesInters)
		{
				if (x>((VueIntersection) uneVueIntersec).getX()-5 && x<((VueIntersection) uneVueIntersec).getX()+5)
				{
					if (y>((VueIntersection) uneVueIntersec).getY()-5 && y<((VueIntersection) uneVueIntersec).getY()+5)
					{
						return ((VueIntersection) uneVueIntersec).getId();
						
					}
				}
		}
		//On retourne -1 si personne n'est selectionnee
		return -1;
	}
	
	
	/**
	 * Redefinition de la methode paintComponent
	 *  qui redessine le JPanel a chaque modification
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		int x;
		int y;
		int x2;
		int y2;
		
		g2.setColor(new Color(255,255,240));
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
	
		if (this.lesInters.size()>0)
		{

			modifEchelle();
			
			//Affichage des Troncons
			g2.setStroke(new BasicStroke(2));
			for (VueIntersection intersec : lesInters)
			{
					x=intersec.getX();
					y=intersec.getY();
					lesTronconsVue=intersec.getLesTroncons();
					for (VueTroncon tr : lesTronconsVue)
					{
						g2.setColor(Color.black);
						if (tr.getCheminLivraison()==false)
						{
							x2=tr.getDestination().getX();
							y2=tr.getDestination().getY();
						
							g2.drawLine(x,y,x2,y2);
						}
						
					}
			}
			
			for (VueIntersection intersec : lesInters)
			{
					x=intersec.getX();
					y=intersec.getY();
					lesTronconsVue=intersec.getLesTroncons();
					for (VueTroncon tr : lesTronconsVue)
					{
						if (tr.getCheminLivraison()==true)
						{
							g2.setColor(Color.green);
							g2.setStroke(new BasicStroke(3));
							x2=tr.getDestination().getX();
							y2=tr.getDestination().getY();
							g2.drawLine(x,y,x2,y2);
						}
						
					}
			}
			
			
			//Affichage des Intersections
			for (VueIntersection intersec : lesInters)
			{
				if (intersec.getEtat()==-1)
				{
					g2.setColor(Color.black);
					g2.fillOval(intersec.getX()-5, intersec.getY()-5, 10, 10);
				}
				else if (intersec.getEtat()==0)
				{
					g2.setColor(Color.blue);
					g2.fillRect(intersec.getX()-6, intersec.getY()-6, 12, 12);
				}
				else if (intersec.getEtat()==1)
				{
					if (intersec.getRespectPlageHoraire()==false)
					{
						g2.setColor(Color.red);
						g2.fillRect(intersec.getX()-6, intersec.getY()-6, 12, 12);
						
					}
					g2.setColor(Color.magenta);
					g2.fillOval(intersec.getX()-5, intersec.getY()-5, 10, 10);
				}
				else if (intersec.getEtat()==2)
				{
					if (intersec.getRespectPlageHoraire()==false)
					{
						g2.setColor(Color.red);
						g2.fillRect(intersec.getX()-6, intersec.getY()-6, 12, 12);
						
					}
					g2.setColor(Color.cyan);
					g2.fillOval(intersec.getX()-5, intersec.getY()-5, 10, 10);
				}
				else if (intersec.getEtat()==3)
				{
					if (intersec.getRespectPlageHoraire()==false)
					{
						g2.setColor(Color.red);
						g2.fillRect(intersec.getX()-6, intersec.getY()-6, 12, 12);
						
					}
					g2.setColor(Color.gray);
					g2.fillOval(intersec.getX()-5, intersec.getY()-5, 10, 10);
				}
				else if (intersec.getEtat()==4)
				{
					if (intersec.getRespectPlageHoraire()==false)
					{
						g2.setColor(Color.red);
						g2.fillRect(intersec.getX()-6, intersec.getY()-6, 12, 12);
						
					}
					g2.setColor(Color.pink);
					g2.fillOval(intersec.getX()-5, intersec.getY()-5, 10, 10);
				}
				else if (intersec.getEtat()==5)
				{
					if (intersec.getRespectPlageHoraire()==false)
					{
						g2.setColor(Color.red);
						g2.fillRect(intersec.getX()-6, intersec.getY()-6, 12, 12);
						
					}
					g2.setColor(Color.yellow);
					g2.fillOval(intersec.getX()-5, intersec.getY()-5, 10, 10);
				}
				else if (intersec.getEtat()>5)
				{
					if (intersec.getRespectPlageHoraire()==false)
					{
						g2.setColor(Color.red);
						g2.fillRect(intersec.getX()-6, intersec.getY()-6, 12, 12);
						
					}
					g2.setColor(Color.magenta);
					g2.fillOval(intersec.getX()-5, intersec.getY()-5, 10, 10);
				}
				
			}
			
		}
		
	}
	
}

