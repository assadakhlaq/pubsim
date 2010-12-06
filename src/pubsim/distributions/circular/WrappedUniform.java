/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.distributions.circular;

import pubsim.distributions.UniformNoise;

/**
 * 
 * @author Robert McKilliam
 */
public class WrappedUniform extends WrappedCircularRandomVariable{

    public WrappedUniform(double mean, double var){
        super(new UniformNoise(mean, var));
    }

}