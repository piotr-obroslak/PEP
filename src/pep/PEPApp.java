/*
 * PEPApp.java
 */

package pep;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class PEPApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new PEPView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
        
        //((JFrame)root).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        root.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ((PEPView)getApplication().getMainView()).saveDefaultParamFile();
            }
        });
        
//        ((JFrame)root).getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, KeyEvent.KEY_PRESSED), "none");
//        ((JFrame)root).getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, KeyEvent.KEY_TYPED), "none");
//        ((JFrame)root).getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, KeyEvent.KEY_RELEASED), "none");
//        ((JPanel)((JFrame)root).getRootPane().getContentPane()).getInputMap(/*JComponent.WHEN_IN_FOCUSED_WINDOW*/JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).
//                put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "none");
        ((PEPView)getApplication().getMainView()).mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "none");
        ((PEPView)getApplication().getMainView()).mainPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).
                put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "none");
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of PEPApp
     */
    public static PEPApp getApplication() {
        return Application.getInstance(PEPApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(PEPApp.class, args);
    }
}
