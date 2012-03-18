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

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EntradaFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	//   private static String TEXTO_ABRIR = "Seleccionar Imagen de Test";
	private static String TEXTO_ABRIR = "Examinar...";
	private NuevoPCA pca;
	private ImagenPanel panelImagen;
	private File selectedFile;
   
	public EntradaFrame(NuevoPCA pca) {
		super("Entrada");

		this.pca = pca;

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("C:\\teocom\\test\\"));
		// chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new PGMFilter());
		chooser.setDialogTitle(TEXTO_ABRIR);

		Action openAction = new OpenFileAction(this, chooser);
		Action acceptAction = new AcceptAction(this);

		JButton botonAbrir = new JButton(openAction);
		botonAbrir.setText(TEXTO_ABRIR);
		JPanel panelBotones = new JPanel();
		panelBotones.add(botonAbrir);

		panelImagen = new ImagenPanel(null);
		panelImagen.setVisible(false);

		JButton botonAceptar = new JButton(acceptAction);
		botonAceptar.setText("Aceptar");
		JPanel panelAceptar = new JPanel();
		panelAceptar.add(botonAceptar);

		// obtener panel de contenido
		Container contenedor = getContentPane();
		contenedor.add(panelBotones, BorderLayout.EAST);
		contenedor.add(panelImagen, BorderLayout.CENTER);
		contenedor.add(panelAceptar, BorderLayout.SOUTH);

		setSize(300, 200);
		setVisible(true);
		setLocationRelativeTo(null);

	} // fin del constructor de DemoPanel

	public static void main(String args[]) {
		EntradaFrame aplicacion = new EntradaFrame(null);
		aplicacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

   
   	public void trabajarImagenTest (){
//		pca.leerAImagen("C:\\teocom\\test\\", imagenes );
		
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
				}
			}
			
			System.out.println(distanciaMenorNombre);
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
  
	public void mostrarImagen(){
		panelImagen.setSelectedFile(selectedFile);
		panelImagen.paint(panelImagen.getGraphics());
		panelImagen.setVisible(false);
	}
	
   	
} 