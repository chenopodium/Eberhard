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

/**
 * This is more like a negative control of a super trivial "model"
 *
 * 
 * @author croth
 */
public class TrivialModel extends AbstractLHVModel {

    /*
    A dummy model, but it can still break it :-)
    
J, N11++ - N12+0 - N210+ - N22++
(J > 0 is QM, J <=0 is classical)
N11 ++, 456.0
N12 +0, 0.0
N21 0+, 0.0
N22 ++, 367.0
J,89.0
J (prob),0.0445
323: A1, 0.0 A2, 1.0 B1, 5.0 B2, 11.0, j=89.0, 82.95% detected
     */
    public TrivialModel(Settings settings) {
        super(settings);

    }

    /*
      Compute the spin at detector A. 
    There are two methods for A and B so that we can use an 
    asymmetrical model. 
    If you prefer to use a symmetrical model, just call the same 
    method twice (compuetSpinB)
    Here, we use a symmetrical model
    @param angleA is the angle at detector A
    @param lamda is the hidden variable 
    @return
     +1 means plus
      0 means zero (extraordinary)
     -1 means no detection
     */
    @Override
    public int computeSpinA(double angleA, double lambda) {
        return measure(angleA, lambda);
    }

    private int measure(double angle, double lambda) {
        double delta = lambda - angle;
        double a = Math.toRadians(delta);
        
        double pdetect = 2.3*Math.abs(Math.sin(a));
   //     p("angle="+angle+", delta="+delta+", p="+pdetect);
        if (Rand.randDouble()<=pdetect) {
            int spin = -(int) Math.signum(Math.sin(a)); 
            if (spin <0) spin =1;
        //    p("Spin: "+spin);
            return spin;
        }
        else return -1;
    }
    /*
      Compute the spin at detector A. 
    There are two methods for A and B so that we can use an 
    asymmetrical model. 
    If you prefer to use a symmetrical model, just call the same 
    method twice (compuetSpinB)
    Here, we use a symmetrical model
    @param angleB is the angle at detector A
    @param lamda is the hidden variable 
    @return
     +1 means plus
      0 means zero (extraordinary)
     -1 means no detection
     */
    @Override
    public int computeSpinB(double angleB, double lambda) {
       return measure(angleB, lambda);
    }

     private static void p(String s) {
        System.out.println("Trivial:"+s);
    }
}
