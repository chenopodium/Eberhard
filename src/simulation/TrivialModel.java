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
 * This is more like a negative control of a super trivial "model"
 *
 * 
 * @author croth
 */
public class TrivialModel extends AbstractLHVModel implements Serializable{
    private static final long serialversionUID =1L; 
    
    private Rand rand;
    /*
    A dummy model
     */
    public TrivialModel(Settings settings) {
        super(settings);
        rand = Rand.getRand();

    }

    /*
      Compute the spin at detector A. 
    There are two methods for A and B so that we can use an 
    asymmetrical model. 
    If you prefer to use a symmetrical model, just call the same 
    method twice (compuetSpinB)
    Here, we use a symmetrical model
    @param angleA is the angle at detector A in degrees
    @param lambda is the hidden variable (can be anything, usually an angle)
    @return
     +1 means plus
      0 means zero (extraordinary)
     -1 means no detection
     */
    @Override
    public int computeSpinA(double angleAtDetector, double lambda) {
        return measure(angleAtDetector, lambda);
    }

    private int measure(double angleAtDetector, double lambda) {
        double delta = lambda - angleAtDetector;
        double a = Math.toRadians(delta);
        
        double pdetect = 2.3*Math.abs(Math.sin(a));
   
        /*
        double theta = vars.getTheta();      
        double a = Math.toRadians(theta + detector.getAngleInDegrees());
        double pdetect = 2.3*Math.abs(Math.sin(a));        
        if (Math.random()<=pdetect) return true;
        */
        if (rand.randDouble()<=pdetect) {
             // we detected it
            int spin = -(int) Math.signum(Math.sin(a)); 
            if (spin <0) spin =0;
        
            return spin;
        }
        else return -1; // we did not detect it
    }
    /*
      Compute the spin at detector A. 
    There are two methods for A and B so that we can use an 
    asymmetrical model. 
    If you prefer to use a symmetrical model, just call the same 
    method twice (compuetSpinB)
    Here, we use a symmetrical model
    @param angleB is the angle at detector A in degrees
    @param lamda is the hidden variable (can be anything, usually an angle)
    @return
     +1 means plus
      0 means zero (extraordinary)
     -1 means no detection
     */
    @Override
    public int computeSpinB(double angleAtDetector, double lambda) {
       return measure(angleAtDetector, lambda);
    }

     private static void p(String s) {
        System.out.println("Trivial:"+s);
    }
}
