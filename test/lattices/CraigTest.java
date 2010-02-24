/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lattices;

import Jama.Matrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import simulator.VectorFunctions;
import static org.junit.Assert.*;

/**
 *
 * @author harprobey
 */
public class CraigTest {

    public CraigTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of getGeneratorMatrix method, of class Craig.
     */
    @Test
    public void testGetGeneratorMatrix() {
        System.out.println("getGeneratorMatrix");
        int n = 9;
        int r = 3;
        
        Craig instance = new Craig(n, r);
        Matrix result = instance.getGeneratorMatrix();
        System.out.println(VectorFunctions.print(result));

        Matrix gen = instance.getGeneratorMatrix();
        //System.out.println(VectorFunctions.print(gen));
        Matrix gram = gen.transpose().times(gen);
        double expResult = Math.sqrt(gram.det());

        double vol = instance.volume();
        assertEquals(expResult, vol, 0.001);
    }

    /**
     * Test Elkies theorem about kissing numbers and Craig's lattices.
     * 
     */
    @Test
    public void testElkiesKissingNumber() {
        System.out.println("testElkiesKissingNumber");

        //p needs to be prime.
        int p = 67;
        int n = p-1;
        int r = (p+1)/4;

        Craig instance = new Craig(n, r);

        Matrix gen = instance.getGeneratorMatrix();
        System.out.println(VectorFunctions.print(gen));

        long res = instance.kissingNumber();
        long expr = p*(p-1);

        System.out.println(res);
        System.out.println(expr);

        assertEquals(expr, res);
    }

}