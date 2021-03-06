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
import java.util.Random;

/**
 *
 * Simple random generator. Feel free to use predefined values or to use a
 * different random generator (It really should not make a difference...)
 *
 * @author croth
 */
public class Rand implements Serializable {

    private static final long serialversionUID = 1L;

    private Random generator;

    private static Rand rand;

    protected int trials;

    public static Rand getRand() {
        if (rand == null) {
            rand = new Rand();
        }
        return rand;
    }

    public Rand() {
        generator = new Random();
    }

    /* Random int from from (inclusive) to to (inclusive) */
    public int randBit() {
        double d = randDouble();
        if (d > 0.5) {
            return 1 ;
        } else {
            return 0;
        }
    }

    public double randDouble() {
        return generator.nextDouble();
    }

    /* Random int from from (inclusive) to to (exclusive) */
    public double randDouble(double from, double to) {
        return generator.nextDouble() * (to - from) + from;
    }

    public void setSeed(long s) {
        generator.setSeed(s);
    }

    /**
     * @param trials the trials to set
     */
    public void setTrials(int trials) {
        this.trials = trials;
    }

}
