package img.utils;

import java.util.logging.Logger;


public class CommonsUtils {
	private static Logger logger = Logger.getLogger(CommonsUtils.class.getSimpleName());

	public static void loguearTiempoEjecucion (long ini, long fin, String className, String msg){
		logger.info(className + " - Msg - " +  msg + " " + (fin - ini) / 1000 + "segs.");
	}
	
}
