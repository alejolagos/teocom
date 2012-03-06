package img.dataobjects;

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

	private int superIndex;

	private Matrix W;	// 	Matriz de proyeccion de PCA
	private Matrix X;	//	Matriz preparada con las imagenes de referencia
	
	private Matrix Y; //Todas las imagenes proyectadas en el subespacio de 'caras'

	public PCA(String dirPathEntrenamiento, String dirPathReferencia) {
		logger.info(dirPathEntrenamiento);
		this.imagenesEntrenamiento = new ArrayList<int[]>();
		this.imagenesReferencia = new ArrayList<int[]>();
		this.leer (dirPathEntrenamiento, this.imagenesEntrenamiento);
		superIndex = 0;
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
	
	public Matrix getY() {
		return Y;
	}

	public void setY(Matrix y) {
		Y = y;
	}

	/**
	 * Prepara las imagenes de entrenamiento o referencia 
	 * @param dirPath
	 * @param imagenes
	 */
	public void leer(String dirPath, List<int[]> imagenes) {
		File folder = new File(dirPath);
	    File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	    	if (!listOfFiles[i].isHidden()) {
				if (listOfFiles[i].isFile()) {
					PGM imagen = new PGM(dirPath+"\\"+listOfFiles[i].getName());
					imagenes.add(imagen.getPixelArray());
					superIndex++;
				} 
				else if (listOfFiles[i].isDirectory()) {
					leer(dirPath+"\\"+listOfFiles[i].getName(), imagenes);
				}
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
		int[][] _X = this.list2Matriz(this.imagenesReferencia);

		double[] media = MathsUtils.getMediaMatriz (_X);
		double[] desvio = MathsUtils.getDesvioStdMatriz (_X);
		
		// Obtengo la matriz con los vectores normalizados
		double[][] Z = MathsUtils.getNormalization(_X, media, desvio);
		
		// Obtengo la matriz X para luego utilizarla en la proyeccion
		Matrix X = MathsUtils.getMatrizNormalizadaMenosMedia(Z, media); // TODO: PARA MI ESTO NO VA... DSP LO CHARLAMOS...
				
		this.X = X;
	}
	
	/**
	 * yi = Wpca(traspuesta)*xi
	 * 
	 */
	public void proyectarImagenesDeReferencia() {
		Matrix Wtrans = this.W.transpose();
		this.Y = Wtrans.times(this.X);
	}
	
	public Matrix pasarAEigenface(List<int[]> _nuevasImagenes) throws Exception {
		int[][] nuevasImagenes = this.list2Matriz(_nuevasImagenes);
		double[] media = MathsUtils.getMediaMatriz (nuevasImagenes);
		double[] desvio = MathsUtils.getDesvioStdMatriz (nuevasImagenes);
		
		// Obtengo la matriz con los vectores normalizados
		double[][] Z = MathsUtils.getNormalization(nuevasImagenes, media, desvio);
		
		// Obtengo la matriz X para luego utilizarla en la proyeccion
		Matrix nuevaImagenMatriz = MathsUtils.getMatrizNormalizadaMenosMedia(Z, media); // TODO: PARA MI ESTO NO VA... DSP LO CHARLAMOS...
		Matrix Wtrans = this.W.transpose();
		return Wtrans.times(nuevaImagenMatriz);
	}
	
}
