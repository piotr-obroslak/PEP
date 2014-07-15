/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pepCalculator;

import java.lang.Math.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

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
        InitVars();
    }
    
    /**
     * 
     */
    public double getW() {
        //return w/1000.; // bo w mm
        return w;   // on a higher level you need to remember this value is in mm
    }
    public int getW_mls() {
        return (int)(Math.ceil(w * mlsPerMm));
    }
    public void setW(double val) {
        w = Norm.EnsureInRange(val, minW, maxW);
    }
    public void decW(boolean x10) {
        double step = (x10 ? 10. * Dw : Dw);
        setW(w - step);
    }
    public void incW(boolean x10) {
        double step = (x10 ? 10. * Dw : Dw);
        setW(w + step);
    }
    
    public double getW_ef() {
        //return we / 1000.; // bo w mm
        return we;   // on a higher level you need to remember this value is in mm
    }

    
    /**
     * 
     */
    public double getH() {
        //return h/1000.; // bo w mm
        return h;   // on a higher level you need to remember this value is in mm
    }
    public void setH(double val) {
        h = Norm.EnsureInRange(val, minH, maxH);
    }
    public void decH(boolean x10) {
        double step = (x10 ? 10. * Dh : Dh);
        setH(h - step);
    }
    public void incH(boolean x10) {
        double step = (x10 ? 10. * Dh : Dh);
        setH(h + step);
    }
    
    /**
     * 
     */
    public double getEps() {
        return er;
    }
    public void setEps(double val) {
        er = Norm.EnsureInRange(val, minEps, maxEps);
    }
    public void decEps(boolean x10) {
        double step = (x10 ? 10. * DEps : DEps);
        setEps(er - step);
    }
    public void incEps(boolean x10) {
        double step = (x10 ? 10. * DEps : DEps);
        setEps(er + step);
    }
    
    public double getEps_ef() {
        return efs;
    }
    
    /**
     * 
     */
    public double getZ0() {
        if (w/h < 1.)
            return Z1;
        else
            return Z2;
    }
    
    /**
     * 
     */
    public double getT() {
        return t;
    }
    public void setT(double val) {
        t = Norm.EnsureInRange(val, minT, maxT);
    }
    
    
    /**
     * wykonuje wyliczenia po zmianie ktoregokolwiek z parametrow
     */
    public void AfterAny() {
        // efs := FEPS(er,h,w,t);
        efs = FEPSM(er,h,w,t);
        
        wnh = w/h;
        if (wnh <= odw2pi)
            DW = FDW1(efs,w,h);
        else
            DW = FDW2(efs,w,h);
        
        we = w+DW;
        DW1 = FDW1(efs,w,h);
        DW2 = FDW2(efs,w,h);
        Z1 = FZ1(efs,we,h);
        Z2 = FZ2(efs,we,h);
    }
    
    private String MakeBackCompatibleForStorage(double Value) {
        
        DecimalFormat df = new DecimalFormat("0.00E0000");
        String str = " " + df.format(Value);
        str = str.replace(',', '.');
        int idx = str.indexOf('E');
        if (Character.isDigit(str.charAt(idx+1)))
        {
            String str1 = str.substring(0, idx+1) + '+' + str.substring(idx+1, str.length());
            return str1;
        }
        
        return str;
    }
    
    public boolean SaveDefaultCfg() {
        
        StringBuilder str = new StringBuilder();
        str.append("To jest zbior konfiguracyjny do wyliczen linii paskowej niesymetrycznej\n");
        str.append(MakeBackCompatibleForStorage(w)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(h)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(er)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(t)); str.append("\n");
        
        return ShortTxtFile.EasyWrite(FName, str.toString());
    }
    
    private double GetValueFromStorageString(String str) {
        
        BigDecimal val = new BigDecimal(str);
        return val.doubleValue();
    }

    public boolean LoadDefaultCfg() {
        
        java.util.List<String> content = ShortTxtFile.EasyRead(FName);
        if (content == null)
            return false;
        
        // element [0] to linia z komentarzem
        w = GetValueFromStorageString(content.get(1).trim());
        h = GetValueFromStorageString(content.get(2).trim());
        er = GetValueFromStorageString(content.get(3).trim());
        t = GetValueFromStorageString(content.get(4).trim());
        
        if (w*h*er*t >= 1e-9)   /* kontrola poprawnosci danych ze zbioru */
            return true;

        //InitVars();
        return false;
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
    
    
    /**
     * podstawienia wstepne:
     */
    private void InitVars() {

        //krok = 1E-3;
        h = 1.5;
        er = 5;
        t = 30E-3;
        w = 2.56;
    }

    
    // attributes...
    private static final String
            FName   = "LPN.cfg";
    
    private static final double
            minW    = 0.02,
            maxW    = 100.,
            Dw      = 0.02; /** przyrost szerokosci paska*/
    
    private static final double
            minH    = 0.05,
            maxH    = 100.,
            Dh      = 0.01;
    
    private static final double
            minEps  = 1.,
            maxEps  = 200.,
            DEps    = 0.05;
    
    private static final double
            minT    = 1e-3,
            maxT    = 10e0,
            Dt      = 1e-3;

    private static final double
            mlsPerMm     = 1000. / 25.4;
    
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
            t,      /* grubosc folii */
            w,      /* prawdziwa szerokosc paska */
            we,     /* efektywna szerokosc paska - we wzorach na Z0 */
            h,      /*  */
            er,     /* epsilon wzgledny materialu */
            efs,    /* epsilon wzgledny  - wartosc aktualna */
            efsm,   /* j.w. - wg skryptu Misiaszka */
            Zz,     /* zalozona wartosc Z0 */
            Z1,     /* aktualna wartosc liczonego Z0 wg 1. wzoru */
            Z2,     /* aktualna wartosc liczonego Z0 wg 2. wzoru */
            DZ,     /* aktualny blad Z0 */
            //krok,   /* krok zwiekszania szerokosci paska 'w' */
            DW,     /* poprawka szerokosci - we = w+DW */
            DW1,    /* poprawka szerokosci - 1szy wzor */
            DW2,    /* poprawka szerokosci - 2gi wzor */
            wnh;    /* w/h */
}
