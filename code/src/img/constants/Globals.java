package img.constants;


public class Globals {
	
	public static String PROPERTIES_PATH = "config/app.properties";
	
	public static int BackGround=0,ForeGround=255;
	public static int NW=0,N=1,NE=2,E=3,SE=4,S=5,SW=6,W=7;
	
	// En el 824 arranca con los positivos
	//  824:    10499.530332797367
	//  988:  1024818.0694527895
	// 1023: 1.8256840235378237E9 --> 1.825.684.000
	public static int COLS_PCA_MATRIZ_PROYECCION = 200;
	
	public static int PANEL_IMAGEN_WIDTH = 400;
	public static int PANEL_IMAGEN_HEIGHT = 250;
	public static int FRAME_WIDTH = PANEL_IMAGEN_WIDTH + 150;
	public static int FRAME_HEIGHT = PANEL_IMAGEN_HEIGHT + 100;
}
