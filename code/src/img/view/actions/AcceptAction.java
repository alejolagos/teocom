package img.view.actions;

import img.view.frames.EntradaFrame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

//Action para el boton Abrir
public class AcceptAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private JFrame frame;

    public AcceptAction(JFrame frame) {
        super("Accept...");
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent evt) {
    	((EntradaFrame)frame).trabajarImagenTest();
    }

};
