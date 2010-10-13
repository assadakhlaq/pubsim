/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.bearing;

import distributions.GaussianNoise;
import distributions.RandomVariable;
import distributions.circular.CircularMeanVariance;
import distributions.circular.CircularRandomVariable;
import distributions.circular.ProjectedNormalDistribution;
import distributions.circular.VonMises;
import distributions.circular.WrappedGaussianNoise;
import distributions.circular.WrappedUniform;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import static simulator.Util.fracpart;

/**
 *
 * @author Robby McKilliam
 */
public class RunSimulations {
    
    public static void main(String[] args) throws Exception {

        int n = 4096;
        double angle = 0.1;
        int seed = 26;
        int iterations = 5000;

        String nameetx = "_" + Integer.toString(n);

        ConstantAngleSignal signal_gen = new ConstantAngleSignal();
        signal_gen.setLength(n);
        //CircularRandomVariable noise = new VonMises.Mod1();
        CircularRandomVariable noise = new WrappedUniform.Mod1(0.0,0.0);
        signal_gen.setNoiseGenerator(noise);

        //double from_var_db = 15;
        //double to_var_db = -10;
        double from_var_db = -11.2;
        double to_var_db = -31.8;
        double step_var_db = -0.1;

        Vector<Double> var_array = new Vector<Double>();
        Vector<Double> var_db_array = new Vector<Double>();
        for(double vardb = from_var_db; vardb >= to_var_db; vardb += step_var_db){
            var_db_array.add(new Double(vardb));
            var_array.add(new Double(Math.pow(10.0, ((vardb)/10.0))));
        }

        Vector<BearingEstimator> estimators = new Vector<BearingEstimator>();

        //add the estimators you want to run
        //estimators.add(new LeastSquaresEstimator());
        //estimators.add(new SamplingLatticeEstimator(12*n));
        //estimators.add(new KaysEstimator());
        //estimators.add(new PSCFDEstimator());
        //estimators.add(new VectorMeanEstimator());

        Iterator<BearingEstimator> eitr = estimators.iterator();
        while(eitr.hasNext()){

            BearingEstimator est = eitr.next();

            Vector<Double> mse_array = new Vector<Double>(var_array.size());
            Vector<Double> wrappedvar_array = new Vector<Double>(var_array.size());
            java.util.Date start_time = new java.util.Date();
            for(int i = 0; i < var_array.size(); i++){

                noise.setVariance(var_array.get(i));

                double mse = runIterations(est, signal_gen, iterations);

                mse_array.add(mse/iterations);
                double wrappedvar = noise.getWrappedVariance();
                wrappedvar_array.add(wrappedvar);

                System.out.println(var_array.get(i) + "\t" + wrappedvar + "\t" + mse/iterations);


            }
            java.util.Date end_time = new java.util.Date();
            System.out.println(est.getClass().getName() +
                    " completed in " +
                    (end_time.getTime() - start_time.getTime())/1000.0
                    + "seconds");

            try{
                String fname = est.getClass().getName() + "_" + noise.getClass().getName();
                File file = new File(fname.concat(nameetx).replace('$', '.'));
                BufferedWriter writer =  new BufferedWriter(new FileWriter(file));
                for(int i = 0; i < var_array.size(); i++){
                    writer.write(
                            wrappedvar_array.get(i).toString().replace('E', 'e')
                            + "\t" + mse_array.get(i).toString().replace('E', 'e'));
                    writer.newLine();
                }
                writer.close();
            } catch(IOException e) {
                System.out.println(e.toString());
            }

        }

        Vector<Double> mse_array = new Vector<Double>(var_array.size());
        Vector<Double> wrappedvar_array = new Vector<Double>(var_array.size());
        //finally print out the asymptotic circularVariance
        for(int i = 0; i < var_array.size(); i++){
                noise.setVariance(var_array.get(i));
                double mse = (new CircularMeanVariance(noise)).circularVariance()/n;
                //double mse = LeastSquaresEstimator.asymptoticVariance(noise, n);
                double wrappedvar = noise.getWrappedVariance();
                wrappedvar_array.add(wrappedvar);
                mse_array.add(mse);
                System.out.println(wrappedvar + "\t" + mse);
        }
        try{
            String fname = "asmyp_arg_" + noise.getClass().getName();
            File file = new File(fname.concat(nameetx).replace('$', '.'));
            BufferedWriter writer =  new BufferedWriter(new FileWriter(file));
            for(int i = 0; i < var_array.size(); i++){
                writer.write(
                        wrappedvar_array.get(i).toString().replace('E', 'e')
                        + "\t" + mse_array.get(i).toString().replace('E', 'e'));
                writer.newLine();
            }
            writer.close();
        } catch(IOException e) {
            System.out.println(e.toString());
        }
  
    }

    /**
     * Runs \param iterations number of iterations of the QAM receiver and
     * returns the codeword error rate (CER)
     */
    public static double runIterations(BearingEstimator rec, ConstantAngleSignal siggen, int iterations){

        double mse = 0.0;
        Random r = new Random();
        for(int i = 0; i < iterations; i++){

            double angle = r.nextDouble() - 0.5;
            siggen.setAngle(angle);

            siggen.generateReceivedSignal();

            double anglehat = rec.estimateBearing(siggen.generateReceivedSignal());

            double err = fracpart(siggen.getAngle() - anglehat);

            mse += err*err;

        }

        return mse;
    }

}
