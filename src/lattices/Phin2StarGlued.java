/*
 * Phin2StarGlued.java
 *
 * Created on 12 August 2007, 20:15
 */

package lattices;

import simulator.*;

/**
 * O(n^4 log(n)) nearest point algorithm for the lattice Pn.  This just runs the
 * An* nearest point algorithm for all O(n^3) glue vectors.  Phin2Star is a faster
 * O(n^3 log(n)) algorithm.  
 * <p>
 * This currently only works for odd n.  The glue vectors are slightly different
 * for even n.  There are also 4 times more of them for even n!
 * 
 * 
 * @author Robby McKilliam
 */
public class Phin2StarGlued extends Phin2Star {
    
    protected Anstar anstar;
    
    protected double[] g, vt, yt, y, ut;

    public Phin2StarGlued(){}

    public Phin2StarGlued(int n){
        setDimension(n);
    }
    
    @Override
    public void setDimension(int n){
        this.n = n;
        
        anstar = new AnstarBucketVaughan();
        anstar.setDimension(n+1);
        
        g = new double[n+2];
        v = new double[n+2];
        u = new double[n+2];
        yt = new double[n+2];
        y = new double[n+2];
        
    }
    
    /**
     * Find the nearest point in Phin2Star by searching the o(n^3)
     * translates/glues of An*.  Currently, this only works
     * for odd n.
     */
    @Override
    public void nearestPoint(double[] y){
        if (n != y.length-2)
	    setDimension(y.length-2);
        
        project(y, this.y);
        
        double d = sumg2(y.length);
        if(n%2 == 0) d = 2*d;
               
        for (int j = 0; j < n+2; j++)
            g[j] = -1.0/(n+2) + (j + 1.0 - (n+3.0)/2.0)/d;
        
        g[(n+2)/2 - 1] += 1.0;
        
        double bestdist = Double.POSITIVE_INFINITY;
        //iterate over all glue vectors
        for(int i = 0; i < d; i++){
            
            for (int j = 0; j < n+2; j++)
                yt[j] = this.y[j] - i*g[j];
            
            //solve the nearestPoint algorithm in An* for this glue
            anstar.nearestPoint(yt);
            vt = anstar.getLatticePoint();
            ut = anstar.getIndex();
            
            double dist = 0.0;
            for (int j = 0; j < n+2; j++)
                dist += Math.pow( vt[j] + i*g[j] - this.y[j], 2);
            
            if(dist < bestdist){
                bestdist = dist;
                for (int j = 0; j < n+2; j++){
                    v[j] = vt[j] + i*g[j];
                    u[j] = ut[j];
                }
                u[(n+2)/2 - 1] += i;
                
            }
        }
    }
    
}
