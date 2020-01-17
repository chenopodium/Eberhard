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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Just a simple class that runs N trias with the given lhv model and inequality
 *
 * @author croth
 */
public class Engine implements Serializable {

    private static final long serialversionUID = 1L;

    static DecimalFormat f = new DecimalFormat("#.##");
    /* The seetings auch as angles a1, a2, b1, bc etc */
    Settings settings;

    /*Random number generator */
    Rand rand;

    /* The model that computes the spin, given an angle and a hidden variable */
    AbstractLHVModel model;

    /* The entangler. Normally not all photons are entangled */
    Entangler entangler;

    /* The counts that are collected at each detector during the experiment */
    Counts counts;

    /* Which in equality to use, such as CH, "Guistina2015" or other (whatever you like)*/
    Inequality inequality;

    /* Just for writing a "log" file with the results */
    String log;

    /* Whether to write the results into a file or not */
    boolean writeLog;

    /* Whether we use a fair random generator or an unfair one */
    boolean fairGenerator;

    /*
    Just a simple class that runs N trias with the given lhv model and inequality
    @param lhv a local hidden variable model
    @param ineq an inequality (like CH)
     */
    public Engine(AbstractLHVModel lhv, Inequality ineq, boolean fairGenerator) {
        this.model = lhv;
        this.settings = model.getSettings();
        this.fairGenerator = fairGenerator;
        createRand();
        // Normally we want to write a log
        this.writeLog = true;
        this.settings = model.getSettings();
        this.inequality = ineq;
        if (inequality == null) {
            inequality = new CH();
        }
        this.entangler = new Entangler();
        this.settings.setA(inequality.getPreferredA());
        this.settings.setB(ineq.getPreferredB());

    }

    private void createRand() {
        //rand = Rand.getRand();
        if (fairGenerator) {
            rand = Rand.getRand();
        } else {
            rand = SkewedRand.getRand(true, 0.1);
        }
        rand.setSeed(settings.getSeed());
    }

    /* Run given number of trials, and if value is specified,
    uses the settings given in values.
    @param trials number of trials
    @values user supplied (random) settings for angles A and B (which can be null)
    @continueExperiment whether this is a new experiment or whether we continue to collect data from a previous run
     */
    public double run(int trials, int[][] values, boolean continueExperiment) {

        if (counts == null || !continueExperiment) {
            counts = new Counts();
            String header = "Setting A, Setting B, Angle A, Angle B, Spin A, Spin B, A Detected, B Detected,  Hidden variable\n";
            writeFile(header, "log.csv", false);
        }
        log = "";
        rand.setTrials(trials);
        entangler.setTrials(trials);
        if (values != null && values.length > 0) {
            trials = values.length;

            for (int t = 0; t < trials; t++) {
                // Use the angles from the user supplied values
                if (values[t][0] >= 0 && values[t][1] >= 0) {
                    runOnePair(rand.randDouble(0, 180), values[t][0], values[t][1]);
                    if (t > 0 && t % 500000 == 0) {
                        p("Trial " + t + " of " + trials + " with " + values[t][0] + " and " + values[t][1]);
                    }
                } else {
                    p("Found illegal settings " + values[t][0] + "/" + values[t][1]);
                }

            }
        } else if (settings.getAngleGenerator() == Settings.LAMBDAGENERATOR.RANDOMANGLES) {
            // Generate random angles for each trial
            for (int t = 0; t < trials; t++) {
                int whichA = rand.randBit();
                int whichB = rand.randBit();
                runOnePair(rand.randDouble(0, 180), whichA, whichB);
                if (t > 0 && t % 500000 == 0) {
                    p("Trial " + t + " of " + trials);
                }
            }
        } else {
            for (int t = 0; t < trials;) {
                // Iterate over all the angles uniformly
                for (double photonAngle = 0; photonAngle < 180; photonAngle++) {
                    int whichA = rand.randBit();
                    int whichB = rand.randBit();
                    runOnePair(photonAngle, whichA, whichB);
                    t++;
                    if (t % 500000 == 0) {
                        p("Trial " + t + " of " + trials );
                    }
                }

            }
        }

        inequality.setCounts(counts);
        if (writeLog) {
            writeResults();
        }
        return inequality.compute();
    }

    /* Measure the spins for one pair of photons.
    This can be done in a symmetrical way (use the same function for both detectors),
    or in an asymmetrical way (see comment in the code below)
    @param photonAngle hidden variable
    @param whichAngleB the angleA (0 or 1) for a1 or a2 (these angles are in degrees)
    @param whichB the angleB (0 or 1) for b1 or b2 (these angles are in degrees)
    @param write just a flag whether to write a log or not
     */
    private void runOnePair(double photonAngleDegree, int whichAngleA, int whichAngleB) {

        double angleA = settings.getA()[whichAngleA];
        double angleB = settings.getB()[whichAngleB];

        /* Note: there is a asymmetrical model which breaks the inequality even more than the symmetrical one
        in this case, use model.computeSpinA and model.computeSpinB for each side.
        But, some people might complain about this since in real experiments, at least it is claimed
        that the two sides are symmetrical.
        So, to make sure there are no complaints like this, we can also just invoke
        computeSpinB both times (please take a look at the code), which is symmetrical.
        The inequality breaking is not that good in this case, but still good enough :-)
         */
        boolean gotEntangledPair = entangler.photonsCreatedAndEntangled();
        int spinA = -1;
        int spinB = -1;
        if (gotEntangledPair) {
            spinA = model.computeSpinB(angleA, photonAngleDegree);
            spinB = model.computeSpinB(angleB, photonAngleDegree);
        }
        

        /* this is just for logging */
        int Adetected = spinA >= 0 ? 1 : 0;
        int Bdetected = spinB >= 0 ? 1 : 0;

        if (writeLog) {
            log += whichAngleA + ", " + whichAngleB + ", " + angleA + ", " + angleB + ", " + spinA + ", " + spinB
                    + ", " + Adetected + ", " + Bdetected + ", " + f.format(photonAngleDegree) + "\n";
        }
        /* Add the counts */
        counts.addResultOfOnePair(whichAngleA, whichAngleB, spinA, spinB);

        if (writeLog && log.length() > 10000) {
            writeFile(log, "log.csv", true);
            log = "";
        }
    }


    /* This method is just for me (Chantal), a super silly thing to find better angles :-).
    I know I know, it could be optimized... but this was just quick way to get angles that
    break the inequality for a situation where I could not compute it with any other means :-).
     */
    public void findAngles(Inequality in) {
        double maxj = 0;
        writeLog = false;
        Settings maxsettings = settings;
        long count = 0;
        for (double a1 = 0; a1 < 90; a1++) {
            settings.getA()[0] = a1;

            for (double a2 = 1; a2 < 90; a2++) {
                if (a1 == a2) {
                    continue;
                }
                settings.getA()[1] = a2;

                for (double b1 = 0; b1 < 45; b1++) {
                    settings.getB()[0] = b1;

                    if (b1 == a1 || b1 == a2) {
                        continue;
                    }
                    for (double b2 = -45; b2 < 45; b2++) {
                        settings.getB()[1] = b2;

                        if (b1 == b2 || b1 == a1 || b1 == a1) {
                            continue;
                        }
                        // resset the counts
                        counts = null;
                        createRand();
                        double j = run(1000, null, false);
                        count++;
                        boolean passed = in.isBroken(j);
                        if (count % 10000 == 0 || (j > maxj * 0.8 && passed)) {
                            String greenBold = "\033[34;1m";
                            String reset = "\033[0m";
                            if (!passed) {
                                greenBold = "";
                                reset = "";
                            } else if (j > maxj * 0.8) {
                                in.setCounts(counts);
                                in.computeString();
                                createRand();
                                double res = this.run(100000, null, false);
                                if (in.isBroken(res)) {
                                    p(getSummary());
                                }

                            }
                            p(count + ": " + greenBold + settings.toShortString() + ", j=" + j + reset
                                    + ", " + f.format(counts.getPercentBothDetected()) + "% detected");
                        }
                        if (in.isBroken(j)) {
                            if (j >= maxj) {
                                maxj = j;
                                maxsettings = settings;
                                p(maxsettings.toShortString());
                                p("j=" + maxj);
                                in.setCounts(counts);//new Guistina2015(counts);
                                in.computeString();

                                counts = null;
                                createRand();
                                double res = this.run(100000, null, false);
                                if (in.isBroken(res)) {
                                    p("\n% detected, " + f.format(counts.getPercentBothDetected()) + "%");
                                    p(getSummary());
                                }
                            }

                        }
                    }
                }
            }
        }
        p(maxsettings.toString());
        p("j=" + maxj);

    }

    public String getCountSummary() {
        double tot = counts.getTotalTrials();
        int sa1 = counts.getSettingA(0);
        int sa2 = counts.getSettingA(1);
        int sb1 = counts.getSettingB(0);
        int sb2 = counts.getSettingB(1);

        double a = counts.getSingleA();
        double b = counts.getSingleB();
        
        double a1b1 = counts.getSettingAB(0, 0) * 100.0 / tot;
        double a1b2 = counts.getSettingAB(0, 1) * 100.0 / tot;
        double a2b1 = counts.getSettingAB(1, 0) * 100.0 / tot;
        double a2b2 = counts.getSettingAB(1, 1) * 100.0 / tot;
        
        String s = "\n\nSetting Counts:";
        s += "\ntotal, " + tot;
        s += "\nSA1, " + sa1 + ", SA2, " + sa2 + ", SB1, " + sb1 + ", SB2, " + sb2;
        s += "\nSA1B1, " + counts.getSettingAB(0, 0) + ", SA1B2, " + counts.getSettingAB(0, 1)
                + ", SA2B1, " + counts.getSettingAB(1, 0) + ", SA1B2, " + counts.getSettingAB(1, 1);
        s += "\n%: SA1, " + sa1 * 100.0 / tot + ", SA2, " + sa2 * 100.0 / tot + ", SB1, " + sb1 * 100.0 / tot + ", SB2, " + sb2 * 100.0 / tot;
        s += "\n%: SA1B1, " +a1b1+ ", SA1B2, " +a1b2
                + ", SA2B1, " +a2b1+ ", SA2B2, " +a2b2;
        
        s += "\n\nMeasurement Counts:\n";
        s += "\nA1, " + counts.getSingleA(0) + ", A2, " + counts.getSingleA(1) + ", B1, " + counts.getSingleB(0) + ", B2, " + counts.getSingleB(1);
        s += "\n% of A: A1%: " + counts.getSingleA(0) * 100 / a + ", A2%: " + counts.getSingleA(1) * 100 / a;
        s += "\n% of B: B1%: " + counts.getSingleB(0) * 100 / b + ", B2%: " + counts.getSingleB(1) * 100 / b;
        s += "\n% of tot: A1%: " + counts.getSingleA(0) * 100 / tot + ", A2%: " + counts.getSingleA(1) * 100 / tot + ", B1%: " + counts.getSingleB(0) * 100 / tot + ", B2%: " + counts.getSingleB(1) * 100 / tot;
        s += "\nA: " + a + ", " + counts.getPercentSingleA() + ", B: " + b + ", " + counts.getPercentSingleB();
        return s;

    }

    private void writeResults() {
        writeFile(log, "log.csv", true);

        String summary = getSummary();
        writeFile(summary, "summary.csv", false);
        p(summary);
    }

    private String getSummary() {
        String summary = settings.toString();
        summary += "\nTrials, " + counts.getTotalTrials() + ", the number of pairs that we have produced in total";
        summary += "\nModel, " + model.getClass().getName() + ", the name of the class that computes the measurement for a photon, an angle at a detector and a hidden variable";
        summary += "\n% detected, " + f.format(counts.getPercentBothDetected()) + "%";
        summary += "\nTotal count , " + counts.getTotalTrials() + "";

        List<Inequality> ins = new ArrayList<>();
        ins.add(inequality);
        //  ins.add(new Guistina2015());
        // ins.add(new CH());
        for (Inequality in : ins) {
            in.setCounts(counts);
            summary += "\n\nInequality, " + in.getClass().getName() + ", the name of the class that contains the inequality formula";
            summary += in.computeString();
        }
        summary += getCountSummary();
        return summary;
    }

    private String getSummary(Inequality in) {
        in.setCounts(counts);
        String summary = "\n\nInequality, " + in.getClass().getName() + ", the name of the class that contains the inequality formula";
        summary += in.computeString();

        return summary;
    }

    /* pure lazyness :-) */
    private static void p(String s) {
        System.out.println(s);
    }

    /* just a simple helper method to write a log file */
    private void writeFile(String s, String file, boolean append) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));
            writer.write(s);
            writer.close();
        } catch (IOException e) {
            p("Could not write fiel " + file + " because " + e.getMessage());
        }
    }

    /**
     * @param writeLog the writeLog to set
     */
    public void setWriteLog(boolean writeLog) {
        this.writeLog = writeLog;
    }

    private void findRnd(Inequality in) {
        double maxj = 0;
        writeLog = false;

        long count = 0;
        for (double bias = 0.62; bias < 0.7; bias += 0.001) {

            rand = SkewedRand.getRand(true, bias);
            double j = run(10000, null, false);
            count++;
            boolean passed = in.isBroken(j);

            String greenBold = "\033[34;1m";
            String reset = "\033[0m";

            if (!passed) {
                greenBold = "";
                reset = "";
            }
            p(count + ": " + greenBold + settings.toShortString() + ", j=" + j + reset
                    + ", " + f.format(counts.getPercentBothDetected()) + "% detected, bias " + bias);
            if (j > maxj * 0.8) {
                in.setCounts(counts);
                in.computeString();
                rand = SkewedRand.getRand(true, bias);
                double res = this.run(100000, null, false);
                if (in.isBroken(res)) {
                    p(getSummary(in));
                    p(getCountSummary());
                }

            }
        }

    }

    private void testRand(Inequality in, double bias) {
        writeLog = false;

        int positive = 0;
        int tot = 100;

        rand = SkewedRand.getRand(true, bias);
        rand.setSeed(settings.getSeed());
        for (int i = 0; i < tot; i++) {
            counts = null;

            double j = run(10000, null, false);
            boolean passed = in.isBroken(j);

            if (passed) {
                positive++;
            }
        }

        double per = positive * 100.0 / (double) tot;
        p("\n------------- Bias " + bias + ", positive " + per + "% (" + positive + " out of " + tot + ")");
        if (per > 50) {
            p(getCountSummary());
            p(getSummary(in));
            for (int t = 0; t < 5; t++) {
                counts = null;
                rand = SkewedRand.getRand(true, bias);

                double j = run(100000, null, false);
                p("j:" + j);
                if (j > 0) {
                    p(getCountSummary());
                    p(getSummary(in));
                }
            }
        }

    }

    /* Main method and entry point for the program */
    public static void main(String[] args) {

        long seed = 1234;
        int trials = 1000000;

        Settings settings = new Settings();
        settings.setSeed(seed);
        settings.setTrials(trials);
        Inequality in = new Guistina2015();
        Engine engine = new Engine(new WangLHVModel(settings), in, false);

        // engine.findAngles(in);
        // engine.run(trials, null, false);
        //engine.findRnd(in);
        for (double bias = 0.40; bias < 0.5; bias += 0.1) {
            engine.testRand(in, bias);
        }
    }

}
