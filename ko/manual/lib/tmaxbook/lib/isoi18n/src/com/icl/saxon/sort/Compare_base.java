/**
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
 * This class is the template for all language-specific Saxon comparator
 * implementations. To use this, either subclass from this or just
 * copy this file and set the language code.
 * 
 */
public abstract class Compare_base extends TextComparer {

    static Comparator comparator;

    public Compare_base(String langCode) {
        super();
        comparator = Saxoni18nService.getComparatorForLanguageCode(langCode);
    }

    public int compare(java.lang.Object obj, java.lang.Object obj1) {
        return comparator.compare(obj, obj1);
    }

}
