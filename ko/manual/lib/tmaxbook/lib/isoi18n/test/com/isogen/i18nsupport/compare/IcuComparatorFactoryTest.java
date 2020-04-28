/*--------------------------------------------------------------------------------
Copyright (C) 2004 ISOGEN International

http://www.isogen.com

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

--------------------------------------------------------------------------------*/
package com.isogen.i18nsupport.compare;

import java.util.Comparator;
import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class IcuComparatorFactoryTest extends TestCase {
	
    public IcuComparatorFactoryTest(java.lang.String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(IcuComparatorFactoryTest.class);
        return suite;
    }


	public void testConstructComparatorForLocale() {
		IcuComparatorFactoryImpl compFact = new IcuComparatorFactoryImpl();
		Locale locale = null;
		
		// English collation:
		locale = new Locale("en", "");
		Comparator comp = null;
		try {
			comp = compFact.getComparatorForLocale(locale);
		} catch (ComparatorException e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
		assertNotNull(comp);
		assertEquals(1, comp.compare("b", "a"));
		
		// Thai collation:
		//
		// In Thai some vowels have to be reordered
		// before collation.
		locale = new Locale("th", "");
		try {
			comp = compFact.getComparatorForLocale(locale);
		} catch (ComparatorException e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
		assertNotNull(comp);
		assertEquals(0, comp.compare("\u0e40\u0e01", "\u0e01\u0e40"));
		
		
		
	}

	
	public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
	}

}
