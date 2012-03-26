package img.utils;

import org.apache.log4j.Logger;



public class CommonsUtils {
	private static Logger logger = Logger.getLogger(CommonsUtils.class);

	public static void loguearTiempoEjecucion (long ini, long fin, String className, String msg){
		logger.info(className + " - Msg - " +  msg + " " + (fin - ini) / 1000 + "segs.");
	}
	
}
