package com.isogen.i18nsupport;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {


    public AllTests(String arg0) {
		super(arg0);
	}
	
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(ComparatorTest.suite());
        suite.addTest(I18nUtilTest.suite());
        suite.addTest(IndexConfigTest.suite());
        suite.addTest(StaticTextDatabaseTest.suite());
        return suite;
    }

}
