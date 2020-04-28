/*--------------------------------------------------------------------------------
    Copyright (C) 2002 ISOGEN International
 
    http://www.isogen.com
 
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

 --------------------------------------------------------------------------------*/
package com.isogen.i18nsupport;

import java.io.File;
import java.net.MalformedURLException;

import junit.framework.*;

/**
 *
 * @version
 */
public class StaticTextDatabaseTest extends TestCase {
    
    StaticTextDatabase textDb = null;

    public StaticTextDatabaseTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(StaticTextDatabaseTest.class);

        return suite;
    }

    public void setUp() {
        textDb = new StaticTextDatabase();
        
    }

    public void testLoadStaticTextDatabase() {
    	String fileNamePropName = "com.innodata.i18nsupport.staticTextDatabaseFilename";
    	String staticTextDbFilename = System.getProperty(fileNamePropName);
    	if (staticTextDbFilename == null) {
    		fail("System property '" + fileNamePropName + "' not set. " + 
    		     "Needs to be path to static text database document.");			
    	}
    	File docFile = new File(staticTextDbFilename);
    	java.net.URL docUrl = null;
        try {
			docUrl = docFile.toURL();
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}
        try {
            textDb.loadStaticTextDatabase(docUrl);
            // NOTE: uncomment the next two lines to enable visual inspection
            //       of the translation table report.
            // String tableReport = textDb.printStaticTextDatabase();
            // System.out.print(I18nUtil.escapeUnicodeString(tableReport));
        } catch (I18nServiceError exc) {
            exc.printStackTrace();
            fail("Unexpected exception from loadStaticTextDatabase");
        }
    }

 }
