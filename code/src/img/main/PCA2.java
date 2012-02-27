package img.main;

import static org.math.array.DoubleArray.random;
import static org.math.array.DoubleArray.sum;
import static org.math.array.DoubleArray.transpose;
import static org.math.array.LinearAlgebra.eigen;
import static org.math.array.StatisticSample.covariance;
import static org.math.array.StatisticSample.mean;
import static org.math.array.StatisticSample.stddeviation;

import org.math.array.DoubleArray;
import org.math.plot.FrameView;
import org.math.plot.Plot2DPanel;

import Jama.EigenvalueDecomposition;

/**
 * Copyright : BSD License
 * @author Yann RICHET
 */

 /**
  *	http://jmathtools.berlios.de/doku.php?id=cases:pca
  */
public class PCA2 {

	double[][] X; // initial datas : lines = events and columns = variables

	double[] meanX, stdevX;

	double[][] Z; // X centered reduced

	double[][] cov; // Z covariance matrix

	double[][] U; // projection matrix

	double[] info; // information matrix

	public PCA2(double[][] _X) {
		X = _X;

		stdevX = stddeviation(X);
		meanX = mean(X);

		Z = center_reduce(X);

		cov = covariance(Z);

		EigenvalueDecomposition e = eigen(cov);
		U = transpose(e.getV().getArray());
		info = e.getRealEigenvalues();//.get.get1DRealD(); // covariance matrix is symetric, so only real eigenvalues...
	}

	// normalization of x relatively to X mean and standard deviation
	public double[][] center_reduce(double[][] x) {
		double[][] y = new double[x.length][x[0].length];
		for (int i = 0; i < y.length; i++)
			for (int j = 0; j < y[i].length; j++)
				y[i][j] = (x[i][j] - meanX[j]) / stdevX[j];
		return y;
	}

	// de-normalization of y relatively to X mean and standard deviation
	public double[] inv_center_reduce(double[] y) {
		return inv_center_reduce(new double[][] { y })[0];
	}

	// de-normalization of y relatively to X mean and standard deviation
	public double[][] inv_center_reduce(double[][] y) {
		double[][] x = new double[y.length][y[0].length];
		for (int i = 0; i < x.length; i++)
			for (int j = 0; j < x[i].length; j++)
				x[i][j] = (y[i][j] * stdevX[j]) + meanX[j];
		return x;
	}

	private void view() {
		// Plot
		Plot2DPanel plot = new Plot2DPanel();

		// initial Datas plot
		plot.addScatterPlot("datas", X);

		// line plot of principal directions
		plot.addLinePlot(Math.rint(info[0] * 100 / sum(info)) + " %", meanX, inv_center_reduce(U[0]));
		plot.addLinePlot(Math.rint(info[1] * 100 / sum(info)) + " %", meanX, inv_center_reduce(U[1]));

		// display in JFrame
		new FrameView(plot);
	}

	private void print() {
		// Command line display of results
		System.out.println("projection vectors\n" + DoubleArray.toString(transpose(U)));
		System.out.println("information per projection vector\n" + DoubleArray.toString(info));
	}

	public static void main(String[] args) {
		double[][] xinit = random(1000, 2, 0, 10);

		// artificial initialization of relations
		double[][] x = new double[xinit.length][];
		for (int i = 0; i < x.length; i++)
			x[i] = new double[] { xinit[i][0] + xinit[i][1], xinit[i][1] };

		// MI NO COMPRENDER POR QUE LE PASA UNA MATRIZ
		PCA2 pca = new PCA2(x);
		pca.print();
		pca.view();
	}

}