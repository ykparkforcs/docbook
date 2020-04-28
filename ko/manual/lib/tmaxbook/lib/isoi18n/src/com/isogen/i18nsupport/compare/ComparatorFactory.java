/*--------------------------------------------------------------------------------
    Copyright (C) 2002, 2004 ISOGEN International

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


/**
 * Factory class for creating new Comparator objects. Comparators are a generic 
 * abstraction of the Java Collator API that allows different comparator 
 * implementations, such as the ICU4J Collator classes. 
 */
public interface ComparatorFactory {
	
	/**
	 * Constructs a Comparator for the specified locale.
	 * @param locale The Locale for which a Comparator is desired.
	 * @return The Comparator for the Locale.
	 * @throws ComparatorException
	 */
	public Comparator getComparatorForLocale(Locale locale) throws ComparatorException;
	
}
