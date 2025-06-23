/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import model.circuit.CircuitEuc;
import model.circuit.CircuitGeo;
import model.coord.CoordEuc;
import model.coord.CoordGeo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author p2410537
 */
public class StringGeoTest {
    private CircuitGeo circuit;
    
    @Test
    public void calculateDistanceTest() {
      
        CoordGeo a = new CoordGeo(3.0, 4.0);
        CoordGeo b = new CoordGeo(3.0, 4.0);
        CoordGeo c = new CoordGeo(0.0, 0.0);
        CoordGeo d = new CoordGeo(6.0, 0.0);

        List<CoordGeo> coords = Arrays.asList(a, b);
        List<CoordGeo> coords2 = Arrays.asList(c, a);
        List<CoordGeo> coords3 = Arrays.asList(a, c);
        List<CoordGeo> coords4 = Arrays.asList(c, a, d);
        
        // Distance null pour 2 points identiques
        double result = circuit.calculateCircuitLength(coords);
        double expected = 0.0;
        assertEquals(expected, result, 1e-6);
        
        // Distance égale à celle attendu
        double result2 = circuit.calculateCircuitLength(coords2);
        double expected2 = 5.0;
        assertEquals(expected2, result2, 1e-6);
        
        // Distance égale entre 2 points peu importe le sens
        double result3 = circuit.calculateCircuitLength(coords3);
        assertEquals(result2, result3, 1e-6);
        
        // Distance entre trois points
        double result4 = circuit.calculateCircuitLength(coords4);
        double expected4 = 10.0;
        assertEquals(expected4, result4, 1e-6);
    }
    
    @Test
    public void bestGreedyTest() {
        circuit.addCoord(new CoordGeo(0.0, 0.0));
        circuit.addCoord(new CoordGeo(1.0, 0.0));
        circuit.addCoord(new CoordGeo(1.0, 1.0));
        circuit.bestGreedyAlgorithm();

        List<CoordGeo> result = circuit.getGreedyCircuit();

        assertEquals(4, result.size()); // longueur de circuit correcte
        assertEquals(result.get(0), result.get(result.size() - 1)); // Circuit commence et finis au même point
    }
    
    @Test
    public void bestInsertionTest() {
        // Circuit commence et finis au même point
        CoordGeo a = new CoordGeo(0.0, 0.0);
        CoordGeo b = new CoordGeo(1.0, 0.0);
        CoordGeo c = new CoordGeo(1.0, 1.0);

        circuit.addCoord(a);
        circuit.addCoord(b);
        circuit.addCoord(c);
        circuit.bestInsertionAlgorithm();
        List<CoordGeo> result = circuit.getInsertionCircuit();

        assertEquals(result.get(0), result.get(result.size() - 1)); 
        
        // Vérifie que chaque point de départ apparaît une seule fois (hors dernier point)
        int occurrencesA = Collections.frequency(result.subList(0, 3), a);
        int occurrencesB = Collections.frequency(result.subList(0, 3), b);
        int occurrencesC = Collections.frequency(result.subList(0, 3), c);

        assertEquals(1, occurrencesA);
        assertEquals(1, occurrencesB);
        assertEquals(1, occurrencesC);
        
        // longueur de circuit correcte
        assertEquals(4, result.size()); 
    }
    
    @Test
    public void randomAlgorithmTest() {
        //Circuit commence et finis au même point
        CoordGeo a = new CoordGeo(0.0, 0.0);
        CoordGeo b = new CoordGeo(1.0, 0.0);
        CoordGeo c = new CoordGeo(1.0, 1.0);

        circuit.addCoord(a);
        circuit.addCoord(b);
        circuit.addCoord(c);

        circuit.randomAlgorithm();
        List<CoordGeo> result = circuit.getRandomCircuit();
        assertEquals(result.get(0), result.get(result.size() - 1)); 
        
        //Longueur de circuit correcte
        assertEquals(4, result.size()); 

        // Vérifie que chaque point de départ apparaît une seule fois (hors dernier point)
        assertEquals(1, Collections.frequency(result.subList(0, 3), a));
        assertEquals(1, Collections.frequency(result.subList(0, 3), b));
        assertEquals(1, Collections.frequency(result.subList(0, 3), c));
        
        // plusieurs appels donnent des résultats différents
        circuit.randomAlgorithm();
        List<CoordGeo> result1 = new ArrayList<>(circuit.getRandomCircuit());

        circuit.randomAlgorithm();
        List<CoordGeo> result2 = new ArrayList<>(circuit.getRandomCircuit());
        
        assertTrue(result1 != result2) ;
    }
    
    
    @Test
    public void createDistanceMatrixTest() {
        circuit.addCoord(new CoordGeo(0.0, 0.0));
        circuit.addCoord(new CoordGeo(3.0, 4.0));
        circuit.addCoord(new CoordGeo(0.0, 1.0));
        circuit.addCoord(new CoordGeo(1.0, 1.0));
        circuit.addCoord(new CoordGeo(2.0, 2.0));
        
        //Test la taille de la matrice
        Object[][] matrix = circuit.createDistanceMatrix();
        assertTrue(5 == matrix.length);
        assertTrue(6 == matrix[0].length);
        
        // Test si lrs diagonales sont égales à 0
        assertEquals("0.00", matrix[0][1]);
        assertEquals("0.00", matrix[1][2]);
        
        //Test si c'est symétrique
        assertEquals(matrix[0][2], matrix[1][1]);
        
        //Test distances correctes
        assertEquals("5,00", matrix[0][2]); 
        assertEquals("5,00", matrix[1][1]);
        
        //Test des noms de colonnes
        assertEquals("Lieu 1", matrix[0][0]);
        assertEquals("Lieu 2", matrix[1][0]);
    }
    
    @Test
    public void negativeCoordTest() {
        CircuitGeo circuitNeg = new CircuitGeo();
        circuitNeg.addCoord(new CoordGeo(-1.0, -1.0));
        circuitNeg.addCoord(new CoordGeo(2.0, 3.0));
        circuitNeg.addCoord(new CoordGeo(-3.0, 4.0));
        circuitNeg.addCoord(new CoordGeo(2.0, -1.0));
        List<CoordGeo> original = Arrays.asList(
            new CoordGeo(0.0, 0.0),
            new CoordGeo(-2.0, -3.0),
            new CoordGeo(-1.0, 2.0),
            new CoordGeo(3.0, -1.0),
            new CoordGeo(1.0, 1.0)
        );
        //Pour calculateDistance
        //Pour bestGreedy
        
        //Pour bestInsertion
        circuitNeg.bestInsertionAlgorithm();
        List<CoordGeo> result = circuitNeg.getInsertionCircuit();
        assertEquals(5, result.size());
        assertTrue(result.containsAll(circuitNeg.getCoords()));
        assertNotNull(result);
        
        //Pour randomAlgorithm
        circuitNeg.randomAlgorithm();
        result = circuitNeg.getRandomCircuit();
        assertNotNull(result);
        assertEquals(5, result.size());
        assertTrue(result.containsAll(circuitNeg.getCoords()));
        
        //Pour ameliorerCircuitParEchange
        double originalLength = circuitNeg.calculateCircuitLength(original);
        circuitNeg.ameliorerCircuitParEchange(original);
        List<CoordGeo> improved = circuitNeg.getAmeliorateCircuit();
        double improvedLength = circuitNeg.calculateCircuitLength(improved);
        assertNotNull(improved);
        assertEquals(original.size(), improved.size());
        assertTrue(improved.containsAll(original));
        assertTrue(improvedLength <= originalLength);
        
        //Pour createDistanceMatrix
        Object[][] matrix = circuitNeg.createDistanceMatrix();
        assertEquals("Lieu 1", matrix[0][0]);
        assertEquals("Lieu 2", matrix[1][0]);
        assertEquals("0.00", matrix[0][1]);
        assertEquals("5,00", matrix[0][2]);
    }
    
    @Test
    public void onlyOnePointTest() {
        CircuitGeo circuit1 = new CircuitGeo();
        circuit1.addCoord(new CoordGeo(5.0, 0.0));
        List<CoordGeo> onePoint = List.of(new CoordGeo(0.0, 0.0));
        //Pour calculateDistance
        //Pour bestGreedy
        //Pour bestInsertion
        
        
        //Pour randomAlgorithm
        circuit1.randomAlgorithm();
        List<CoordGeo> result = circuit1.getRandomCircuit();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(circuit1.getCoords().get(0), result.get(0));
        
        //Pour ameliorerCircuitParEchange
        circuit1.ameliorerCircuitParEchange(onePoint);
        result = circuit1.getAmeliorateCircuit();
        assertEquals(onePoint, result);
        
        //Pour createDistanceMatrix
        Object[][] matrix = circuit1.createDistanceMatrix();
        assertEquals(1, matrix.length);
        assertEquals(2, matrix[0].length);
        assertEquals("Lieu 1", matrix[0][0]);
        assertEquals("0.00", matrix[0][1]);
    }
    
    @Test
    public void emptyTest() {
        CircuitGeo circuitVide = new CircuitGeo();
        List<CoordGeo> emptyList = new ArrayList<>();
        
        //Pour calculateDistance
        //Pour bestGreedy
        //Pour bestInsertion
        //Pour randomAlgorithm
  
        //Pour ameliorerCircuitParEchange
        circuitVide.ameliorerCircuitParEchange(emptyList);
        List<CoordGeo>result = circuitVide.getAmeliorateCircuit();
        assertNotNull(result);
        assertEquals(0, result.size());
        
        //Pour createDistanceMatrix
        Object[][] matrix = circuitVide.createDistanceMatrix();
        assertNotNull(matrix);
        assertEquals(0, matrix.length);
    }
     
    public StringGeoTest() {
    }
    
    @Before
    public void setUp() {
        circuit = new CircuitGeo();
    }
   
}
