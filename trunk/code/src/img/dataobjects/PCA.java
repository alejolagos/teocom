package img.dataobjects;

import img.constants.Globals;
import img.utils.CommonsUtils;
import img.utils.MathsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class PCA {
	private static Logger logger = Logger.getLogger(PCA.class.getSimpleName());
	private ArrayList<int[]> imagenes;
	private Matrix w;

	public ArrayList<int[]> getImagenes() {
		return imagenes;
	}

	public void setImagenes(ArrayList<int[]> imagenes) {
		this.imagenes = imagenes;
	}
	
	public Matrix getW() {
		return w;
	}

	public void setW(Matrix w) {
		this.w = w;
	}

	
	public PCA(String dirPath) {
		logger.info(dirPath);
		this.imagenes = new ArrayList<int[]>();
		this.leer(dirPath);
	}

	private void leer(String dirPath) {
		File folder = new File(dirPath);
	    File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        PGM imagen = new PGM(dirPath+"\\"+listOfFiles[i].getName());
			this.imagenes.add(imagen.getPixelArray());
	      } else if (listOfFiles[i].isDirectory()) {
	        leer(dirPath+"\\"+listOfFiles[i].getName());
	      }
	    }
	}
	
	public Matrix entrenar() throws Exception {
		long ini = 0;
		long fin = 0;
		
		int[][] X = this.list2Matriz();

		// Obtengo matriz de covarianza
		logger.info("------- CALCULO CONVARIANZA -------");
		ini = System.currentTimeMillis();
		Matrix covarianza = MathsUtils.getMatrizCovarianza(X);
		fin = System.currentTimeMillis();
		CommonsUtils.loguearTiempoEjecucion(ini, fin, this.getClass().getSimpleName(), "Tiempo calcular Covarianza:");
		
		// Obtengo los autovectores/autovalores
		logger.info("------- OBTENGO AUTEVECTORES/AUTOVALORES -------");
		ini = System.currentTimeMillis();
		EigenvalueDecomposition E = covarianza.eig();
		fin = System.currentTimeMillis();
		CommonsUtils.loguearTiempoEjecucion(ini, fin, this.getClass().getSimpleName(), "Tiempo calcular Autovectores:");

		// Obtengo la matriz de proyeccion con los autovectores mas significativos
		logger.info("------- OBTENGO MATRIZ DE PROYECCION -------");
		ini = System.currentTimeMillis();
		Matrix W = MathsUtils.getMatrizProyeccionPCA(E.getV(), Globals.COLS_PCA_MATRIZ_PROYECCION);
		fin = System.currentTimeMillis();
		CommonsUtils.loguearTiempoEjecucion(ini, fin, this.getClass().getSimpleName(), "Tiempo obtener Autovectores mas significativos:");
		
		this.setW(W);
		
		return W;
	}
	
	private int [][] list2Matriz() throws Exception{
		if (imagenes == null || imagenes.isEmpty()){
			throw new Exception ("No hay imagenes cargadas.");
		}
		
		int m = this.imagenes.get(0).length;	// Rows
		int n = this.imagenes.size();			// Cols
		
		int[][] X = new int[m][n];

		int j = 0;
		for (int[] imagen : imagenes) {
			for (int i = 0; i < m; i++){
				X[i][j] = imagen[i];
			}
			j++;
		}
		
		return X;
	}
	
}
