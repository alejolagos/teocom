package img.main;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.PNMEncodeParam;

public class MainNuevo {
    static int width, height; 
    static String type;
    static int max; 
	
	public static void main(String args[])
	{
		try {
			String strFilePath = "C:/teocom/entrenamiento/test/1.pgm";
			String outputFilePath = "C:/teocom/entrenamiento/test/Picture 2.pgm";
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			fis = new FileInputStream(strFilePath);
			bis = new BufferedInputStream(fis);

			readHeader(bis);
			if (!type.equals("P5")) {
				throw new Exception("I can't know this type.");
			}
			BufferedImage bufimage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			int outX, outY;
			int i = -1;
			outX = 0;
			outY = 0;
			while ((i = bis.read()) != -1) {
				bufimage.setRGB(outX, outY, (((i << 8) | i) | (i << 16)));
				outX = outX + 1;
				if (outX == width) {
					outX = 0;
					outY = outY + 1;
				}
			}
			
			int heightNuevo = 32;
            int widthNuevo = 32;

            BufferedImage bufferedImage = new BufferedImage(widthNuevo, heightNuevo, BufferedImage.TYPE_BYTE_GRAY);

			bufferedImage.createGraphics().drawImage(ResizerUtil.resize(bufimage, widthNuevo, heightNuevo), 0, 0, null);

			FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);

			PNMEncodeParam param = new PNMEncodeParam();
			param.setRaw(true);

			ImageEncoder encoder = ImageCodec.createImageEncoder("PNM", fileOutputStream, param);

			encoder.encode(bufferedImage);

			fileOutputStream.close();
			
			//Paso de bufferedImage a inputStream, asi no hace falta guardar
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "gif", os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * P5
     * 300 300
     * 100
     */
	public static void readHeader(BufferedInputStream bis) throws Exception {
        int iCh = 0;
        int cnt = 0;
        StringBuffer[] arrSb = new StringBuffer[4];

        //main loop
        do {
            iCh = bis.read();
            arrSb[cnt] = new StringBuffer();
            //skip space \n \r
            while ((char) iCh == ' '
                || (char) iCh == '\n'
                || (char) iCh == '\r') {
                iCh = bis.read();
            }

            do {
                arrSb[cnt].append((char) iCh);
                iCh = bis.read();
            } while (
                iCh != -1
                    && (char) iCh != ' '
                    && (char) iCh != '\n'
                    && (char) iCh != '\r');

            cnt++;
            bis.mark(1);
        }
        while ((iCh) != -1 && cnt < arrSb.length);

        bis.reset();

        type = new String(arrSb[0]);
        width = Integer.parseInt(new String(arrSb[1]));
        height = Integer.parseInt(new String(arrSb[2]));
        max = Integer.parseInt(new String(arrSb[3]));
    }

}
