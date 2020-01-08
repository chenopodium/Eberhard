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
import java.text.DecimalFormat;

/**
 *
 * @author croth
 */
public class Engine {

    static DecimalFormat f = new DecimalFormat("#.##");
    Settings settings;
    Rand rand = new Rand();
    AbstractLHVModel model;
    Counts counts;
    String log;
    Inequality inequality;

    public Engine(AbstractLHVModel lhv, Inequality ineq) {
        this.model = lhv;
        if (model == null) {
            model = new WangLHVModel(settings);
        }
        this.settings = model.getSettings();
        this.inequality = ineq;
        if (inequality == null) {
            inequality = new CH();
        }
        this.settings.A = inequality.getPreferredA();
        this.settings.B = ineq.getPreferredB();

    }

    public double run(int trials, int[][] values, boolean writeLog) {
        counts = new Counts();
        String top = settings.toString();

        if (values != null && values.length > 0) {
            if (writeLog) {
                p("Using user supplied random settings for angles (" + values.length + " values)");
            }
            trials = values.length;
            settings.angleGenerator = Settings.LAMBDAGENERATOR.SUPPLIED;
        } else {
            if (writeLog) {
                p("Using random setting " + settings.angleGenerator);
            }
        }

        if (writeLog) {
            top += "\nTrials, " + trials;
            top += "\nModel, " + model.getClass().getName();
            top += "\nInequality, " + inequality.getClass().getName();
            p(top);
            log = top + "\n\nSetting A, Setting B, Angle A, Angle B, Spin A, Spin B, Both Detected, Coincidence, Hidden variable\n";
            writeFile(log, "log.csv", false);
        }
        log = "";
        if (settings.angleGenerator == Settings.LAMBDAGENERATOR.SUPPLIED) {
            for (int t = 0; t < trials; t++) {
                if (values[t][0] >= 0 && values[t][1] >= 0) {
                    runOnePair(rand.randDouble(0, 180), values[t][0], values[t][1], writeLog);
                    if (t > 0 && t % 500000 == 0) {
                        p("Trial " + t + " of " + trials + " with " + values[t][0] + " and " + values[t][1]);
                    }
                } else {
                    p("Found illegal settings " + values[t][0] + "/" + values[t][1]);
                }
            }
        } else if (settings.angleGenerator == Settings.LAMBDAGENERATOR.RANDOMANGLES) {
            for (int t = 0; t < trials; t++) {
                int whichA = rand.randInt(0, 1);
                int whichB = rand.randInt(0, 1);
                runOnePair(rand.randDouble(0, 180), whichA, whichB, writeLog);
                if (t > 0 && t % 500000 == 0) {
                    p("Trial " + t + " of " + trials + " with " + values[t][0] + " and " + values[t][1]);
                }
            }
        } else {
            for (int t = 0; t < trials;) {
                for (double photonAngle = 0; photonAngle < 180; photonAngle++) {
                    int whichA = rand.randInt(0, 1);
                    int whichB = rand.randInt(0, 1);
                    runOnePair(photonAngle, whichA, whichB, writeLog);
                    t++;
                    if (t % 500000 == 0) {
                        p("Trial " + t + " of " + trials + " with " + values[t][0] + " and " + values[t][1]);
                    }
                }

            }
        }
        inequality.setCounts(counts);

        if (writeLog) {
            String end = "\n" + inequality.computeString();
            end += "\n% detected, " + f.format(counts.getPercentBothDetected()) + "%";
            end += "\nTotal count , " + counts.getTotalTrials() + "";
            log += end;
            String summary = top + end;
            p(end);
            writeFile(log, "log.csv", true);
            writeFile(summary, "summary.csv", false);
        }
        return inequality.compute();
    }

    private void writeFile(String s, String file, boolean append) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));
            writer.write(s);
            writer.close();
        } catch (IOException e) {
            p("Could not write fiel " + file + " because " + e.getMessage());
        }
    }

    private void runOnePair(double photonAngle, int whichA, int whichB, boolean write) {

        double A = settings.A[whichA];
        double B = settings.B[whichB];

        /* Note: there is a asymmetrical model which breaks the inequality even more than the symmetrical one
        in this case, use model.computeSpinA and computeSpinB for each side.
        But, some people might complain about this since in real experiments, at least it is claimed
        that the two sides are symmetrical.
        So, to make sure there are no complaints like this, we can also just invoke
        computeSpinB both times (please take a look at the code), which is symmetrical.
        The inequality breaking is not that good in this case, but still good enough :-)
        */
        int spinA = model.computeSpinB(A, photonAngle);
        int spinB = model.computeSpinB(B, photonAngle);
       
        /* this is just for logging */
        boolean detected = (spinA >= 0 && spinB >= 0);
        boolean coinc = (spinA == spinB && spinB >= 0);
        if (write) {
            log += whichA + ", " + whichB + ", " + A + ", " + B + ", " + spinA + ", " + spinB + 
                    ", " + (detected ? 1 : 0) + ", " + (coinc ? 1 : 0) + ", " + f.format(photonAngle) + "\n";
        }
        /* Add the counts */
        counts.addResultOfOnePair(whichA, whichB, spinA, spinB);

        if (log.length() > 10000 && write) {
            writeFile(log, "log.csv", true);
            log = "";
        }
    }

    private static void p(String s) {
        System.out.println(s);
    }

    public void findAngles(Inequality in) {
        double maxj = 0;
        Settings maxsettings = settings;
        long count = 0;
        for (double a1 = 0; a1 < 90; a1++) {
            settings.A[0] = a1;

            for (double a2 = 1; a2 < 90; a2++) {
                if (a1 == a2) {
                    continue;
                }
                settings.A[1] = a2;

                for (double b1 = 0; b1 < 45; b1++) {
                    settings.B[0] = b1;

                    if (b1 == a1 || b1 == a2) {
                        continue;
                    }
                    for (double b2 = -45; b2 < 45; b2++) {
                        settings.B[1] = b2;

                        if (b1 == b2 || b1 == a1 || b1 == a1) {
                            continue;
                        }
                        double j = run(2000, null, false);
                        count++;
                        if (count % 10000 == 0 || j > maxj * 0.8) {
                            String greenBold = "\033[34;1m";
                            String reset = "\033[0m";
                            if (j <= 0) {
                                greenBold = "";
                                reset = "";
                            } else if (j > maxj * 0.8) {
                                in.setCounts(counts);//new Guistina2015(counts);
                                in.computeString();

                            }
                            p(count + ": " + greenBold + settings.toShortString() + ", j=" + j + reset
                                    + ", " + f.format(counts.getPercentBothDetected()) + "% detected");
                        }
                        if (j >= 0) {
                            if (j >= maxj) {
                                maxj = j;
                                maxsettings = settings;
                                p(maxsettings.toShortString());
                                p("j=" + maxj);
                                in.setCounts(counts);//new Guistina2015(counts);
                                in.computeString();
                                p("\n% detected, " + f.format(counts.getPercentBothDetected()) + "%");
                            }

                        }
                    }
                }
            }
        }
        p(maxsettings.toString());
        p("j=" + maxj);

    }

}
