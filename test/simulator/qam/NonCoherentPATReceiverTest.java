/*
 * NonCoherentPATReceiverTest.java
 * JUnit based test
 *
 * Created on 28 November 2007, 09:44
 */

package simulator.qam;

import junit.framework.*;
import simulator.GaussianNoise;

/**
 *
 * @author Robby
 */
public class NonCoherentPATReceiverTest extends TestCase {
    
    public NonCoherentPATReceiverTest(String testName) {
        super(testName);
    }

    /**
     * Test of decode method, of class simulator.qam.NonCoherentPATReceiver.
     */
    public void testDecode() {
        System.out.println("decode");
        
        int M = 16;
        int T = 10;
        long seed = 11311;
        
        PilotAssistedFadingNoisyQAM siggen = new PilotAssistedFadingNoisyQAM();
        siggen.generateChannel();
        siggen.setQAMSize(M);
        siggen.setLength(T);
        siggen.setPATSymbol(3,3);
        //siggen.setChannel(1.0,0.0);
        
        GaussianNoise noise = new GaussianNoise(0.0,0.0001);
        siggen.setNoiseGenerator(noise);
        
        NonCoherentPATReceiver instance = new NonCoherentPATReceiver();
        instance.setQAMSize(M);
        instance.setT(T);
        instance.setPATSymbol(3,3);
        
        siggen.generateQAMSignal();
        siggen.generateReceivedSignal();
        
        instance.decode(siggen.getReal(), siggen.getImag());
        
        assertEquals(true, simulator.VectorFunctions.distance_between(
                instance.getReal(),siggen.getTransmittedRealQAMSignal())<0.00001);
        assertEquals(true, simulator.VectorFunctions.distance_between(
                instance.getImag(),siggen.getTransmittedImagQAMSignal())<0.00001);
    }
    
}
