/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pepCalculator;

/**
 *
 * @author P. Obroslak
 */
public class DisplayValue {

    DisplayValue(double Val, String Sym) {
        m_Val = Val;
        m_Sym = Sym;
    }
    
    public double GetVal() { return m_Val; }
    public String GetSym() { return m_Sym; }
    
    private double m_Val;
    private String m_Sym;
}
