/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.poly;

import Jama.Matrix;
import distributions.GaussianNoise;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import simulator.VectorFunctions;

/**
 *
 * @author Robby McKilliam
 */
public class MaximumLikelihoodTest {

    public MaximumLikelihoodTest() {
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

    @Test
    public void testPolyFunctionValue() {
        System.out.println("testPolyFunctionValue");
        double[] yr = {0.1, 0.1, 0.1};
        double[] yi = {0.1, 0.1, 0.1};
        double[] p = {0.1, 0.1, 0.1};
        Matrix P = VectorFunctions.columnMatrix(p);
        MaximumLikelihood.PolynomialPhaseLikelihood func
                = new MaximumLikelihood.PolynomialPhaseLikelihood(yr, yi);
        //calculated in Matlab
        double expr = -3.055198893365938;
        double res = func.value(P);
        assertEquals(res, expr, 0.000001);
        
    }

    @Test
    public void testPolyFunctionGradient() {
        System.out.println("testPolyFunctionGradient");
        double[] yr = {0.1, 0.1, 0.1};
        double[] yi = {0.1, 0.1, 0.1};
        double[] p = {0.1, 0.1, 0.1};
        Matrix P = VectorFunctions.columnMatrix(p);
        MaximumLikelihood.PolynomialPhaseLikelihood func
                = new MaximumLikelihood.PolynomialPhaseLikelihood(yr, yi);
        MaximumLikelihood.PolynomialPhaseLikelihoodAutoDerivative funcA
                = new MaximumLikelihood.PolynomialPhaseLikelihoodAutoDerivative(yr, yi);
        //calculated in Matlab
        Matrix gf = func.gradient(P);
        Matrix gfA = funcA.gradient(P);
        //System.out.println("gf = " + VectorFunctions.print(gf));
        //System.out.println("gfA = " + VectorFunctions.print(gfA));
        for(int i = 0; i < gf.getRowDimension(); i ++)
            assertEquals(gfA.get(i,0), gf.get(i,0), 0.00001);

    }

    @Test
    public void testPolyFunctionHessian() {
        System.out.println("testPolyFunctionHessian");
        double[] yr = {0.1, 0.1, 0.1};
        double[] yi = {0.1, 0.1, 0.1};
        double[] p = {0.1, 0.1, 0.1};
        Matrix P = VectorFunctions.columnMatrix(p);
        MaximumLikelihood.PolynomialPhaseLikelihood func
                = new MaximumLikelihood.PolynomialPhaseLikelihood(yr, yi);
        MaximumLikelihood.PolynomialPhaseLikelihoodAutoDerivative funcA
                = new MaximumLikelihood.PolynomialPhaseLikelihoodAutoDerivative(yr, yi);
        //calculated in Matlab
        Matrix Hf = func.hessian(P);
        Matrix HfA = funcA.hessian(P);
        //System.out.println("gf = " + VectorFunctions.print(Hf));
        //System.out.println("gfA = " + VectorFunctions.print(HfA));
        assertEquals(0.0, Hf.minus(HfA).normF(), 0.01);
        

    }

    /**
     * Test of estimate method, of class MaximumLikelihood.
     */
    @Test
    public void testEstimate() {
        int n = 10;
        double[] params = {0.1, 0.1, 0.1};
        int a = params.length;

        PolynomialPhaseSignal siggen = new PolynomialPhaseSignal();
        siggen.setLength(n);
        siggen.setParameters(params);
        siggen.setNoiseGenerator(new GaussianNoise(0, 0.00001));

        siggen.generateReceivedSignal();

        MaximumLikelihood inst = new MaximumLikelihood(params.length, 100);
        inst.setSize(n);

        double[] p = inst.estimate(siggen.getReal(), siggen.getImag());

        System.out.println(VectorFunctions.print(p));

        assertTrue(VectorFunctions.distance_between(p, params) < 0.0001);
    }

}