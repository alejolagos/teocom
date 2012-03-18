package img.view;

// Uso de un objeto JPanel para ayudar a distribuir los componentes.
import img.dataobjects.Imagen;
import img.dataobjects.NuevoPCA;
import img.dataobjects.PGM;
import img.utils.MathsUtils;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EntradaFrame extends JFrame {
	
   private JPanel panelBotones;
//   private File selectedFile;
   private NuevoPCA pca;

   private static String TEXTO_ABRIR = "Seleccionar Imagen de Test";
   
   
   public EntradaFrame(NuevoPCA pca) {
      super( "Entrada" );
      
      this.pca = pca;

      JFileChooser chooser = new JFileChooser(); 
      chooser.setCurrentDirectory(new File("C:\\teocom\\test\\"));
//      chooser.setAcceptAllFileFilterUsed(false);
      chooser.setFileFilter(new PGMFilter());
      chooser.setDialogTitle(TEXTO_ABRIR);
      
      Action openAction = new OpenFileAction(this, chooser);
      
      JButton botonAbrir = new JButton(openAction);
      botonAbrir.setText(TEXTO_ABRIR);
      
      // configurar panel y establecer su esquema
      panelBotones = new JPanel();
//      panelBotones.setLayout( new GridLayout( 1, 1 ) );
      panelBotones.add(botonAbrir);

      // obtener panel de contenido
      Container contenedor = getContentPane();
      contenedor.add( panelBotones, BorderLayout.SOUTH );

      setSize(300, 200);
      setVisible(true);
      setLocationRelativeTo(null);

   } // fin del constructor de DemoPanel

//   public static void main( String args[] ){
//	   DemoPanel aplicacion = new DemoPanel();
//	   aplicacion.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//   }
   
   // Action para el boton Abrir
   class OpenFileAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		JFrame frame;
	    JFileChooser chooser;
	    File file;

	    OpenFileAction(JFrame frame, JFileChooser chooser) {
	        super("Open...");
	        this.chooser = chooser;
	        this.frame = frame;
	    }

	    public void actionPerformed(ActionEvent evt) {
	    	// Show dialog; this method does not return until dialog is closed
//	    	chooser.showOpenDialog(frame);
//	    	file = chooser.getSelectedFile();

//	    	// Show the dialog; wait until dialog is closed
	    	int result = chooser.showOpenDialog(frame);

	    	// Determine which button was clicked to close the dialog
	    	switch (result) {
	    		case JFileChooser.APPROVE_OPTION:
		            // Approve (Open or Save) was clicked
	    			file = chooser.getSelectedFile();
	    			((EntradaFrame)frame).trabajarImagenTest(file);
	    			break;
	    		case JFileChooser.CANCEL_OPTION:
					// Cancel or the close-dialog icon was clicked
					break;
	    		case JFileChooser.ERROR_OPTION:
					// The selection process did not complete successfully
					break;
	        }
	        
	        // Get the selected file
	        
	    }

	    public File getFile (){
	    	return this.file;
	    }
   };
   
   class PGMFilter extends javax.swing.filechooser.FileFilter {
	    public boolean accept(File file) {
	        String filename = file.getName();
	        return filename.endsWith(".pgm");
	    }
	    public String getDescription() {
	        return "*.pgm";
	    }
	    
	}
   
   	public void trabajarImagenTest (File file){
//		pca.leerAImagen("C:\\teocom\\test\\", imagenes );
		
		PGM imagenPGM = new PGM(file.getAbsolutePath());
		
		Imagen imagen = new Imagen();
		imagen.setImagen(imagenPGM.getPixelArrayDouble());
		imagen.setNombre(file.getAbsolutePath());
	   
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
   
} 