/*
 * Created on Nov 6, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.isogen.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author avega
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IgnoreFilter implements FilenameFilter {

	public boolean accept(File arg0, String arg1) {
		return !arg1.startsWith(".#") && !arg1.equals("CVS"); 
	}

}
