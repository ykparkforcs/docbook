/*
 * Created on Jul 26, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.isogen.indexhelper;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.isogen.i18nsupport.I18nService;
import com.isogen.i18nsupport.I18nUtil;
import com.isogen.i18nsupport.I18nUtilError;
import com.isogen.i18nsupport.MissingLocaleException;
import com.isogen.i18nsupport.compare.ComparatorFactory;

/**
*
* @author  eliot
* @version
*
* Holds the index configuration data for a single national language.
*/
class IndexRuleSet {

   public static final int SORT_METHOD_BETWEEN_KEYS = 0;
   public static final int SORT_METHOD_BY_MEMBERS = 1;
   public static final int SORT_ENGLISH_BEFORE = 0;
   public static final int SORT_ENGLISH_AFTER = 1;
   public static final int SORT_GROUPS_BY_KEYS = 0;
   public static final int SORT_GROUPS_BY_LABELS = 1;


   String langCode = null;     // The language code for the locale(s) this configuration applies to
   Locale locale = null;
   String collationRuleSpec = null;
   
   TreeMap indexGroups; // Index group defs by group key. Keys are sorted in accending collation order
   ArrayList threeCharMembers = new ArrayList(); // List of members that are 3 characters
   ArrayList twoCharMembers = new ArrayList(); // List of members that are 2 characters
   Hashtable members2keys = new Hashtable(); // Reverse index of group keys by member
   Comparator comparator = null;  // Comparator used for this language to sort stuff within the index
   Element configElem = null; // The DOM node that is the index_config element for this config.
   public int sortStrategy = SORT_METHOD_BY_MEMBERS; // Default
   public int sortEnglish = SORT_ENGLISH_BEFORE;       // Default
   public int sortGroups = SORT_GROUPS_BY_KEYS;        // Default
   IndexHelper indexHelper = null;

   public IndexRuleSet(IndexHelper inIndexHelper,
                       String inLangCode,
                       Element indexConfigElement,
                       java.io.File configFile)
                               throws IndexHelperException {
       // NOTE: configFile is intended to allow us to find resources relative to the
       //       the configuration file, but that's not implemented yet.
   	
       indexHelper = inIndexHelper;
       configElem = indexConfigElement;
       langCode = inLangCode;
       try {
           locale = I18nUtil.getLocaleFromLangCode(langCode);
       } catch (MissingLocaleException e) {
       	   String lang = "";
       	   String country = "";
           if (inLangCode.indexOf("-") > 0) {
           	  lang = inLangCode.substring(0,2);
           	  country = inLangCode.substring(3);
           } else {
           	  lang = inLangCode;
           }
       	// System.err.println("*** No built-in locale for langCode '" + langCode + "', trying " + lang + ", " + country);
           locale = new Locale(lang, country);
       }
       Element colSpecElem = null;
       try {
           colSpecElem = I18nUtil.getElement(configElem, "collation_spec");
           if (I18nUtil.hasElementChildren(colSpecElem)) {
               Element collatorSpecElem = I18nUtil.getElement(colSpecElem, "java_collation_spec");
               NodeList nl = colSpecElem.getElementsByTagName("include_collation_spec");
               if (nl.getLength() == 0) {
                   collationRuleSpec = I18nUtil.getElementContent(collatorSpecElem);
               } else {
                   // FIXME: For now we're just processing the first rule spec file we get.
                   Element ecsElem = (Element)nl.item(0);
                   String collationRuleFileName = I18nUtil.getElementContent(ecsElem);
                   File collationRuleFile = new File(configFile.getParentFile(), collationRuleFileName);
						
                   if (!collationRuleFile.exists()) {
                   	System.err.println("*** Cannot find comparator rule spec file " + collationRuleFile.getAbsolutePath());
                   	
                   }
                   String baseRules = ""; // WEK: For now just requiring that the rules be complete.
                   // String baseRules = ((RuleBasedCollator)java.text.Collator.getInstance()).getRules();
                   String addedRules = I18nUtil.readUnicodeFile(collationRuleFile, "UTF8");
                   //System.err.println("*** addedRules='" + addedRules + "'");
                   collationRuleSpec = baseRules + addedRules;
               }

           }
       } catch (Throwable e) {
           throw new IndexHelperException(e.getMessage());
		}

       // System.err.println("collationRuleSpec=" + collationRuleSpec);
       // See if there are any custom sort rules for this language code:

   		ComparatorFactory compFact;
		try {
			compFact = I18nService.constructComparatorFactory();
			comparator = compFact.getComparatorForLocale(locale);
		} catch (Throwable e) {
			throw new IndexHelperException(e);
		}

       try {
           Element sbkElem = I18nUtil.getElement(configElem, "sort_between_keys");
           this.sortStrategy = IndexRuleSet.SORT_METHOD_BETWEEN_KEYS;
       } catch (I18nUtilError e) {
           // Nothing to do
       }

       try {
           Element sgbkElem = I18nUtil.getElement(configElem, "sort_by_members");
           this.sortGroups = IndexRuleSet.SORT_GROUPS_BY_LABELS;
       } catch (I18nUtilError e) {
           // Nothing to do
       }


       indexGroups = new TreeMap(comparator);

       Element igsElem = null;
       try {
           igsElem = I18nUtil.getElement(indexConfigElement, "group_definitions");
       } catch (I18nUtilError e) {
           throw new IndexHelperException(e.getMessage());
       }
       if (igsElem == null) {
           throw new IndexHelperException("No group_definitions element within index_config element");
       }
       NodeList nl = igsElem.getElementsByTagName("term_group");
       if (nl.getLength() == 0) {
           throw new IndexHelperException("No term_group elements within group_definitions element");
       }
       String lastKey = "\u0002";
       // System.err.println("found " + nl.getLength() + " term_group elements");
       for (int i = 0; i < nl.getLength(); i++) {
           Element igElem = (Element)nl.item(i);
           IndexGroup ig = new IndexGroup(this, igElem);
           
           //System.err.println("key=" +
           //        I18nUtil.escapeUnicodeString(ig.key));
           if (comparator.compare(ig.key, lastKey) < 0) {
               throw new IndexHelperException("Key '" + I18nUtil.escapeUnicodeString(ig.key) +
                                              "' sorts below previous key '" +
                                              I18nUtil.escapeUnicodeString(lastKey) + "' for language " + langCode);
           }
           lastKey = ig.key;
           indexGroups.put(ig.key, ig);
           // System.err.println("indexGroups.size()=" + indexGroups.size());
           for (int j = 0; j < ig.members.size(); j++) {
               String mem = (String)ig.members.get(j);
               if (mem.length() == 3) {
                   threeCharMembers.add(mem);
               } else if (mem.length() == 2) {
                   twoCharMembers.add(mem);
               }
               members2keys.put(mem, ig.key);
           }
       }
       // System.err.println(this.toString());
       // Now do some validation on the index groups to make sure everything is cool.
       IndexGroup lastGroup = (IndexGroup)(this.indexGroups.get(this.indexGroups.lastKey()));
       if (this.sortStrategy == IndexRuleSet.SORT_METHOD_BETWEEN_KEYS) {
           if (lastGroup.lastMember == null) {
               throw new IndexHelperException("Grouping method is 'between keys' for language '" +
                                              this.langCode + "' but last <group_members> does not specify " +
                                              "a final member character.");
           }
       }
   }

   public String getLanguage() {
       return langCode;
   }

   public Locale getLocale() {
       return locale;
   }

   public Iterator getGroupKeysIterator() throws IndexHelperException {
       // Need to get English keySet iterator and append or prepend it to
       // the main language iterator.
       ArrayList al = new ArrayList(indexGroups.keySet());
       // Get the second key list
       IndexRuleSet engConfig = this.indexHelper.getIndexRuleSet("en");
       if (this.sortEnglish == IndexRuleSet.SORT_ENGLISH_AFTER) {
           al.addAll(al.size(),engConfig.indexGroups.keySet());
       } else {
           al.addAll(0,engConfig.indexGroups.keySet());
       }
       return al.iterator();
   }

   /**
    * Gets the group keys iterator for just the base language, not with English added.
    * @return
    * @throws IndexHelperException
    */
   public Iterator getBaseGroupKeysIterator() throws IndexHelperException {
       ArrayList al = new ArrayList(indexGroups.keySet());
       // Get the second key list
       return al.iterator();
   }

   public String getGroupKey(String indexEntry)  throws IndexHelperException {
       if (indexEntry.equals("")) {
           return null;
       }
       if (this.sortStrategy == IndexRuleSet.SORT_METHOD_BETWEEN_KEYS) {
           return this.getSortKeyByKeys(indexEntry);
       }
       if (this.sortStrategy == IndexRuleSet.SORT_METHOD_BY_MEMBERS) {
           return this.getSortKeyByMembers(indexEntry);
       }
       throw new IndexHelperException("Unrecognized sort method value '" +
                                       String.valueOf(this.sortStrategy) + "'");
   }

   /**
    * Returns the term group sort key for the specified index entry string,
    * using the explicitly-defined group membership.
    *
    * Return "#NUMERIC" if the entry does not map to an index key in the configuration
    * (i.e., the string is a numeric or other special character).
    *
    * @param indexEntry the index entry string for which a sort key is requested.
    */
   String getSortKeyByMembers(String indexEntry) {
       // Check for 3- and 2-character sequences then lookup the sort key
       String candMember = null;
       if (indexEntry.length() > 2) {
           if (threeCharMembers.contains(indexEntry.substring(0,3))) {
               candMember = indexEntry.substring(0,3);
           }
       }
       if (candMember == null & (indexEntry.length() > 1)) {
           if (twoCharMembers.contains(indexEntry.substring(0,2))) {
               candMember = indexEntry.substring(0,2);
           }
       }
       if (candMember == null) {
           candMember = indexEntry.substring(0,1);
       }
       if (members2keys.containsKey(candMember)) {
           return (String)members2keys.get(candMember);
       }
       return "#NUMERIC";
   }


   /**
    * Returns the index group sort key for the specified index entry string,
    * using the group keys as the sort boundaries.
    *
    * Return "#NUMERIC" if the entry does not map to an index key in the configuration
    * (i.e., the string is a numeric or other special character).
    *
    * @param indexEntry the index entry string for which a sort key is requested.
    */
   String getSortKeyByKeys(String indexEntry) throws IndexHelperException {
       // Iterate over the indexGroups key list and compare
	   Object[] keys = this.indexGroups.keySet().toArray();
       /*
        * WEK: commenting this out as it prevents us from sorting English 
        * terms with non-English terms for zh-CN. 
        
	   if (this.comparator.compare(indexEntry,(String)keys[0]) < 0) {
	       IndexRuleSet engConfig = this.indexHelper.getIndexRuleSet("en");
	       return engConfig.getGroupKey(indexEntry);
	   }
       */
	   //String c = indexEntry.substring(0,1);
	   //System.err.println("Index entry=" + I18nUtil.escapeUnicodeString(indexEntry));
	   for (int i = 0; i < (keys.length); i++) {
	       String secondKey = null;
	       if (i == (keys.length - 1)) {
	           secondKey = ((IndexGroup)this.indexGroups.get(this.indexGroups.lastKey())).lastMember;
	
	       } else {
	           secondKey = (String)keys[i+1];
	       }
	       if ((this.comparator.compare(indexEntry,(String)keys[i]) >= 0) &&
	           (this.comparator.compare(indexEntry, secondKey) < 0)) {
	           return (String)keys[i];
	       }
	   }
	   return "#NUMERIC";
   }

   public IndexGroup getIndexGroup(String groupKey) throws IndexHelperException {
       if (indexGroups.containsKey(groupKey)) {
           return (IndexGroup)indexGroups.get(groupKey);
       }
       if (!this.langCode.equals("en")) {
           IndexRuleSet engConfig = this.indexHelper.getIndexRuleSet("en");
           return engConfig.getIndexGroup(groupKey);
       }
       throw new IndexHelperException("IndexHelper.getIndexGroup(): No group with key '" +
                                      I18nUtil.escapeUnicodeString(groupKey) +
                                      "' for language '" + langCode + "'");
   }

   public String getGroupSortKey(String groupKey)  throws IndexHelperException {
       if (indexGroups.containsKey(groupKey)) {
           return ((IndexGroup)indexGroups.get(groupKey)).groupSortKey;
       }
       throw new IndexHelperException("IndexHelper.getGroupSortKey(): No group with key '" +
                                      I18nUtil.escapeUnicodeString(groupKey) +
                                      "' for language '" + langCode + "'");
   }

   /**
    * Returns the Collator object for this index configuration.
    *
    */
   public Comparator getComparator() {
       return comparator;
   }

   /**
    * Produces a string representation of the index configuration rules.
    * @param includeCollationRules
    * @return
    */
   public String toString(boolean includeCollationRules) {
       String outStr = "\nIndex Configuration\n";
       outStr = outStr + "\n\tLanguage: " + this.langCode;
       // FIXME: Get the description
       if (includeCollationRules) {
           if (collationRuleSpec != null) {
               outStr = outStr + "\n\tCollation spec: " + this.collationRuleSpec;
           } else {
               outStr = outStr + "\n\tCollation spec: Use Java rules";
           }
       } else {
           outStr = outStr + "\n\tCollation spec: {not shown}";
       }
       outStr = outStr + "\n\tIndex Groups:";
       Iterator gi = this.indexGroups.values().iterator();
       while (gi.hasNext()) {
           outStr = outStr + ((IndexGroup)gi.next()).toString();
       }
       return outStr + "\n";
   }

   public String toString() {
       return toString(true);
   }

   void findAlphaStarts(Collator col) {
       // This is a one-time-use thing but it seemed useful to keep the code around in
       // case we need it again at some point.
       // Given the Unicode-to-pinyin mapping from the Unihan database, figures out
       // which character actually starts each letter group.

       String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
                            "J", "K", "L", "M", "N", "O", "P", "Q", "R",
                            "S", "T", "U", "V", "W", "X", "Y", "Z" };

       try {
           String uhPY = I18nUtil.readUnicodeFile("mandarin.txt", "UTF8");
           java.io.Reader sr = new java.io.StringReader(uhPY);
           java.io.LineNumberReader lnr = new java.io.LineNumberReader(sr);
           String inline = null;
           inline = "";
           int i = 0; // Index into letters[]
           String candChar = "\uFFF0";  // Candidate for start of a letter group
           String alphaCode = "";
           alphaCode = letters[0];
           while (inline != null) {
               try {
                   inline = lnr.readLine();
               } catch (java.io.IOException e) {
                   System.err.println(alphaCode + "=" +
                                   I18nUtil.escapeUnicodeString(candChar));
                   break;
               }
               if (inline == null) {
                   System.err.println(alphaCode + "=" +
                                   I18nUtil.escapeUnicodeString(candChar));
                   break;
               }
               int p = inline.indexOf("  ");
               if (p <= 0) {
                   continue;
               }
               //System.err.println("inline='" + inline + "'");
               String charCode = inline.substring(0,p);
               String pinyin = inline.substring(p+2);
               String firstChar = pinyin.substring(0,1);
               if (firstChar.compareTo(alphaCode) > 0) {
                   // We've scanned to the next letter group, go to the
                   // next entry in letters[]
                   i++;
                   System.err.println(alphaCode + "=" +
                                   I18nUtil.escapeUnicodeString(candChar));
                   alphaCode = letters[i];
                   candChar = "\uFFF0";
               }
               //
               if (col.compare(candChar, charCode) > 0) {
                   candChar = charCode;
               }
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
