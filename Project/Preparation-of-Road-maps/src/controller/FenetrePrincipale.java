/***********************************************************************
 * Module:  FenetrePrincipale.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package controller;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import model.AbstractModel;
import model.FeuilleDeRoute;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Main window of the application :
 * Sets up the general parameters of the window, the menu as well as the contents of windows.
 */
@SuppressWarnings("serial")
public class FenetrePrincipale extends JFrame{
	// The contents of the window
	private JPanel content;
	// Pathway of the file xml containing time slots. Attention, not managed errors
	protected static final String cheminFicPH="files/plagesHoraires.xml";
	
	/**
	 * Builder of the window: launch the application
	 */
	public FenetrePrincipale() {
		initialisation_fenetre();
		initialisation_menu();
		initialisation_contenu();
		// Display of the window
		this.setVisible(true);
	}
	/**
	 * Initialize the general parameters of the window
	 */
	private void initialisation_fenetre() {
		this.setTitle("Management Road maps");
		this.setSize(800,500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 * Create the menu
	 */
	private void initialisation_menu() {
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu menuMenu = new JMenu("Menu");
		menuBar.add(menuMenu);
		
		JMenuItem menuMenu_Quitter = new JMenuItem("Cancel");
	    menuMenu_Quitter.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent event){
		        System.exit(0);
		      }
		    });
		menuMenu.add(menuMenu_Quitter);

	    }

	/**
	 * Create the contents of the window (a single possible contents
	 * in this prototype)
	 */
	private void initialisation_contenu() {
		File plageH = new File(cheminFicPH);
		AbstractModel unModele=new FeuilleDeRoute(plageH);
	    content = new FenetreGenerationFdR(unModele);
	    this.setContentPane(content);
	    }
}
