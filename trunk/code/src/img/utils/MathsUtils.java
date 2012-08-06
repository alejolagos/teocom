package img.utils;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class MathsUtils {

	/**
	 * Calcula la media de una matriz de vectores
	 */
	public static double[] getMediaMatriz (int [][] X){
		int m = X.length;		// Rows
		int n = X[0].length;	// Cols

		double[] media = new double[m];
		
		for (int i = 0; i < m; i++){
			for (int j = 0; j < n; j++){
				media[i] += X[i][j];
			}
//			media[i] = media[i] / (double)m;
			media[i] = media[i] / (double)n;
		}
		return media;
	}
	
	/**
	 * Calculo la covarianza de una matriz de vectores
	 */
	public static Matrix getMatrizCovarianza (int[][] X){
		int m = X.length;
		int n = X[0].length;
		
		double[] u = getMediaMatriz(X);
		
		double[][] cov = new double[m][m];
		double[][] x_menos_u = new double[m][n];

		// Preparo la X menos la media
		for (int j = 0; j < n; j++){
			for (int i = 0; i < m; i++){
				x_menos_u[i][j] = X[i][j] - u[i];
			}
		}
		
		// Calculo la matriz de covarianza
		for (int k = 0; k < n; k++){ 	// es para pasar a la siguiente imagen
			for (int j = 0; j < m; j++){ 	
				for (int i = 0; i < m; i++){
					cov[i][j] += x_menos_u[i][k] * x_menos_u[j][k];
				}
			}
		}
		
		return new Matrix(cov);
	}
	
	/**
	 * Obtengo la matriz de proyeccion con los autovectores mas significativos
	 * @param E
	 * @param d
	 * @return
	 */
	public static Matrix getMatrizProyeccionPCA (EigenvalueDecomposition E, int d){
		Matrix V = E.getV(); // Viene ordenada por los autovalores de menor a mayor
		int m = V.getRowDimension();
		int n = V.getColumnDimension();
		
		if (d > n){
			d = n; 	// TODO
		}
//		E.getRealEigenvalues()
		Matrix W = new Matrix(m, d);
		
		// Obtengo los autovectores mas significativos, de atras para adelante
		for (int j = n-1; j >= n-d; j--){
			int jPos = n-1-j;
			for (int i = 0; i < n; i++){
				W.set(i, jPos, V.get(i, j));
			}
		}
		
		return W;
	}
	
	// normalization of x relatively to X mean and standard deviation
	public static double[][] getNormalization (int[][] X, double[] media, double[] desvio) {
		int m = X.length;
		int n = X[0].length;
		
		double[][] y = new double[m][n];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				y[i][j] = (X[i][j] - media[j]) / desvio[j];
		return y;
	}
	
	public static double[] getDesvioStdMatriz(int[][] X) {
        double[] var = getVarianzaMatriz(X);
        
        int n = var.length;
        
        for (int i = 0; i < n; i++){
            var[i] = Math.sqrt(var[i]);
        }
        return var;
    }

    public static double[] getVarianzaMatriz (int[][] X) {
        int m = X.length;
        int n = X[0].length;
        
        double[] var = new double[n];
        
        int degrees = (m - 1);
        double c;
        double s;
        
        for (int j = 0; j < n; j++) {
            c = 0;
            s = 0;
            
            for (int k = 0; k < m; k++){
                s += X[k][j];
            }
            s = s / m;
            
            for (int k = 0; k < m; k++){
                c += (X[k][j] - s) * (X[k][j] - s);
            }
            var[j] = c / degrees;
        }
        return var;
    }

    
    /**
	 * Calculo la covarianza de una matriz de vectores
	 */
	public static Matrix getMatrizNormalizadaMenosMedia (double[][] Z, double[] media){
		int m = Z.length;
		int n = Z[0].length;

		double[][] z_menos_u = new double[m][n];

		// Preparo la X menos la media
		for (int j = 0; j < n; j++){
			for (int i = 0; i < m; i++){
				z_menos_u[i][j] = Z[i][j] - media[i];
			}
		}
		
		return new Matrix(z_menos_u);
	}
	
	/**
	 * Devuelve la distancia euclideana entre los dos vectores	
	 */
	public static double distanciaEntreVectores(double[] array1, double[] array2) {
		double distancia;
		double suma = 0;
		for (int i = 0; i < array1.length; i++) {
			suma = suma + ( (array1[i] - array2[i]) * (array1[i] - array2[i]) );
		}
		distancia = Math.sqrt(suma);
		return distancia;
	}
		
	
}
