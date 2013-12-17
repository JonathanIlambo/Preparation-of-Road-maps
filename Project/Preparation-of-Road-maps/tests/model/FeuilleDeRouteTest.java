package model;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class FeuilleDeRouteTest {	

	@Test
	public void testCreerZone() {
		String cheminPlan = "files/test/plan1.xml"; 
		File file = new File(cheminPlan); 
		FeuilleDeRoute feuilleDeRoute = new FeuilleDeRoute(file);
		
		assertEquals("Result",AbstractModel.OK,feuilleDeRoute.creerZone(file)); 
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testViderZone() {
		
		/** Plages Horaires */
		String cheminPlan = "files/test/plagesHoraires.xml"; 
		File file1 = new File(cheminPlan); 
		FeuilleDeRoute feuilleDeRoute = new FeuilleDeRoute(file1);
		
		/** Zone Geographique */ 
		cheminPlan = "files/test/plan1.xml";
		File file2 = new File(cheminPlan);
		feuilleDeRoute.setXml(file2); 
		feuilleDeRoute.creerZone(file2);
		
		/** Vider livraison */ 
		feuilleDeRoute.viderZone(); 
		
		assertEquals("Result",null,feuilleDeRoute.getZoneGeo()); 		
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testGenererFeuille() {
		
		/** Plages Horaires */
		String cheminPlan = "files/test/plagesHoraires.xml"; 
		File file1 = new File(cheminPlan); 
		FeuilleDeRoute feuilleDeRoute = new FeuilleDeRoute(file1);
		feuilleDeRoute.genererPlagesHoraires(); 
		
		/** Zone Geographique */ 
		cheminPlan = "files/test/plan1.xml";
		File file2 = new File(cheminPlan);
		feuilleDeRoute.setXml(file2); 
		feuilleDeRoute.creerZone(file2);
		
		/** Generer Feuille de route */
		assertEquals("Result",AbstractModel.ERR,feuilleDeRoute.genererFeuille());		
	}

	@SuppressWarnings("static-access")
	@Test
	public void testIntersectionIsLivraison() {
		/** Plages Horaires */
		String cheminPlan = "files/test/plagesHoraires.xml"; 
		File file1 = new File(cheminPlan); 
		FeuilleDeRoute feuilleDeRoute = new FeuilleDeRoute(file1);
		
		/** Zone Geographique */ 
		cheminPlan = "files/test/plan1.xml";
		File file2 = new File(cheminPlan);
		feuilleDeRoute.setXml(file2); 
		feuilleDeRoute.creerZone(file2);
		
		/** Generer Feuille de route */
		feuilleDeRoute.genererFeuille();
		
		assertEquals("Result",false,feuilleDeRoute.intersectionIsLivraison(0)); 		
	}

	@SuppressWarnings("static-access")
	@Test
	public void testIntersectionIsEntrepot() {
		/** Plages Horaires */
		String cheminPlan = "files/test/plagesHoraires.xml"; 
		File file1 = new File(cheminPlan); 
		FeuilleDeRoute feuilleDeRoute = new FeuilleDeRoute(file1);
		
		/** Zone Geographique */ 
		cheminPlan = "files/test/plan1.xml";
		File file2 = new File(cheminPlan);
		feuilleDeRoute.setXml(file2); 
		feuilleDeRoute.creerZone(file2);
		
		/** Generer Feuille de route */
		feuilleDeRoute.genererFeuille();
		
		assertEquals("Result",true,feuilleDeRoute.intersectionIsEntrepot(0)); 		
	}
}
