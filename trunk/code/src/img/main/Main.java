package img.main;

import img.view.frames.EntradaFrame;

import javax.swing.JFrame;

import org.apache.log4j.Logger;


public class Main {
	
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(EntradaFrame.class);
		logger.info("Nueva instancia");
		EntradaFrame aplicacion = new EntradaFrame();
		aplicacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
	}

}
