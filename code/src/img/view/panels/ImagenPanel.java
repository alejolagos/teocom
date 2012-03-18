package img.view.panels;

import img.dataobjects.PGM;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagenPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private File selectedFile;
	
	public ImagenPanel (File selectedFile){
		Dimension d = new Dimension(32, 32);
		this.setSize(d);
		this.selectedFile = selectedFile;
	}
	
	public void setSelectedFile (File selectedFile){
		this.selectedFile = selectedFile;
	}
	
	public void paintComponent (Graphics g){
		if (selectedFile != null){
			Dimension tamanio = this.getSize();
			PGM pgm = new PGM(selectedFile.getAbsolutePath());
			ImageIcon imagen = new ImageIcon(pgm.pgm2jpeg().toByteArray());
			g.drawImage(imagen.getImage(), 0, 0, tamanio.width, tamanio.height, null);
			setOpaque(false);
		}
	}
	
	
}
