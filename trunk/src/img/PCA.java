package img;

import java.io.File;
import java.util.ArrayList;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class PCA {
	private ArrayList<int[]> imagenes;
	private int[][] w;

	public ArrayList<int[]> getImagenes() {
		return imagenes;
	}

	public void setImagenes(ArrayList<int[]> imagenes) {
		this.imagenes = imagenes;
	}
	
	public int[][] getW() {
		return w;
	}

	public void setW(int[][] w) {
		this.w = w;
	}

	
	public PCA(String dirPath) {
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
	
	public int[][] entrenar() {
		
		Matrix covarianza = this.primeraParte();
		
		EigenvalueDecomposition E = covarianza.eig();
		double[] d = E.getRealEigenvalues();
		
		
		return w;
	}
	
	private Matrix primeraParte() {
		int i;
		
		//calculo la media
		int[] media = new int[imagenes.get(0).length];
		for (int[] imagen : imagenes) {
			i = 0;
			while (i<imagen.length) {
				media[i]=media[i]+imagen[i];
				i++;
			}
		}
		
		i = 0;
		while (i<media.length) {
			media[i]=media[i]/imagenes.size();
			i++;
		}
		
		//calculo x-media
		int[] aux = new int[media.length];
		for (int[] imagen : imagenes) {
			i = 0;
			while (i<imagen.length) {
				aux[i] = imagen[i]-media[i];
				i++;
			}
		}
		
//		//calculo la matriz de covarianza
//		double[][] covarianza = new double[media.length][media.length];
//		int j = 0;
//		i = 0;
//		
//		while (i<media.length) {
//			while (j<media.length) {
//				covarianza[i][j] = aux[i]*aux[j];
//				j++;
//			}
//			i++;
//		}
//				
//		Matrix matriz = new Matrix(covarianza);
		
		Matrix covarianza = new Matrix(media.length, media.length);
		int j = 0;
		i = 0;
		
		while (i<media.length) {
			while (j<media.length) {
				covarianza.set(i, j, aux[i]*aux[j]);
				j++;
			}
			i++;
		}
		
		return covarianza;
	}

}
