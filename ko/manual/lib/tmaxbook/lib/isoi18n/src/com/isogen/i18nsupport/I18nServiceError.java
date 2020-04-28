/*--------------------------------------------------------------------------------
    Copyright (C) 2002 ISOGEN International

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
package com.isogen.i18nsupport;

/**
 *
 * @version $Revision: 1.1.1.1 $
 */
public class I18nServiceError extends java.lang.Exception {

    public I18nServiceError(String msg) {
        super(msg);
    }

	public I18nServiceError(Throwable e) {
		super(e);
	}
}


