package img.main;

import img.dataobjects.Imagen;
import img.dataobjects.NuevoPCA;
import img.dataobjects.PCA;
import img.utils.MathsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.math.plot.utils.Array;

import Jama.Matrix;


public class MainTest {
	private static Logger logger = Logger.getLogger(MainTest.class.getSimpleName());
	
	/**
	 * @param args
	 */
	public static void main2(String[] args) {
		// TODO Auto-generated method stub
		PCA pca = new PCA("C:\\teocom\\entrenamiento\\", "C:\\teocom\\referencia\\");
		try {
			logger.info("-------- INICIO ENTRENAMIENTO --------");
			pca.entrenar();
			logger.info("--------  FIN ENTRENAMIENTO --------");
			
			logger.info("-------- INICIO REFERENCIA --------");
			pca.prepararMatrizXReferencia();
			logger.info("--------  FIN REFERENCIA --------");
			
			logger.info("--------  INICIO OBTENCION DE Y --------");
			pca.proyectarImagenesDeReferencia();
			logger.info("--------  FIN OBTENCION DE Y --------");
			
			List<int[]> imagenes = new ArrayList<int[]>();
			pca.leer("C:\\teocom\\test\\", imagenes);
			Matrix imagenesMatrix = pca.pasarAEigenface(imagenes);
			
			double[] imagenTest = MainTest.matrizToArray(0, imagenesMatrix);
			double distanciaMenor = 99999;
			int indexMenor = 0;
			double aux;

			for (int i = 0; i < pca.getY().getColumnDimension(); i++) {
				double[] imagenMatriz = MainTest.matrizToArray(i, pca.getY());
				aux = MainTest.distancia(imagenMatriz, imagenTest);
				if (aux < distanciaMenor) {
					distanciaMenor = aux;
					indexMenor = i;
				}
			}
			
			System.out.println(indexMenor);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Error critico, no puede ejecutarse la aplicacion." + e.getCause());
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NuevoPCA pca = new NuevoPCA("C:\\teocom\\entrenamiento\\");
		try {
			logger.info("-------- INICIO ENTRENAMIENTO --------");
			pca.entrenar();
			logger.info("--------  FIN ENTRENAMIENTO --------");
			
			pca.generarImagenesDeReferencia("C:\\teocom\\referencia\\");
			
			List<Imagen> imagenes = new ArrayList<Imagen>();
			pca.leerAImagen("C:\\teocom\\test\\", imagenes );
			Imagen imagenTest = imagenes.get(0);
			pca.pasarAEigenface(imagenTest);

			String distanciaMenorNombre = "";
			double distanciaMenor = 99999;
			double aux;
			
			for (Imagen imagenBase : pca.getImagenesReferencia()) {
				aux = MathsUtils.distanciaEntreVectores(imagenBase.getImagen(), imagenTest.getImagen());
				if (aux < distanciaMenor) {
					distanciaMenor = aux;
					distanciaMenorNombre = imagenBase.getNombre();
				}
			}
			
			System.out.println(distanciaMenorNombre);
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Error critico, no puede ejecutarse la aplicacion." + e.getCause());
			e.printStackTrace();
		}
		
	}

	
	
	private static double[] matrizToArray (int index, Matrix matriz) {
		double[] imagenTest = new double[matriz.getRowDimension()];
		for (int i = 0; i < matriz.getRowDimension(); i++) {
			imagenTest[i] = matriz.get(i, index);
		}
		return imagenTest;
	}
	
}
