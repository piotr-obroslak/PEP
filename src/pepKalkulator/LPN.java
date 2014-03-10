/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pepKalkulator;

import java.lang.Math.*;

/**
 * Klasa <code>LPN</code> zawiera wzory do wyliczania Z0 linii paskowej
 * niesymetrycznej.
 *
 * <p>Wedlug: B. Smolski: "Tranzystorowe uklady wzmacniajace w zakresie
 * wielkich czestotliwosci" oraz S. Misiaszek "Elementy i Uklady..."
 * (wzory sa prawie takie same; w koncu korzystam z Misiaszka).
 * Wzory opisuja zaleznosci nieco skomplikowane: Z0 jest opisane dwuzakresowo
 * dla dwoch zakresow stosunku w/h;
 * epsilon wystepujacy we wzorze na Z0  nie jest 'normalny' a tzw. efektywny -
 * opisany innym (u Misiaszka tez duzakresowym) wzorem;
 * szerokosc 'we' paska we wzorach na Z0 jest poprawionym 'w'
 * (we=w+DW) - wzor na 'DW' tez jest dwuzakresowy (to ze Smolskiego).
 *
 * @author A. Burd
 * @author P. Obroslak
 */
public class LPN {

    public LPN() {
    }


    /**
     * wyliczenie Z0 dla w/h <= 1
     */
    private double FZ1(double epf, double we, double h) {
        FZType = "FZ1"; /* slad - ktory wzor byl ostatni */
        return 60/Math.sqrt(epf)*Math.log(8*h/we+0.25*we/h);
    }


    /**
     * wyliczenie Z0 dla w/h >= 1
     */
    private double FZ2(double epf, double we, double h) {
        FZType = "FZ2"; /* slad - ktory wzor byl ostatni */
        return 376.7/(Math.sqrt(epf)*(we/h+1.393+0.667*Math.log(we/h+1.444))) ;
    }


    /**
     * poprawka szerokosci dla w/h <= 1/(2ă)
     */
    private double FDW1(double epf, double w, double h) {
        DWType = "DW1"; /* slad - ktory wzor byl ostatni */
        return 1.25/pi*t*(1+Math.log(4*pi*w/t));
    }


    /**
     * poprawka szerokosci dla w/h >= 1/(2ă)
     */
    private double FDW2(double epf, double w, double h) {
        DWType = "DW2"; /* slad - ktory wzor byl ostatni */
        return 1.25/pi*t*(1+Math.log(2*h/t));
    }


    /**
     * epsilon efektywny
     */
    private double FEPS(double er, double h, double w, double t) {
        return (er+1)/2+((er-1)/2)/(Math.sqrt(1+10*h/w))-(er-1)/4.6*t/(h*Math.sqrt(w/h));
    }


    /**
     * epsilon efektywny wg skryptu Misiaszka - wzor dzielony
     */
    private double FEPSM(double er, double h, double w, double t) {
        if (w/h < 1) {
            return (er+1)/2+((er-1)/2)*(1/Math.sqrt(1+12*h/w)+0.04*(1-w/h)*(1-w/h))-(er-1)/4.6*t/(h*Math.sqrt(w/h));
        }
        else {
            return (er+1)/2+((er-1)/2)/(Math.sqrt(1+12*h/w))-(er-1)/4.6*t/(h*Math.sqrt(w/h));
        }
    }

    
    // attributes...
    private static final String
            FName   = "LPN.cfg";

    private static final double
            pi      = Math.PI,
            dwapi   = 2.*pi,
            odw2pi  = 1./(dwapi);

    private static final double
            ErrMax  = 1e-3; /** to jest dopuszczalny blad wzgledny */

    private static final double
            krokMin = 1e-5,
            krokMax = 0.2;


    private String
            FZType,
            DWType;

    private double
            t;
}
