package img.utils;

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
//			media[i] /= m;
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

		
		for (int j = 0; j < n; j++){
			for (int i = 0; i < m; i++){
				x_menos_u[i][j] = X[i][j] - u[i];
			}
		}
		
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
	 * Obtengo la matriz de los autovectores mas significativos
	 * @param E
	 * @param d
	 * @return
	 */
	public static Matrix getMatrizAuvectoresSignificativos (Matrix E, int d){
		int m = E.getRowDimension();
		int n = E.getColumnDimension();
		
		if (d > n){
			d = n; 	// TODO
		}
		
		Matrix W = new Matrix(m, d);
		
		for (int i = 0; i < n; i++){
			for (int j = 0; j < d; j++){
				W.set(i, j, E.get(i, j));
			}
		}
		
		return W;
		
	}
	
}
