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
 * Siimple class that counts the results of pairwise measurements
 *
 * @author croth
 */
public class Counts {

    private int singleA;
    private int singleB;
    private int[][] coincidences;
    private int[][] plus_plus;
    private int[][] plus_zero;
    private int[][] zero_plus;
    private int totalTrials;
    private int bothDetected;

    public Counts() {
        coincidences = new int[2][2];
        plus_plus = new int[2][2];
        plus_zero = new int[2][2];
        zero_plus = new int[2][2];
        totalTrials = 0;
    }

    /* @returns the number of trials where only A had a detection event */
    public double getSingleA() {
        return singleA;
    }

    /* @returns the number of trials where only B had a detection event */
    public double getSingleB() {
        return singleB;
    }

    /* @returns the number of total trials (includes non-detection evets) */
    public int getTotalTrials() {
        return totalTrials;
    }

    /* @returns the percent of trials where only A had a detection event */
    public double getPercentSingleA() {
        return (double) singleA / (double) totalTrials * 100.0;
    }

    /* @returns the percent of trials where only A had a detection event */
    public double getPercentSingleB() {
        return (double) singleB / (double) totalTrials * 100.0;
    }

    /* @returns the percent of trials where a detection was registered both at A and B */
    public double getPercentBothDetected() {
        return (double) bothDetected / (double) totalTrials * 100.0;
    }

    /* @returns the number of trials where the spin measured at A and B was the same (++ or 00) */
    public int getCoincidenceCounts(int whichA, int whichB) {
        return coincidences[whichA][whichB];
    }

     /* @returns the number of trials where the spin measured at A and B was ++ */
    public int getPlusPlusCounts(int whichA, int whichB) {
        return plus_plus[whichA][whichB];
    }
/* @returns the number of trials where the spin measured at A and B was +0 */
    public int getPlusZeroCounts(int whichA, int whichB) {
        return plus_zero[whichA][whichB];
    }
/* @returns the number of trials where the spin measured at A and B was 0+ */
    public int getZeroPlusCounts(int whichA, int whichB) {
        return zero_plus[whichA][whichB];
    }

    /* @returns the percent of trials where the spin measured at A and B was the same  */
    public double getCoincidencePercent(int whichA, int whichB) {
        return (double) coincidences[whichA][whichB] / (double) totalTrials * 100.0;
    }

    /* @returns the percent of trials where the spin measured at A and B was ++ */
    public double getPlusPlusPercent(int whichA, int whichB) {
        return (double) plus_plus[whichA][whichB] / (double) totalTrials * 100.0;
    }

    /* @param whichA The angle to use for A. 0 for a1 and or 1 a2. 
       @param whichB The angle to use for B. 0 for b1 and or 1 b2. 
       @param spinA spin=1 means +, spin=0 means -, spin <0 means not detected
       @param spinB spin=1 means +, spin=0 means -, spin <0 means not detected
     */
    public void addResultOfOnePair(int whichA, int whichB, int spinA, int spinB) {

        totalTrials++;

        if (spinA < 0 && spinB < 0) {
            // BOTH NOT DETECTED  - THEY ARE NOT RECORDED ANYWHERE          
        } else if (spinA < 0) {
            // ONLY B DETECTED
            singleB++;
        } else if (spinB < 0) {
            // ONLY A DETECTED
            singleA++;
        } else {  // BOTH ARE NON NEGATIVE; SO BOTH WERE DETECTED
            bothDetected++;
            if (spinA == spinB) {
                // BOTH ARE THE SAME
                coincidences[whichA][whichB]++;
            }
            if (spinA == 1 && spinB == 1) { // BOTH ARE +
                plus_plus[whichA][whichB]++;
            } else if (spinA == 1 && spinB == 0) { // PLUS AND 0
                plus_zero[whichA][whichB]++;
            } else if (spinA == 0 && spinB == 1) { // 0 AND PLUS
                zero_plus[whichA][whichB]++;
            }
        }
    }

}
