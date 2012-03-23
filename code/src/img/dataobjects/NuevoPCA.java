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

public class NuevoPCA {
	private static Logger logger = Logger.getLogger(PCA.class.getSimpleName());
	private List<int[]> imagenesEntrenamiento;
	private List<Imagen> imagenesReferencia;//Basicamente es la base de datos de la aplicación.
	private double[] media; //media de la matriz generada con las imagenes de entrenamiento

	private Matrix W;	// 	Matriz de proyeccion de PCA
	private Matrix X;	//	Matriz preparada con las imagenes de referencia
	
	private Matrix Y; //Todas las imagenes proyectadas en el subespacio de 'caras'

	public NuevoPCA(String dirPathEntrenamiento) {
		logger.info(dirPathEntrenamiento);
		this.imagenesEntrenamiento = new ArrayList<int[]>();
		this.leer (dirPathEntrenamiento, this.imagenesEntrenamiento);
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
	
	public List<Imagen> getImagenesReferencia() {
		return imagenesReferencia;
	}

	public void setImagenesReferencia(List<Imagen> imagenesReferencia) {
		this.imagenesReferencia = imagenesReferencia;
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
				} 
				else if (listOfFiles[i].isDirectory()) {
					leer(dirPath+"\\"+listOfFiles[i].getName(), imagenes);
				}
	    	}
		}
	}
	
	public void leerAImagen(String dirPath, List<Imagen> imagenes) {
		File folder = new File(dirPath);
	    File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	    	if (!listOfFiles[i].isHidden()) {
				if (listOfFiles[i].isFile()) {
					Imagen imagen = new Imagen();
					PGM imagenPGM = new PGM(dirPath+"\\"+listOfFiles[i].getName());
					imagen.setImagen(imagenPGM.getPixelArrayDouble());
					imagen.setNombre(dirPath+"\\"+listOfFiles[i].getName());
					
					imagenes.add(imagen);
				} 
				else if (listOfFiles[i].isDirectory()) {
					leerAImagen(dirPath+"\\"+listOfFiles[i].getName(), imagenes);
				}
	    	}
		}
	}
	
	
	/**
	 * Entrena el algoritmo PCA obteniendo la matriz de proyeccion W
	 * @return
	 * @throws Exception
	 */
	public Matrix entrenar()  {
		long ini = 0;
		long fin = 0;
		
		int[][] X = this.list2Matriz(this.imagenesEntrenamiento);

		media = MathsUtils.getMediaMatriz(X);
		
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
	private int [][] list2Matriz(List<int[]> imagenes) {
		if (imagenes == null || imagenes.isEmpty()){
			throw new RuntimeException("No hay imagenes cargadas.");
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

	//Pasa el vector que esta dentro de imagen al espacio de 'caras' (o sea, le resta la media y lo multiplica por W)
	public void pasarAEigenface(Imagen imagen) {
		double[][] x_menos_media = new double[imagen.getImagen().length][1];

		// Preparo la X menos la media
		for (int i = 0; i < imagen.getImagen().length; i++){
			x_menos_media[i][0] = imagen.getImagen()[i] - media[i];
		}
		
		Matrix imagenMatriz = new Matrix(x_menos_media);
		Matrix Wtrans = this.W.transpose();
		Matrix imagenEnEigenface = Wtrans.times(imagenMatriz);
		
		imagen.setImagen(imagenEnEigenface.getRowPackedCopy());
	}
	
	//TODO: Esto habria que emprolijarlo un toque.
	public void generarImagenesDeReferencia(String dirImagenesDeReferencia) {
		imagenesReferencia = new ArrayList<Imagen>();
		this.leerAImagen(dirImagenesDeReferencia, imagenesReferencia);
		for ( Imagen imagen : this.imagenesReferencia) {
			this.pasarAEigenface(imagen);
		}
	}
	
	
	
}
