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
 *
 * @author croth
 */
public abstract class Inequality {

    private Counts counts;

    public Inequality() {
       
    }
    public Inequality(Counts counts) {
        this.counts = counts;
    }

    /*
     @return the preferred angles at detector A in degrees (such as 0, 45)
    */
    public abstract double[] getPreferredA();
    
     /*
     @return the preferred angles at detector B in degrees (such as 0, 45)
    */
    public abstract double[] getPreferredB();
    
    /*
    @return computes the value of the inequality (such as J)
    */
    public abstract double compute() ;

    /*
    @return compute a string representation of the inequality used for output (ideally with
    the individual counts, such as N11 etc, the equation, and the result 
    */
    public abstract String computeString() ;
  
    protected static void p(String s) {
        System.out.println(s);
    }

    /**
     * @return the counts
     */
    public Counts getCounts() {
        return counts;
    }

    /**
     * @param counts the counts to set
     */
    public void setCounts(Counts counts) {
        this.counts = counts;
    }
}
