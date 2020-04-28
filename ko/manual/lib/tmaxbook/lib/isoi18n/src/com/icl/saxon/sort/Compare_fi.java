/*
 * Compare_fi.java
 *
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
 */

package com.icl.saxon.sort;
import com.isogen.saxoni18n.Saxoni18nService;

import java.util.Comparator;

/**
 *
 * @author  eliot
 * @version $Revision: 1.2 $
 */
public class Compare_fi extends com.icl.saxon.sort.TextComparer {

    static Comparator comparator;

    public Compare_fi() {
        super();
        //System.err.println("Constructing new Compare_fi object");
        comparator = Saxoni18nService.getComparatorForLanguageCode("fi");
    }

    public int compare(java.lang.Object obj, java.lang.Object obj1) {
        /*
        System.err.println("Compare_fi: Comparing " +
                           I18nUtil.escapeUnicodeString((String)obj) +
                           " with " +
                           I18nUtil.escapeUnicodeString((String)obj1));
         */

        // System.err.println("Returning: " + comparator.compare(obj, obj1));
        return comparator.compare(obj, obj1);
    }

}
