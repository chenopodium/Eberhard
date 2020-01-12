/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

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

@author Chantal Roth
@see https://journals.aps.org/prl/supplemental/10.1103/PhysRevLett.115.250401/Supplemental_material_final.pdf
@see https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf
@see https://physics.aps.org/featured-article-pdf/10.1103/PhysRevLett.115.250401
@see https://pub.math.leidenuniv.nl/~gillrd/Peking/Peking_4.pdf
@see https://en.wikipedia.org/wiki/CHSH_inequality
@see https://plato.stanford.edu/entries/bell-theorem/
@see https://pdfs.semanticscholar.org/8864/c5214a30a7acd8d186f53e8991cd8bc88f84.pdf
@see http://www.askingwhy.org/blog/first-puzzle-just-a-probability/puzzle-piece-6-disentangling-the-entanglement/
 */
public class Simulation {

    /* Main method and entry point for the program */
    public static void main(String[] args) {
        p("Please see the README.TXT for instructions");
        int[][] values = null;
        long seed = 1234;
        String ineq = "C";
        String model = "W";
        String mode = "RESTART";
        String statefile = "saved.ser";
        int trials = 100000;

        if (args != null && args.length > 1) {
            for (int i = 0; i + 1 < args.length; i += 2) {
                String key = args[i].toUpperCase();
                String value = args[i + 1];
                if (key.startsWith("-")) {
                    key = key.substring(1);
                }
                if (key.startsWith("F")) {
                    File f = new File(value);
                    values = readSettings(f);
                } else if (key.startsWith("S")) {
                    try {
                        seed = Long.parseLong(value);
                    } catch (Exception ex) {
                        p("Could not convert " + value + " to long. Try something like 24252");

                    }
                } else if (key.startsWith("T")) {
                    try {
                        trials = Integer.parseInt(value);
                    } catch (Exception ex) {
                        p("Could not convert " + value + " to int. Try something like 10000");
                    }
                } else if (key.startsWith("I")) {
                    value = value.toUpperCase();
                    if (value.startsWith("C")) {
                        ineq = "CH";
                    } else if (value.startsWith("G")) {
                        ineq = "GUISTINA";
                    }
                } else if (key.startsWith("MODEL")) {
                    value = value.toUpperCase();
                    if (value.startsWith("T")) {
                        ineq = "TRIVIAL";
                    } else if (value.startsWith("W")) {
                        ineq = "WANG";
                    }
                } else if (key.startsWith("MODE")) {
                    value = value.toUpperCase();
                    if (value.startsWith("C")) {
                        mode = "CONTINUE";
                    } else {
                        mode = "RESTART";
                    }

                }

            }

        }

        Engine engine = null;
        Settings settings = new Settings();
        settings.setSeed(seed);
        settings.setTrials(trials);

        boolean continueExperiment = false;
        if (mode.equalsIgnoreCase("CONTINUE")) {
            p("Attempting to continue last run using file " + statefile);
            engine = loadModel(statefile);
            if (engine == null) {
                p("I was not able to read the file " + statefile);
            } else {
                continueExperiment = true;
            }
        } else {
            Inequality in;
            AbstractLHVModel lhv;
            if (ineq.startsWith("C")) {
                in = new CH();

            } else {
                in = new Guistina2015();
            }

            if (model.startsWith("T")) {
                lhv = new TrivialModel(settings);

            } else {
                lhv = new WangLHVModel(settings);
            }

            engine = new Engine(lhv, in);
   engine.findAngles(in);
        }

         engine.run(trials, values, continueExperiment);
        // save model to a file with all settings, in case we want to continue
        saveModel(engine, statefile);
        System.exit(0);
    }

    /* Read the settings to be used froma file.
    The file should consist of one line per experiment (per pair), such as
    0, 1
    The first value is the setting to be used at detector A (angle a1 or a2)
    The second value is the setting to be used at detector B (anngle b1 or b2)
    The values should be separated by , or ; or tab (but consistently :-)
    Only 0 and 1 are allowed (as there are only 2 settings per detector :-)
     */
    private static int[][] readSettings(File file) {
        ArrayList<String> lines = new ArrayList<>();
        String sep = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                if (sep == null) {
                    if (line.contains(",")) {
                        sep = ",";
                    } else if (line.contains("\t")) {
                        sep = "\t";
                    } else if (line.contains(";")) {
                        sep = ";";
                    } else if (line.contains(" ")) {
                        sep = " ";
                    } else {
                        continue;
                    }
                    p("Using separator " + sep);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int values[][] = new int[lines.size()][2];
        int pos = -1;
        for (String line : lines) {
            pos++;
            String[] it = line.split(sep);
            values[pos][0] = -1;
            values[pos][1] = -1;
            if (it.length < 2) {
                p("Setting " + line + " is not valid. Expecting 2 values, but got " + it.length + ". Values is " + Arrays.toString(it));
                continue;
            }
            int s0 = Integer.parseInt(it[0]);
            int s1 = Integer.parseInt(it[1]);
            if (s0 < 0 || s0 > 1 || s1 < 0 || s1 > 1) {
                p("Setting " + line + " is not valid. Expecting setting 1 and 2 only");
                continue;
            }
            values[pos][0] = s0;
            values[pos][1] = s1;

        }
        p("Found " + values.length + " settings in file " + file);
        return values;
    }

    private static void p(String s) {
        System.out.println(s);
    }

    private static Engine loadModel(String filename) {
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            Engine engine = (Engine) in.readObject();

            in.close();
            file.close();
            return engine;
        } catch (Exception ex) {
            p("I could not read the saved state from file " + filename + " because: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    private static void saveModel(Engine engine, String filename) {
        try {
            // Saving the current counts, seed and settings to a file 
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(engine);
            out.close();
            file.close();
        } catch (IOException ex) {
            p("I could not save the current state to file " + filename + " because: " + ex.getMessage());
        }
    }
}
