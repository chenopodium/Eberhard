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
public class SkewedRand extends Rand {

    private static final long serialversionUID = 1L;

    int counter;
    double bias;
    static SkewedRand skewed;

    public static Rand getRand(boolean reset, double bias) {
        if (skewed == null) {
            skewed = new SkewedRand();
        }
        if (reset) {
            skewed.counter = 0;
        }
        skewed.bias = bias;
        return skewed;
    }

    public SkewedRand() {
        super();
    }

    @Override
    public void setTrials(int trials) {
        this.trials = trials;
        this.counter = 0;
    }

    /* Random int from from (inclusive) to to (inclusive) */
    @Override
    public int randBit() {

        double per = (double) counter * 100 / 2.0 / trials;
        counter++;

        /*
        0  - 25:  generate 12 equally. Generate fewer photons
        25 - 50:  generate 2 more. Generate a bit more photons
        50 - 75:  generate 1 more. Generate normal photons
        75 - 100: generate 12 equally. Generate fewer photons
        */
        double r = super.randDouble();
        if (per < 28) {
            return r <0.5? 0:1;
        }
        else if (per < 52) {
            return r <0.5 + bias ? 1 :0;
        }
         else if (per < 73) {
            return r <0.5 + bias ? 0 : 1;
        }
         else {
             return r <0.5? 0:1;
         }

    }



}
