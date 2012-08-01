package img.view.exceptions;

public class PropertiesException extends AppImageTestException {

	private static final long serialVersionUID = 1L;

	public PropertiesException(String fileName) {
		super("Error al cargar el archivo de configuracion: " + fileName);
	}

	public PropertiesException(String parameter, String message) {
		super("- " + parameter + ": " + message);
	}

	
}
