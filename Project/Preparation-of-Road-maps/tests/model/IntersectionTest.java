package model;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class IntersectionTest {

	@SuppressWarnings("static-access")
	@Test
	public void testGenererIntersection() {
		Map<Section,Integer> tronconIdDestination = new HashMap<Section,Integer>();
		ZoneGeo zoneGeo = new ZoneGeo(); 
		String chemin = "files/test/plan1.xml";
		File file = new File(chemin);
		zoneGeo.setXml(file);
		zoneGeo.genererZoneGeo();
		
		// Take root
		Element racine = zoneGeo.getDocument().getDocumentElement();
		
		String tag="Noeud"; 
		NodeList noeuds = racine.getElementsByTagName(tag);
		
		for (int i=0 ; i <noeuds.getLength() ; i++)
		{ 
			Element intersectionElement = (Element) noeuds.item(i);
			
			Intersection intersection = new Intersection();
			
			assertEquals("Result",AbstractModel.OK,intersection.genererIntersection(intersectionElement,tronconIdDestination));
		}
	}

}
