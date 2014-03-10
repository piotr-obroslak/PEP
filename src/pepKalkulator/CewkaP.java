/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pepKalkulator;

import java.lang.Math.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Klasa <code>CewkaP</code> zawiera wzory do wyliczania indukcyjnosci cewki
 * powietrznej. Dodatkowo liczona jest dobroc cewki.
 * 
 * <p>Wzor na L wedlug: Kuliszewski 'Transformatory telekom.' str. 189 i 190.
 * Zastosowane wzory: 12.2 i 12.3. Wzor 12.2 wymaga uzycia nomogramu z rys 12.2.
 * W programie nomogram zastapilem tablica odczytanych z nomogramu pozycji;
 * wartosci znajdujace sie pomiedzy pozycjami sa interpolowane.
 * Dobroc wg teorii obwodow: Ql=(2*ă*f*L)/Rs.
 * Wzor empiryczny (12.1, str 189) przeniesiony z 'Mercurego':
 * <ul><li>L - ind. [nH]
 * <li>n - l. zwojow,
 * <li>l - dl. cewki [cm],
 * <li>d - srednica cewki [cm]</ul>
 * Lk(n,l,d):=(ă*n*d)^2/(4.5*d+10*l)*10
 *
 * @author A. Burd
 * @author P. Obroslak
 *
 */
public class CewkaP {

    public CewkaP() {
        InitVars();
        PodstawNTab();
    }


    /**
     * wykonuje wyliczenia po zmianie ktoregokolwiek z parametrow
     */
    public void AfterAny() {
        Lx = FLk(X,D,N);
        Ql = FQl(Lx,fx,Rs);
        Lmi = FLmi();
    }


    /**
     * Zwraca dlugosc cewki.
     * @return dlugosc cewki
     */
    public double getX() {
        //return X/1000.;   // bo w [mm]
        return X;   // on a higher level you need to remember this value is in mm
    }
    public void setX(double val) {
        X = Norm.EnsureInRange(val, minX, maxX);
    }
    public void decX(boolean x10) {
        double krok = (x10 ? 10. * krokX : krokX);
        setX(X - krok);
    }
    public void incX(boolean x10) {
        double krok = (x10 ? 10. * krokX : krokX);
        setX(X + krok);
    }
    

    /**
     * Zwraca srednice cewki.
     * @return srednica cewki
     */
    public double getD() {
        //return D/1000.;   // bo w [mm]
        return D;   // on a higher level you need to remember this value is in mm
    }
    public void setD(double val) {
        D = Norm.EnsureInRange(val, minD, maxD);
    }
    public void decD(boolean x10) {
        double krok = (x10 ? 10. * krokD : krokD);
        setD(D - krok);
    }
    public void incD(boolean x10) {
        double krok = (x10 ? 10. * krokD : krokD);
        setD(D + krok);
    }
    

    /**
     * Zwraca liczbe zwojow cewki.
     * @return liczba zwojow cewki
     */
    public double getN() {
        return N;
    }
    public void setN(double val) {
        N = Norm.EnsureInRange(val, minN, maxN);
    }
    public void decN(boolean x10) {
        double krok = (x10 ? (double)1e-1/*krokN / 10.*/ : krokN);
        setN(N - krok);
    }
    public void incN(boolean x10) {
        double krok = (x10 ? (double)1e-1/*krokN / 10.*/ : krokN);
        setN(N + krok);
    }

    
    /**
     * Zwraca opornosc szeregowa
     * @return opornosc szeregowa
     */
    public double getRs() {
        return Rs;
    }
    public void setRs(double val) {
        Rs = Norm.EnsureInRange(val, minRs, maxRs);
    }
    public void decRs(boolean x10) {
        double krok = (x10 ? krokRs * 10. : krokRs);
        setRs(Rs - krok);
    }
    public void incRs(boolean x10) {
        double krok = (x10 ? krokRs * 10. : krokRs);
        setRs(Rs + krok);
    }

    /**
     * Zwraca wyznaczona indukcyjnosc cewki.
     * @return indukcyjnosc cewki
     */
    public double getLx() {
        return Lx;
    }


    /**
     * Zwraca wyznaczona dobroc cewki.
     * @return dobroc cewki
     */
    public double getQl() {
        return Ql;
    }


    /**
     * Zwraca wyznaczona indukcyjnosc cewki przy zadanej przenikalnosci magnetycznej
     * @return indukcujnosc przy zadanej przenikalnosci magnetycznej
     */
    public double getLmi() {
        return Lmi;
    }
    
    
    /**
     * Zwraca czestotliwosc pracy
     * @return czestotliwosc pracy
     */
    public double getFx() {
        return fx;
    }
    public void setFx(double val) {
        fx = Norm.EnsureInRange(val, minFx, maxFx);
    }
    public void incFx() {
        setFx(getFx() + 1E2);
    }
    public void decFx() {
        setFx(getFx() - 1E2);
    }
    
    
    /**
     * Zwraca wzgledna przenikalnosc magnetyczna
     * @return wzgledna przenikalnosc magnetyczna
     */
    public double getMi() {
        return Mi;
    }
    public void setMi(double val) {
        Mi = val;
    }
    
    
    /**
     * Zwraca wzor zastosowany do wyliczen
     * @return wzor zastosowany do wyliczen
     */
    public String getWylKom() {
        return WylKom;
    }
    
    
    /**
     * Zwraca parametry przemiatania dlugosci cewki.
     */
//    public double getMinX() {
//        return minX;
//    }
//    public double getMaxX() {
//        return maxX;
//    }
    
    
    /**
     * Zwraca parametry przemiatania srednicy cewki.
     */
//    public double getMinD() {
//        return minD;
//    }
//    public double getMaxD() {
//        return maxD;
//    }
    
    
    /**
     * Zwraca parametry przemiatania liczby zwojow.
     */
//    public double getMinN() {
//        return minN;
//    }
//    public double getMaxN() {
//        return maxN;
//    }
    
    
    public boolean SaveDefaultCfg() {
        
        StringBuilder str = new StringBuilder();
        str.append("To jest zbior konfiguracyjny do wyliczen cewki powietrznej\n");
        str.append(MakeBackCompatibleForStorage(Lx)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(X)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(D)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(N)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(Rs)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(fx)); str.append("\n");
        str.append(MakeBackCompatibleForStorage(Mi)); str.append("\n");
        
        return ShortTxtFile.EasyWrite(FName, str.toString());
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
        X = GetValueFromStorageString(content.get(2).trim());
        D = GetValueFromStorageString(content.get(3).trim());
        N = GetValueFromStorageString(content.get(4).trim());
        Rs = GetValueFromStorageString(content.get(5).trim());
        fx = GetValueFromStorageString(content.get(6).trim());
        Mi = GetValueFromStorageString(content.get(7).trim());
        
        if (Lx*X*D*N*Rs > 0.)
            return true;
        
        //InitVars();
        return false;
    }


    /**
     * odtwarza nomogram z rys. 12.2; nomogram "wspolpracuje" ze wzorem nr 12.2
     * DnX to stosunek wymiarow cewki - D/X
     */
    private double Fnom(double DnX) {

//        if (DnX < 0.01) /* poza zakresem; obsluga - wzor 12.1 */ {
//            return 0.;
//        }
        if (DnX < NTabX[0]) /* poza zakresem; obsluga - wzor 12.1 */ {
            return 0.;
        }

//        if (DnX > 10)   /* poza zakresem; bardzo duza srednica */ {
//            return 0.;  /* TAK! BO NIE WIADOMO, CO Z TYM ZROBIC! */
//        }
        if (DnX > NTabX[rozmNom-1]) /* poza zakresem; bardzo duza srednica */ {
            return 0.;  /* TAK! BO NIE WIADOMO, CO Z TYM ZROBIC! */
        }

//        if (DnX == 10)  /* wartosc maks. */ {
//            return 0.022;
//        }
        if (DnX == NTabX[rozmNom-1]) /* wartosc maks. */ {
            return NTabY[rozmNom-1];
        }

        double xl, xp;  /* x-lewy i x-prawy dla DnX; z tablicy NTabX */
        double yl, yp;  /* y-lewy i y-prawy dla F(DnX); z tablicy NTabY */
        double x, y;    /* x - bezposr. wartosc D/L; y - interpolowana wartosc F(DnX) */

        x = DnX;        /* zmienna x jest tylko dla przejrzystosci */
        byte i = 0;

        while (DnX >= NTabX[i]) {
            i++;
        }

        xl = NTabX[i-1];
        xp = NTabX[i];
        yl = NTabY[i-1];
        yp = NTabY[i];
        y = (yp-yl)/(xp-xl)*(x-xl)+yl;

        return y;
    }


    /**
     * wyliczenie dokladne - z nomogramem
     * obejmuje przypadki: 0.1 < D/X < 10
     * wyliczenie Lx przy danych: X (dl.) i D (srednica) oraz N - liczba zwojow
     * Kuliszewski: wzor 12.2 i nomogram z rys 12.2
     */
    private double FLn(double X, double D, double N) {

        double Lno;     /* wynik wzoru z nomogramem w uH */
        double Xc, Dc;  /* wartosci dl. i srednicy w cm */

        Dc = D/10.;
        Xc = X/10.;
        Lno = Fnom(Dc/Xc)*Math.pow(N, 2.) *Dc;   /* [uH] */
        return Lno/1e6; /* [H] */
    }


    /**
     * wyliczenie Lx przy danych: X (dl.) i D (srednica) oraz N - liczba zwojow
     */
    private double FLk(double X, double D, double N) {

        double Ln;      /* wynik obliczen wzoru 12.1, w nH */
        double Xc, Dc;  /* wartosci dl. i srednicy w cm */

//        if (D/X > 0.01) {
//        }
        if (D/X > NTabX[0]) /* cewka o malym stosunku D/X */ {

            if (D/X < NTabX[rozmNom-1]) {
                WylKom = "Wzor: \"nomogramowy\"";
            }
            else if (D/X > NTabX[rozmNom-1]) /*poza zasiegiem wyliczen*/ {
                WylKom = "Za duzy stosunek D/X!";
                Lx = 0;
            }

            return FLn(X,D,N);
        }

        WylKom = "Wzor: empiryczny";
        Xc = X/10;      /* przejscie na cm */
        Dc = D/10;      /* przejscie na cm */
        Ln = (Math.pow(Math.PI*N*Dc,2.)/(4.5*Dc+10*Xc))*10; /* nH */
        return Ln/1e9;  /* H */
    }


    /**
     * wyliczenie Lmi przy danych: Lx, Mi
     */
    private double FLmi() {
        return Lx*Mi;
    }


    /**
     * wyliczenie dobroci w obw. szeregowym przy danym Lx i Cx
     */
    private double FQl(double Lx, double fx, double Rs) {
        return (2*Math.PI*fx*Lx)/Rs;
    }


    /**
     * podstawienia wstepne:
     */
    private void InitVars() {
        X = 10.;
        D = 4.;
        N = 1e1;
        Rs = 1e-2;
        fx = 1e3;
        krokX = 1e-1;
        krokD = 1e-1;
        krokN = 1e0;
        krokRs = 1E-3;
        Mi = 1.;
        WylKom = "komentarz do wzorow";
    }

    
    /**
     * do podstawienia wartosci tablicy NTab aproksymacji nomogramu
     * FTAB:=[  [0.01, 0.0001],
     *          [0.02, 0.0002],
     *          [0.05, 0.0005],
     *          [0.2, 0.0017],
     *          [0.5, 0.0037],
     *          [2, 0.01],
     *          [6, 0.02],
     *          [10, 0.022]]
     */
    private void PodstawNTab() {
        NTabX[0] = 0.01;
        NTabX[1] = 0.02;
        NTabX[2] = 0.05;
        NTabX[3] = 0.2;
        NTabX[4] = 0.5;
        NTabX[5] = 1.;
        NTabX[6] = 1.3;
        NTabX[7] = 2.;
        NTabX[8] = 6.;
        NTabX[9] = 10.;

        NTabY[0] = 0.0001;
        NTabY[1] = 0.0002;
        NTabY[2] = 0.0005;
        NTabY[3] = 0.0017;
        NTabY[4] = 0.0037;
        NTabY[5] = 0.0063;
        NTabY[6] = 0.008;
        NTabY[7] = 0.01;
        NTabY[8] = 0.02;
        NTabY[9] = 0.022;
    }


    // attributes...
    private static final String
            opis[] = {
        "OBLICZENIA INDUKCYJNOSCI CEWKI POWIETRZNEJ",
        "Wyznaczanie indukcyjnosci i dobroci",
        "Na podst. Kuliszewski: \"Transformatory telekom.\" str. 189, 190",
        "L - indukcyjnosc, Lu - ind. z rdzeniem, X - dl. cewki, D - srednica",
        "Rs - opornosc szeregowa, fx - czestotliwosc pracy, Ql - dobroc przy Rs i fx",
        "uw - wzgl. przenikalnosc magnetyczna."};

    private static final String
            quitInfo [] = {
        "Przy wyjsciu program automatycznie zapamietuje ostatnio ustawione parametry",
        "(X,D,Rs,fx,u), jesli choc raz parametry zostaly zapamietane. Ostatnio",
        "ustawione parametry pamietane sa w lokalnej kartotece w zbiorze cewkap.CFG."};
    
    private static final String
            FName   = "cewkap.cfg";

    private static final double
            minX    = .1,   /** minim. dlugosc w mm */
            maxX    = 100.; /** maks. dl. w mm */

    private static final double
            minD    = .1,   /** minim. srednica w mm */
            maxD    = 100.; /** maks. sred. w mm */

    private static final double
            minN    = .1,   /** minim. liczba zwojow */
            maxN    = 1e4;  /** maks. liczba zwojow */

    private static final double
            minRs   = 1e-3, /** minim. opornosc szeregowa */
            maxRs   = 100;  /** maks. opornosc szeregowa */
    
    private static final double
            minFx   = 1e-3, /** minim. czestotliwosc pracy cewki */
            maxFx   = 10e9; /** maks. czestotliwosc pracy cewki */

    private static final short
            rozmNom = 10;   /** rozmiar tablic do aproksymacji nomogramu*/
    private static final double[]
            NTabX = new double[rozmNom],
            NTabY = new double[rozmNom];


    private String
            WylKom;  /* komentarz - sposob wyliczania */
    
    private double
            X,      /* dlugosc cewki */
            D,      /* srednica */
            N,      /* liczba zwojow */
            Rs,     /* opornosc szeregowa */
            fx,     /* czestotliwosc pracy */
            Lx,     /* indukcyjnosc cewki */
            Ql,     /* dobroc */
            Mi,     /* wzgledna przenikalnosc magnetyczna */
            Lmi,    /* indukcyjnosc przy danym Mi */
            krokX,  /* krok przemiatania dlugosci */
            krokD,  /* krok przemiatania srednicy */
            krokN,  /* krok przemiatania liczby zwojow */
            krokRs; /* krok przemiatania Rs */
            //mi;     /* wzgl. przenik. magnet. */
}
