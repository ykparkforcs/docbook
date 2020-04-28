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

public class ComparatorException extends Exception {

	/**
	 * @param msg Message for this exception instance.
	 * @param e Exception to be chained to this one.
	 */
	public ComparatorException(String msg, Exception e) {
		super(msg, e);
		
	}

}
