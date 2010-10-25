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
import lattices.reduction.SloanesReduction;

/**
 *
 * @author Robby McKilliam
 */
public class FermatTest {

    public FermatTest() {
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
     * Test of getGeneratorMatrix method, of class Fermat.
     */
    @Test
    public void testGetGeneratorMatrix() {
        System.out.println("getGeneratorMatrix");
        int n = 7;
        int r = 2;

        Fermat instance = new Fermat(n, r);
        Matrix result = instance.getGeneratorMatrix();
        System.out.println(VectorFunctions.print(result));
    }
    
    @Test
    public void testprintInverse() {
        System.out.println("print inverse");
        int n = 7;
        int r = 2;

        Fermat instance = new Fermat(n, r);
        Matrix result = instance.getGeneratorMatrix();
        System.out.println(VectorFunctions.print(result.inverse()));
    }
    
    @Test
    public void testprintSloaneReduced() {
        System.out.println("print Slonae reduced");
        int n = 11;
        int r = 3;

        Fermat instance = new Fermat(n, r);
        Matrix L = instance.getGeneratorMatrix().inverse();
        L = SloanesReduction.upperTriangularBasis(L);
        double[] v = new SloanesReduction(L, 7).getProjectionVector();
        System.out.println(VectorFunctions.print(v));
    }



}