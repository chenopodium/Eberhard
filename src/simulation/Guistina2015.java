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
 *
 * @author croth
 * @see
 * https://journals.aps.org/prl/supplemental/10.1103/PhysRevLett.115.250401/Supplemental_material_final.pdf
 * @see
 * https://physics.aps.org/featured-article-pdf/10.1103/PhysRevLett.115.250401
 */
public class Guistina2015 extends Inequality implements Serializable {

    private static final long serialversionUID = 1L;
    /* The angles  in degrees to use in degrees for detector A and B */

    private double[] A = {0, 90};
    private double[] B = {45, 135};

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

    public Guistina2015() {

    }

    public Guistina2015(Counts counts) {
        super(counts);
    }

    /*
    @return computes the value of the inequality (such as J)
     */
    @Override
    public double compute() {

        /* A detected and B detected */
        double n11 = getCounts().getDetected(0, 0);
        
         /*  A deteced and B not detected */
        double n12 = getCounts().getDetZero(0, 1);
        
         /* A not deteced and B detected */
        double n21 = getCounts().getZeroDet(1, 0);
        
         /* A detected and B detected */
        double n22 = getCounts().getDetected(1, 1);

        double j = n11
                - n12
                - n21
                - n22;

        return j;
    }

    /*
    @return compute a string representation of the inequality used for output (ideally with
    the individual counts, such as N11 etc, the equation, and the result 
     */
    @Override
    public String computeString() {

        double n11 = getCounts().getDetected(0, 0);
        double n12 = getCounts().getDetZero(0, 1);
        double n21 = getCounts().getZeroDet(1, 0);
        double n22 = getCounts().getDetected(1, 1);

        
        double tot = getCounts().getTotalTrials();
        double a1b1 = getCounts().getSettingAB(0, 0) * 100.0 / tot;
        double a1b2 = getCounts().getSettingAB(0, 1) * 100.0 / tot;
        double a2b1 = getCounts().getSettingAB(1, 0) * 100.0 / tot;
        double a2b2 = getCounts().getSettingAB(1, 1) * 100.0 / tot;
        
        
        /* Using commas so that it can be imported into Excel */
        String s = "\nName, Inequality used in the Guistina 2015 experiment, see https://journals.aps.org/prl/supplemental/10.1103/PhysRevLett.115.250401/Supplemental_material_final.pdf";
        s += "\nFormula, J = N11(++) - N12(+0) - N21(0+) - N22(++)";
        s += "\nExplanation, if J > 0 is agrees with QM and if J <=0 it is a classical result\n";

        s += "\nSetting, Count, % Setting, Scaled back to fair";
        s += "\nN11 ++, " + n11+", "+a1b1+", "+n11/a1b1*25.0;
        s += "\nN12 +0, " + n12+", "+a1b2+", "+n12/a1b2*25.0;
        s += "\nN21 0+, " + n21+", "+a2b1+", "+n21/a2b1*25.0;
        s += "\nN22 ++, " + n22+", "+a2b1+", "+n22/a2b2*25.0;

        double j = compute();
        
        double jfair = n11/a1b1*25.0 - n12/a1b2*25.0 - n21/a2b1*25.0 -n22/a2b2*25.0;

        s += "\nJ," + j+", ,"+jfair;

    
        double pj = j / tot;

        s += "\nJ/total," + pj;

        return s;
    }

}
