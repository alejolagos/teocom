package img.view.actions;

import img.view.filters.PGMFilter;
import img.view.frames.EntradaFrame;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

//Action para el boton Abrir
public class OpenFileAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	JFrame frame;
    JFileChooser chooser;
    File file;

    public OpenFileAction(JFrame frame, JFileChooser chooser) {
        super("Open...");
        this.chooser = chooser;
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent evt) {
    	int result = chooser.showOpenDialog(frame);

    	// Determine which button was clicked to close the dialog
    	switch (result) {
    		case JFileChooser.APPROVE_OPTION:
	            // Approve (Open or Save) was clicked
    			file = chooser.getSelectedFile();
    			
//    			if ( ((PGMFilter) (chooser.getFileFilter())).accept(file) ) {
    			if ( file != null && file.getName().endsWith(PGMFilter.getExtensionFilter()) ) {
	    			((EntradaFrame)frame).setSelectedFile(file);
	    			((EntradaFrame)frame).mostrarImagenTest();
    			}
    			else{
    				((EntradaFrame)frame).errorArchivoTestSeleccionado(file);
    			}
    			break;
    		case JFileChooser.CANCEL_OPTION:
				// Cancel or the close-dialog icon was clicked
				break;
    		case JFileChooser.ERROR_OPTION:
				// The selection process did not complete successfully
				break;
        }
    }
    
}