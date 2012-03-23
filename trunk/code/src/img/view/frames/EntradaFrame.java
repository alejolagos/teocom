package img.view.frames;

// Uso de un objeto JPanel para ayudar a distribuir los componentes.
import img.dataobjects.Imagen;
import img.dataobjects.NuevoPCA;
import img.dataobjects.PGM;
import img.utils.MathsUtils;
import img.view.actions.AcceptAction;
import img.view.actions.OpenFileAction;
import img.view.filters.PGMFilter;
import img.view.panels.ImagenPanel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EntradaFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	//   private static String TEXTO_ABRIR = "Seleccionar Imagen de Test";
	private static String TEXTO_ABRIR = "Examinar...";
	private NuevoPCA pca;
	private ImagenPanel panelImagenTest;
	private File selectedFile;
	private Imagen resultFile;
	
	private static Logger logger = Logger.getLogger(EntradaFrame.class.getSimpleName());
   
	public EntradaFrame() {
		super("Entrada");

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 500);
		setVisible(true);
		setLocationRelativeTo(null);
		
		JLabel label1 = new JLabel("INICIALIZANDO APLICACIÓN...", JLabel.CENTER);
		label1.setVerticalTextPosition(JLabel.BOTTOM);
		label1.setHorizontalTextPosition(JLabel.CENTER);
		
		// obtener panel de contenido
		Container contenedor = getContentPane();
		contenedor.add(label1);
		
		this.pca = new NuevoPCA("C:\\teocom\\entrenamiento\\");
		logger.info("-------- INICIO ENTRENAMIENTO --------");
		label1.setText("ENTRENANDO AL SISTEMA...");
		this.repaint();
		pca.entrenar();
		logger.info("--------  FIN ENTRENAMIENTO --------");
		
		label1.setText("GENERANDO BASE DE DATOS...");
		this.repaint();
			
		pca.generarImagenesDeReferencia("C:\\teocom\\referencia\\");
		
		label1.setText("SE FINALIZÓ LA GENERACION DE BASE DE DATOS");
		this.repaint();

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("C:\\teocom\\test\\"));
		chooser.addChoosableFileFilter(new PGMFilter());
		chooser.setDialogTitle(TEXTO_ABRIR);

		Action openAction = new OpenFileAction(this, chooser);
		Action acceptAction = new AcceptAction(this);

		JButton botonAbrir = new JButton(openAction);
		botonAbrir.setText(TEXTO_ABRIR);
		JPanel panelBotones = new JPanel();
		panelBotones.add(botonAbrir);

		panelImagenTest = new ImagenPanel();

		JButton botonAceptar = new JButton(acceptAction);
		botonAceptar.setText("Aceptar");
		JPanel panelAceptar = new JPanel();
		panelAceptar.add(botonAceptar);

		contenedor.remove(label1);
		contenedor.add(panelBotones, BorderLayout.EAST);
		contenedor.add(panelImagenTest, BorderLayout.CENTER);
		contenedor.add(panelAceptar, BorderLayout.SOUTH);
		repintar();
		

	} // fin del constructor de DemoPanel

	@SuppressWarnings("unused")
	public static void main(String args[]) {
		EntradaFrame aplicacion = new EntradaFrame();
	}
	
	private void repintar() {
		// Tengo qeu hacer estas 3 cosas para que se repinte :S
		getContentPane().repaint();
		setVisible(false);
		setVisible(true);
	}

   
   	public void trabajarImagenTest (){
		PGM imagenPGM = new PGM(selectedFile.getAbsolutePath());
		
		Imagen imagen = new Imagen();
		imagen.setImagen(imagenPGM.getPixelArrayDouble());
		imagen.setNombre(selectedFile.getAbsolutePath());
	   
		try {
			pca.pasarAEigenface(imagen);

			String distanciaMenorNombre = "";
			double distanciaMenor = 99999;
			double aux;
			
			for (Imagen imagenBase : pca.getImagenesReferencia()) {
				aux = MathsUtils.distanciaEntreVectores(imagenBase.getImagen(), imagen.getImagen());
				if (aux < distanciaMenor) {
					distanciaMenor = aux;
					distanciaMenorNombre = imagenBase.getNombre();
					this.resultFile = imagenBase;
				}
			}
			
			//
			if (resultFile != null && resultFile.getNombre() != null && !resultFile.getNombre().equals("")){
				this.mostrarImagenResult();
				System.out.println(distanciaMenorNombre);
			}
			else{
				// mostrar no encontrado
				System.out.println("NO ENCONTRADO");
			}
				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }

	public File getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(File selectedFile) {
		this.selectedFile = selectedFile;
	}
	
	public Imagen getResultFile() {
		return resultFile;
	}

	public void setResultFile(Imagen resultFile) {
		this.resultFile = resultFile;
	}

	public void mostrarImagenTest(){
		panelImagenTest.setTestFile(selectedFile);
		panelImagenTest.paint(panelImagenTest.getGraphics());
		panelImagenTest.setVisible(true);
	}

	public void mostrarImagenResult(){
		File resultFile = new File (this.resultFile.getNombre());
		panelImagenTest.setResultFile(resultFile);
		panelImagenTest.paint(panelImagenTest.getGraphics());
		panelImagenTest.setVisible(true);
		panelImagenTest.setResultFile(null);
	}

	public void errorArchivoTestSeleccionado(File file) {
		// TODO
		System.out.println("Error archivo seleccionado...");
	}
	
   	
} 