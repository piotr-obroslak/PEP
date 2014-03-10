/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pepKalkulator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;

/**
 *
 * @author P. Obroslak
 */
public class ShortTxtFile {
    
    public static boolean EasyWrite(String name, String content) {
        try {
            return EasyWriteWorker(name, content);
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    public static java.util.List<String> EasyRead(String name) {
        try {
            return EasyReadWorker(name);
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    private static boolean EasyWriteWorker(String name, String content) throws IOException {
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(name));
            out.write(content);
        }
        finally {
            out.close();
        }
        
        return true;
    }
    
    private static java.util.List<String> EasyReadWorker(String name) throws IOException {
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(new FileInputStream(name));
            BufferedReader reader = new BufferedReader(in);

            java.util.List<String> result = new java.util.LinkedList<String>();
            
            String line;
            while ((line = reader.readLine()) != null)
                result.add(line);
            
            return result;
        }
        finally {
            if (in != null)
                in.close();
        }
    }
    
}
