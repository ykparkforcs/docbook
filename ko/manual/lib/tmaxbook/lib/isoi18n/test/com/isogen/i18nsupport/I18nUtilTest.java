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

import junit.framework.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

/**
 *
 * @version
 */
public class I18nUtilTest extends TestCase {

    public I18nUtilTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(I18nUtilTest.class);

        return suite;
    }

    public void testGetLocaleForLangCode() {
        Locale locale_zh_tw = new Locale("zh", "TW");
        Locale locale_en_ww = new Locale("en", "");
        Locale locale_en_us = new Locale("en", "US");
        Locale locale_he_ww = new Locale("he", "");
        Locale locale_jp_ww = new Locale("ja", "JP");
        boolean gotException = false;
        try {
            Locale candLocale = I18nUtil.getLocaleFromLangCode("yy_XX");
        } catch (Exception e) {
            // System.err.println(e.getMessage());
            gotException = true;
        }
        assertTrue("Failed to get exception from bogus language code yy_XX", gotException);
        try {
            Locale candLocale = I18nUtil.getLocaleFromLangCode("zh-TW");
            assertEquals(locale_zh_tw.getLanguage(), candLocale.getLanguage());
            assertEquals(locale_zh_tw.getCountry(), candLocale.getCountry());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        try {
            Locale candLocale = I18nUtil.getLocaleFromLangCode("en");
            assertEquals(locale_en_ww.getLanguage(), candLocale.getLanguage());
            assertEquals(locale_en_ww.getCountry(), candLocale.getCountry());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        try {
            Locale candLocale = I18nUtil.getLocaleFromLangCode("en-US");
            assertEquals(locale_en_us.getLanguage(), candLocale.getLanguage());
            assertEquals(locale_en_us.getCountry(), candLocale.getCountry());
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            Locale candLocale = I18nUtil.getLocaleFromLangCode("he");
            assertEquals(locale_he_ww.getLanguage(), candLocale.getLanguage());
            assertEquals(locale_he_ww.getCountry(), candLocale.getCountry());
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            Locale candLocale = I18nUtil.getLocaleFromLangCode("ja-JP");
            assertEquals(locale_jp_ww.getLanguage(), candLocale.getLanguage());
            assertEquals(locale_jp_ww.getCountry(), candLocale.getCountry());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testGetCollatorForLangCode() {
        Locale locale_zh_tw = new Locale("zh", "TW");
        Locale locale_en_ww = new Locale("en", "");
        Locale locale_en_us = new Locale("en", "US");
        Locale locale_he_ww = new Locale("he", "");
        Locale locale_jp_ww = new Locale("ja", "JP");
    }    
    
    public void testWriteUnicodeFile() {
    	File outFile = null;
    	String testStr = "\u0e40";
    	FileInputStream fis = null;
    	try {
        	// First, writing as UTF-16:
    		outFile = File.createTempFile("testWriteUnicodeFile", ".txt");
			I18nUtil.writeUnicodeFile(testStr, outFile.getAbsolutePath(), "UTF-16");
			fis = new FileInputStream(outFile);
			byte[] b = new byte[10];
			fis.read(b);
			assertEquals("5th byte is not null", 0x00, b[4]);
			assertEquals("fe", I18nUtil.byteToHex(b[0]));
			assertEquals("ff", I18nUtil.byteToHex(b[1]));
			assertEquals("0e", I18nUtil.byteToHex(b[2]));
			assertEquals("40", I18nUtil.byteToHex(b[3]));
			fis.close();
			outFile.delete();

        	// First, now as UTF-8:
			I18nUtil.writeUnicodeFile(testStr, outFile.getAbsolutePath(), "UTF-8");
			fis = new FileInputStream(outFile);
			b = new byte[10];
			fis.read(b);
			assertEquals("4th byte is not null", 0x00, b[3]);
			assertEquals("e0", I18nUtil.byteToHex(b[0]));
			assertEquals("b9", I18nUtil.byteToHex(b[1]));
			assertEquals("80", I18nUtil.byteToHex(b[2]));
			fis.close();
    	} catch (IOException e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ": " + e.getMessage());
		} catch (I18nUtilError e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			outFile.deleteOnExit();
		}
		
    	
    }
}
