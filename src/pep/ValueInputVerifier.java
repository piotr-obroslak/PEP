/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pep;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;


/**
 *
 * @author Piotrek
 */
public class ValueInputVerifier extends javax.swing.InputVerifier {
 
    @Override
    public boolean verify(JComponent input) {
        JTextField tf = (JTextField)input;
        return true;
    }
}
