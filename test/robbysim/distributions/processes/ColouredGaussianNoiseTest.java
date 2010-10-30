/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package robbysim.distributions.processes;

import robbysim.distributions.processes.ColouredGaussianNoise;
import Jama.Matrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static robbysim.VectorFunctions.columnMatrix;
import static robbysim.VectorFunctions.print;


/**
 *
 * @author harprobey
 */
public class ColouredGaussianNoiseTest {

    public ColouredGaussianNoiseTest() {
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
     * Simulate some noise and compute the estimated covariance.  Check
     * it's close to the true covariance.
     */
    @Test
    public void testGenerateReceivedSignalZeroMean() {
        System.out.println("generateReceivedSignal with zero mean");
        
        int n = 3;
        Matrix cor = Matrix.random(n, n);
        cor = cor.times(cor.transpose());
        System.out.println(print(cor));
        ColouredGaussianNoise inst = new ColouredGaussianNoise(cor);
        
        int numiters = 1000;
        Matrix estcor = new Matrix(n,n);
        for(int i = 0; i < numiters; i++) {
            Matrix corv = columnMatrix(inst.generateReceivedSignal());
            estcor.plusEquals(corv.times(corv.transpose()));
            //System.out.println(print(estcor));
        }
        estcor.timesEquals(1.0/numiters);
        System.out.println(print(estcor));
        
        assertTrue(estcor.minus(cor).normF() < 0.3);

    }

     /**
     * Test that the generate takes the prescribed mean by Monte Carlo.
     */
    @Test
    public void testMean() {
        System.out.println("test mean");

        int n = 2;
        Matrix cor = Matrix.random(n, n);
        cor = cor.times(cor.transpose());
        System.out.println(print(cor));

        double[] mean = {1.0,1.0};
        ColouredGaussianNoise inst = new ColouredGaussianNoise(mean, cor);

        int numiters = 1000;
        Matrix estmean = new Matrix(n,1);
        for(int i = 0; i < numiters; i++) {
            Matrix corv = columnMatrix(inst.generateReceivedSignal());
            estmean.plusEquals(corv);
            //System.out.println(print(estcor));
        }
        estmean.timesEquals(1.0/numiters);
        System.out.println(print(estmean));

        assertTrue(estmean.minus(columnMatrix(mean)).normF() < 0.05);

    }

}