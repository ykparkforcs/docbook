/*--------------------------------------------------------------------------------
    Copyright (C) 2003 ISOGEN International
 
    http://www.isogen.com
 
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

 --------------------------------------------------------------------------------*/
package com.isogen.indexhelper;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Enumeration;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.apache.xerces.parsers.DOMParser;

import com.isogen.i18nsupport.*;

/**
 *
 * @version $Revision: 1.3 $
 *
 * Provides services that support the generation of back-of-the-book
 * indexes.
 */
public class IndexHelper {

    Document configDom;
    java.io.File configFile;
    java.util.Hashtable indexConfigs = new java.util.Hashtable();

    Hashtable EngKey2zhCNkeyCache = new java.util.Hashtable();
    /**
     *
     * Construct new indexHelper with configuration.
     *
     * @parm configFilename Filename of index configuration XML document.
     */
    public IndexHelper(String configFilename) throws IndexHelperException {
        configFile = new java.io.File(configFilename);
        loadIndexRuleSetDom(configFilename);
    }

    public IndexHelper() {
        configFile = null;
    }

    /**
     * Loads the specified index configuration file.
     * 
     * @param configFilename Filename of the index configuration file.
     * @throws IndexHelperException
     */
    public void loadIndexRuleSetDoc(String configFilename) 
                                throws IndexHelperException {
	    configFile = new java.io.File(configFilename);
	    if (!configFile.exists()) {
	    	throw new IndexHelperException("loadIndexRuleSetDoc(): Cannot find index configuration file '" + configFile.getAbsolutePath() + "'");
	    }
        loadIndexRuleSetDom(configFile.getAbsolutePath());
    }
    
    /**
     * Loads the DOM for the index configuration doc. Doesn't do anything with
     * the doc at this point--that is done when a request is actually made.
     */
    void loadIndexRuleSetDom(String configFilename) throws IndexHelperException {
        DOMParser dp = new org.apache.xerces.parsers.DOMParser();
        try {
            dp.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dp.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
            dp.parse(configFilename);
        } catch (Throwable e) {
            throw new IndexHelperException("Error constructing DOM from docid '" +
                                            configFilename + ": " +
                                            e.getMessage());
        }
        configDom = dp.getDocument();
        Element rootElem = configDom.getDocumentElement();
        if (rootElem == null) {
           throw new IndexHelperException("Failed to find root element of index configuration document " +
                                          configFilename);
        }
    }

    /**
     * Loads the index configuration information for the specified language. Raises
     * an exception if the language's configuration information can't be found.
     *
     * @param langCode The language code for the language. Must match the value in
     * a <national_language> element of an <index_config> element. Normally this is
     * an ISO locale specification as for xml:lang.
     */

    IndexRuleSet getIndexRuleSet(String langCode) throws IndexHelperException {
        if (indexConfigs.containsKey(langCode)) {
            return (IndexRuleSet)(indexConfigs.get(langCode));
        }

    	// System.err.println("*** getIndexRuleSet(): loading index config for language " + langCode);
        IndexRuleSet ic = loadIndexRuleSet(langCode);
        indexConfigs.put(langCode, ic);
    	// System.err.println("*** getIndexRuleSet(): returning config for language " + langCode);
        return ic;
    }

    IndexRuleSet loadIndexRuleSet(String langCode) throws IndexHelperException {
        Element rootElem = configDom.getDocumentElement();
        if (rootElem == null) {
           throw new IndexHelperException("Failed to find root element of index configuration document ");
        }
        NodeList tempnl = rootElem.getElementsByTagName("index_config");
        if (tempnl.getLength() == 0) {
            throw new IndexHelperException("Failed to find any 'index_config' elements");
        }
        Element indexConfigElement = null;
        for (int i = 0; i < tempnl.getLength(); i++) {
            Element candIndexConfigElem = (Element)(tempnl.item(i));
            NodeList nl = candIndexConfigElem.getElementsByTagName("national_language");
            if (nl.getLength() == 0) {
                throw new IndexHelperException("Missing required national_language element within index_config element");
            }
            String candLangCode = I18nUtil.getElementContent((Element)nl.item(0));
            if (candLangCode.equals("")) {
                System.err.println("WARNING: IndexRuleSet - Null value for <national_language> element in <index_config>");
                continue;
            }
            if (candLangCode.equals(langCode)) {
                indexConfigElement = candIndexConfigElem;
                break;
            }
        }
        if (indexConfigElement == null & langCode.equals("en")) {
            throw new IndexHelperException(" - Failed to find index configuration for language '" + langCode + "'");
        }
        if (indexConfigElement == null & !langCode.equals("en")) {
            System.err.println(" - Failed to find index configuration for language '" + langCode + "', trying English.");
            return this.getIndexRuleSet("en");
        }
        // System.err.println("*** loadIndexRuleSet: loading index rule set for " + langCode);
        IndexRuleSet ic = new IndexRuleSet(this, langCode, indexConfigElement, configFile);

        return ic;
    }

    public Iterator getIndexGroupKeysIterator(String langCode) throws IndexHelperException {
        IndexRuleSet ic = getIndexRuleSet(langCode);
        return ic.getGroupKeysIterator();
    }

    /**
     * Returns the group key for the index group the indexEntry sorts within.
     *
     * @param langCode The language code for the output language.
     * @param indexEntry The index entry for which the group key will be provided.
     */
    public String getGroupKey(String langCode, String indexEntry) throws IndexHelperException {
        if (indexEntry.equals("")) {
            return null;
        }
        IndexRuleSet ic = getIndexRuleSet(langCode);
        String key = ic.getGroupKey(indexEntry);
        if (key.equals("#NUMERIC")) {
            
            	// For non-Latin alphabets latin characters will be grouped in #NUMERIC
            	// so try against the English config. If it's truly numeric it will
            	// still be numeric.
            IndexRuleSet enic = getIndexRuleSet("en");
            // NOTE: This is a hack to handle the special case of Simplified Chinese,
            //       where the English terms need to sort under the appropriate groups.
            //       I think a better solution is to create a custom collator rule set
            //       that integrates the latin characters with the Chinese, but this
            //       is easier to implement quickly.
            key = enic.getGroupKey(indexEntry);
            if (langCode.equals("zh-CN")) {
                String engKey = key;
                if (EngKey2zhCNkeyCache.containsKey(engKey)) {
                    key = (String)(EngKey2zhCNkeyCache.get(engKey));
                } else {
                    Iterator iter = ic.getBaseGroupKeysIterator();
                    while (iter.hasNext()) {
                        String zhKey = (String)(iter.next());
                        String label = this.getGroupLabel(langCode, zhKey);
                        if (engKey.equals(label)) {
                            key = zhKey;
                            EngKey2zhCNkeyCache.put(engKey, label);
                            break;
                        } else {
                            EngKey2zhCNkeyCache.put(label, zhKey); // HACK! Depends on English key being same as label
                        }
                    }
                }                
            } 
        }
        return key;
    }

    /**
     * Given a group key, returns the groups display label string.
     *
     * @param langCode The language code for the index configuration.
     * @param groupKey The sort key (as returned by getGroupKey()) for the group.
     */
    public String getGroupLabel(String langCode, String groupKey) throws IndexHelperException {
        IndexRuleSet ic = getIndexRuleSet(langCode);
        // System.err.println("groupKey='" + groupKey + "'");
        if (groupKey.equals("#NUMERIC")) {
            return groupKey;
        }
        IndexGroup ig = ic.getIndexGroup(groupKey);
        // System.err.println("ig=" + ig);
        return ig.label;
     }

    /**
     * Given a group key, returns the group sort key, that is, the key that the
     * index style sheet uses to sort the groups in presentation order. This is usually
     * the same as the sort key and the group label, but isn't always (e.g., Traditional
     * Chinese).
     *
     * @param langCode The language code for the index configuration.
     * @param groupKey The sort key (as returned by getGroupKey()) for the group.
     */
    public String getGroupSortKey(String langCode, String groupKey) throws IndexHelperException {
        IndexRuleSet ic = getIndexRuleSet(langCode);
        if (groupKey.equals("#NUMERIC")) {
            return groupKey;
        }
        IndexGroup ig = ic.getIndexGroup(groupKey);
        return ig.groupSortKey;
     }

    public String printIndexRuleSet(String langCode, 
                                   boolean includeCollationRules) 
                            throws IndexHelperException {
        IndexRuleSet ic = getIndexRuleSet(langCode);
        return ic.toString(includeCollationRules);
    }
    
    public String printIndexRuleSet(String langCode) 
                            throws IndexHelperException  {
        return printIndexRuleSet(langCode, false);
    }
    
    public String toString() {
         return toString(true);
    }

    public String toString(boolean includeCollationRules) {
         String outStr = "";
         Enumeration  configs = this.indexConfigs.elements();
         while (configs.hasMoreElements()) {
             IndexRuleSet ic = (IndexRuleSet)configs.nextElement();
             outStr = outStr + ic.toString(includeCollationRules);
         }
         return outStr;
    }
}
