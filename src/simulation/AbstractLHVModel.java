/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

/**
 *
 * @author croth
 */
public abstract class AbstractLHVModel {

    /* The settings to be used that contains the angles a1, a2, b1 and b2.
    It also contains the entanglement efficiency
     */
    protected Settings settings;

    public AbstractLHVModel(Settings settings) {
        this.settings = settings;
      
    }

    /*
    @param angleA is the angle at detector A
    @param lamda is the hidden variable 
    @return
     +1 means plus
      0 means zero (extraordinary)
     -1 means no detection
     */
    public abstract int computeSpinA(double angleA, double lambda);

    /*
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
