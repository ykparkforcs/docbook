/*--------------------------------------------------------------------------------
    Copyright (C) 2004 Innodata Isogen
 
    http://www.innodata-isogen.com
 
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
package com.isogen.saxoni18n;

import junit.framework.*;

import com.icl.saxon.expr.XPathException;
import com.icl.saxon.sort.*;
import com.isogen.saxoni18n.Saxoni18nService;

/**
 * Tests the parts of the Saxoni18nService class that can be tested in isolation.
 * 
 * NOTE: To run this test you must specify the com.innodata.i18n.home system property
 * and it must point to the root directory for the project (the directory that contains
 * the config/ directory).
 *
 * @version
 */
public class SaxonI18nTest extends TestCase {

    public SaxonI18nTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SaxonI18nTest.class);

        return suite;
    }

    /**
     * Tests the Saxon Compare_* classes.
     *
     */
    public void testComparators() {
    	Comparer comparer = null;
    	String stringOne = "a"; // String one should always sort before string two
    	String stringTwo = "b";
    	
    	// Arabic:
    	
    	comparer = new Compare_ar();
    	assertEquals(-1, comparer.compare(stringOne, stringTwo));
    }
    
    public void testGetVersionAndRelease() {
    	String vandr = Saxoni18nService.getVersionAndRelease();
        // System.err.println("Version and Release = '" + vandr + "'");
    	assertNotNull(vandr);
    }
    /**
     * Tests the getGeneratedText*() methods that don't require a singleton node set.
     *
     */
    public void testGetGeneratedText() {
    	String key = "toc";
    	String genText = null;
    	if (!Saxoni18nService.getStaticTextDatabaseFileName().equals("#none")) {
			try {
		    	// String staticTextDb = Saxoni18nService.printStaticTextDatabase();
		    	// System.out.println(staticTextDb);
				genText = Saxoni18nService.getGeneratedTextForKeyBefore(key, "en");
			} catch (XPathException e) {
				e.printStackTrace();
				fail("Unexpected exception: " + e.getClass().getName());
			}
			assertEquals("Contents", genText);
	    	
			try {
				genText = Saxoni18nService.getGeneratedTextForKeyBefore(key, "is");
			} catch (XPathException e) {
				e.printStackTrace();
				fail("Unexpected exception: " + e.getClass().getName());
			}
			assertEquals("Efnisyfirlit", genText);
    	
    	} else {
    		System.err.println("com.innodata.i18n.staticTextDatabaseFilename set to '#none', testGetGeneratedText test case skipped.");
    	}
    }
    
    /**
     * Tests simplified Chinese index configuration.
     */
    public void testGetIndexGroupKey() throws XPathException {
    	Saxoni18nService.getIndexGroupKey("zh-CN", "\u5416");
    }
}
