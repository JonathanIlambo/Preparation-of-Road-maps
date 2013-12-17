/***********************************************************************
 * Module:  FenetreGenerationFdR.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package controller;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import model.AbstractModel;
import model.Chemin;
import model.Entrepot;
import model.PlageHoraire;
import model.Troncon;
import view.PanelPlan;


/**
 * Contents of the window which manage the generation of the Road map
 * Also contains the controller: every new feature added to the application afterward
 * Can have its own controller
 */
@SuppressWarnings("serial")
public class FenetreGenerationFdR extends JPanel implements MouseListener{
	// Entry point of packages modele
	private AbstractModel leModele;
	// Entry point of packages view
	private PanelPlan panelPlan;
	
	/**
	 * The application is managed as a machine in states, where every state defines the possible actions
	 * By the user.
	 * @see FenetreGenerationFdR#MODE_VIDE
	 * @see FenetreGenerationFdR#MODE_CHARGE
	 * @see FenetreGenerationFdR#MODE_GENERE
	 * @see FenetreGenerationFdR#MODE_AJOUT
	 * @see FenetreGenerationFdR#MODE_SUPPRESSION
	 */
	private int state;
	/**	Empty state: any plan loads
	 * @see FenetreGenerationFdR#state*/
	private static final int MODE_VIDE = 0;
	/**	State loads: plan loads, no generated tour
	 * @see FenetreGenerationFdR#state*/
	private static final int MODE_CHARGE = 1; 
	/**	Generated state: generated tour
	 * @see FenetreGenerationFdR#state*/
	private static final int MODE_GENERE = 2;
	/**	State addition: mode addition in a generated tour
	 * @see FenetreGenerationFdR#state*/
	private static final int MODE_AJOUT =3;
	/**	State abolition: mode abolition in a generated tour
	 * @see FenetreGenerationFdR#state*/
	private static final int MODE_SUPPRESSION = 4;
	
	// Variable utilisee dans le mode ajout pour memoriser un precedent clic
	private int memIdIntersection; 
	private static final int AUCUNE = -1;
	
	// Elements de l'interface dont l'etat change en fonction de l'etat de l'application
	/** Combobox qui permet le choix de la zone geo : un seul choix dans le prototype */
	private JComboBox<Integer> comboBoxZone;
	/** Bouton qui permet l'ouverture d'un nouveau plan xml */
	private JButton boutonNordOuvrir; 
	/** Bouton qui permet le lancement du calcul de la tournee */
	private JButton boutonCentreGenerer;
	/** Bouton qui permet l'annulation d'une action */
	private JButton boutonCentreAnnuler;
	/** Bouton qui permet l'activation du mode ajout dans une tournee deja calculee */
	private JButton boutonCentreAjouter;
	/** Bouton qui permet l'activation du mode suppression dans une tournee deja calculee */
	private JButton boutonCentreSupprimer;
	/** Bouton qui permet la generation et l'ouverture de la version pdf de l'itineraire du livreur */
	private JButton boutonCentreVPapier;
	/** Combobox qui permet la selection des plages horaires */
	private JComboBox<String> comboBoxPlageH;
	/** Label qui donne des indications a l'utilisateur */
	private JLabel labelSudIndications;
	
	private final int NBMAXLIVRAISONPARPH = 10; //Nombre maximal de livraison dans une seule plage horaire
	private LinkedList<AbstractCommande> commandes; // Liste des commandes : DP commandes
	
	// -------------------------------------------------------- Initialisation
	
	/**
	 * Constructeur de la classe : creer un contenu qui permet la generation d'une feuille de route
	 * @param unModele le modele gere par cette fenetre, sur lequel seront effectuees 
	 * 													les actions de l'utilisateur
	 */
	public FenetreGenerationFdR(AbstractModel unModele) {
		leModele=unModele;
		commandes= new LinkedList<AbstractCommande>();

		// Code en partie genere automatiquement par le plugin Eclipse Window Builder

		this.setLayout(new BorderLayout(10, 10));

		// Zone Nord : choix de la zone
		JPanel panelNord = new JPanel();
		add(panelNord, BorderLayout.NORTH);
		GridBagLayout gbl_panelNord = new GridBagLayout();
		gbl_panelNord.columnWidths = new int[]{99, 57, 80, 77, 0, 0};
		gbl_panelNord.rowHeights = new int[]{25, 0};
		gbl_panelNord.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelNord.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelNord.setLayout(gbl_panelNord);

		JLabel labelNordZone = new JLabel("Geographical zone n°");
		GridBagConstraints gbc_labelNordZone = new GridBagConstraints();
		gbc_labelNordZone.ipady = 10;
		gbc_labelNordZone.ipadx = 10;
		gbc_labelNordZone.fill = GridBagConstraints.HORIZONTAL;
		gbc_labelNordZone.anchor = GridBagConstraints.WEST;
		gbc_labelNordZone.insets = new Insets(0, 0, 0, 5);
		gbc_labelNordZone.gridx = 1;
		gbc_labelNordZone.gridy = 0;
		panelNord.add(labelNordZone, gbc_labelNordZone);

		comboBoxZone = new JComboBox<Integer>();
		comboBoxZone.addItem(1);
		GridBagConstraints gbc_comboBoxZone = new GridBagConstraints();
		gbc_comboBoxZone.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxZone.insets = new Insets(0, 0, 0, 5);
		gbc_comboBoxZone.gridx = 2;
		gbc_comboBoxZone.gridy = 0;
		panelNord.add(comboBoxZone, gbc_comboBoxZone);
		comboBoxZone.addItemListener(new ComboBoxZone_Listener());

		JLabel labelNordOuvrir = new JLabel("Load a plan XML:");
		GridBagConstraints gbc_labelNordOuvrir = new GridBagConstraints();
		gbc_labelNordOuvrir.fill = GridBagConstraints.HORIZONTAL;
		gbc_labelNordOuvrir.anchor = GridBagConstraints.WEST;
		gbc_labelNordOuvrir.insets = new Insets(0, 0, 0, 5);
		gbc_labelNordOuvrir.gridx = 3;
		gbc_labelNordOuvrir.gridy = 0;
		panelNord.add(labelNordOuvrir, gbc_labelNordOuvrir);

		boutonNordOuvrir = new JButton("Open");
		GridBagConstraints gbc_boutonNordOuvrir = new GridBagConstraints();
		gbc_boutonNordOuvrir.fill = GridBagConstraints.HORIZONTAL;
		gbc_boutonNordOuvrir.anchor = GridBagConstraints.EAST;
		gbc_boutonNordOuvrir.gridx = 4;
		gbc_boutonNordOuvrir.gridy = 0;
		panelNord.add(boutonNordOuvrir, gbc_boutonNordOuvrir);
		boutonNordOuvrir.addActionListener(new BoutonNordOuvrir_Listener());

		/* Zone Sud : label d'indications */
		labelSudIndications = new JLabel();
		add(labelSudIndications, BorderLayout.SOUTH);

		/* Zone centrale : Boutons, plan */
		JPanel panelCentre = new JPanel();
		panelCentre.setBorder(null);
		add(panelCentre, BorderLayout.CENTER);
		GridBagLayout gbl_panelCentre = new GridBagLayout();
		gbl_panelCentre.columnWidths = new int[]{27, 18, 86, 189, 0};
		gbl_panelCentre.rowHeights = new int[]{10, 0, 40, 0, 25, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0};
		gbl_panelCentre.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelCentre.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelCentre.setLayout(gbl_panelCentre);

		JLabel labelCentrePlageH = new JLabel("Selection of the time slot :");
		GridBagConstraints gbc_labelCentrePlageH = new GridBagConstraints();
		gbc_labelCentrePlageH.anchor = GridBagConstraints.WEST;
		gbc_labelCentrePlageH.fill = GridBagConstraints.HORIZONTAL;
		gbc_labelCentrePlageH.insets = new Insets(0, 0, 5, 5);
		gbc_labelCentrePlageH.gridx = 0;
		gbc_labelCentrePlageH.gridy = 3;
		panelCentre.add(labelCentrePlageH, gbc_labelCentrePlageH);

		JLabel labelCentreActions = new JLabel("Actions :");
		GridBagConstraints gbc_labelCentreActions = new GridBagConstraints();
		gbc_labelCentreActions.anchor = GridBagConstraints.NORTHWEST;
		gbc_labelCentreActions.insets = new Insets(0, 0, 5, 5);
		gbc_labelCentreActions.gridx = 2;
		gbc_labelCentreActions.gridy = 3;
		panelCentre.add(labelCentreActions, gbc_labelCentreActions);
		labelCentreActions.setAlignmentY(Component.TOP_ALIGNMENT);

		comboBoxPlageH = new JComboBox<String>();
		GridBagConstraints gbc_comboBoxPlageH = new GridBagConstraints();
		gbc_comboBoxPlageH.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxPlageH.fill = GridBagConstraints.VERTICAL;
		gbc_comboBoxPlageH.gridx = 0;
		gbc_comboBoxPlageH.gridy = 5;
		panelCentre.add(comboBoxPlageH, gbc_comboBoxPlageH);
		init_PlagesHoraires();

		boutonCentreGenerer = new JButton("Generate FdR");
		GridBagConstraints gbc_boutonCentreGenerer = new GridBagConstraints();
		gbc_boutonCentreGenerer.insets = new Insets(0, 0, 5, 5);
		gbc_boutonCentreGenerer.fill = GridBagConstraints.BOTH;
		gbc_boutonCentreGenerer.gridx = 2;
		gbc_boutonCentreGenerer.gridy = 5;
		panelCentre.add(boutonCentreGenerer, gbc_boutonCentreGenerer);
		boutonCentreGenerer.addActionListener(new BoutonCentreGenerer_Listener());

		boutonCentreAjouter = new JButton("Add");
		GridBagConstraints gbc_boutonCentreAjouter = new GridBagConstraints();
		gbc_boutonCentreAjouter.fill = GridBagConstraints.BOTH;
		gbc_boutonCentreAjouter.insets = new Insets(0, 0, 5, 5);
		gbc_boutonCentreAjouter.gridx = 2;
		gbc_boutonCentreAjouter.gridy = 6;
		panelCentre.add(boutonCentreAjouter, gbc_boutonCentreAjouter);
		boutonCentreAjouter.addActionListener(new BoutonCentreAjouter_Listener());
		
		boutonCentreSupprimer = new JButton("Delete");
		GridBagConstraints gbc_boutonCentreSupprimer = new GridBagConstraints();
		gbc_boutonCentreSupprimer.fill = GridBagConstraints.BOTH;
		gbc_boutonCentreSupprimer.insets = new Insets(0, 0, 5, 5);
		gbc_boutonCentreSupprimer.gridx = 2;
		gbc_boutonCentreSupprimer.gridy = 7;
		panelCentre.add(boutonCentreSupprimer, gbc_boutonCentreSupprimer);
		boutonCentreSupprimer.addActionListener(new BoutonCentreSupprimer_Listener());
		
		boutonCentreAnnuler = new JButton("Cancel");
		GridBagConstraints gbc_boutonCentreAnnuler = new GridBagConstraints();
		gbc_boutonCentreAnnuler.fill = GridBagConstraints.BOTH;
		gbc_boutonCentreAnnuler.insets = new Insets(0, 0, 5, 5);
		gbc_boutonCentreAnnuler.gridx = 2;
		gbc_boutonCentreAnnuler.gridy = 9;
		panelCentre.add(boutonCentreAnnuler, gbc_boutonCentreAnnuler);
		boutonCentreAnnuler.addActionListener(new BoutonCentreAnnuler_Listener());

		boutonCentreVPapier = new JButton("Paper version");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 16;
		panelCentre.add(boutonCentreVPapier, gbc_btnNewButton);
		boutonCentreVPapier.addActionListener(new BoutonCentreVPapier_Listener());
		
		// Creation du plan de la zone
		panelPlan = new PanelPlan();
		GridBagConstraints gbc_panelPlan = new GridBagConstraints();
		gbc_panelPlan.weighty = 1.0;
		gbc_panelPlan.weightx = 1.0;
		gbc_panelPlan.gridheight = 14;
		gbc_panelPlan.fill = GridBagConstraints.BOTH;
		gbc_panelPlan.insets = new Insets(10, 10, 10, 10);
		gbc_panelPlan.gridx = 3;
		gbc_panelPlan.gridy = 1;
		panelCentre.add(panelPlan, gbc_panelPlan);

		panelPlan.addMouseListener(this);
		
		// Initialisation de l'etat de l'interface
		activeModeVide();
	}

	/**
	 * Recupere les plages horaires disponoibles et initialise la combobox de selection des plages horaires
	 */
	private void init_PlagesHoraires() {
		ArrayList<PlageHoraire> listePlageH = leModele.getPlagesHoraires();

		if(listePlageH != null){
			SimpleDateFormat formatPH = new SimpleDateFormat("kk:mm");
			for(PlageHoraire ph : listePlageH)
			{
				String horaire=new String();

				horaire+= formatPH.format(ph.getHeureDebut());
				horaire+=" - ";
				horaire+= formatPH.format(ph.getHeureFin());
				comboBoxPlageH.addItem(horaire);
			}		
		}
	}

	// -------------------------------------------------------- Controleur
	
	/**
	 * Annule la derniere action de l'utilisateur et met a jour l'etat de l'interface
	 * @see AbstractCommande
	 */
	private void annuler() {
		//Annulation
		commandes.removeLast().Undo();
		//Actualisation du plan
		panelPlan.refresh();
		//Actualisation de l'interface
		switch(state){
		case MODE_CHARGE :
			activeModeCharge();
			break;
		case MODE_GENERE :
			activeModeGenere();
			break;
		default : // Cas impossible, ne se produit jamais
			break;
		}
	}

	/**
	 * Effectue le traitement approprie lorsqu'une intersection est cliquee sur le plan
	 * @param idIntersection identifiant de l'intersection cliquee
	 * @see AbstractCommande
	 */
	private void traiterClic(int idIntersection){
		AbstractCommande laCommande;
		switch(state){
		case MODE_CHARGE :
			//Si on clique sur l'entrepot : aucune action
			if(leModele.intersectionIsEntrepot(idIntersection)){
				return;
			}
			
			//Determination de l'action a effectuer
			if(leModele.intersectionIsLivraison(idIntersection)){
				//On supprime une livraison
				laCommande=new GenFdRCmd_Suppression(leModele,idIntersection,
						leModele.getPHLivraison(idIntersection));
			}
			else{
				//On ajoute une livraison
				
				// Si la plage horaire est pleine
				if(leModele.nbLivraisonsPourPlage(comboBoxPlageH.getSelectedIndex())==NBMAXLIVRAISONPARPH){
					JOptionPane.showMessageDialog(null,
							"This time slot in already affected its number max of delivery",
							"Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else{
				laCommande=new GenFdRCmd_Ajout(leModele,idIntersection,
						comboBoxPlageH.getSelectedIndex());
				}
			}
			
			//Execution de la commande
			laCommande.Do();
			commandes.addLast(laCommande);
			//On actualise l'interface
			activeModeCharge();
			
			break;
			
		case MODE_AJOUT :
			if(memIdIntersection==AUCUNE){//Phase 1 : Selection du point de livraison a ajouter
				
				//Si on tente d'ajouter une livraison sur l'entrepot
				if(leModele.intersectionIsEntrepot(idIntersection)){
					JOptionPane.showMessageDialog(null,
							"Impossible d'ajouter une livraison sur l'entrepot.",
							"Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				//Si on tente d'ajouter une livraison sur une autre livraison
				if(leModele.intersectionIsLivraison(idIntersection)){
					JOptionPane.showMessageDialog(null,
							"This intersection is already a delivery.",
							"Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// Si la plage horaire est pleine
				if(leModele.nbLivraisonsPourPlage(comboBoxPlageH.getSelectedIndex())==NBMAXLIVRAISONPARPH){
					JOptionPane.showMessageDialog(null,
							"This time slot has already affected its number max of delivery",
							"Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// Memorisation de l'intersection ou ajouter la future livraison
				memIdIntersection=idIntersection;
				labelSudIndications.setText("Cliquez sur la livraison/entrepot precedent la nouvelle livraison");
			}
			else{//phase 2 : selection de la livraison apres laquelle on veux ajouter la nouvelle livraison
				//Si on ne choisi pas une livraison ou l'entrepot
				if(!(leModele.intersectionIsLivraison(idIntersection)||leModele.intersectionIsEntrepot(idIntersection)) ){
					JOptionPane.showMessageDialog(null,
							"Cette intersection n'est pas une livraison ou l'entrepot.",
							"Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				laCommande = new GenFdRCmd_AjoutDansTournee(leModele,
															memIdIntersection,
															idIntersection,
															comboBoxPlageH.getSelectedIndex());
				// Execution de la commande
				laCommande.Do();
				commandes.addLast(laCommande);
				//On actualise l'interface
				activeModeGenere();
				
			}
			break;
			
		case MODE_SUPPRESSION :
			//Si on tente de supprimer l'entrepot
			if(leModele.intersectionIsEntrepot(idIntersection)){
				JOptionPane.showMessageDialog(null,
						"Impossible de supprimer l'entrepot.",
						"Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//Si on tente de supprimer une livraison non prévue
			if(!leModele.intersectionIsLivraison(idIntersection)){
				JOptionPane.showMessageDialog(null,
						"Cette intersection n'est pas une livraison.",
						"Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			laCommande=new GenFdRCmd_SuppressionDansTournee(leModele,idIntersection,
											leModele.getPHLivraison(idIntersection));
			// Execution de la commande
			laCommande.Do();
			commandes.addLast(laCommande);
			//On actualise l'interface
			activeModeGenere();
			
			break;
		case MODE_GENERE : //Rien a faire dans ce prototype
			break;
		default : // Cas impossible, non atteignable
			break;
		}
		//Actualisation du plan
		panelPlan.refresh();
	}

	/**
	 * Traite le retour des methodes du modele pour afficher ou non des informations a l'utilisateur
	 * @param codeErr code de retour d'une methode. Ces codes sont definis dans la classe AbstractModel.
	 * @return true si l'action sur le modele s'est bien deroulee, ou false si elle ne doit pas etre prise en compte
	 * @see AbstractModel
	 */
	private boolean traiterRetour(int codeErr){
		String message;
		boolean retour;
		if(codeErr!=AbstractModel.OK){
			switch(codeErr){
			case AbstractModel.SOLUTION_NOT_FOUND:
				message="No found tour";
				break;
			case AbstractModel.PARSER_ERROR_FILE:
				message="Syntax error in the chosen file";
				break;
			case AbstractModel.PARSER_ERROR_INTERSECTION:
				message="The chosen file contains incoherence";
				break;
			default :
				message="An indefinite error occurred";	
			}
			// Lancement d'une popup de signalement d'erreur
			JOptionPane.showMessageDialog(null,
						message,
						"Erreur",
						JOptionPane.ERROR_MESSAGE);
			retour = false;
		}
		else{
			retour = true;
		}

		return retour;
	}

	/**
	 * Met l'interface dans l'etat vide ; aucun plan n'est charge
	 * @see FenetreGenerationFdR#state
	 */
	private void activeModeVide(){
		// Eventuel nettoyage du modele
		leModele.viderZone();
		boutonCentreGenerer.setVisible(true);
		boutonCentreGenerer.setEnabled(false);
		boutonCentreAnnuler.setVisible(true);
		boutonCentreAnnuler.setEnabled(false);
		// Nettoyage des commandes
		commandes.clear();
		boutonCentreVPapier.setVisible(true);
		boutonCentreVPapier.setEnabled(false);
			
		boutonCentreAjouter.setVisible(false);	
		boutonCentreSupprimer.setVisible(false);
		
		labelSudIndications.setText("To start, open a plan");
		state=MODE_VIDE;
	}

	/**
	 * Met l'interface dans l'etat charge ; un plan xml a ete ouvert, et aucun calcul 
	 * 										de tournee n'a ete lance
	 * @see FenetreGenerationFdR#state
	 */
	private void activeModeCharge(){
		//Si on etait en mode vide : ouverture d'un plan
		if(state==MODE_VIDE){
			JFileChooser dialogue = new JFileChooser();
			dialogue.showOpenDialog(null);

			if(!traiterRetour(leModele.creerZone(dialogue.getSelectedFile()))){
				//Une erreur s'est produite : on ne change pas d'etat
				return;
			}
			panelPlan.CreerAllVueIntersecTroncon(leModele.getIntersections());
		}
		//Actualisation de l'interface
		boutonCentreGenerer.setVisible(true);
		boutonCentreGenerer.setEnabled(leModele.nbLivraisons()>0);
		boutonCentreAnnuler.setVisible(true);
		boutonCentreAnnuler.setEnabled(!commandes.isEmpty());
		boutonCentreVPapier.setVisible(true);
		boutonCentreVPapier.setEnabled(false);
		
		boutonCentreAjouter.setVisible(false);	
		boutonCentreSupprimer.setVisible(false);
		
		labelSudIndications.setText("Click directly the plan to add/delete deliveries");
		state=MODE_CHARGE;
	}

	/**
	 * Met l'interface dans l'etat genere ; un calcul de tournee a ete demande
	 * @see FenetreGenerationFdR#state
	 */
	private void activeModeGenere(){
		if(state==MODE_CHARGE){ // Si on vient du mode charge : l'utilisateur vient de demander un calcul
			if(!traiterRetour(leModele.genererFeuille())){
				//Une erreur s'est produite, on ne change pas d'etat
				return;
			}
			//Nettoyage des commandes
			commandes.clear();
		}
		boutonCentreGenerer.setVisible(false);
		boutonCentreAjouter.setVisible(true);
		boutonCentreAjouter.setEnabled(true);
		boutonCentreAjouter.setText("Add");
		boutonCentreSupprimer.setVisible(true);
		boutonCentreSupprimer.setText("Delete");
		boutonCentreSupprimer.setEnabled(leModele.nbLivraisons()>0);
		boutonCentreVPapier.setEnabled(true);
		boutonCentreAnnuler.setEnabled(!commandes.isEmpty());
		labelSudIndications.setText("You can verify/modify the calculated tour");
		state=MODE_GENERE;
	}
		
	/**
	 * Met l'interface dans l'etat ajout d'une livraison dans un tournee calculee
	 * @see FenetreGenerationFdR#state
	 */
 	private void activeModeAjout(){
		boutonCentreAjouter.setText("Cancel Addition");
		boutonCentreSupprimer.setEnabled(false);
		boutonCentreAnnuler.setEnabled(false);
		boutonCentreVPapier.setEnabled(false);
		labelSudIndications.setText("Click a free intersection to add it a delivery");
		
		memIdIntersection=AUCUNE;
		state=MODE_AJOUT;
	}
	
	/**
	 * Met l'interface dans l'etat suppression d'une livraison dans un tournee calculee
	 * @see FenetreGenerationFdR#state
	 */
	private void activeModeSuppression(){
		boutonCentreSupprimer.setText("Cancel Suppression");
		boutonCentreAjouter.setEnabled(false);
		boutonCentreAnnuler.setEnabled(false);
		boutonCentreVPapier.setEnabled(false);
		labelSudIndications.setText("Click a delivery to delete it");
		
		state=MODE_SUPPRESSION;
	}


	/**
	 * Genere un fichier PDF qui contient le chemin ordonne a suivre, rues, directions, temps...etc
	 * @throws Exception IOException throw Exception lors de l'essaie automatique d'ouvrir le fichier PDF (par l'appli defaut du SE)
	 * @throws Exception DocumentException - itext : signale l'occurence d'un erreur dans le document  
	 */
	private void genererPDF() throws Exception {
	
		// Contient le chemin ou sera enregistrer le fichier pdf genere
		String nomFic = "tournee.pdf";
		
		// Mise en forme du contenu du fichier PDF
		Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
		Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
		Font smallBoldgray = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.BOLD, BaseColor.DARK_GRAY);
		
		// Choix de l'emplacement ou enregistrer le fichier 
	    JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Save paper version");
		int choice = chooser.showSaveDialog(null);
		if(choice != JFileChooser.APPROVE_OPTION){   
				return;
		  }
		else{
			//Recuperation du chemin choisi par l'utilisateur
			nomFic = chooser.getSelectedFile().getAbsolutePath();
			
		    Document document = new Document(PageSize.A4); // A4 => version papier
		    PdfWriter.getInstance(document, new FileOutputStream(nomFic));
		    document.open();
		      
			//Ajout de Metadata au fichier PDF
		    document.addTitle("Relevé Zone Geographique N°1");
		    document.addSubject("Relevé Zone Geographique N°1");
		    document.addKeywords("Java, PDF, iText");
		    document.addAuthor("Hexanome H4304");
		    document.addCreator("Hexanome H4304");
		    
		    //Ajout de contenu dans le fichier
		    Paragraph preface = new Paragraph();
		    preface.add(new Paragraph("Relevé Zone Geographique N°1", catFont)); //add a big title
		    preface.add(new Paragraph(" ")); //Add an empty line
		    preface.add(new Paragraph("Raport généré par: " + "SuperviseurXY " + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		        smallBold));
		    preface.add(new Paragraph(" ")); //Ajout d'une ligne vide
		    preface.add(new Paragraph(" ")); //Ajout d'une ligne vide
		    preface.add(new Paragraph("La liste suivante représente le trajet ordonné à suivre: ", smallBold));
		    document.add(preface);
		    
		    Chemin chemintemp;
		    Paragraph liste_des_rues = new Paragraph();
		    ArrayList<Chemin> itineraire = leModele.getItineraire();
		    
		    chemintemp = itineraire.get(0);
		    liste_des_rues.add(new Paragraph(" ")); //Ajout d'une ligne vide
		    liste_des_rues.add(new Paragraph("Quitter l'entrepot à: "+chemintemp.getDepart().getHeurePassage(), redFont));
		 	int i;	    
		    for(i = 0; i < itineraire.size()-1; i++){ 
		    	chemintemp = itineraire.get(i);
		    	liste_des_rues.add(new Paragraph(" ")); //Ajout d'une ligne vide
		    	liste_des_rues.add(new Paragraph("Livraison No."+(i+1)+", Destination \""+chemintemp.getDestination().getIntersection().getId()+"\"", smallBold));
		    	liste_des_rues.add(new Paragraph("Heure du passage prevus: "+chemintemp.getDestination().getHeurePassage(), redFont));
		    
		    	for(Troncon t:chemintemp.getTroncons()){ //t = Troncon
		    		liste_des_rues.add(new Paragraph("Prendre Rue: "+t.getRue()+" Direction: \""
		                                         +t.getDestination().getId()+"\"  ::  [Distance pour franchir "
		    			                         +t.getLongueur()+"m. | Vitesse Moyenne "
		    			                         +t.getVitesse()+" km/h]", smallBoldgray));
		    	}
		    	liste_des_rues.add(new Paragraph("Temps prevus pour arriver à partir de la livraison précédente: "+formatHHMMSS(chemintemp.getDuree()), smallBoldgray));
		    } //end for: chemin
		    
		    //Retour a l'entrepot
		    chemintemp = itineraire.get(i);
		    liste_des_rues.add(new Paragraph(" ")); //Ajout d'une ligne vide
		    liste_des_rues.add(new Paragraph("Revenir à l'entrepot ", redFont));
		    for(Troncon t:chemintemp.getTroncons()){ //t = Troncon
		    	liste_des_rues.add(new Paragraph("Prendre Rue: "+t.getRue()+" Direction: \""
		                                         +t.getDestination().getId()+"\"  ::  [Distance pour franchir "
		    			                         +t.getLongueur()+"m. | Vitesse Moyenne "
		    			                         +t.getVitesse()+" km/h]", smallBoldgray));
		    }
		    liste_des_rues.add(new Paragraph("Temps prevus pour arriver à l'entrepot: "+formatHHMMSS(chemintemp.getDuree()), smallBoldgray));
		    
		    
		    liste_des_rues.add(new Paragraph("Heure prevus d'arriver a l'entrepot: "+((Entrepot)chemintemp.getDestination()).getHeureRetour(), redFont));
		    
		    document.add(liste_des_rues);   
		    document.close();
		 
		      /*
		       * Les 3 lignes de code ci dessous permettent de automaticement ouvrir let the PDF par l'appli. defaut du SE 
		       * i.e : adobe pdf reader
		       */
		    Desktop desktop = Desktop.getDesktop();
			File file = new File(nomFic);
			desktop.open(file);
		} //end else
	
	}
	
	/** 
	 * Methode formatHHMMSS utilisee pour afficher la duree (secondes) en format HH:MM:SS
	 * @param secsIn
	 * @return String format HH:MM:SS  (XXh : YYmin : zzsec)
	 */
	private String formatHHMMSS(int secsIn){
		int hours = secsIn / 3600,
				remainder = secsIn % 3600,
				minutes = remainder / 60,
				seconds = remainder % 60;
				return ( (hours < 10 ? "0" : "") + hours
				+ "h : " + (minutes < 10 ? "0" : "") + minutes
				+ "min : " + (seconds< 10 ? "0" : "") + seconds
				+ "sec");
	}
	// -------------------------------------------------------- Evenementiel
	
	// Implementation de MouseListener
	@Override
	public void mouseClicked(MouseEvent e) {
		int idIntersection =panelPlan.EstClique(e.getX(),e.getY());
		if(idIntersection != AUCUNE){
			traiterClic(idIntersection);
		}
	}
	// Evenements non gérés
	@Override
	public void mouseEntered(MouseEvent e) {
		// Vide
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// Vide
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// Vide
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// Vide
	}

	// Implementation des Listener des elements de l'interface
	
	/**
	 * Listener de la combobox de selection des zones geographique
	 * @see FenetreGenerationFdR#comboBoxZone
	 */
	class ComboBoxZone_Listener implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent e) {
			// Vide : une seule zone géographique disponible dans ce prototype
		}
	}
	
	/**
	 * Listener du bouton Ouvrir
	 * @see FenetreGenerationFdR#boutonNordOuvrir
	 */
	class BoutonNordOuvrir_Listener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(state!=MODE_VIDE){
				int rep = JOptionPane.showConfirmDialog(null, 
						"Vous allez perdre le travail deja effectue,\nContinuer quand meme ?",
						"Ouvrir un nouveau plan",
						JOptionPane.YES_NO_OPTION);
				if(rep==JOptionPane.NO_OPTION){
					return;
				}
				else{
					activeModeVide();
				}
			}
			activeModeCharge();
			}
		}
	
	/**
	 * Listener du bouton Generer
	 * @see FenetreGenerationFdR#boutonCentreGenerer
	 */
	class BoutonCentreGenerer_Listener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			activeModeGenere();
		}
	}
	
	/**
	 * Listener du bouton Annuler
	 * @see FenetreGenerationFdR#boutonCentreAnnuler
	 */
	class BoutonCentreAnnuler_Listener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			annuler();
		}
	}
	
	/**
	 * Listener du bouton Ajouter
	 * @see FenetreGenerationFdR#boutonCentreAjouter
	 */
	class BoutonCentreAjouter_Listener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(state==MODE_GENERE){
				activeModeAjout();
			}
			else if(state==MODE_AJOUT){
				activeModeGenere();
			}
		}
	}
	
	/**
	 * Listener du bouton Supprimer
	 * @see FenetreGenerationFdR#boutonCentreSupprimer
	 */
	class BoutonCentreSupprimer_Listener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(state==MODE_GENERE){
				activeModeSuppression();
			}
			else if(state==MODE_SUPPRESSION){
				activeModeGenere();
			}
		}
	}
	
	/**
	 * Listener du bouton Version Papier
	 * @see FenetreGenerationFdR#boutonCentreVPapier
	 */
	class BoutonCentreVPapier_Listener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
			genererPDF();
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}
		
	}


}// End of class FenetreGenerationFdR
