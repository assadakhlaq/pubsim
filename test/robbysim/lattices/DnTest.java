/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package robbysim.lattices;

import robbysim.lattices.Dn;
import Jama.Matrix;
import junit.framework.TestCase;
import robbysim.distributions.GaussianNoise;
import robbysim.lattices.decoder.SphereDecoder;
import robbysim.distributions.processes.IIDNoise;
import robbysim.VectorFunctions;

/**
 *
 * @author Robby
 */
public class DnTest extends TestCase {
    
    public DnTest(String testName) {
        super(testName);
    }            

    /**
     * Test of nearestPoint method, of class Dn.
     */
    public void testNearestPoint() {
        System.out.println("nearestPoint");
        
        int n = 7;
        int iters = 100;
        
        GaussianNoise noise = new GaussianNoise(0.0, 1000.0);
        IIDNoise siggen = new IIDNoise(noise, n);
        
        Dn instance = new Dn(n);
        SphereDecoder tester = new SphereDecoder(instance);
        
        for(int i = 0; i < iters; i++){
            double[] y = siggen.generateReceivedSignal();
            instance.nearestPoint(y);
            tester.nearestPoint(y);
            assertEquals(VectorFunctions.distance_between(instance.getLatticePoint(), tester.getLatticePoint())<0.0001, true);
        }
    }

        /**
     * Test of nearestPoint method, of class Dn.
     */
    public void testGeneratorMatrix() {
        System.out.println("testGeneratorMatrix");

        int n = 5;

        Dn instance = new Dn(n);
        Matrix G = instance.getGeneratorMatrix();
        System.out.println(VectorFunctions.print(G));

        System.out.println(G.det());

    }


}
