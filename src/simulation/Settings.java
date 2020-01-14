/*
 The MIT License (MIT)

Copyright (c) 2019-2020 Chantal Roth

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
associated documentation files (the "Software"), to deal in the Software without restriction, 
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial 
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package simulation;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author croth
 */
public class Settings implements Serializable {

    private static final long serialversionUID = 1L;
    /* The entanglement efficiency to use (referred to as r in many cases ) */
    private double entanglementEfficiency = 0.6; //r in the  paper from F. Wang

    /*
    The (default) angles in degrees to use at the detectors in degrees.
    Note that these values are overwritten by the inequality
     */
    private double[] A = {0, 90};
    private double[] B = {45, 135};

    private long seed = 1234;
    private long trials = 100000;

    private LAMBDAGENERATOR angleGenerator = LAMBDAGENERATOR.RANDOMANGLES;

    public enum LAMBDAGENERATOR {
        RANDOMANGLES, ITERATE, SUPPLIED
    }

    public String toShortString() {
        return "A1, " + getA()[0]
                + " A2, " + getA()[1]
                + " B1, " + getB()[0]
                + " B2, " + getB()[1];
    }

    @Override
    public String toString() {
        return "Date, " + (new Date(System.currentTimeMillis()).toString() + "\n"
                + "A1, " + getA()[0] + ", angle at detector A in degrees \n"
                + "A2, " + getA()[1] + ", angle at detector A in degrees\n"
                + "B1, " + getB()[0] + ", angle at detector B in degrees\n"
                + "B2, " + getB()[1] + ", angle at detector B in degrees\n"
                + "entanglementEfficiency, " + getEntanglementEfficiency() + "\n"
                + "Seed, " + getSeed() + ", the seed used in the random generator");
    }

    /**
     * @return the entanglementEfficiency
     */
    public double getEntanglementEfficiency() {
        return entanglementEfficiency;
    }

    /**
     * @param entanglementEfficiency the entanglementEfficiency to set
     */
    public void setEntanglementEfficiency(double entanglementEfficiency) {
        this.entanglementEfficiency = entanglementEfficiency;
    }

    /**
     * @return the A
     */
    public double[] getA() {
        return A;
    }

    /**
     * @param A the A to set
     */
    public void setA(double[] A) {
        this.A = A;
    }

    /**
     * @return the B
     */
    public double[] getB() {
        return B;
    }

    /**
     * @param B the B to set
     */
    public void setB(double[] B) {
        this.B = B;
    }

    /**
     * @return the seed
     */
    public long getSeed() {
        return seed;
    }

    /**
     * @param seed the seed to set
     */
    public void setSeed(long seed) {
        this.seed = seed;
    }

    /**
     * @return the angleGenerator
     */
    public LAMBDAGENERATOR getAngleGenerator() {
        return angleGenerator;
    }

    /**
     * @param angleGenerator the angleGenerator to set
     */
    public void setAngleGenerator(LAMBDAGENERATOR angleGenerator) {
        this.angleGenerator = angleGenerator;
    }

    /**
     * @return the trials
     */
    public long getTrials() {
        return trials;
    }

    /**
     * @param trials the trials to set
     */
    public void setTrials(long trials) {
        this.trials = trials;
    }
}
