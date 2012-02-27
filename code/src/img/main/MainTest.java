package img.main;

import img.dataobjects.PCA;

import java.util.logging.Level;
import java.util.logging.Logger;


public class MainTest {
	private static Logger logger = Logger.getLogger(MainTest.class.getSimpleName());
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PCA pca = new PCA("C:\\teocom\\entrenamiento\\");
		try {
			logger.info("-------- INICIO ENTRENAMIENTO --------");
			pca.entrenar();
			logger.info("--------  FIN ENTRENAMIENTO --------");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Error critico, no puede ejecutarse la aplicacion." + e.getCause());
			e.printStackTrace();
		}
	}
		

	
}
