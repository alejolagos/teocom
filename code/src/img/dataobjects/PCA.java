package img.dataobjects;

import static org.math.array.StatisticSample.mean;
import static org.math.array.StatisticSample.stddeviation;
import img.constants.Globals;
import img.utils.CommonsUtils;
import img.utils.MathsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class PCA {
	private static Logger logger = Logger.getLogger(PCA.class.getSimpleName());
	private List<int[]> imagenesEntrenamiento;
	private List<int[]> imagenesReferencia;

	private Matrix W;	// 	Matriz de proyeccion de PCA
	private Matrix X;	//	Matriz prepara con las imagenes de referencia

	public PCA(String dirPathEntrenamiento, String dirPathReferencia) {
		logger.info(dirPathEntrenamiento);
		this.imagenesEntrenamiento = new ArrayList<int[]>();
		this.imagenesReferencia = new ArrayList<int[]>();
		this.leer (dirPathEntrenamiento, this.imagenesEntrenamiento);
		this.leer (dirPathReferencia, this.imagenesReferencia);
	}

	public Matrix getW() {
		return W;
	}

	public void setW(Matrix W) {
		this.W = W;
	}
	
	public Matrix getX() {
		return X;
	}
	
	public void setX(Matrix x) {
		X = x;
	}
	
	/**
	 * Prepara las imagenes de entrenamiento o referencia 
	 * @param dirPath
	 * @param imagenes
	 */
	private void leer(String dirPath, List<int[]> imagenes) {
		File folder = new File(dirPath);
	    File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				PGM imagen = new PGM(dirPath+"\\"+listOfFiles[i].getName());
				imagenes.add(imagen.getPixelArray());
			} 
			else if (listOfFiles[i].isDirectory()) {
				leer(dirPath+"\\"+listOfFiles[i].getName(), imagenes);
			}
		}
}
	
	/**
	 * Entrena el algoritmo PCA obteniendo la matriz de proyeccion W
	 * @return
	 * @throws Exception
	 */
	public Matrix entrenar() throws Exception {
		long ini = 0;
		long fin = 0;
		
		int[][] X = this.list2Matriz(this.imagenesEntrenamiento);

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
		Matrix W = MathsUtils.getMatrizProyeccionPCA(E, Globals.COLS_PCA_MATRIZ_PROYECCION);
		fin = System.currentTimeMillis();
		CommonsUtils.loguearTiempoEjecucion(ini, fin, this.getClass().getSimpleName(), "Tiempo obtener Autovectores mas significativos:");
		
		this.setW(W);
		
		return W;
	}
	
	/**
	 * Convierte la lista de enteros a matriz
	 * @return
	 * @throws Exception
	 */
	private int [][] list2Matriz(List<int[]> imagenes) throws Exception{
		if (imagenes == null || imagenes.isEmpty()){
			throw new Exception ("No hay imagenes cargadas.");
		}
		
		int m = imagenes.get(0).length;		// Rows
		int n = imagenes.size();			// Cols
		
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

	/**
	 * Armo la matriz de referencia con las imagenes normalizadas como vectores
	 * @throws Exception
	 */
	public void prepararMatrizXReferencia () throws Exception{
		long ini = 0;
		long fin = 0;
		
		int[][] _X = this.list2Matriz(this.imagenesReferencia);

		double[] media = MathsUtils.getMediaMatriz (_X);
		double[] desvio = MathsUtils.getDesvioStdMatriz (_X);
		
		// Obtengo la matriz con los vectores normalizados
		double[][] Z = MathsUtils.getNormalization(_X, media, desvio);
		
		// Obtengo la matriz X para luego utilizarla en la proyeccion
		Matrix X = MathsUtils.getMatrizNormalizadaMenosMedia(Z, media); // TODO: PARA MI ESTO NO VA... DSP LO CHARLAMOS...
				
		this.X = X;
	}
}
