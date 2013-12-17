package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class DijkstraTest {

	@Test
	public void testAlgoDijkstra() {
		Dijkstra d = new Dijkstra();
		ArrayList<Integer> noeuds = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5));
		ArrayList<ArrayList<ArrayList<Integer>>> chemin = new ArrayList<ArrayList<ArrayList<Integer>>>();
		int distance[][]= {{-1,20,-1,13,-1,15},{20,-1,45,-1,-1,-1},{50,-1,-1,10,150,-1},{20,-1,-1,-1,80,-1},{-1,35,-1,85,-1,15},{-1,-1,30,5,15,-1}};
		ArrayList<Integer> fin = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5));
		int debut = 0;
		chemin = d.algoDijkstra(noeuds, distance, debut,fin);
		
		assertEquals("Result",6,chemin.size()); 
	}

}