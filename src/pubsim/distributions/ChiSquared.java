/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pubsim.distributions;

/**
 * A ChiSquared random variable.  A sum of the squares of k independent and 
 * identical normal random variables.
 * @author Robby McKilliam
 */
public class ChiSquared extends AbstractRealRandomVariable {

    final protected int k;
    final protected double var;
    
    final protected GaussianNoise noise;
    
    public ChiSquared(int k){
        this.k = k;
        var = 1.0;
        noise = new GaussianNoise(0, var);
    }
    
    public ChiSquared(int k, double var){
        this.k = k;
        this.var = var;
        noise = new GaussianNoise(0, var);
    }
    
    @Override
    public Double getMean() {
        return var*k;
    }

    @Override
    public Double getVariance() {
        return 2.0*var*var*k;
    }

    @Override
    public double pdf(Double x) {
        double xs = x/var;
        double denom = pubsim.Util.gamma(k/2.0)*Math.pow(2.0,k/2.0);
        double num = Math.pow(xs,k/2.0-1.0)*Math.exp(-xs/2.0);
        return num/denom/var;
    }
    
    @Override
    public Double getNoise() {
        double sum = 0.0;
        for(int i = 0; i < k; i++) {
            double X = noise.getNoise();
            sum += X*X;
        }
        return sum;
    }
    
 /**
  * Sum of square of 2 independent normal random variables
  */
 public static class ChiSquared2 extends ChiSquared {
     
     public ChiSquared2(){
         super(2);
     }
     
     public ChiSquared2(double var){
         super(2,var);
     }
     
    @Override
    public double pdf(Double x) {
        return Math.exp(-x/var/2.0)/2.0/var;
    }
        
 }
    
    
}
