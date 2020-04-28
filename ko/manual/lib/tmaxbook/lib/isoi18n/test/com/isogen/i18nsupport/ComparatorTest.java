package com.isogen.i18nsupport;

import java.util.Comparator;
import java.util.Locale;

import com.isogen.i18nsupport.compare.ComparatorFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ComparatorTest extends TestCase {

    ComparatorFactory compFact;

    public ComparatorTest(String arg0) {
		super(arg0);
	}
    public static Test suite() {
        TestSuite suite = new TestSuite(ComparatorTest.class);
        return suite;
    }
	
	public void setUp() {
		try {
			compFact = I18nService.constructComparatorFactory();
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	
	public void testThaiComparator() {
		Locale locale = new Locale("th", "");
		Comparator comparator = null;
		try {
			comparator = compFact.getComparatorForLocale(locale);
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		//RuleBasedCollator col = (RuleBasedCollator)comparator;
		// String rules = col.getRules();
		// System.err.println(I18nUtil.escapeUnicodeString(rules));
		assertEquals(-1, comparator.compare("\u0e2d", "\u0e2e"));
		assertEquals(-1, comparator.compare("\u0e2f", "\u0e45"));
        // This assert fails: not sure what the correct behavior should be. WEK
		// assertEquals(-1, comparator.compare("\u0e2e", "\u0e2f"));
	}
    
    /**
     * Tests the behavior of the Simplified Chinese (zh-CN) comparator.
     * 
     * This test is based on the Zhongda Chinese-English Dictionary,
     * ISBN 962-996-093-1, published 1999 by The Chinese University Press.
     * www.chineseupress.com
     *
     */
    public void testZhCnComparator() {
        
        String da1la = "\u8037\u62c9";
        String da1li = "\u7b54\u7406";
        String da4   = "\u5927";
        Locale locale = new Locale("zh", "CN");
        Comparator comparator = null;
        try {
            comparator = compFact.getComparatorForLocale(locale);
        } catch (Throwable e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertEquals(-1, comparator.compare(da1la, da1li));
        assertEquals(-1, comparator.compare(da1la, da4));
        assertEquals(-1, comparator.compare(da1li, da4));
        
    }
	
	public static void main(String[] args) {
	}

}
