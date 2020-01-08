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

/**
 *
 * @author croth
 * @see https://journals.aps.org/prl/supplemental/10.1103/PhysRevLett.115.250401/Supplemental_material_final.pdf
 * @see https://physics.aps.org/featured-article-pdf/10.1103/PhysRevLett.115.250401
 */
public class Guistina2015 extends Inequality {

     /* The angles to use in degrees for detector A and B */
    //private double[] A = {0, 2};
    // private double[] B = {1, -45};
    
   
    private double[] A = {0, 1};
    private double[] B = {2, -39};

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

        double n11 = getCounts().getPlusPlusCounts(0, 0);
        double n12 = getCounts().getPlusZeroCounts(0, 1);
        double n21 = getCounts().getZeroPlusCounts(1, 0);
        double n22 = getCounts().getPlusPlusCounts(1, 1);

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

        double n11 = getCounts().getPlusPlusCounts(0, 0);
        double n12 = getCounts().getPlusZeroCounts(0, 1);
        double n21 = getCounts().getZeroPlusCounts(1, 0);
        double n22 = getCounts().getPlusPlusCounts(1, 1);

        /* Using commas so that it can be imported into Excel */
        String s = "\nJ, N11(++) - N12(+0) - N21(0+) - N22(++)";
        s += "\n(J > 0 is QM, J <=0 is classical)";

        s += "\nN11 ++, " + n11;
        s += "\nN12 +0, " + n12;
        s += "\nN21 0+, " + n21;
        s += "\nN22 ++, " + n22;

        double j = compute();

        s += "\nJ," + j;

        double tot = getCounts().getTotalTrials();

        double pj = j / tot;

        s += "\nJ (prob)," + pj;
        p(s);
        return s;
    }

}
