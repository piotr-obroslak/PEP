/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pepCalculator;

import java.lang.Math.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Klasa <code>Resonance</code> zawiera wzory do wyliczania obwodu rezonansowego
 * LC(R). Wyliczenie obejmuje czestotliwosc rezonansowa i dobroc obwodu.
 * 
 * <p>Na podstawie wzorow ze skryptu "Teoria obwodow cz.1"
 * <ul><li>L - ind.
 * <li>C - pojemn.
 * <li>fr - czestotl. rezonansowa
 * <li>Rr - opornosc rownol.
 * <li>Rs - opornosc szeregowa
 * <li>Qr - dobroc przy danym Rr
 * <li>Qs - dobroc przy danym Rs
 * </ul>
 * 
 * @author A. Burd
 * @author P. Obroslak
 */
public class Resonance {
    
    public Resonance() {
        InitVars();
    }
    
    /**
     * wyliczenia po zmianie Lx
     */
    public void AfterL() {
        fr = FRez(Lx, Cx);
        Cx = FCx(fr, Lx);
        Qr = FQr(Lx, Cx, Rr);
        Qs = FQs(Lx, Cx, Rs);
        ro = Fro(Lx, Cx);
    }
    
    /**
     * wyliczenia po zmianie Cx
     */
    public void AfterC() {
        fr = FRez(Lx, Cx);
        Lx = FLx(fr, Cx);
        Qr = FQr(Lx, Cx, Rr);
        Qs = FQs(Lx, Cx, Rs);
        ro = Fro(Lx, Cx);
    }
    
    /**
     * wyliczenia po zmianie Rs
     */
    public void AfterRs() {
        Qs = FQs(Lx, Cx, Rs);
    }
    
    /**
     * wyliczenia po zmianie Rr
     */
    public void AfterRr() {
        Qr = FQr(Lx, Cx, Rr);
    }
    
    /**
     * wyliczenia po zmianie fr
     */
    public void Afterfr() {
        //Lx = Frez(Lx, Cx);
        // in original code it was like above, but...
        fr = FRez(Lx, Cx);
        Lx = FLx(fr, Cx);
        Qr = FQr(Lx, Cx, Rr);
        Qs = FQs(Lx, Cx, Rs);
        ro = Fro(Lx, Cx);
    }
    
    /**
     * Zwraca indukcyjnosc
     * @return indukcyjnosc
     */
    public double getLx() {
        return Lx;
    }
    public void setLx(double val) {
        Lx = Norm.EnsureInRange(val, minLx, maxLx);
    }
    private void chLx(double multiplier) {
        setLx(Lx + (multiplier * stepLx));
    }
    public void decLx(boolean x10) {
        chLx(x10 ? -10.  : -1.);
    }
    public void incLx(boolean x10) {
        chLx(x10 ? 10.  : 1.);
    }
    
    /**
     * Zwraca pojemnosc
     * @return pojemnosc
     */
    public double getCx() {
        return Cx;
    }
    public void setCx(double val) {
        Cx = Norm.EnsureInRange(val, minCx, maxCx);
    }
    private void chCx(double multiplier) {
        setCx(Cx + (multiplier * stepCx));
    }
    public void decCx(boolean x10) {
        chCx(x10 ? -10.  : -1.);
    }
    public void incCx(boolean x10) {
        chCx(x10 ? 10.  : 1.);
    }
    
    /**
     * zwraca czestotliwosc rezonansowa
     * @return czestotliwosc rezonansowa
     */
    public double getFR() {
        return fr;
    }
    public void setFR(double val) {
        fr = Norm.EnsureInRange(val, minFR, maxFR);
    }
    
    
    /**
     * zwraca dobroc przy danym Rr
     * @return dobroc przy danym Rr
     */
    public double getQr() {
        return Qr;
    }
    
    /**
     * zwraca dobroc przy danym Rs
     * @return dobroc przy danym Rs
     */
    public double getQs() {
        return Qs;
    }
    
    /**
     * @return opornosc rownolegla
     */
    public double getRr() {
        return Rr;
    }
    public void setRr(double val) {
        Rr = Norm.EnsureInRange(val, minRr, maxRr);
    }
    private void chRr(double multiplier) {
        setRr(Rr + (multiplier * stepRr));
    }
    public void decRr(boolean x10) {
        chRr(x10 ? -10. : -1.);
    }
    public void incRr(boolean x10) {
        chRr(x10 ? 10. : 1.);
    }
    
    /**
     * @return opornosc szeregowa
     */
    public double getRs() {
        return Rs;
    }
    public void setRs(double val) {
        Rs = Norm.EnsureInRange(val, minRs, maxRs);
    }
    private void chRs(double multiplier) {
        setRs(Rs + (multiplier * stepRs));
    }
    public void decRs(boolean x10) {
        chRs(x10 ? -10. : -1.);
    }
    public void incRs(boolean x10) {
        chRs(x10 ? 10. : 1.);
    }
    
    /**
     * @return the mysterious ro
     */
    public double getRO() {
        return ro;
    }
    
    /**
     * @return step Lx
     */
    public double getStepLx() {
        return stepLx;
    }

    /**
     * @return step Cx
     */
    public double getStepCx() {
        return stepCx;
    }
    
    /**
     * @return checks if fr is variable
     */
    public boolean isVarFR() {
        return Var_fr;
    }
    
    /**
     * @return checks if Lx is variable
     */
    public boolean isVarLx() {
        return Var_Lx;
    }
    
    /**
     * @return checks if Cx is variable
     */
    public boolean isVarCx() {
        return Var_Cx;
    }

    
    private String MakeBackCompatibleForStorage(double Value) {
        
        DecimalFormat df = new DecimalFormat("0.00000E0000");
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
        str.append("To jest zbior konfiguracyjny do wyliczen obwodu rezonansowego\n");
        str.append(MakeBackCompatibleForStorage(Lx)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(Cx)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(fr)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(Rr)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(Rs)); str.append("\n");
        
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
        Lx = GetValueFromStorageString(content.get(1).trim());
        Cx = GetValueFromStorageString(content.get(2).trim());
        fr = GetValueFromStorageString(content.get(3).trim());
        Rr = GetValueFromStorageString(content.get(4).trim());
        Rs = GetValueFromStorageString(content.get(5).trim());
        
        if (Lx*Cx*fr*Rr*Rs > 0.)
            return true;
        
        //InitVars();
        return false;
    }
    
    /**
     * wyliczenie frez
     */
    private double FRez(double Lx, double Cx) {
        
        if (!Var_fr)
            return fr;
        
        return 1/(2*Math.PI*Math.sqrt(Lx*Cx));
    }
    
    /**
     * wyliczenie Lx przy danym Cx i frez
     */
    private double FLx(double fr, double Cx) {
        return 1/(Math.pow(2*Math.PI*fr, 2.)*Cx);
    }
    
    /**
     * wyliczenie Cx przy danym Lx i frez
     */
    private double FCx(double fr, double Lx) {
        return 1/(Math.pow(2*Math.PI*fr, 2.)*Lx);
    }
    
    /**
     * wyliczenie opornosci charakterystycznej (ro) przy danym Lx i Cx
     */
    private double Fro(double Lx, double Cx) {
        return Math.sqrt(Lx/Cx);
    }
    
    /**
     * wyliczenie dobroci w obw. rownoleglym przy danym Lx i Cx
     */
    private double FQr(double Lx, double Cx, double Rr) {
        return Rr/(Math.sqrt(Lx/Cx));
    }
    
    /**
     * wyliczenie dobroci w obw. szeregowym przy danym Lx i Cx
     */
    private double FQs(double Lx, double Cx, double Rs) {
        return (Math.sqrt(Lx/Cx))/Rs;
    }
    
    /**
     * podstawienia wstepne
     */
    private void InitVars() {
        
        Cx = 100e-12;
        Lx = 100e-9;
        fr = 50e6;
        Rr = 1e3;
        Rs = 1e-2;
        
        stepCx = 1e-12;
        stepLx = 1e-9;
        stepRr = 10;
        stepRs = 1e-3;

        Var_fr = false;
        Var_Lx = true;
        Var_Cx = true;
    }
    
    
    // attributes
    private static final String
            FName = "rezon.cfg";
    
    private static final double
            minCx = 1e-15,
            maxCx = 1;
    
    private static final double
            minLx = .1e-9,
            maxLx = 1;
    
    private static final double
            minRs = 1e-3,
            maxRs = 100;
    
    private static final double
            minRr = 1e-2,
            maxRr = 1e9;
    
    private static final double
            minFR = 1e-3,
            maxFR = 1e33;
    
    private boolean
            Var_fr, /* czy zmienne fr */
            Var_Cx, /* czy zmienne Cx */
            Var_Lx; /* czy zmienne Lx */
    
    private double
            Lx,
            Cx,
            fr,
            Rr,
            Rs,
            Qr,
            Qs,
            ro,
            stepCx,
            stepLx,
            stepRr,
            stepRs;
}
