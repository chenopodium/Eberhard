/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.io.Serializable;

/**
 *
 * @author croth
 */
public abstract class AbstractLHVModel implements Serializable{
    private static final long serialversionUID =1L; 
    /* The settings to be used that contains the angles a1, a2, b1 and b2.
    It also contains the entanglement efficiency
     */
    protected Settings settings;

    public AbstractLHVModel(Settings settings) {
        this.settings = settings;
      
    }

    /*
    Compute the spin at detector A. 
    There are two methods for A and B so that we can use an 
    asymmetrical model. 
    If you prefer to use a symmetrical model, just call the same 
    method twice (compuetSpinB)
    @param angleA is the angle at detector A
    @param lamda is the hidden variable 
    @return
     +1 means plus
      0 means zero (extraordinary)
     -1 means no detection
     */
    public abstract int computeSpinA(double angleA, double lambda);

    /*
    Compute the spin at detector A. 
    There are two methods for A and B so that we can use an 
    asymmetrical model. 
    If you prefer to use a symmetrical model, just call the same 
    method twice (compuetSpinB)
    @param angleB is the angle at detector A
    @param lamda is the hidden variable 
    @return
     +1 means plus
      0 means zero (extraordinary)
     -1 means no detection
     */
    public abstract int computeSpinB(double angleB, double lambda);

    public Settings getSettings() {
        return settings;
    }

}
