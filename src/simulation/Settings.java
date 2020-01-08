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

import java.util.Date;

/**
 *
 * @author croth
 */
public class Settings {

    /* The entanglement efficiency to use (referred to as r in many cases ) */
    public double entanglementEfficiency = 0.7; //r in the  paper from F. Wang

    /*
    The (default) angles to use at the detectors in degrees.
    Note that these values are overwritten by the inequality
     */
    public double[] A = {0, 45};
    public double[] B = {-180 / 16.0, 180 / 16.0};

    public LAMBDAGENERATOR angleGenerator = LAMBDAGENERATOR.RANDOMANGLES;

    public enum LAMBDAGENERATOR {
        RANDOMANGLES, ITERATE, SUPPLIED
    }

    public String toShortString() {
        return  "A1, " + A[0] 
                + " A2, " + A[1] 
                + " B1, " + B[0] 
                + " B2, " + B[1];
    }
    @Override
    public String toString() {
        return "Date, " + (new Date(System.currentTimeMillis()).toString() + "\n\n"
                + "A1, " + A[0] + "\n"
                + "A2, " + A[1] + "\n"
                + "B1, " + B[0] + "\n"
                + "B2, " + B[1] + "\n"
                + "entanglementEfficiency, " + entanglementEfficiency + "\n"
                + "seed, " + Rand.getSeed());
    }
}
