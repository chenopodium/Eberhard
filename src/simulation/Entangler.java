/*
 * The MIT License
 *
 * Copyright 2020 croth.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package simulation;

import java.io.Serializable;

/**
 *
 * @author croth
 */
public class Entangler implements Serializable {

    private static final long serialversionUID = 1L;

    private int trials;
    private int counter;

    double eff = 1.0;
    Rand rand;

    double factor = 1.9;

    public Entangler() {
        rand = Rand.getRand();
    }

    public boolean photonsCreatedAndEntangled() {
        counter++;
        double per = counter * 100.0 / trials;
        double r = rand.randDouble();

        /*
        0  - 25:  generate 12 equally. Generate fewer photons
        25 - 50:  generate 2 more. Generate a bit more photons
        50 - 75:  generate 1 more. Generate normal photons
        75 - 100: generate 12 equally. Generate fewer photons
         */
        if (per < 28) {
            return r < eff / factor / factor;
        } else if (per < 52) {
            return r < eff / factor;
        } else if (per < 73) {
            return r < eff;
        } else {
            return r < eff / factor;
        }

    }

    /**
     * @return the trials
     */
    public int getTrials() {
        return trials;
    }

    /**
     * @param trials the trials to set
     */
    public void setTrials(int trials) {
        this.trials = trials;
        this.counter = 0;
    }
}
