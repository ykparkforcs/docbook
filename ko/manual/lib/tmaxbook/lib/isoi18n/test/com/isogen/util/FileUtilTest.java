/*
 */
package com.isogen.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FileUtilTest extends TestCase {

	public FileUtilTest(String testName) {
		super(testName);
	}
    public static Test suite() {
        TestSuite suite = new TestSuite(FileUtilTest.class);

        return suite;
    }

    public void testGetAbsolutePath() {
    	String basePath = "C:/foo/bar/testdocs/docbook_comprehensive_test_001";
    	String relativePath = "graphics/graphic_01.svg";
    	String desiredAbsPath = "C:\\foo\\bar\\testdocs\\docbook_comprehensive_test_001\\graphics\\graphic_01.svg";
    	String absPath = FileUtil.getAbsolutePath(basePath, relativePath);
    	// System.err.println("absPath='" + absPath + "'");
    	assertEquals(desiredAbsPath, absPath);    
    	
    	basePath = ".";
    	relativePath = "C:\\Documents and Settings\\Administrator\\banshee\\dist\\logo.gif";
    	desiredAbsPath = "C:\\Documents and Settings\\Administrator\\banshee\\dist\\logo.gif";
    	absPath = FileUtil.getAbsolutePath(basePath, relativePath);
    	assertEquals(desiredAbsPath, absPath);    	
    }
    
    public void testGetRelativePath() {
    	// First test relative paths with same ancestor:
    	
    	String filePath = "C:\\customer\\banshee-dev\\tests\\testdocs\\docbook_comprehensive_test_001\\graphics\\svg\\windmills_01.svg";
    	String targetPath = "C:\\customer\\banshee-dev\\tests\\testdocs\\docbook_comprehensive_test_001\\";
    	String desiredRelativePath = "graphics/svg/windmills_01.svg";
    	
    	String relativePath = FileUtil.getRelativePath(targetPath,filePath);
    	//System.err.println("relativePath='" + relativePath + "'");
    	assertEquals(desiredRelativePath, relativePath);

    	// Paths with no common ancstry:
    	
    	filePath = "d:\\foo\\bar\\tests\\testdocs\\docbook_comprehensive_test_001\\graphics\\svg\\windmills_01.svg";
    	targetPath = "C:\\customer\\banshee-dev\\tests\\testdocs\\docbook_comprehensive_test_001\\";
    	desiredRelativePath = "d:\\foo\\bar\\tests\\testdocs\\docbook_comprehensive_test_001\\graphics\\svg\\windmills_01.svg";
    	
    	relativePath = FileUtil.getRelativePath(targetPath,filePath);
    	//System.err.println("relativePath='" + relativePath + "'");
    	assertEquals(desiredRelativePath, relativePath);
    }
}
