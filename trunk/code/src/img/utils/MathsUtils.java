package img.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
			media[i] = media[i] / (double)m;
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
	
//	public static int[] getEigenvaluesOrdenados (double[] eigenvalues){
//		int[] pos = new int[eigenvalues.length];
//		for (int i = 0; i < pos.length; i++){
//			pos[i] = i;
//		}
//		
//		double aux;
//		int auxPos;
//		
//		for (int i = 0; i < eigenvalues.length-1; i++){
//			for (int j = 0; j < eigenvalues.length-1; j++){
//				if ( eigenvalues[j] < eigenvalues[j+1] ){
//					
//					aux = eigenvalues[j];
//					eigenvalues[j] = eigenvalues[j+1];
//					eigenvalues[j+1] = aux;
//					
//					auxPos = pos[j];
//					pos[j] = pos[j+1];
//					pos[j+1] = auxPos;
//				}
//			}
//		}
//		
//		return pos;
//	}
	
}
