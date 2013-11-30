/***********************************************************************
 * Module:  ZoneGeoTest.java
 * Author:  Jonathan Ilambo
 ***********************************************************************/

package model;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class ZoneGeoTest {

	@Test
	public void testGenererZoneGeo() {
		ZoneGeo zoneGeo = new ZoneGeo(); 
		String chemin = "files/test/plan1.xml";
		File file = new File(chemin);
		zoneGeo.setXml(file);
		assertEquals("Result",AbstractModel.OK,zoneGeo.genererZoneGeo());
	}
}
