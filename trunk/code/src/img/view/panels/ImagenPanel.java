package img.view.panels;

import img.dataobjects.PGM;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagenPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private File testFile;
	private File resultFile;
	
	public ImagenPanel (){
		Dimension d = new Dimension(64, 32);
		this.setSize(d);
	}
	
	public void setTestFile (File testFile){
		this.testFile = testFile;
	}
	
	public void setResultFile (File resultFile){
		this.resultFile = resultFile;
	}
	
	public void paintComponent (Graphics g){
		if (testFile == null){
			g.clearRect(0, 0, 32, 32);
		}
		else{
			PGM pgm = new PGM(testFile.getAbsolutePath());
			ImageIcon imagen = new ImageIcon(pgm.pgm2jpeg().toByteArray());
			g.drawImage(imagen.getImage(), 0, 0, pgm.getColumns(), pgm.getRows(), null);
			setOpaque(false);
		}

		if (resultFile == null){
			g.clearRect(32, 0, 32, 32);
		}
		else{
			PGM pgm = new PGM(resultFile.getAbsolutePath());
			ImageIcon imagen = new ImageIcon(pgm.pgm2jpeg().toByteArray());
			g.drawImage(imagen.getImage(), 32, 0, pgm.getColumns(), pgm.getRows(), null);
			setOpaque(false);
		}
	}
	
	
}
