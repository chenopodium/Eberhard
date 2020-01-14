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

/**
 * @see https://en.wikipedia.org/wiki/CHSH_inequality (there is a section on CH)
 * @see
 * https://www.slideshare.net/gill1109/yet-another-statistical-analysis-of-the-data-of-the-loophole-free-experiments-of-2015-revised
 * The CHSH inequality of the form N11 + N12 + N21 - N22 - singleA - singleB The
 * value is <= 0 for classical experiments and >0 for QM
 * @author croth
 */
public class CHSH extends Inequality implements Serializable {

    private static final long serialversionUID = 1L;

    /* The angles in degrees to use in degrees for detector A and B */
 /*
    A1, 0.0, angle at detector A in degrees 
A2, 1.0, angle at detector A in degrees
B1, 2.0, angle at detector B in degrees
B2, -36.0, angle at detector B in degrees
     */
    private double[] A = {0, 90};
    private double[] B = {45, 135};

    // A1, 0.0 A2, 8.0 B1, 1.0 B2, -8.0, j=374.0
    public CHSH() {
    }

    public CHSH(Counts counts) {
        super(counts);
    }
 
    public boolean isBroken(double result) {
        return  result >2.0;
    }
    /*
     @return the preferred angles at detector A in degrees (such as 0, 45)
     */
    @Override
    public double[] getPreferredA() {
        return A;
    }

    /*
     @return the preferred angles at detector B in degrees (such as 0, 45)
     */
    @Override
    public double[] getPreferredB() {
        return B;
    }

    /*
    @return computes the value of the inequality (such as J)
    From Richard:
    + cor11 - cor12 + cor21 + cor22 

    cor11 <- mean((x * y)[a == 1 & b == 1])
cor12 <- mean((x * y)[a == 1 & b == 2])
cor21 <- mean((x * y)[a == 2 & b == 1])
cor22 <- mean((x * y)[a == 2 & b == 2])
    
     */
    @Override
    public double compute() {

        double c11 = getCounts().getCorr(0, 0);
        double c12 = getCounts().getCorr(0, 1);
        double c21 = getCounts().getCorr(1, 0);
        double c22 = getCounts().getCorr(1, 1);

        // From Richard:   + cor11 - cor12 + cor21 + cor22 
        double s = c11 - c12 + c21 + c22;
        return s;
    }

    /*
    @return compute a string representation of the inequality used for output (ideally with
    the individual counts, such as N11 etc, the equation, and the result 
     */
    @Override
    public String computeString() {
        double c11 = getCounts().getCorr(0, 0);
        double c12 = getCounts().getCorr(0, 1);
        double c21 = getCounts().getCorr(1, 0);
        double c22 = getCounts().getCorr(1, 1);

        // From Richard:   + cor11 - cor12 + cor21 + cor22         
        String s = "\nName, CHSH inequality";
        s += "\nFormula, S = c11 – c12 + c21 + c22 <=2";
        s += "\nExplanation, if S<=2 it agrees with QM and if S>2 classical result";
        s += "\nc11, " + c11 + "\nc12, " + c12 + "\nc21, " + c21 + "\nc22, " + c22;

        double S = compute();

        s += "\nCHSH," + S;

        return s;
    }

}
