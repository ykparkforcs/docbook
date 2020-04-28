/*
 * Created on Sep 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.isogen.i18nsupport.compare;

import java.util.Comparator;
import java.util.Locale;

import com.ibm.icu.text.Collator;
import com.ibm.icu.text.RuleBasedCollator;

/**
 * Constructs ICU collators. These collators are more complete and accurate
 * than the built-in Java collators.
 * 
 * At the moment this factory does not support the use of local collation rules.
 * However, the whole point of using the ICU collators is that you shouldn't 
 * need any local collation rules.
 */
public class IcuComparatorFactoryImpl implements ComparatorFactory {

	public Comparator getComparatorForLocale(Locale locale) throws ComparatorException {
		// TODO: Use comparator/index configuration to get any local collation rules.
		String collationRules = null;
		Collator col = null;
		if (collationRules != null) {
			try {
				col = new RuleBasedCollator(collationRules);
			} catch (Exception e) {
				// e.printStackTrace();
				String msg = this.getClass().getName() + ".getComparatorForLocale(): Exception constructing a RuleBasedCollator";
				throw new ComparatorException(msg, e);
			}
		} else {
			Locale noLocale = new Locale("no");

			if (locale.equals(noLocale)) {
				locale = new Locale("nb");
			} 

			col = Collator.getInstance(locale);			
		}
		return (Comparator)col;
	}

}
