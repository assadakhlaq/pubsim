/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.phaseunwrapping.oned;

/**
 *
 * @author Robby McKilliam
 */
public interface OneDUnwrapperInterface {

    public double[] unwrap(double[] y);

    public void setSize(int N);

}
