/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pep;

import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Piotrek
 */
public class StepCountingSpinnerNumberModel extends SpinnerNumberModel {
    
    public StepCountingSpinnerNumberModel(int value, int minimum, int maximum, int stepSize) {
        super(value, minimum, maximum, stepSize);
    }
    
    public StepCountingSpinnerNumberModel() {
        this(0, -16777216, 16777215, 1);
    }
    
    @Override
    public void setValue(Object value) {
        history = getValue();
        super.setValue(value);
    }
    
    public Object getHistory() {
        return history;
    }

    private Object history;
}
