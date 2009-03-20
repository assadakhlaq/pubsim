/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.phaseunwrapping.twod;

import distributions.NoiseGenerator;

/**
 * Interface for generating 2D wrapped data
 * @author Robby McKilliam
 */
public interface WrappedData {

    public double[][] getWrappedData();

    public double[][] getTrueData();

    public double[][] getWrappedIntegers();

    public void generateData();

    public void setSize(int M, int N);

    public void setNoiseGenerator(NoiseGenerator noise);

}