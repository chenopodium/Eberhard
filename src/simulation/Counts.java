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
 * Siimple class that counts the results of pairwise measurements
 *
 * @author croth
 */
public class Counts implements Serializable {

    private static final long serialversionUID = 1L;

    private int[] singleA;
    private int[] singleB;
    private int[][] detected;
    private int[][] plusplus;
    private int[][] tot;
    private int[][] coincidence;
    private int[][] det_zero;
    private int[][] zero_det;
    private int[] Acounts;
    private int[] Bcounts;

    private int totalTrials;
    private int bothDetected;

    public Counts() {
        Acounts = new int[2];
        Bcounts = new int[2];
        plusplus = new int[2][2];
        detected = new int[2][2];
        coincidence = new int[2][2];
        det_zero = new int[2][2];
        zero_det = new int[2][2];
        tot = new int[2][2];
        singleA = new int[2];
        singleB = new int[2];

        totalTrials = 0;
    }

    /* @returns the number of trials where only A had a detection event */
    public int getSingleA() {
        return singleA[0] + singleA[1];
    }

    public int getSingleA(int whichA) {
        return singleA[whichA];
    }

    /* @returns the number of trials where only B had a detection event */
    public int getSingleB() {
        return singleB[0] + singleB[1];
    }

    public int getSingleB(int whichB) {
        return singleB[whichB];
    }

    /* @returns the number of total trials (includes non-detection evets) */
    public int getTotalTrials() {
        return totalTrials;
    }

    /* @returns the percent of trials where only A had a detection event */
    public double getPercentSingleA() {
        return (double) getSingleA() / (double) totalTrials * 100.0;
    }

    /* @returns the percent of trials where only A had a detection event */
    public double getPercentSingleB() {
        return (double) getSingleB() / (double) totalTrials * 100.0;
    }

    /* @returns the percent of trials where a detection was registered both at A and B */
    public double getPercentBothDetected() {
        return (double) bothDetected / (double) totalTrials * 100.0;
    }

    /* @returns the number of trials where the spin measured at A and B was ++ */
    public int getDetected(int whichA, int whichB) {
        return detected[whichA][whichB];
    }

    /* @returns the number of trials where the spin measured at A and B was ++ */
    public double getCorr(int whichA, int whichB) {
        return (double) coincidence[whichA][whichB] / (double) tot[whichA][whichB];
    }

    /* @returns the number of trials where the spin measured at A and B was ++ */
    public int getCoincidenceCounts(int whichA, int whichB) {
        return coincidence[whichA][whichB];
    }

    /* @returns the number of trials where the spin measured at A and B was +0 */
    public int getDetZero(int whichA, int whichB) {
        return det_zero[whichA][whichB];
    }

    /* @returns the number of trials where the spin measured at A and B was 0+ */
    public int getZeroDet(int whichA, int whichB) {
        return zero_det[whichA][whichB];
    }

    /* @returns the percent of trials where the spin measured at A and B was ++ */
    public double getDetectedPercent(int whichA, int whichB) {
        return (double) detected[whichA][whichB] / (double) totalTrials * 100.0;
    }

    /* @param whichA The angle to use for A. 0 for a1 and or 1 a2. 
       @param whichB The angle to use for B. 0 for b1 and or 1 b2. 
       @param spinA spin=1 means +, spin=0 means -, spin <0 means not detected
       @param spinB spin=1 means +, spin=0 means -, spin <0 means not detected
     */
    public void addResultOfOnePair(int whichA, int whichB, int spinA, int spinB) {

        totalTrials++;

        boolean Adetected = spinA >= 0;
        boolean Bdetected = spinB >= 0;

        if (spinA == 1) {
            singleA[whichA]++;
        }
        if (spinB == 1) {
            singleB[whichB]++;
        }

        if (totalTrials < 10) {
            //   p("whichA: "+whichA+", whichB: "+whichB+", spinA: "+spinA+", spinB: "+spinB);
        }
        if (!Adetected && !Bdetected) {
            // BOTH NOT DETECTED  - THEY ARE NOT RECORDED ANYWHERE          
        } else if (!Adetected) {  // ONLY B DETECTED
            zero_det[whichA][whichB]++;
        } else if (!Bdetected) {  // ONLY A DETECTED
            det_zero[whichA][whichB]++;
        } else if (Adetected && Bdetected) { // BOTH ARE DETECTED, 
            tot[whichA][whichB]++;
            bothDetected++;
            detected[whichA][whichB]++;
            if (spinA == spinB) {
                coincidence[whichA][whichB]++;
            }
            if (spinA == 1 && spinB == 1) {
                plusplus[whichA][whichB]++;
            }
        }
    }

    private void p(String s) {
        System.out.println("Counts: " + s);
    }

}
