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
 * The CH inequality of the form N11 + N12 + N21 - N22 - singleA - singleB 
 * The value is <= 0 for classical experiments and >0 for QM
 * @author croth
 */
public class CH extends Inequality {

    /* The angles to use in degrees for detector A and B */
    private double[] A = {0, 45};
    private double[] B = {-180 / 16.0, 180 / 16.0};

    public CH() {
    }

    public CH(Counts counts) {
        super(counts);
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
     */
    @Override
    public double compute() {

        double n11 = getCounts().getCoincidenceCounts(0, 0);
        double n12 = getCounts().getCoincidenceCounts(0, 1);
        double n21 = getCounts().getCoincidenceCounts(1, 0);
        double n22 = getCounts().getCoincidenceCounts(1, 1);
        double j = n11
                + n12
                + n21
                - n22
                - getCounts().getSingleA()
                - getCounts().getSingleB();

        return j;
    }

    /*
    @return compute a string representation of the inequality used for output (ideally with
    the individual counts, such as N11 etc, the equation, and the result 
     */
    @Override
    public String computeString() {

        /* Using commas so that it can be imported into Excel */
        double n11 = getCounts().getCoincidenceCounts(0, 0);
        double n12 = getCounts().getCoincidenceCounts(0, 1);
        double n21 = getCounts().getCoincidenceCounts(1, 0);
        double n22 = getCounts().getCoincidenceCounts(1, 1);

        String s = "\nJ, N11 + N12 + N21 - N22 - singleA - singleB ";
        s += "\n(J >0 is QM, J <= 0 is classical)";
        s += "\nN11, " + n11 + "\nN12, " + n12 + "\nN21, " + n21 + "\nN22, " + n22 + "\nsingle A , " + getCounts().getSingleA() + "\nsingle B , " + getCounts().getSingleB();

        double j = compute();

        s += "\nJ," + j;

        double tot = getCounts().getTotalTrials();

        double pj = j / tot;

        s += "\nJ (prob)," + pj;
        p(s);
        return s;
    }

}
