package img.dataobjects;

import img.constants.Globals;
import img.utils.ResizerUtil;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.PNMEncodeParam;

public class PGM
{
	private String FilePath;
	
	//pgm imageheader
	private String Type;
	private String Comment;
	private int Columns,Rows,MaxGray;
	
	//pgm imagedata
	private int[][] Pixels;
	
	private String typeFile;
	private int widthFile;
	private int heightFile;
	
	//constructors
	public PGM()
	{
		FilePath="";
		Type="";
		Comment="";
		Columns=0;
		Rows=0;
		MaxGray=0;
		Pixels=null;
	}
	
	public PGM(String tpath)
	{
		FilePath=tpath;
		try {
			readImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PGM(int tColumns,int tRows)
	{
		FilePath="";
		Type="P5";
		Comment="";
		MaxGray=255;
		setDimension(tColumns,tRows);
	}
	
	//get functions
	public String getFilePath()
	{
		return(FilePath);
	}
	
	public String getType()
	{
		return(Type);
	}
	
	public String getComment()
	{
		return(Comment);
	}
	
	public int getColumns()
	{
		return(Columns);
	}
	
	public int getRows()
	{
		return(Rows);
	}
	
	public int getMaxGray()
	{
		return(MaxGray);
	}
	
	public int getPixel(int tr,int tc)
	{
		return(tr<0||tr>Rows-1||tc<0||tc>Columns-1?0:Pixels[tr][tc]);
	}
	
	public int[][] getNeighbor(int tr,int tc)
	{
		int neighbor[][]=new int[3][3];
		
		neighbor[0][0]=getPixel(tr-1,tc-1); //NW
		neighbor[0][1]=getPixel(tr-1,tc  ); //N
		neighbor[0][2]=getPixel(tr-1,tc+1); //NE
		neighbor[1][0]=getPixel(tr  ,tc-1); //W
		neighbor[1][1]=getPixel(tr  ,tc  );
		neighbor[1][2]=getPixel(tr  ,tc+1); //E
		neighbor[2][0]=getPixel(tr+1,tc-1); //SW
		neighbor[2][1]=getPixel(tr+1,tc  ); //S
		neighbor[2][2]=getPixel(tr+1,tc+1); //SE
		
		return(neighbor);
	}
	
	public int getNeighbor(int tr,int tc,int direction)
	{
		int pval=0;
		if     (direction==Globals.NW) pval=getPixel(tr-1,tc-1);
		else if(direction==Globals.W ) pval=getPixel(tr  ,tc-1);
		else if(direction==Globals.SW) pval=getPixel(tr+1,tc-1);
		else if(direction==Globals.S ) pval=getPixel(tr+1,tc  );
		else if(direction==Globals.SE) pval=getPixel(tr+1,tc+1);
		else if(direction==Globals.E ) pval=getPixel(tr  ,tc+1);
		else if(direction==Globals.NE) pval=getPixel(tr-1,tc+1);
		else if(direction==Globals.N ) pval=getPixel(tr-1,tc  );
		return(pval);
	}
	
	//set functions
	public void setFilePath(String tFilePath)
	{
		FilePath=tFilePath;
	}
	
	public void setType(String tType)
	{
		Type=tType;
	}
	
	public void setComment(String tComment)
	{
		Comment=tComment;
	}
	
	public void setDimension(int tColumns,int tRows)
	{
		Rows=tRows;
		Columns=tColumns;
		Pixels=new int[Rows][Columns];
	}
	
	public void setMaxGray(int tMaxGray)
	{
		MaxGray=tMaxGray;
	}
	
	public void setPixel(int tr,int tc,int tval)
	{
		if(tr<0||tr>Rows-1||tc<0||tc>Columns-1) return;
		else Pixels[tr][tc]=tval;
	}
	
	//methods
	public void readImage() throws IOException
	{
		InputStream fin = leerYResizear(FilePath);
		this.readImage(fin);
	}
	
	
	public void readImage(InputStream fin)
	{
		try
		{
			int c;
			String tstr;
			
			//read first line of ImageHeader
			tstr="";
			c=fin.read();
			tstr+=(char)c;
			c=fin.read();
			tstr+=(char)c;
			Type=tstr;
			
			//read second line of ImageHeader
			c=fin.read(); //read cr
			c=fin.read(); //read Lf (linefeed)
			c=fin.read(); //read '#'
			tstr="";
			boolean iscomment=false;
			while((char)c=='#') //read comment
			{
				iscomment=true;
				tstr+=(char)c;
				while(c!=10&&c!=13)
				{
					c=fin.read();
					tstr+=(char)c;
				}
				//c=fin.read(); //read cr
				c=fin.read(); //read next '#'
			}
			if(tstr.equals("")==false)
			{
				Comment=tstr.substring(0,tstr.length()-1);
				fin.skip(-1);
			}
			
			//read third line of ImageHeader
			//read columns
			tstr="";
			if(iscomment==true) c=fin.read();
			tstr+=(char)c;
			while(c!=32&&c!=10&&c!=13)
			{
				c=fin.read();
				tstr+=(char)c;
			}
			tstr=tstr.substring(0,tstr.length()-1);
			Columns=Integer.parseInt(tstr);
			
			//read rows
			c=fin.read();
			tstr="";
			tstr+=(char)c;
			while(c!=32&&c!=10&&c!=13)
			{
				c=fin.read();
				tstr+=(char)c;
			}
			tstr=tstr.substring(0,tstr.length()-1);
			Rows=Integer.parseInt(tstr);
			
			//read maxgray
			c=fin.read(); //read cr
			c=fin.read();
			tstr="";
			tstr+=(char)c;
			while(c!=32&&c!=10&&c!=13)
			{
				c=fin.read();
				tstr+=(char)c;
			}
			tstr=tstr.substring(0,tstr.length()-1);
			MaxGray=Integer.parseInt(tstr);
			
			//read pixels from ImageData
			Pixels=new int[Rows][Columns];
			for(int tr=0;tr<Rows;tr++)
			{
				for(int tc=0;tc<Columns;tc++)
				{
					c=(int)fin.read();
					setPixel(tr,tc,c);
				}
			}
			
			fin.close();
		}
		catch(Exception err)
		{
			System.out.println("Error: "+err);
			System.exit(-1);
		}
	}
	
	public void writeImage()
	{
		try
		{
			FileOutputStream fout=new FileOutputStream(FilePath);
			
			//write image header
			//write PGM magic value 'P5'
			String tstr;
			tstr="P5"+"\n";
			fout.write(tstr.getBytes());
			
			//write comment
			Comment=Comment+"\n";
			//fout.write(comment.getBytes());
			
			//write columns
			tstr=Integer.toString(Columns);
			fout.write(tstr.getBytes());
			fout.write(32); //write blank space
			
			//write rows
			tstr=Integer.toString(Rows);
			fout.write(tstr.getBytes());
			fout.write(32); //write blank space
			
			//write maxgray
			tstr=Integer.toString(MaxGray);
			tstr=tstr+"\n";
			fout.write(tstr.getBytes());
			
			for(int r=0;r<Rows;r++)
			{
				for(int c=0;c<Columns;c++)
				{
					fout.write(getPixel(r,c));
				}
			}
			
			fout.close();
		}
		catch(Exception err)
		{
			System.out.println("Error: "+err);
			System.exit(-1);
		}
	}
	
	public void writeImageAs(String tFilePath)
	{
		PGM imgout=new PGM(getColumns(),getRows());
		
		for(int r=0;r<getRows();r++)
		{
			for(int c=0;c<getColumns();c++)
			{
				imgout.setPixel(r,c,getPixel(r,c));
			}
		}
		
		imgout.setFilePath(tFilePath);
		imgout.writeImage();
	}
	
	public BufferedImage getBufferedImage()
	{
		BufferedImage timg=new BufferedImage(Columns,Rows,BufferedImage.TYPE_INT_RGB);
		
		for(int r=0;r<getRows();r++)
		{
			for(int c=0;c<getColumns();c++)
			{
				int tgray=getPixel(r,c);
				int trgb=getRGBValue(tgray,tgray,tgray);
				timg.setRGB(c,r,trgb);
			}
		}
		
		return(timg);
	}
	
	/**
	 * Devuelve una lista de ints correspondientes a la matriz de pixeles
	 */
	public int[] getPixelArray() {
		int array[] = new int[Rows*Columns];
		int contArray = 0;
		int i = 0;
		int j = 0;
		
		while (i<Pixels.length) {
			j = 0;
			while (j<Pixels[i].length) {
				array[contArray] = Pixels[i][j];
				contArray++; 
				j++;
			}
			i++;
		}

		return array;
	}
	
	/**
	 * Devuelve una lista de ints correspondientes a la matriz de pixeles en double
	 */
	public double[] getPixelArrayDouble() {
		double array[] = new double[Rows*Columns];
		int contArray = 0;
		int i = 0;
		int j = 0;
		
		while (i<Pixels.length) {
			j = 0;
			while (j<Pixels[i].length) {
				array[contArray] = Pixels[i][j];
				contArray++; 
				j++;
			}
			i++;
		}

		return array;
	}	

	//static methods
	public static int getRGBValue(int tred,int tgreen,int tblue)
	{
		return((tred<<16)+(tgreen<<8)+tblue);
	}
	
	/**
	 * Devuelve un ByteArrayOutputStream para ser mostrado como jpg
	 */
	public ByteArrayOutputStream pgm2jpegOriginal () throws IOException {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		fis = new FileInputStream(this.FilePath);
		bis = new BufferedInputStream(fis);

		readHeader(bis);
		if (!typeFile.equals("P5")) {
			throw new IOException("I can't know this type.");
		}
		BufferedImage bufimage = new BufferedImage(widthFile, heightFile, BufferedImage.TYPE_3BYTE_BGR);
		int outX, outY;
		int i = -1;
		outX = 0;
		outY = 0;
		while ((i = bis.read()) != -1) {
			bufimage.setRGB(outX, outY, (((i << 8) | i) | (i << 16)));
			outX = outX + 1;
			if (outX == widthFile) {
				outX = 0;
				outY = outY + 1;
			}
		}
		
		ByteArrayOutputStream myJpg = new ByteArrayOutputStream();
		try {
			javax.imageio.ImageIO.write(bufimage, "jpg", myJpg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return myJpg;
	}
	
	private ByteArrayInputStream leerYResizear(String strFilePath) throws IOException
	{
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			fis = new FileInputStream(strFilePath);
			bis = new BufferedInputStream(fis);

			readHeader(bis);
			if (!typeFile.equals("P5")) {
				throw new IOException("I can't know this type.");
			}
			BufferedImage bufimage = new BufferedImage(widthFile, heightFile, BufferedImage.TYPE_3BYTE_BGR);
			int outX, outY;
			int i = -1;
			outX = 0;
			outY = 0;
			while ((i = bis.read()) != -1) {
				bufimage.setRGB(outX, outY, (((i << 8) | i) | (i << 16)));
				outX = outX + 1;
				if (outX == widthFile) {
					outX = 0;
					outY = outY + 1;
				}
			}
			
			int heightNuevo = 32;
            int widthNuevo = 32;

            BufferedImage bufferedImage = new BufferedImage(widthNuevo, heightNuevo, BufferedImage.TYPE_BYTE_GRAY);

			bufferedImage.createGraphics().drawImage(ResizerUtil.resize(bufimage, widthNuevo, heightNuevo), 0, 0, null);

			
			//Paso de bufferedImage a inputStream, asi no hace falta guardar
			PNMEncodeParam param = new PNMEncodeParam();
			param.setRaw(true);

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageEncoder encoder = ImageCodec.createImageEncoder("PNM", os, param);

			encoder.encode(bufferedImage);

			return new ByteArrayInputStream(os.toByteArray());
	}
	
    /**
     * P5
     * 300 300
     * 100
     * @throws IOException 
     */
	private void readHeader(BufferedInputStream bis) throws IOException {
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

        typeFile = new String(arrSb[0]);
        widthFile = Integer.parseInt(new String(arrSb[1]));
        heightFile = Integer.parseInt(new String(arrSb[2]));
    }
}
