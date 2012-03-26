package img.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

//Action para el boton Abrir
public class ExitAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

    public ExitAction(JFrame frame) {
        super("Exit...");
    }

    public void actionPerformed(ActionEvent evt) {
    	System.exit(0);
    }

};
