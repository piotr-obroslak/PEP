/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pepKalkulator;

/**
 *
 * @author P. Obroslak
 */

class NormalizedVal {

    NormalizedVal(double Value, int Exponent) {
        m_Value = Value;
        m_Exponent = Exponent;
    }

    public double getValue() { return m_Value; }
    public int getExponent() { return m_Exponent; }

    private double m_Value;
    private int m_Exponent;
}

public class Norm {
    
    public static double EnsureInRange(double val, double min, double max) {
        double r = val;
        
        if (val < min)
            return min;

        if (val > max)
            return max;
        
        return r;
    }

    public static double Round(double Value, int DecimalDigits) {

        if (DecimalDigits < 0)
            return 0.;

        int Mul = 1;
        while (DecimalDigits-- > 0) {
            Mul *= 10;
        }
        
        return (double)(Math.round(Value * Mul)) / Mul;
    }

    public static DisplayValue GetDispVal(double Value) {
        NormalizedVal nv = NormalizeThousand(Value);
        return new DisplayValue(Round(nv.getValue(), 3), SymFromExp(nv.getExponent()));
    }
    
    public static double GetValueFromString(String str) {

        if (str.length() == 0)
            throw new NumberFormatException();

        /* the last character doesn't have to be a part of the number */
        double multiplier = 1.;
        
        char ch = str.charAt(str.length()-1);
        if (!Character.isDigit(ch))
        {
            switch (ch) {
                case 'f':
                    multiplier = 1e-15;
                    break;
                    
                case 'p':
                    multiplier = 1e-12;
                    break;
                    
                case 'n':
                    multiplier = 1e-9;
                    break;
                    
                case 'u':
                    multiplier = 1e-6;
                    break;
                    
                case 'm':
                    multiplier = 1e-3;
                    break;
                    
                case 'k':
                    multiplier = 1e+3;
                    break;
                    
                case 'M':
                    multiplier = 1e+6;
                    break;
                    
                case 'G':
                    multiplier = 1e+9;
                    break;
                    
                case 'T':
                    multiplier = 1e+12;
                    break;
                    
                default:
                    throw new NumberFormatException();
            }
            
            str = str.substring(0, str.length()-1);
        }
        
        return Double.parseDouble(str) * multiplier;
    }
    
    private static NormalizedVal NormalizeTen(double Value) {
        
        int Exp = 0;
        double Val = Value;
        
        while (Val < 1.)
        {
            Val *= 10.;
            --Exp;
        }
        
        while (Val >= 10.)
        {
            Val /= 10.;
            ++Exp;
        }
        
        return new NormalizedVal(Val, Exp);
    }

    private static NormalizedVal NormalizeThousand(double Value) {
        
        int Idx = 0;
        while (Exponents[++Idx] != 0);

        double Val = Value;

        while ((Val<1.) && (Idx>0))
        {
            Val *= 1000.;
            --Idx;
        }
        
        while ((Val >= 1000.) && (Idx<Exponents.length-1))
        {
            Val /= 1000.;
            ++Idx;
        }
        
        return new NormalizedVal(Val, Exponents[Idx]);
    }
    
    private static String SymFromExp(int Exponent) {
        
        switch (Exponent)
        {
            case -15: return "f";
            case -12: return "p";
            case -9: return "n";
            case -6: return "\u03bc";
            case -3: return "m";
            case 0: return "";
            case 3: return "k";
            case 6: return "M";
            case 9: return "G";
            case 12: return "T";
        }

        return "?";
    }
        
    private static final int Exponents[] = {
        -15, -12, -9, -6, -3, 0, 3, 6, 9, 12
    };
}
