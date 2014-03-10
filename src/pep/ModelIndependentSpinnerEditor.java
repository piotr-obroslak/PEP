/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pep;



/**
 *
 * @author Piotrek
 */
public class ModelIndependentSpinnerEditor extends javax.swing.JSpinner.DefaultEditor {
    
    ModelIndependentSpinnerEditor(javax.swing.JSpinner spinner) {
        super(spinner);
        
        javax.swing.JFormattedTextField jtf = getTextField();
        jtf.setEditable(true);
        jtf.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
    }
    
    @Override
    public void stateChanged(javax.swing.event.ChangeEvent e) {
        javax.swing.JSpinner spinner = (javax.swing.JSpinner)(e.getSource());
    }
    
    @Override
    public void propertyChange(java.beans.PropertyChangeEvent e)
    {
        javax.swing.JSpinner spinner = getSpinner();

        if (spinner == null) {
            // Indicates we aren't installed anywhere.
            return;
        }

        Object source = e.getSource();
        String name = e.getPropertyName();
        if ((source instanceof javax.swing.JFormattedTextField) && "value".equals(name)) {
            Object lastValue = spinner.getValue();

            // Try to set the new value
            try {
                //I skip this on purpose!!!!!!!!
                //spinner.setValue(getTextField().getValue());
            } catch (IllegalArgumentException iae) {
                // SpinnerModel didn't like new value, reset
                try {
                    //((javax.swing.JFormattedTextField)source).setValue(lastValue);
                } catch (IllegalArgumentException iae2) {
                    // Still bogus, nothing else we can do, the
                    // SpinnerModel and JFormattedTextField are now out
                    // of sync.
                }
            }
        }
    }

    
    public void setText(String str) {
        getTextField().setText(str);
    }
    
    public String getText(String str) {
        return getTextField().getText();
    }
}
