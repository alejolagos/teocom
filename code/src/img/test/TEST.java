package img.test;

import java.io.File;

import img.dataobjects.Imagen;
import img.dataobjects.PGM;
import img.utils.MathsUtils;
import img.view.exceptions.PropertiesException;
import img.view.frames.EntradaFrame;

import javax.swing.JFrame;

import org.apache.log4j.Logger;


public class TEST {
	
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(EntradaFrame.class);
		logger.info("=====================================================");
		logger.info("Nueva instancia TEST");
		logger.info("=====================================================");
		EntradaFrame aplicacion = new EntradaFrame();
		aplicacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
		
		Imagen resultFile = null;
		
		File folder = new File(aplicacion.getTestFolder());
		
	    File[] listOfFiles = folder.listFiles();

	    String toLog = "Cantidad Entrenamiento: " + aplicacion.getCantImgEntrenamiento();
	    logger.info(toLog);
	    toLog = "Cantidad Referencia: " + aplicacion.getCantImgReferencia();
	    logger.info(toLog);
	    
	    toLog = "Entrada Nombre;Entrada Foto;Distancia;Encontrada Nombre;Encontrada Foto;Coincidencia";
	    logger.info(toLog);
	    
	    
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	if (!listOfFiles[i].isHidden()) {
				if (listOfFiles[i].isFile()) {
					Imagen imagen = new Imagen();
					
					PGM imagenPGM = new PGM(aplicacion.getTestFolder()+"\\"+listOfFiles[i].getName());
					imagen.setImagen(imagenPGM.getPixelArrayDouble());
					imagen.setNombre(aplicacion.getTestFolder()+"\\"+listOfFiles[i].getName());
					
					aplicacion.getPca().pasarAEigenface(imagen);
					
					String distanciaMenorNombre = "";
					double distanciaMenor = 99999;
					double aux;
					
					String nombreArchivoTest = imagen.getNombre().replace("images\\orl_test\\\\", ""); 
					toLog = nombreArchivoTest.substring(0,3) + ";" + nombreArchivoTest.substring(4,6) + ";";
					
					for (Imagen imagenBase : aplicacion.getPca().getImagenesReferencia()) {
						aux = MathsUtils.distanciaEntreVectores(imagenBase.getImagen(), imagen.getImagen());
						if (aux < distanciaMenor) {
							distanciaMenor = aux;
							distanciaMenorNombre = imagenBase.getNombre();
							resultFile = imagenBase;
						}
					}
					
//					distanciaMenor /= 1000;
					toLog += String.valueOf(distanciaMenor).replace(".", ",") +";";
					
					if (distanciaMenor > aplicacion.getDistanciaEuclidea())
						resultFile = null;
					
					//
					if (resultFile != null && resultFile.getNombre() != null && !resultFile.getNombre().equals("")){
						String nombreArchivoEncontrado = resultFile.getNombre().replace("images\\orl_db\\\\", "");
						toLog += nombreArchivoEncontrado.substring(0,3) + ";" + nombreArchivoEncontrado.substring(4,6) + ";";
						
						toLog += nombreArchivoTest.substring(1,3).equals(nombreArchivoEncontrado.substring(1,3)) ? "Si" : "No";
					}
					else{
						toLog += "NO ENCONTRADO;NO ENCONTRADO;No";
					}
					logger.info(toLog);
				} 
	    	}
		}
		
		   
		
	}

}
