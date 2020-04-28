package com.isogen.i18nsupport;

import java.util.Comparator;

import com.isogen.indexhelper.IndexHelper;
import com.isogen.indexhelper.IndexHelperException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class IndexConfigTest extends TestCase {

	IndexHelper indexHelper = null;
	
	public IndexConfigTest(String arg0) {
		super(arg0);
	}
    
    public static Test suite() {
        TestSuite suite = new TestSuite(IndexConfigTest.class);
        return suite;
    }
    	
	public void setUp() {
    	String fileNamePropName = "com.innodata.i18nsupport.indexConfigFilename";
    	String configFilename = System.getProperty(fileNamePropName);
    	if (configFilename == null) {
    		fail("System property '" + fileNamePropName + "' not set. " + 
    		     "Needs to be path to the index configuration document.");			
    	}
        // System.err.println("*** Loading index configuration file '" + configFilename + "'");
		indexHelper = new IndexHelper();
		try {
			indexHelper.loadIndexRuleSetDoc(configFilename);
			
		} catch (IndexHelperException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}

	public void testGetGroupSortKey() {
		String langCode = "ZH_TW"; // Traditional Chinese
		
		String groupKey1 = "\u4E00"; 
		String groupKey2 = "\u4E01";
		String groupKey20 = "\u56B7";
		
		try {
			String sortKey1 = indexHelper.getGroupSortKey(langCode, groupKey1);
			assertEquals(groupKey1, sortKey1);
			String sortKey2 = indexHelper.getGroupSortKey(langCode, groupKey2);
			assertEquals(groupKey2, sortKey2);
			String sortKey20 = indexHelper.getGroupSortKey(langCode, groupKey20);
			assertEquals(groupKey20, sortKey20);
			I18nService i18nService = new I18nService();
			Comparator col = i18nService.getComparatorForLanguageCode(langCode);
			assertEquals(-1, col.compare(sortKey2, sortKey20));
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	
    public void testGroupEnglishForZhCn() {
        String langCode = "zh-CN"; // Traditional Chinese
        String zhKeyA = "\u554a"; // Group key for zh "A" group
        
        try {
            String zhGroupKey = indexHelper.getGroupKey(langCode, zhKeyA);
            String enGroupKey = indexHelper.getGroupKey(langCode, "amelia");
            assertEquals(zhGroupKey, enGroupKey);
        } catch (Throwable e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
    }
    
    public void testGroupEnglishForJaJp() {
        String langCode = "JA_JP"; // Japanese
        String enKeyA = "A"; // Group key for English"A" group
        String jaKey1 = "\u3042"; // First Japanese key
        
        try {
            String enGroupKey = indexHelper.getGroupKey(langCode, "amelia");
            assertEquals(enGroupKey, enKeyA);
            String jaGroupKey = indexHelper.getGroupKey(langCode, "\u3044");
            assertEquals(jaGroupKey, jaKey1);
            
            I18nService iserve = new I18nService();
            Comparator jaJpComp = iserve.getComparatorForLanguageCode(langCode);
            assertEquals(jaJpComp.compare("amelia", "\u3044"), -1);
            
        } catch (Throwable e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
    }
    
    public void testZhCnIndexConfig() {
        String langCode = "zh-CN";
        String groupKeyA = "\u963f";
        String groupKeyE = "\u59b8";
        String a1yi2 = "\u963f\u59e8";
        String e1jiao1 = "\u963f\u80f6";
        try {
            String groupKey;
            groupKey = indexHelper.getGroupKey(langCode, "apple");
            assertEquals(groupKeyA, groupKey);
            groupKey = indexHelper.getGroupKey(langCode, a1yi2);
            assertEquals(groupKeyA, groupKey);
            assertEquals(groupKeyE, indexHelper.getGroupKey(langCode, "elvis"));
            // The word e1jiao1 starts with a character (\u963f) that is both the first
            // character in the "A" group and in the "E" group. Therefore the
            // E words that start with \u963f should group under E, not A.
            // Until the collation rules are fixed or we implement a special
            // collator to handle these cases, this test case cannot pass.
            //groupKey = indexHelper.getGroupKey(langCode, e1jiao1);
            //assertEquals(groupKeyE, groupKey);
            
        } catch (IndexHelperException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testZhTwIndexConfig() {
        String langCode = "ZH_TW";
        String groupKey6stroke = "\u4e1e";
        String groupKey7stroke = "\u4e32";
        try {
            String groupKey;
            groupKey = indexHelper.getGroupKey(langCode, "\u4eff");
            assertEquals(groupKey6stroke, groupKey);
            groupKey = indexHelper.getGroupKey(langCode, "\u4f50");
            assertEquals(groupKey7stroke, groupKey);
            
        } catch (IndexHelperException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testThaiIndexConfig() {
		String langCode = "th";
		String groupKey = "";
//		Iterator iter = null;
//        try {
//            iter = indexHelper.getIndexGroupKeysIterator(langCode);
//        } catch (IndexHelperException e1) {
//            fail(e1.getMessage());
//        }
//        while (iter.hasNext()) {
//            groupKey = (String)(iter.next());
//            System.err.println("key=" + I18nUtil.escapeUnicodeString(groupKey));
//        }
		try {
			groupKey = indexHelper.getGroupKey(langCode, "apple");
			assertEquals("A", indexHelper.getGroupLabel(langCode, groupKey));
			groupKey = indexHelper.getGroupKey(langCode, "\u0e15\u0e31\u0e27\u0e2d\u0e22\u0e48\u0e32\u0e07");
			assertEquals("\u0e15", indexHelper.getGroupLabel(langCode, groupKey));
			groupKey = indexHelper.getGroupKey(langCode, "\u0e40\u0e04\u0e23\u0e37\u0e48\u0e2d\u0e07\u0e2b\u0e21\u0e32\u0e22\u0e01\u0e32\u0e23\u0e04\u0e49\u0e32");
			assertEquals("\u0e01", indexHelper.getGroupLabel(langCode, groupKey));
		} catch (IndexHelperException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

    public void testGetgroupKeyMultiCharGroups() {
        String langCode = "hu";
        java.util.Iterator groupKeysIterator = null;
        java.util.Enumeration groupKeys = null;

        String indexEntry_01 = "csaaa";
        String indexEntry_02 = "caaa";
        String indexEntry_03 = "daaa";
        String indexEntry_04 = "dzaa";
        String indexEntry_05 = "dzsaaa";
        String groupKey = null;
        String groupLabel = null;

        try {
            groupKey = indexHelper.getGroupKey(langCode, indexEntry_01);
            assertEquals(I18nUtil.escapeUnicodeString("CS"),
                                I18nUtil.escapeUnicodeString(groupKey));
            groupLabel = indexHelper.getGroupLabel(langCode, groupKey);
            assertEquals(I18nUtil.escapeUnicodeString("CS"),
                                I18nUtil.escapeUnicodeString(groupLabel));

            groupKey = indexHelper.getGroupKey(langCode, indexEntry_02);
            assertEquals(I18nUtil.escapeUnicodeString("C"),
                                I18nUtil.escapeUnicodeString(groupKey));
            groupLabel = indexHelper.getGroupLabel(langCode, groupKey);
            assertEquals(I18nUtil.escapeUnicodeString("C"),
                                I18nUtil.escapeUnicodeString(groupLabel));

            groupKey = indexHelper.getGroupKey(langCode, indexEntry_05);
            assertEquals(I18nUtil.escapeUnicodeString("DZS"),
                                I18nUtil.escapeUnicodeString(groupKey));
            groupLabel = indexHelper.getGroupLabel(langCode, groupKey);
            assertEquals(I18nUtil.escapeUnicodeString("DZS"),
                                I18nUtil.escapeUnicodeString(groupLabel));

        } catch (IndexHelperException e) {
            fail(e.getMessage());
        }

    }

}
