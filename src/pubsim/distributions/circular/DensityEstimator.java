/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.distributions.circular;

import pubsim.distributions.RandomVariable;
import static pubsim.Util.fracpart;

/**
 * Circular kernel density estimator based on periodic convolution with a
 * kernel function.
 * @author Robby McKilliam
 */
public class DensityEstimator extends CircularRandomVariable {

    protected final double[] d;
    protected final RandomVariable ker;

    /**
     * Constructor takes an array of d and a kernel function.
     */
    public DensityEstimator(final double[] data, RandomVariable kernel){
        d = data;
        ker = kernel;
    }

    @Override
    public double pdf(double x) {
        double pdfsum = 0.0;
        for(int n = 0; n < d.length; n++)
            pdfsum += ker.pdf(fracpart(x - d[n]));
        return pdfsum/d.length;
    }

}