package img.main;

import img.view.frames.EntradaFrame;

import javax.swing.JFrame;

import org.apache.log4j.Logger;


public class Main {
	
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(EntradaFrame.class);
		logger.info("=====================================================");
		logger.info("Nueva instancia");
		logger.info("=====================================================");
		EntradaFrame aplicacion = new EntradaFrame();
		aplicacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
	}

}
