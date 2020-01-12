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
 * @see https://www.slideshare.net/gill1109/yet-another-statistical-analysis-of-the-data-of-the-loophole-free-experiments-of-2015-revised
 * The CH inequality of the form N11 + N12 + N21 - N22 - singleA - singleB 
 * The value is <= 0 for classical experiments and >0 for QM
 * @author croth
 */
public class CH extends Inequality implements Serializable{
    private static final long serialversionUID =1L; 

    /* The angles in degrees to use in degrees for detector A and B */
    /*
    private double[] A = {0, 45};  
    private double[] B = {-180 / 16.0, 180 / 16.0};
*/
    private double[] A = {0, 8};  
    private double[] B = {1,-8};
    
    // A1, 0.0 A2, 8.0 B1, 1.0 B2, -8.0, j=374.0
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
    From Richard:
    N12(a, b) – N12(a, b’) + N12(a’, b) +N12(a’, b’) -N1(a’) – N2(b) <= 0

    a, a’, b and b’ are the settings;

    N12 is the total number of double detections which are both “+” (the 12 stands for Lab 1 *and* Lab 2)
    N1 is the number of detections which are “+” in Lab 1
    N2 is the number of detections which are “+” in Lab 2

    Notice that all four N12 are involved, one with a minus sign, the other three with a plus sign.
    The one with a minus sign has settings a’, b
    The two singles counts belong to the other settings a, b’ respectively
     */
    @Override
    public double compute() {
        
        // Plus means detected
        // the first number is lab 1
        // the second number is lab 2
        // 1 is the first angle
        // 2 is the second angle
        int n11 = getCounts().getCoincidenceCounts(0, 0); // N12(a, b)
        int n12 = getCounts().getCoincidenceCounts(0, 1); // N12(a, b’)
        int n21 = getCounts().getCoincidenceCounts(1, 0); // N12(a’, b)
        int n22 = getCounts().getCoincidenceCounts(1, 1); // N12(a’, b’) 
        int sA = getCounts().getSingleA(1);            // N1(a’)
        int sB = getCounts().getSingleB(0);            // N2(b)
        // From Richard: N12(a, b) – N12(a, b’) + N12(a’, b) +N12(a’, b’) -N1(a’) – N2(b) <= 0
        
        int j = n11   //  N12(a, b)
                - n12 //  N12(a, b’)
                + n21 //  N12(a’, b)
                + n22 //  N12(a’, b’) 
                - sA  //  N1(a’) 
                - sB; //  N2(b)

        return j;
    }

    /*
    @return compute a string representation of the inequality used for output (ideally with
    the individual counts, such as N11 etc, the equation, and the result 
     */
    @Override
    public String computeString() {

        int n11 = getCounts().getCoincidenceCounts(0, 0); // N12(a, b)
        int n12 = getCounts().getCoincidenceCounts(0, 1); // N12(a, b’)
        int n21 = getCounts().getCoincidenceCounts(1, 0); // N12(a’, b)
        int n22 = getCounts().getCoincidenceCounts(1, 1); // N12(a’, b’) 
        int sA = getCounts().getSingleA(1);            // N1(a’)
        int sB = getCounts().getSingleB(0);            // N2(b)
               
        String s = "\nName, CH inequality, see https://www.slideshare.net/gill1109/yet-another-statistical-analysis-of-the-data-of-the-loophole-free-experiments-of-2015-revised";
         // From Richard: N12(a, b) – N12(a, b’) + N12(a’, b) +N12(a’, b’) -N1(a’) – N2(b) <= 0
        s +="\nFormula, CH = N11 – N12 + N21 + N22  - singleA(1) – singleB(2) <= 0 ";
        s += "\nExplanation, if J >0 it agrees with QM and if J <= 0 it is a classical result";
        s += "\nN11, " + n11 + "\nN12, " + n12 + "\nN21, " + n21 + "\nN22, " + n22 + 
              "\nsingleA(2) , " + sA + "\nsingleB(1) , " + sB;

        double CH = compute();

        s += "\nCH," + CH;

        int tot = getCounts().getTotalTrials();

        double pCH = CH / (double)tot;

        s += "\nCH/total," + pCH;
       
        return s;
    }

}
