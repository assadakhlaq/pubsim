/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.fes;

import flanagan.complex.Complex;
import flanagan.math.FourierTransform;

/**
 * The is a periodogram estimator that uses the FFT implemented
 * by Michael Flagan
 * (http://www.ee.ucl.ac.uk/~mflanaga/java/FourierTransform.html#fft).
 * This librarby automatically zero pads if the data is not a power of 2
 * so, you need to be concious of this when choosing data length.
 * @author Robby McKilliam
 */
public class PeriodogramFFTEstimator implements FrequencyEstimator {

    protected int oversampled,  n;
    protected FourierTransform fft;
    protected Complex[] sig;

    /**Max number of iterations for the Newton step */
    static final int MAX_ITER = 15;

    /**Step variable for the Newton step */
    static final double EPSILON = 1e-10;

    /** oversampling defaults to 4 as per Rife and Borstyn */
    public PeriodogramFFTEstimator() {
        oversampled = 4;
    }

    /** Contructor that sets the number of samples to be taken of
     * the periodogram.
     */
    public PeriodogramFFTEstimator(int oversampled) {
        this.oversampled = oversampled;
    }

    public void setSize(int n) {
        this.n = n;
        sig = new Complex[FourierTransform.nextPowerOfTwo(oversampled * n)];
        fft = new FourierTransform();
    }

    public double estimateFreq(double[] real, double[] imag) {
        if (n != real.length) {
            setSize(real.length);
        }

        for (int i = 0; i < n; i++) {
            sig[i] = new Complex(real[i], imag[i]);
        }
        for (int i = n; i < sig.length; i++) {
            sig[i] = new Complex(0.0, 0.0);
        }

        fft.setData(sig);
        fft.transform();
        Complex[] ft = fft.getTransformedDataAsComplex();

        //note that the FFT is generally defined with exp(-jw) but
        //periodogram has exp(jw) so freq are -ve here.
        double maxp = 0;
        double fhat = 0.0;
        double f = 0.0;
        double fstep = 1.0 / ft.length;
        for (int i = 0; i < ft.length; i++) {
            double p = ft[i].squareAbs();
            if (p > maxp) {
                maxp = p;
                fhat = f;
            }
            f-=fstep;
        }

        //System.out.println(fhat);

        //Newton Raphson
        int numIter = 0;
        f = fhat;
        double lastf = f - 2 * EPSILON, lastp = 0;
        while (Math.abs(f - lastf) > EPSILON && numIter <= MAX_ITER) {
            double p = 0, pd = 0, pdd = 0;
            double sumur = 0, sumui = 0, sumvr = 0, sumvi = 0,
                    sumwr = 0, sumwi = 0;
            for (int i = 0; i < n; i++) {
                double cosf = Math.cos(-2 * Math.PI * f * i);
                double sinf = Math.sin(-2 * Math.PI * f * i);
                double ur = real[i] * cosf - imag[i] * sinf;
                double ui = imag[i] * cosf + real[i] * sinf;
                double vr = 2 * Math.PI * i * ui;
                double vi = -2 * Math.PI * i * ur;
                double wr = 2 * Math.PI * i * vi;
                double wi = -2 * Math.PI * i * vr;
                sumur += ur;
                sumui += ui;
                sumvr += vr;
                sumvi += vi;
                sumwr += wr;
                sumwi += wi;
            }
            p = sumur * sumur + sumui * sumui;
            if (p < lastp) //I am not sure this is necessary, Vaughan did it for period estimation.
            {
                f = (f + lastf) / 2;
            } else {
                lastf = f;
                lastp = p;
                if (p > maxp) {
                    maxp = p;
                    fhat = f;
                }
                pd = 2 * (sumvr * sumur + sumvi * sumui);
                pdd = 2 * (sumvr * sumvr + sumwr * sumur + sumvi * sumvi + sumwi * sumui);
                f += pd / Math.abs(pdd);
            }
            numIter++;
        }

        //System.out.println("f = " + fhat);

        return fhat - Math.round(fhat);
    }
}