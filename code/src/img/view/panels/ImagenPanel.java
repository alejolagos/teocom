package img.view.panels;

import img.dataobjects.PGM;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagenPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private File testFile;
	private File resultFile;
	
	private static int WIDTH_PANEL = 400;
	private static int HEIGHT_PANEL= 400;
	private static int WIDTH_IMG  = 92;
	private static int HEIGHT_IMG = 112;
	private static int POS_X_TITULO_TEST = 20;
	private static int POS_Y_TITULO_TEST = 22;
	private static int POS_X_TITULO_RESU = 126;
	private static int POS_Y_TITULO_RESU = 22;
	private static int POS_X_IMAGEN_TEST = 34;
	private static int POS_Y_IMAGEN_TEST = 32;
	private static int POS_X_IMAGEN_RESU = 134;
	private static int POS_Y_IMAGEN_RESU = 32;
	
	public ImagenPanel (){
		Dimension d = new Dimension(WIDTH_PANEL, HEIGHT_PANEL);
		this.setSize(d);
	}
	
	public void setTestFile (File testFile){
		this.testFile = testFile;
	}
	
	public void setResultFile (File resultFile){
		this.resultFile = resultFile;
	}
	
	public void paintComponent (Graphics g){
		g.clearRect(0, 0, WIDTH_PANEL, HEIGHT_PANEL);
		g.drawRect(0, 0, WIDTH_PANEL, HEIGHT_PANEL);
		
		g.setFont( new Font("Arial",Font.BOLD, 16));
		g.setColor(Color.BLUE);
		g.drawString("Entrada", POS_X_TITULO_TEST, POS_Y_TITULO_TEST);

		g.setFont( new Font("Arial",Font.BOLD, 16));
		g.setColor(Color.RED);
		g.drawString("Salida", POS_X_TITULO_RESU, POS_Y_TITULO_RESU);
	
		try {
			if (testFile != null){
				PGM pgm = new PGM(testFile.getAbsolutePath());
				ImageIcon imagen = new ImageIcon(pgm.pgm2jpegOriginal().toByteArray());
				g.drawImage(imagen.getImage(), POS_X_IMAGEN_TEST, POS_Y_IMAGEN_TEST, WIDTH_IMG, HEIGHT_IMG, null);
				setOpaque(false);
			}
	
			if (resultFile != null){
				PGM pgm = new PGM(resultFile.getAbsolutePath());
				ImageIcon imagen = new ImageIcon(pgm.pgm2jpegOriginal().toByteArray());
				g.drawImage(imagen.getImage(), POS_X_IMAGEN_RESU, POS_Y_IMAGEN_RESU, WIDTH_IMG, HEIGHT_IMG, null);
				setOpaque(false);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
