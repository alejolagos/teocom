package img.view.frames;

// Uso de un objeto JPanel para ayudar a distribuir los componentes.
import img.constants.Globals;
import img.dataobjects.Imagen;
import img.dataobjects.NuevoPCA;
import img.dataobjects.PGM;
import img.utils.MathsUtils;
import img.view.actions.AcceptAction;
import img.view.actions.ExitAction;
import img.view.actions.OpenFileAction;
import img.view.exceptions.CrearBaseDatosException;
import img.view.exceptions.EntrenarException;
import img.view.exceptions.InicializarException;
import img.view.exceptions.PropertiesException;
import img.view.filters.PGMFilter;
import img.view.panels.ImagenPanel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class EntradaFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	//   private static String TEXTO_ABRIR = "Seleccionar Imagen de Test";
	private static String TEXTO_ABRIR = "Examinar...";
	private NuevoPCA pca;
	private ImagenPanel panelImagenTest = new ImagenPanel();
	private File selectedFile;
	private String mensajeErrorFile = "";
	private Imagen resultFile;
	
	private static Logger logger = Logger.getLogger(EntradaFrame.class.getSimpleName());
	
	private Properties propFile;
   
	private JLabel label1;
	private int i = 0;
	private Timer timer;
	private JProgressBar progressBar;
	private JPanel panelPb = new JPanel();
	private JPanel panelBotones;
	private JPanel panelAceptar;
	private JFileChooser chooser;
	private int tiempoTarea;
	private JButton botonAbrir;
	private JButton botonAceptar;
	private JButton botonSalir;
	
	static final int TIEMPO_REFRESH_TIMER = 500;		// milisegundos
	static final int TIEMPO_TAREA_INICIO = 1;			// segundos
	static final int TIEMPO_TAREA_ENTRENAMIENTO = 52;	// segundos
	static final int TIEMPO_TAREA_BASE_DATOS = 5;		// segundos
	
	public EntradaFrame() {
		super("PCA - IMAGE TEST");
		
		// Configuracion ventana
		this.configureFrame();
		this.createTimer();
		this.createLabel1();
		this.createProgressBar();
		
		// obtener panel de contenido
		Container contenedor = getContentPane();
		contenedor.add(label1);
		contenedor.add(panelPb, BorderLayout.SOUTH);

		try {
			this.openPropertiesFile();

			// Trabajar imagenes
			this.inicializar();
			this.entrenar();
			this.crearBaseDeDatos();

			this.repaint();
		
			this.createFileChooser();
			
			contenedor.remove(panelPb);
			contenedor.remove(label1);
			contenedor.add(panelBotones, BorderLayout.EAST);
			contenedor.add(panelImagenTest, BorderLayout.CENTER);
			contenedor.add(panelAceptar, BorderLayout.SOUTH);
		} catch (Exception e) {
			this.repaint();
			
			contenedor.remove(panelPb);
			contenedor.remove(label1);
			contenedor.add(panelImagenTest, BorderLayout.CENTER);

			panelImagenTest.setErrorMsg(e.getMessage());
			panelImagenTest.setTestFile(null);
			panelImagenTest.paint(panelImagenTest.getGraphics());
			panelImagenTest.setVisible(true);
		}
		
		
		
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

	
	private void openPropertiesFile() throws PropertiesException {
		String fileName = Globals.PROPERTIES_PATH;
		try{
			this.propFile = new Properties();
		    InputStream is;
			is = new FileInputStream(fileName);
			propFile.load(is);
		}catch (Exception e) {
			throw new PropertiesException(fileName);
		}
	}
	
   	public void trabajarImagenTest (){
		PGM imagenPGM = new PGM(selectedFile.getAbsolutePath());
		
		Imagen imagen = new Imagen();
		imagen.setImagen(imagenPGM.getPixelArrayDouble());
		imagen.setNombre(selectedFile.getAbsolutePath());
	   
		try {
			pca.pasarAEigenface(imagen);

			String distanciaMenorNombre = "";
			double distanciaEuclidea = 1042;
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
			
			logger.info("Distancia menor encontrada " + distanciaMenor);
			
			if (distanciaMenor > distanciaEuclidea)
				this.resultFile = null;
			
			//
			if (resultFile != null && resultFile.getNombre() != null && !resultFile.getNombre().equals("")){
				this.mostrarImagenResult();
				logger.info(distanciaMenorNombre);
			}
			else{
				this.mensajeErrorFile = "NO ENCONTRADO";
				this.mostrarErrorImagenResult();
				// mostrar no encontrado
				logger.info("NO ENCONTRADO");
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

	public void mostrarErrorImagenResult(){
		panelImagenTest.setErrorMsg(mensajeErrorFile);
		panelImagenTest.paint(panelImagenTest.getGraphics());
		panelImagenTest.setVisible(true);
	}
	
	public void mostrarErrorImagenTest(){
		panelImagenTest.setErrorMsg(mensajeErrorFile);
		panelImagenTest.paint(panelImagenTest.getGraphics());
		panelImagenTest.setVisible(true);
		
		botonAceptar.setEnabled(false);
	}

	public void mostrarImagenTest(){
		panelImagenTest.setTestFile(selectedFile);
		panelImagenTest.paint(panelImagenTest.getGraphics());
		panelImagenTest.setVisible(true);

		botonAceptar.setEnabled(true);
	}

	public void mostrarImagenResult(){
		File resultFile = new File (this.resultFile.getNombre());
		panelImagenTest.setResultFile(resultFile);
		panelImagenTest.paint(panelImagenTest.getGraphics());
		panelImagenTest.setVisible(true);
		panelImagenTest.setResultFile(null);
	}

	public void errorArchivoTestSeleccionado(File file) {
		String msg = (file != null && file.getName() != null) ?  "El archivo \"" + file.getName() + "\"  que intenta abrir no es una imagen PGM valida." : "";
		this.mensajeErrorFile = msg; 
		logger.info(msg);
	}
	
	class PanelListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			
		}
	}
	
	private void configureFrame(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Globals.FRAME_WIDTH, Globals.FRAME_HEIGHT);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	private void createProgressBar(){
		progressBar = new JProgressBar(0);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setVisible(true);
		panelPb.add(progressBar);
	}
	
	private void createTimer(){
		timer = new Timer(TIEMPO_REFRESH_TIMER, new ActionListener() {
			  public void actionPerformed(ActionEvent evt) {
				  if (i == tiempoTarea){
					  Toolkit.getDefaultToolkit().beep();
					  timer.stop();
					  progressBar.setValue(0);
				  }
				  i++;// = i + 1;
				  progressBar.setValue(i);
			  }
		});
	}
	
	private void createLabel1(){
		label1 = new JLabel();
		label1.setHorizontalAlignment(JLabel.CENTER);
		label1.setVisible(true);
		label1.setVerticalTextPosition(JLabel.BOTTOM);
		label1.setHorizontalTextPosition(JLabel.CENTER);
	}
	
	private void inicializar() throws InicializarException{
		try{
			label1.setText("INICIALIZANDO APLICACIÓN...");
			tiempoTarea = TIEMPO_TAREA_INICIO; 
			progressBar.setValue(tiempoTarea);	// Porque es muy cortita
			progressBar.setMaximum(tiempoTarea);
			i = 0;
			timer.start();
		}catch (Exception e){
			throw new InicializarException();
		}
	}
	
	private void entrenar() throws EntrenarException{
		try{
			this.pca = new NuevoPCA(propFile.getProperty("entrenamientoFolder"));
			logger.info("-------- INICIO ENTRENAMIENTO --------");
			label1.setText("ENTRENANDO AL SISTEMA...");
			tiempoTarea = TIEMPO_TAREA_ENTRENAMIENTO * 1000 / TIEMPO_REFRESH_TIMER; 
			progressBar.setMaximum(tiempoTarea);
			timer.start();
			this.repaint();
			
			pca.entrenar();
			
			logger.info("--------  FIN ENTRENAMIENTO --------");
		} catch (Exception e) {
			throw new EntrenarException();
		}
	}
	
	private void crearBaseDeDatos() throws CrearBaseDatosException{
		try{
			label1.setText("GENERANDO BASE DE DATOS...");
			tiempoTarea = TIEMPO_TAREA_BASE_DATOS * 1000 / TIEMPO_REFRESH_TIMER; 
			progressBar.setMaximum(tiempoTarea);
			timer.start();
			
			this.repaint();
			
			pca.generarImagenesDeReferencia(propFile.getProperty("referenciaFolder"));
			
			label1.setText("SE FINALIZÓ LA GENERACION DE BASE DE DATOS");	
		} catch (Exception e) {
			throw new CrearBaseDatosException();
		}
	}
	
	private void createFileChooser(){
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(propFile.getProperty("defaultTestFolder")));
		chooser.addChoosableFileFilter(new PGMFilter());
		chooser.setDialogTitle(TEXTO_ABRIR);
		
		Action openAction = new OpenFileAction(this, chooser);
		Action acceptAction = new AcceptAction(this);
		Action exitAction = new ExitAction(this);

		// Examinar
		panelBotones = new JPanel();
		botonAbrir = new JButton(openAction);
		botonAbrir.setText(TEXTO_ABRIR);
		panelBotones.add(botonAbrir);
		
		// Aceptar / Salir
		panelAceptar = new JPanel();
		botonAceptar = new JButton(acceptAction);
		botonAceptar.setText("Procesar");
		botonSalir = new JButton(exitAction);
		botonSalir.setText("Salir");
		panelAceptar.add(botonAceptar);
		panelAceptar.add(botonSalir);

		botonAceptar.setEnabled(false);
	}

} 