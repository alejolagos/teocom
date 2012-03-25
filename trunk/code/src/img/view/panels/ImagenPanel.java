package img.view.panels;

import img.constants.Globals;
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
	private String errorMsg;
	private boolean imagenNoEncontrada = false;

	public void setImagenNoEncontrada(boolean imagenNoEncontrada) {
		this.imagenNoEncontrada = imagenNoEncontrada;
	}

	private static int WIDTH_PANEL = Globals.PANEL_IMAGEN_WIDTH;
	private static int HEIGHT_PANEL= Globals.PANEL_IMAGEN_HEIGHT;
	private static int WIDTH_IMG  = 92;
	private static int HEIGHT_IMG = 112;
	
	private static int POS_X_IMAGEN_TEST = 72;
	private static int POS_X_IMAGEN_RESU = POS_X_IMAGEN_TEST * 2 + WIDTH_IMG;
	private static int POS_X_TITULO_TEST = POS_X_IMAGEN_TEST + 16;
	private static int POS_X_TITULO_RESU = POS_X_IMAGEN_RESU + 16;
	private static int POS_X_MSG_ERROR = 20;
	private static int POS_X_MSG_NO_ENCONTRADO = POS_X_IMAGEN_RESU;

	private static int POS_Y_TITULO_TEST = 50;
	private static int POS_Y_IMAGEN_TEST = POS_Y_TITULO_TEST + 30;
	private static int POS_Y_IMAGEN_RESU = POS_Y_IMAGEN_TEST;
	private static int POS_Y_TITULO_RESU = POS_Y_TITULO_TEST;
	private static int POS_Y_MSG_ERROR = POS_Y_IMAGEN_TEST + 20;
	private static int POS_Y_MSG_NO_ENCONTRADO = POS_Y_IMAGEN_RESU + 20;
	
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

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public void paintComponent (Graphics g){
		g.clearRect(0, 0, WIDTH_PANEL, HEIGHT_PANEL);
		g.drawRect(0, 0, WIDTH_PANEL, HEIGHT_PANEL);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH_PANEL, HEIGHT_PANEL);
		
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
				g.setColor(Color.BLACK);
				setOpaque(false);
			}

			if (errorMsg != null && !errorMsg.trim().equals("")){
				g.setFont( new Font("Arial",Font.BOLD, 10));
				g.setColor(Color.RED);
				g.drawString(errorMsg, POS_X_MSG_ERROR, POS_Y_MSG_ERROR);
				setOpaque(false);
//				this.errorMsg = "";
			}
	
			if (resultFile != null){
				PGM pgm = new PGM(resultFile.getAbsolutePath());
				ImageIcon imagen = new ImageIcon(pgm.pgm2jpegOriginal().toByteArray());
				g.drawImage(imagen.getImage(), POS_X_IMAGEN_RESU, POS_Y_IMAGEN_RESU, WIDTH_IMG, HEIGHT_IMG, null);
				setOpaque(false);
			}
			else if (imagenNoEncontrada) {
				g.setFont( new Font("Arial",Font.BOLD, 10));
				g.setColor(Color.RED);
				g.drawString("No Encontrado", POS_X_MSG_NO_ENCONTRADO, POS_Y_MSG_NO_ENCONTRADO);
				setOpaque(false);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
