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
 * <p>
 * A simple LHV simulation that breaks the CH inequality (and derivations
 * thereof) with better detection efficiency than the Guistina 2015 experiment.
 * <a href="https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf">LHV
 * Simulation!</a>
 * </p>
 *
 * @see https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf
 * @author croth
 */
public class WangLHVModel extends AbstractLHVModel implements Serializable{
    private static final long serialversionUID =1L; 
    /*
    @see https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf
     */
    public WangLHVModel(Settings settings) {
        super(settings);

    }

    /*
    Compute the spin at detector A. 
    There are two methods for A and B so that we can use an 
    asymmetrical model. 
    If you prefer to use a symmetrical model, just call the same 
    method twice (such as compuetSpinB)
    @param angleA is the angle at detector A in degrees
    @param lamda is the hidden variable (can be anything, usually an angle)
    @return
     +1 means plus
      0 means zero (extraordinary)
     -1 means no detection
     */
    @Override
    public int computeSpinA(double angleA, double lambda) {
        if (lambda >= angleA && lambda <= angleA + 90) {
            return 1;
        } else if (lambda >= angleA + 90 || lambda <= 180) {
            return 0;
        } else {
            return -1;
        }
    }

    /*
    Compute the spin at detector A. 
    There are two methods for A and B so that we can use an 
    asymmetrical model. 
    If you prefer to use a symmetrical model, just call the same 
    method twice (such as compuetSpinB)
    @param angleB is the angle at detector A in degrees
    @param lamda is the hidden variable (can be anything, usually an angle)
    @return
     +1 means plus
      0 means zero (extraordinary)
     -1 means no detection
     */
    @Override
    public int computeSpinB(double angleB, double lambda) {

        double thetaPlus = thetaPlus(angleB);
        double thetaMinus = thetaMinus(angleB);

        double lrad = Math.toRadians(lambda);

        int spinB = -1;

        if (lambda >= thetaPlus && lambda <= thetaPlus + 90) {
            double thetarad = Math.toRadians(thetaPlus);
            double s = Math.abs(Math.sin(2 * (lrad - thetarad)));
            double probB = PbPlus(angleB) * s;
            if (probB > 0) {
                // if (Math.random() <= probB) {
                spinB = 1;
                // }
            }

            return spinB;
        }
        if (lambda >= thetaMinus + 90 && lambda <= thetaMinus + 180) {
            double thetarad = Math.toRadians(thetaMinus);
            double s = Math.abs(Math.sin(2 * (lrad - thetarad)));
            double probB = PbMinus(angleB) * s;
            if (probB > 0) {
                //  if (Math.random() <= probB) {
                spinB = 0;
                //   }
            }
            return spinB;
        }
        // -1 means not detected        
        return -1;
    }

    /*
    @see https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf
    @param angle is in degrees
     */
    private double PbPlus(double angle) {
        double b = Math.toRadians(angle);
        double c = Math.cos(b);
        double s = Math.sin(b);
        double r= settings.getEntanglementEfficiency();
        return (r * r * c * c + s * s) / (1 + r * r);
    }

    /*
    @see https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf
     @param angle is in degrees
     */
    private double PbMinus(double angle) {
        double b = Math.toRadians(angle);
        double c = Math.cos(b);
        double s = Math.sin(b);
        double r= settings.getEntanglementEfficiency();
        return (r * r * s * s + c * c) / (1 + r * r);
    }

    /*
    @see https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf
     */
    private double thetaPlus(double angle) {
        double b = Math.toRadians(angle);
        double c = Math.cos(b);
        double s = Math.sin(b);
        double r= settings.getEntanglementEfficiency();
        double t = (r * r * c * c - s * s) / (r * r * c * c + s * s);
        double theta = Math.acos(t) / 2.0;
        return Math.toDegrees(theta);
    }

    /*
    @see https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf
     @param angle is in degrees
     */
    private double thetaMinus(double angle) {
        double b = Math.toRadians(angle);
        double c = Math.cos(b);
        double s = Math.sin(b);
        double r= settings.getEntanglementEfficiency();
        double t = (-r * r * s * s + c * c) / (r * r * s * s + c * c);
        double theta = Math.acos(t) / 2.0;
        return Math.toDegrees(theta);
    }

}
