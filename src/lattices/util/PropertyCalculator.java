/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lattices.util;

import lattices.LatticeAndNearestPointAlgorithm;
import simulator.VectorFunctions;

/**
 * This computes approximations of various second
 * moments and the outradius of a lattice.
 * @author Robby McKilliam
 */
public class PropertyCalculator {

    private PointEnumerator points;
    private double outradius = Double.NEGATIVE_INFINITY;
    private double sm = 0.0;
    private double vol, N;
    private int numpoints = 0;

    public PropertyCalculator(LatticeAndNearestPointAlgorithm L, int samples){
        points = new SampledInVoronoi(L, samples);
        vol = L.volume();
        N = L.getDimension();
        while(points.hasMoreElements()){
            calculateProperty(points.nextElementDouble());

        }
    }

    /**
     * Print percentage complete to System.out while running.  Value of print boolean
     * does not matter.
     */
    public PropertyCalculator(LatticeAndNearestPointAlgorithm L, int samples, boolean print){
        points = new SampledInVoronoi(L, samples);
        vol = L.volume();
        N = L.getDimension();
        int oldper = 0;
        while(points.hasMoreElements()){
            calculateProperty(points.nextElementDouble());
            int p = (int)points.percentageComplete();
            if(p == oldper){
                System.out.println(p + "%");
                System.out.flush();
                oldper = p + 5;
            }
        }
    }

    /**
     * Runs with points generated in uniformly (psuedoranomly) in the Voronoi region.
     * Stops after 100 consecutive points don't change the dimensionless second moment
     * by more than the tolerance.  This is unlikely to perform sensibly though, the convergence
     * of calculating the Voronoi region this way is very slow!  If we could somehow compute
     * something above and something below (but both converge) it would be better.
     */
    public PropertyCalculator(LatticeAndNearestPointAlgorithm L, double tolerance){
        points = new UniformInVornoi(L, Integer.MAX_VALUE);
        vol = L.volume();
        N = L.getDimension();
        double oldG = 0;
        int count = 0;
        while(count < 100){
            calculateProperty(points.nextElementDouble());
            double G = dimensionalessSecondMoment();
            System.out.println(Math.abs(G - oldG));
            if(Math.abs(G - oldG) < tolerance){
                    count++;
            }else{
                oldG = G;
                count = 0;
            }
        }
    }

    /**
     * Runs with points generated in uniformly (psuedoranomly) in the Voronoi region.
     * After every "printevery" trials it prints the currently computed second moment
     * to the screen.  The user must stop this it runs foreever!  blah does nothing.
     * This is not a very well written class!
     */
    public PropertyCalculator(LatticeAndNearestPointAlgorithm L, int printevery, int blah){
        points = new UniformInVornoi(L, Integer.MAX_VALUE);
        vol = L.volume();
        N = L.getDimension();
        double oldG = 0;
        int count = 0;
        while(true){
            calculateProperty(points.nextElementDouble());
            count++;
            if(count == printevery){
                System.out.println(secondMoment() + ", "
                        + normalisedSecondMoment() + ", "
                        + dimensionalessSecondMoment());
                count = 0;
            }
        }
    }

    protected void calculateProperty(double[] p){
        //System.out.println(VectorFunctions.print(p));
        double mag2 = VectorFunctions.sum2(p);
        if(mag2 > outradius) outradius = mag2;
        sm += mag2;
        numpoints++;
    }

    public double outRadius() {return Math.sqrt(outradius);}

    public double coveringRadius() {return outRadius();}

    /**
     * Carefull here, the normalised second moment does not need to
     * be divided by the volume due to way that the points are generated
     * (i.e. using the fundamental parralelipided).  If you write the integral
     * down it becomes clear that the change of variables occurs with a
     * Jacobian that is equal to the volume, hence cancellation.
     *
     * Really, I think that Conway and Sloane got this a little wrong.  The
     * normalised second moment should be I = U/vol^(2/n + 1) then it is scale
     * independent.  The the dimensionless second moment is then I/n
     */
     public double normalisedSecondMoment() {
        return sm/numpoints/Math.pow(vol, 2.0/N);
    }

    public double secondMoment() {return sm/numpoints*vol;}

    public double dimensionalessSecondMoment() {
        return normalisedSecondMoment()/N;
    }


}
