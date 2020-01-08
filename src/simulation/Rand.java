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

import java.util.Random;

/**
 *
 * Simple random generator. Feel free to use predefined values or to use a
 * different random generator (It really should not make a difference...)
 *
 * @author croth
 */
public class Rand {

    static Random rand = new Random();

    static long seed = 1234;
    static {
        rand.setSeed(seed);
    }

    /* Random int from from (inclusive) to to (inclusive) */
    public static int randInt(int from, int to) {
        return rand.nextInt(to - from + 1) + from;
    }

    public static double randDouble() {
        return rand.nextDouble();
    }

    /* Random int from from (inclusive) to to (exclusive) */
    public static double randDouble(double from, double to) {
        return rand.nextDouble() * (to - from) + from;
    }

    public static long getSeed() {
        return seed;
    }
    public static void setSeed(long s) {
        seed = s;
        rand.setSeed(s);
    }

}
