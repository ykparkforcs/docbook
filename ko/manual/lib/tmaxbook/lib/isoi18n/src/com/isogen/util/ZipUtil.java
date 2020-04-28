/*
 * Created on Jan 19, 2004
 */
package com.isogen.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author avega
 */
public class ZipUtil {
	public static void unzipFile(File zippedFile, File destination) throws IOException {
		ZipFile zipFile = new ZipFile(zippedFile);
		Enumeration e = zipFile.entries();
                
		while(e.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry) e.nextElement();
            
			File zipEntryFile = new File(destination, zipEntry.getName());
			zipEntryFile.getParentFile().mkdirs();
			zipEntryFile.createNewFile();
			
            FileOutputStream fos = new FileOutputStream(zipEntryFile);            
			InputStream is = zipFile.getInputStream(zipEntry);
			BufferedInputStream bis = new BufferedInputStream(is);

			byte[] buf = new byte[1024];
			int len = bis.read(buf); 
           	
			while (len >= 0) {
			   fos.write(buf, 0, len);
				len = bis.read(buf);
			}
            
			is.close();
			fos.flush();
			fos.close();
		}
	}
	
	public static File zipDirectory(File directory, String basePath, File zipFile) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fileOutputStream);
        
		traverseDirectory(zos, directory, basePath);
		
		zos.flush();       
		zos.close();
        
		return zipFile;
	}

	private static void traverseDirectory(ZipOutputStream zos, File directory, String basePath) throws IOException {
		File[] content = directory.listFiles();
		for(int i = 0; i < content.length; i++) {
			if(content[i].isDirectory()) {
				traverseDirectory(zos, content[i], basePath);				
			} else if(content[i].isFile()) {
				zipFile(zos, content[i], basePath);
			}
		}
		
	}
	
	private static void zipFile(ZipOutputStream zos, File file, String basePath) throws IOException {
		byte[] buf = new byte[1024];
		int len;
		//Create a new Zip entry with the file's name.
		String absolutePath = file.getCanonicalPath();
		// Strip off the leading drive designation ("c:")
		if (absolutePath.charAt(1) == ':') {
			absolutePath = absolutePath.substring(2);
		}
		String zipEntryName = absolutePath;
            
		ZipEntry zipEntry = new ZipEntry(zipEntryName);
            
		//Create a buffered input stream out of the file
		//we're trying to add into the Zip archive.
		FileInputStream fin = new FileInputStream(file);
		BufferedInputStream in = new BufferedInputStream(fin);
		zos.putNextEntry(zipEntry);
		//Read bytes from the file and write into the Zip archive.
		while ((len = in.read(buf)) >= 0) {
		   zos.write(buf, 0, len);
		}
		//Close the input stream.
		in.close();
		//Close this entry in the Zip stream.
		zos.flush();
		zos.closeEntry();
	}
}
