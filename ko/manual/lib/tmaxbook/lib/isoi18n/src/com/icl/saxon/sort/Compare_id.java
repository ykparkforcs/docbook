/*
 * Compare_in.java
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

import java.util.Comparator;

import com.isogen.saxoni18n.Saxoni18nService;

/**
 *
 * @author  eliot
 * @version $Revision: 1.2 $
 */
public class Compare_id extends com.icl.saxon.sort.TextComparer {

    static Comparator comparator;

    public Compare_id() {
        super();
        comparator = Saxoni18nService.getComparatorForLanguageCode("id");
    }

    public int compare(java.lang.Object obj, java.lang.Object obj1) {
        /*
        System.err.println("Compare_id: Comparing " +
                           I18nUtil.escapeUnicodeString((String)obj) +
                           " with " +
                           I18nUtil.escapeUnicodeString((String)obj1));
         */

        // System.err.println("Returning: " + comparator.compare(obj, obj1));
        return comparator.compare(obj, obj1);
    }

}
