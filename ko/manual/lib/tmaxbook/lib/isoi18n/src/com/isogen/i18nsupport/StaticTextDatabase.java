/*
     http://www.innodata-isogen.com

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

    $Revision: 1.2 $
    
 */

 
package com.isogen.i18nsupport;

import java.net.URL;
import java.util.Hashtable;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.apache.xerces.parsers.DOMParser;


/**
 * Manages loading static text datatabase documents.
 * 
 * Exposes four hash tables that are then used by other classes
 * to do lookup of translated text strings.
 * 
 * This design reflects an initial quick refactor to pull all the
 * text database loading code out of the I18nService class to try 
 * to make things a little cleaner. However, there is still more
 * refactoring that could be done to improve the code.
 * 
 */
public class StaticTextDatabase {

    /* WEK: these are public to make the initial object-creation
     *      refactor easier. Should probably do another refactor
     *      to turn these into accessor methods.
     */
    public Hashtable textBefore = new java.util.Hashtable();
    public Hashtable textAfter = new java.util.Hashtable();
    public Hashtable attributeMap = new java.util.Hashtable();
    public static String[] availableLanguages = new String[100];
    public Hashtable langCodes = new java.util.Hashtable();


    String buildTranstableKey(String prefix, String langCode) {
        return prefix + "^" + langCode;
    }

    String getDefaultKey(String prefix) {
        return prefix + "^#DEFAULT";
    }

    String getDatabaseKey(String prefix, Element elemNode) throws I18nServiceError {
        String lang = I18nUtil.getElementLanguage(elemNode, "en");
        return buildTranstableKey(prefix, lang);
    }

    /**
    * Returns the text content of the specified element. Does not recurse
    * into subelements.
    *
    * @param elem The element whose content is to be returned.
    */
   String getElementText(Element elem) throws NullLanguageMappingError {
       String resultStr = I18nUtil.getElementContent(elem);
       if (resultStr.equals("")) {
         throw new NullLanguageMappingError("Element Type=" + elem.getTagName());
       }
       return resultStr;

   }

   /**
	* Returns true if the specified key has an entry in the database.
	*/
   public boolean hasKey(String inKey, String langCode)
											  throws I18nServiceError {
	   String key = buildTranstableKey(inKey,
	   								   langCode);
	   //System.err.println("key='" + key + "'");                                        
	   if (textBefore.containsKey(key)) {
		   return true;
	   } 
	   key = key + "^#DEFAULT";
	   return textBefore.containsKey(key);
   }

   void processTextBeforeOrAfter(String tagname,
                                 Element contextElem,
                                 String beforeOrAfter,
                                 java.util.Hashtable textMap,
                                 java.util.Hashtable langCodes)
                                             throws I18nServiceError {
       NodeList tempnl;
       tempnl = contextElem.getElementsByTagName(beforeOrAfter);
       if (tempnl.getLength() == 0) {
           throw new I18nServiceError("Failed to find " + beforeOrAfter + " element within " + contextElem.getNodeName());
       }
       Element beforeOrAfterElem = (Element)(tempnl.item(0));
       tempnl = beforeOrAfterElem.getElementsByTagName("default_item");
       if (tempnl.getLength() == 1) {
           // FIXME: Should use key building function
           String key = tagname + "^#DEFAULT";
           String text = "";
           try {
               text = getElementText((Element)(tempnl.item(0)));
           } catch (NullLanguageMappingError exc) {
           }
           textMap.put(key, text);
       }
       NodeList items = beforeOrAfterElem.getElementsByTagName("item");
       if (items.getLength() > 0) {
           Element itemElem;
           for (int i = 0; i < items.getLength(); i++) {
               itemElem = (Element)(items.item(i));
               String lang = itemElem.getAttribute("xml:lang");
               if (lang.equals("")) {
                   String text = "";
                   try {
                       text = getElementText((Element)(itemElem));
                   } catch (NullLanguageMappingError exc) {
                   }
                   throw new I18nServiceError("processTextBeforeOrAfter: no lang= value for node with text '" +
                                              text +
                                               "'");
               }
               langCodes.put(lang, lang); // Might have some way to get description for code.

               String key = buildTranstableKey(tagname, lang);
               Element firstChild = I18nUtil.getFirstElementChild(itemElem);
               String text = "";
               try {
                   text = getElementText((Element)(itemElem));
               } catch (NullLanguageMappingError exc) {
               }
               // System.out.println("key='" + key + "', text='" + text + "'");
               textMap.put(key, text);
           }
       }
   }

   void processCommonContexts(Element commonContextElem,
                                     java.util.Hashtable langCodes)
                                               throws I18nServiceError {
       NodeList contexts = commonContextElem.getElementsByTagName("context");
       NodeList tempnl;
       for (int i = 0; i < contexts.getLength(); i++) {
           Element contextElem = (Element)(contexts.item(i));
           Node tempnd = contexts.item(i);
           tempnl = ((Element)tempnd).getElementsByTagName("lookup_key");
           if ( tempnl.getLength() == 0) {
               throw new I18nServiceError("Failed to find lookup_key element within common.context");
           }
           String lookupKey = "";
           try {
               lookupKey = getElementText((Element)(tempnl.item(0)));
           } catch (NullLanguageMappingError exc) {
               throw new I18nServiceError("Failed to get a lookup_key value from database");
           }
           processTextBeforeOrAfter(lookupKey,
                                    contextElem,
                                    "text_before",
                                    textBefore,
                                    langCodes);
           processTextBeforeOrAfter(lookupKey,
                                    contextElem,
                                    "text_after",
                                    textAfter,
                                    langCodes);
       }
   }

   void processAttributeMapping(String tagname,
                                Element attMapElem,
                                Hashtable langCodes)
                                                   throws I18nServiceError {
     // Attribute value keys are: {tagname}@{attname}!{attvalue}^{rest of key}

     NodeList tempnl;
     tempnl = attMapElem.getElementsByTagName("attr_name");
     if (tempnl.getLength() == 0) {
        throw new I18nServiceError("Failed to find <attr_name> element within <attr_map>");
     }
     String attname = "";
           try {
               attname = getElementText((Element)(tempnl.item(0)));
           } catch (NullLanguageMappingError exc) {
               throw new I18nServiceError("Failed to get an attribute name value from table");
           }
     NodeList attValues = attMapElem.getElementsByTagName("attr_values");
     if (attValues.getLength() == 0) {
        throw new I18nServiceError("Failed to find <attr_values> element within <attr_map>");
     }
     Element attValuesElem = (Element)(attValues.item(0));
     String keyPrefix = tagname + "@" + attname;
     NodeList attValueMaps;
     attValueMaps = attValuesElem.getElementsByTagName("attr_value_map");
     // For each attr_value_map, create a table entry for that value's items.
           String key;
     for (int i = 0; i < attValueMaps.getLength(); i++) {
               Element attValueMapElem = (Element)(attValueMaps.item(i));
               tempnl = attValueMapElem.getElementsByTagName("attr_value");
               String attval = "";
               try {
                   attval = getElementText((Element)(tempnl.item(0)));
               } catch (NullLanguageMappingError exc) {
                   throw new I18nServiceError("<attr_value> element had no content.");
               }
               String attKeyPrefix = keyPrefix + "!" + attval;
               tempnl = attValueMapElem.getElementsByTagName("default_item");
               if (tempnl.getLength() == 1) {
                  key = attKeyPrefix + "^#DEFAULT";
                  attributeMap.put(key, I18nUtil.getElementContent((Element)(tempnl.item(0))));
               }
               NodeList items = attValueMapElem.getElementsByTagName("item");
               if (items.getLength() > 0) {
                   Element itemElem;
                   for (int j = 0; j < items.getLength(); j++) {
                       itemElem = (Element)(items.item(j));
                       String lang = itemElem.getAttribute("lang");
                       langCodes.put(lang, lang);

                       key = getDatabaseKey(attKeyPrefix, itemElem);
                       String text = I18nUtil.getElementContent(itemElem);
                       //javax.swing.JOptionPane.showMessageDialog(null, "key='" + key + "', text='" + text + "'");
                       attributeMap.put(key, text);
                   }
               }
     }
   }

   void processCommonAttributeMappings(Element commonAttMapsElem,
                                       java.util.Hashtable langCodes)
                                                 throws I18nServiceError {
       NodeList attMappings;
       attMappings = commonAttMapsElem.getElementsByTagName("attr_map");
       for (int i = 0; i < attMappings.getLength(); i++) {
           processAttributeMapping("#COMMON", (Element)(attMappings.item(i)), langCodes);
       }
   }


   /**
    * @deprecated This form of call should be replaced with the form that takes a
    * URL as its argument.
    * 
    * Loads the specified static text database document into the in-memory
    * database.
    *
    * @param dbDocPath The system-specific path to the static text database
    * document (e.g., static_text_database.xml).
    */
   public void loadStaticTextDatabase(java.lang.String dbDocPath)
                                                   throws I18nServiceError {
       DOMParser dp = new org.apache.xerces.parsers.DOMParser();
       try {
           dp.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
           dp.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
           dp.parse(dbDocPath);
           readDatabaseDoc(dp);
       } catch (Throwable e) {
           throw new I18nServiceError("Error constructing DOM from docid '" + dbDocPath + ": " + e.getMessage());
       }
   }
   
   /**
    * Loads the specified static text database document into the in-memory
    * database.
    *
    * @param dbDocUrl The URL of the static text database document.
    */
   public void loadStaticTextDatabase(URL dbDocUrl)
                                                   throws I18nServiceError {
       DOMParser dp = new org.apache.xerces.parsers.DOMParser();
       try {
           dp.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
           dp.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
           InputSource inSrc = new InputSource(dbDocUrl.openStream());
           dp.parse(inSrc);
           readDatabaseDoc(dp);
       } catch (Throwable e) {
           throw new I18nServiceError("Error constructing DOM from docid '" + dbDocUrl.toExternalForm() + ": " + e.getMessage());
       }
   }
   
   private void readDatabaseDoc(DOMParser dp) throws Throwable {
	    Document dbDoc = dp.getDocument();
	    Element rootElem = dbDoc.getDocumentElement();
	    if (rootElem == null) {
	       throw new I18nServiceError("Failed to find root element of static text database document");
	    }
	    NodeList tempnl = rootElem.getElementsByTagName("contexts_common");
	    if (tempnl.getLength() == 0) {
	        throw new I18nServiceError("Failed to find 'contexts_common' element");
	    }
	    Element commonContextElem = (Element)(tempnl.item(0));
	    processCommonContexts(commonContextElem, langCodes);
	    tempnl = rootElem.getElementsByTagName("attribute_maps_common");
	    if (tempnl.getLength() > 0) {
	        processCommonAttributeMappings((Element)(tempnl.item(0)), langCodes);
	    }
	    java.util.Collection codes = langCodes.values();
	    java.util.Iterator codeIter = codes.iterator();
	    for (int i = 0; i < codes.size(); i++) {
	        availableLanguages[i] = (String)(codeIter.next());
	    }   	
   }

   /**
    * Returns a string containing a report of the in-memory translation table.
    */
   public String printStaticTextDatabase() throws I18nServiceError{
       String result = "\n\nStatic Text Database\n";
       result = result + "Text Before:\n";
       java.util.Enumeration keys = textBefore.keys();
       while (keys.hasMoreElements()) {
           String key = (String)(keys.nextElement());
           result = result + "\n" + key + "=" + textBefore.get(key);
       }

       result = result + "\n\nText After:\n";
       keys = textAfter.keys();
       while (keys.hasMoreElements()) {
           String key = (String)(keys.nextElement());
           result = result + "\n" + key + "=" + textAfter.get(key);
       }

       result = result + "\n\nAttributes:\n";
       keys = attributeMap.keys();
       while (keys.hasMoreElements()) {
           String key = (String)(keys.nextElement());
           result = result + "\n" + key + "=" + attributeMap.get(key);
       }
       return result;
   }

}
