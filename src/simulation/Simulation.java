/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
 */
public class Simulation {

    /* Main method and entry point for the program */
    public static void main(String[] args) {
        p("This simulation uses the model by F. Wang\n" + "https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf");
        p("Arguments:");
        p("-file filename: the file with random settings for A and B (see details below)");
        p("-seed seed: the random seed (a number like 12346). The default is 1234");
        p("-trials nr trials: the number of pairs that are generated (default is 100000) (This is plenty... larger values just make it slower)");
        p("-inequality: CH or Guistina.");
        p("             CH uses N11 + N12 + N21 - N22 - singleA - singleB (<0 is classical)");
        p("             Guistina uses N11++ - N12+0 - N210+ - N22++  (<0 is classical)");
        p("\nExample: java -jar simulation.jar -file c:\\myrandomnumbers.txt -seed 12345  -inequality CH");
        p("\nThe file should be a simple text file with one line for each pair, such as 1,2");
        p("The first number is which angle to use for A (1 or 2), the second is which angle to use for B (1 or 2)");
        p("\nThe results are written to a file summary.csv and also to a more detailed log.csv file with the input angles and counts for each run");

        int[][] values = null;
        long seed = 1234;
        String model = "G";
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
                        model = "CH";
                    } else if (value.startsWith("G")) {
                        model = "GUISTINA";
                    }
                }

            }

        }
        Rand.setSeed(seed);
        Inequality in;
        if (model.startsWith("C")) {
            in = new CH();
        } else {
            in = new Guistina2015();
        }
        Engine engine = new Engine(new Settings(), in);

       // engine.run(trials, values, true);
        engine.findAngles(in);
        
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
                    p("Using separator "+sep);
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
                p("Setting " + line + " is not valid. Expecting 2 values, but got "+it.length+". Values is "+Arrays.toString(it));
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
        p("Found "+values.length+" settings in file "+file);
        return values;
    }

    private static void p(String s) {
        System.out.println(s);
    }

}
