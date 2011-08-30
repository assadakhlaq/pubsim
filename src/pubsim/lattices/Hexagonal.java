/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.lattices;

import Jama.Matrix;
import static pubsim.VectorFunctions.matrixMultVector;
import static pubsim.VectorFunctions.round;

/**
 * The hexangonal lattice with genertor matrix
 * [1 1/2; 0 sqrt(3)/2]
 * @author Robby McKilliam
 */
public class Hexagonal 
        extends NearestPointAlgorithmStandardNumenclature {

    private final double sqrt3 = Math.sqrt(3);
    private final Matrix M, Minv;

    public Hexagonal(){
        n = 2;
        u = new double[2];
        v = new double[2];
        M = computeGeneratorMatrix();
        Minv = M.inverse();
    }

    @Override
    public final double inradius() {
        return 1.0/2.0;
    }

    @Override
    public final double coveringRadius(){
        return 1.0/sqrt3;
    }

    @Override
    public final int getDimension() {
        return 2;
    }

    public Matrix getGeneratorMatrix() {
        return M;
    }

    private Matrix computeGeneratorMatrix(){
        Matrix B = new Matrix(2,2);
        B.set(0, 0, 1.0); B.set(0, 1, 0.5);
        B.set(1, 0, 0.0); B.set(1, 1, 0.5*sqrt3);
        return B;
    }

    public void nearestPoint(double[] y) {
        if(y.length != 2)
            throw new ArrayIndexOutOfBoundsException("y must have length 2");
        nearestPoint(y[0], y[1]);
    }

    /**
     * Uses the fact that the hexagonal lattice is the union of
     * two rectangular lattices.
     * @param x
     * @param y
     */
    public void nearestPoint(double x, double y){
        double ydivsqrt3 = y/sqrt3;
        double v0 = Math.round(x);
        double v1 = sqrt3*Math.round(ydivsqrt3);
        double dist1 = (v0 - x)*(v0 - x) + (v1 - y)*(v1 - y);
        double vt0 = 0.5 + Math.round(x - 0.5);
        double vt1 = sqrt3/2.0 + sqrt3*Math.round( ydivsqrt3 - 0.5 );
        double dist2 = (vt0 - x)*(vt0 - x) + (vt1 - y)*(vt1 - y);
        if(dist1 < dist2){
            v[0] = v0;
            v[1] = v1;
        }else{
            v[0] = vt0;
            v[1] = vt1;
        }
    }

    public final void setDimension(int n) {
        //do nothing
    }

    /**Getter for the interger vector. */
    @Override
    public double[] getIndex() {
        matrixMultVector(Minv, v, u);
        round(u,u);
        return u;
    }

    public double distance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
