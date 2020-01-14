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
    static SkewedRand skewed;
  
    public static Rand getRand(boolean reset) {
        if (skewed == null) {
            skewed = new SkewedRand();
        }
        if (reset) skewed.counter = 0;
        return skewed;
    }

    public SkewedRand() {
        super();
    }

    
    /* Random int from from (inclusive) to to (inclusive) */
    @Override
    public int randBit() {  
        
        double p = (double)counter/2.0/trials;
        counter++;
        
        double r = super.randDouble();
         if (p<0.5) {
             if (r <0.66) return 0;
             else return 1;
         }
         else {
              if (r <0.5) return 0;
             else return 1;
         }
        
    }

    @Override
    public double randDouble() {
        double d= super.randDouble();
        double p = (double)counter/2.0/trials;
       
        return d;
    }

    /* Random int from from (inclusive) to to (exclusive) */
    @Override
    public double randDouble(double from, double to) {
        double p = (double)counter/2.0/trials;
        double d =super.randDouble(from, to);
        return d;
    }


}
