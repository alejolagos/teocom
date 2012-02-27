import img.utils.MathsUtils;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class CovarianzaTest {

	public static int[][] getMatrizTest (){
		int m = 3; // rows
		int n = 2; // cols
		
		int[] x1 = {1,2,3};
		int[] x2 = {4,5,6};
		
		int[][] X = new int[m][n];
		X[0][0] = x1[0];
		X[1][0] = x1[1];
		X[2][0] = x1[2];
		X[0][1] = x2[0];
		X[1][1] = x2[1];
		X[2][1] = x2[2];
		
		return X;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[][] X = getMatrizTest();
		
		Matrix cov = MathsUtils.getMatrizCovarianza(X);
		cov.print(3, 2);
		
		EigenvalueDecomposition E = cov.eig();
		double[] d = E.getRealEigenvalues();
		Matrix V = E.getV();
		Matrix D = E.getD();
		D.print(3, 2);
		V.print(3, 2);
		
		Matrix A = V.times(D.times(V.transpose()));
		A.print(3, 2);
		
		Matrix W = MathsUtils.getMatrizAuvectoresSignificativos(V, 7);
		W.print(W.getColumnDimension(), 2);
	}

}
