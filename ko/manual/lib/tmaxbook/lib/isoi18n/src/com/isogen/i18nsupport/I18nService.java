/*--------------------------------------------------------------------------------
    Copyright (C) 2003, 2004 ISOGEN International

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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.Locale;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import com.isogen.i18nsupport.compare.ComparatorFactory;


/**
 *
 * @version $Revision: 1.5 $
 */
public class I18nService {

    StaticTextDatabase textDb = null;
    // Name of the system property used to configure the comparator factory to use.
    public static final String COMPARATORFACTORYNAMESYSTEMPROPERTY = "com.isogen.i18nsupport.comparatorFactoryClass";
	private static final String DEFAULTCOMPARATORYFACTORYCLASSNAME = "com.isogen.i18nsupport.compare.IcuComparatorFactoryImpl";

	private ComparatorFactory comparatorFactory = null;
	
	private static final String version = "1"; // Major version number.
	private static final String release = "2"; // Release. Increment for each new public release.


	/**
	 * The constructor initializes the static database to a null database.
	 * @throws Throwable
	 */
    public I18nService() throws Throwable {

		textDb = new StaticTextDatabase();
        comparatorFactory = I18nService.constructComparatorFactory();
    }
    
    public static String getVersionAndRelease() {
    	return version + "." + release;
    }

    /**
	 * @return
	 */
	public static ComparatorFactory constructComparatorFactory() throws Throwable {
		String comparatorFactoryClassName = I18nService.DEFAULTCOMPARATORYFACTORYCLASSNAME;
		ComparatorFactory comparatorFactory = null;
		if (System.getProperty(I18nService.COMPARATORFACTORYNAMESYSTEMPROPERTY) != null) {
			comparatorFactoryClassName = System.getProperty(I18nService.COMPARATORFACTORYNAMESYSTEMPROPERTY);
		}
		comparatorFactory = (ComparatorFactory)(Class.forName(comparatorFactoryClassName).newInstance());
		return comparatorFactory;
	}

	/**
     * Returns True if the specified element's language is equal to the target language.
     *
     * @param elemNode The element to check.
     */
    public boolean isTargetLanguage(Element elemNode, String targetLangCode) throws I18nServiceError{
        String langCode = "en";  // Default
        String langAttName = I18nUtil.getLangAttName();
                
        if (elemNode.hasAttribute(langAttName)) {
            langCode = elemNode.getAttribute(langAttName);
        } else if (elemNode.hasAttributeNS("xml", "lang")) {
            langCode = elemNode.getAttributeNS("xml", "lang");
        } else if (elemNode.hasAttribute("xml:lang")) {
            langCode = elemNode.getAttribute("xml:lang");
        }
        return (langCode.equals(targetLangCode));
    };

    /**
     * Returns the generated text-before associated with the specified para element's
     * grandparent.
     *
     * @param elemNode The element whose grandparent determines the generated text
     * to return.
     */
    public String getParaGeneratedTextBefore(Element elemNode) throws I18nServiceError{
        Element parent = (Element)(elemNode.getParentNode().getParentNode());
        String tagname = parent.getNodeName();
        String key = textDb.getDatabaseKey(tagname, elemNode);
        // System.err.println("in getParaGeneratedTextBefore, key=" + key);
        if (textDb.textBefore.containsKey(key)) {
            return (String)textDb.textBefore.get(key);
        }
        return "{No generated text before found for '" + tagname + "' element}";
    };

    /**
     * Returns the generated text-after associated with the specified element's
     * grandparent.
     *
     * @param elemNode The element whose grandparent determines the generated text
     * to return.
     */
    public String getParaGeneratedTextAfter(Element elemNode) throws I18nServiceError{
        Element parent = (Element)(elemNode.getParentNode().getParentNode());
        String tagname = parent.getNodeName();
        String key = textDb.getDatabaseKey(tagname, elemNode);
        if (textDb.textBefore.containsKey(key)) {
            return (String)textDb.textAfter.get(key);
        }
        return "{No generated text after found for '" + tagname + "' element}";
    };

    /**
     * Returns the generated text associated with the specified element's
     * grandparent if the paragraph's container (lang.alts) is the first
     * such element in the grandparent.
     *
     * @param elemNode The element whose grandparent determines the generated text
     * to return.
     */
    public String getParaGeneratedTextBeforeFirst(Element elemNode) throws I18nServiceError{
        Element parentElem = (Element)(elemNode.getParentNode());
        if (isFirstChild(parentElem)) {
            return getParaGeneratedTextBefore(elemNode);
        } else {
            String firstChildTagname = parentElem.getParentNode().getFirstChild().getNodeName();
            throw new I18nServiceError("Element " + parentElem.getNodeName() + " was not first, " + firstChildTagname + " is first child of para's grandparent") ;
        }
    };

    /**
     * Returns the generated text for arbitrary entries
     * in the generated text table. The second parameter must be an element
     * that exhibits, or that has an ancestor that exhibits a language
     * attribute from which the target language is determined.
     */
    public String getGeneratedTextForKeyBefore(String inKey, Element elemNode)
                                                            throws I18nServiceError {
        String key = textDb.getDatabaseKey(inKey, elemNode);
        if (textDb.textBefore.containsKey(key)) {
            return (String)textDb.textBefore.get(key);
        } else {
            key = elemNode.getNodeName() + "^#DEFAULT";
            if (textDb.textBefore.containsKey(key)) {
                return (String)textDb.textBefore.get(key);
            }
        }
        return "{No generated text before found for '" + inKey + "'}";
    }

    /**
     * Returns the generated text for arbitrary entries
     * in the generated text table.
     */
    public String getGeneratedTextForKeyBefore(String inKey, String langCode)
                                               throws I18nServiceError {
        String key = textDb.buildTranstableKey(inKey, langCode);
        // System.err.println("*** key='" + key + "'");
        if (textDb.textBefore.containsKey(key)) {
            return (String)textDb.textBefore.get(key);
        } else {
            key = inKey + "^#DEFAULT";
			// System.err.println("*** default key='" + key + "'");
            if (textDb.textBefore.containsKey(key)) {
                return (String)textDb.textBefore.get(key);
            }
        }
        return "{No generated text before found for '" + inKey + "'}";
    }

	/**
	 * Returns the generated text for arbitrary entries
	 * in the generated text table.
	 */
	public String getGeneratedTextForKeyAfter(String inKey, String langCode)
											   throws I18nServiceError {
		String key = textDb.buildTranstableKey(inKey,
											   langCode);
		//System.err.println("key='" + key + "'");
		if (textDb.textAfter.containsKey(key)) {
			return (String)textDb.textAfter.get(key);
		} else {
			key = inKey + "^#DEFAULT";
			if (textDb.textAfter.containsKey(key)) {
				return (String)textDb.textAfter.get(key);
			}
		}
		return "{No generated text after found for '" + inKey + "'}";
	}

    /**
     * Returns the generated text for arbitrary entries
     * in the generated text table. The second parameter must be an element
     * that exhibits, or that has an ancestor that exhibits a language
     * attribute.
     */
    public String getGeneratedTextForKeyAfter(String inKey, Element elemNode)
                                                            throws I18nServiceError {
        String key = textDb.getDatabaseKey(inKey, elemNode);
        if (textDb.textAfter.containsKey(key)) {
            return (String)textDb.textAfter.get(key);
        } else {
            key = elemNode.getNodeName() + "^#DEFAULT";
            if (textDb.textAfter.containsKey(key)) {
                return (String)textDb.textAfter.get(key);
            }
        }
        return "{No generated text before found for '" + inKey + "'}";
    }

    /**
     * Returns the generated text associated with the specified element.
     *
     * @param elemNode The element whose generated text is to be returned.
     */
    public String getGeneratedTextBefore(Element elemNode, String langCode) throws I18nServiceError{
        String tagname = elemNode.getNodeName();
        return getGeneratedTextForKeyBefore(tagname, langCode);
    };


    /**
     * Returns true if the specified element is the first element child of its parent.
     *
     * @param elemNode The element to be checked.
     */
    public boolean isFirstChild(Element elemNode)  throws I18nServiceError {

        Node parentNode = elemNode.getParentNode();
        if (parentNode == null) {
            return true;
        }
        Node firstChild = parentNode.getFirstChild();
        if (elemNode.equals(firstChild)) {
            return true;
        }

        NodeList children = parentNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            // This has the effect of skipping any initial non-Element
            // nodes.
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.equals(elemNode)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if the specified element is the first element child of its
     * element type (tagname) within its parent's children.
     *
     * @param elemNode The element to be checked.
     */
    public boolean isFirstChildOfType(Element elemNode) throws I18nServiceError {
        return false;
    }

    /**
     * Returns the generated text associated with the specified element if
     * the element is the first child of its parent.
     *
     * @param elemNode The element whose the generated text is returned.
     */
    public String getGeneratedTextBeforeFirst(Element elemNode, String langCode) throws I18nServiceError{
        if (isFirstChild(elemNode)) {
            return getGeneratedTextBefore(elemNode, langCode);
        } else {
            return "";
        }
    };

    /**
     * Returns the text-after associated with the specified element
     *
     * @param elemNode The element whose the generated text is returned.
     */
    public String getGeneratedTextAfter(Element elemNode) throws I18nServiceError{
        String langCode = I18nUtil.getElementLanguage(elemNode, "en");
        String tagname = elemNode.getNodeName();
        String key = textDb.getDatabaseKey(tagname, elemNode);
        if (textDb.textAfter.containsKey(key)) {
            return (String)textDb.textAfter.get(key);
        } else {
            key = elemNode.getNodeName() + "^#DEFAULT";
            if (textDb.textAfter.containsKey(key)) {
                return (String)textDb.textAfter.get(key);
            }
        }
        return "{No generated text after found for '" + tagname + "' element}";
    };

    

	String makeAttKeyPrefix(String tagname, String attName, String attval) throws I18nServiceError{
        return tagname + "@" + attName + "!" + attval;
    }

    String makeAttKey(Element elemNode, String tagname, String attName, String attval) throws I18nServiceError{
        String prefix = makeAttKeyPrefix(tagname, attName, attval);
        return textDb.getDatabaseKey(prefix, elemNode);
    }

  String makeAttKey(String tagname, String attName, String attval, String langCode) throws I18nServiceError {
    String prefix = makeAttKeyPrefix(tagname, attName, attval);
    return textDb.buildTranstableKey(prefix, langCode);
  }

    String makeAttKeyDefault(String tagname, String attName, String attval) throws I18nServiceError{
        String prefix = makeAttKeyPrefix(tagname, attName, attval);
        return textDb.getDefaultKey(prefix);
    }


    /**
     * Returns the translated display text associated with a particular
     * attribute value.
     *
     * @param elemNode The element that exhibits the attribute to get the
     * translation for.
     *
     * @param attname  The name of the attribute whose value is to be translated.
     *
     */
    String lookupAttributeTranslation(Element attHolderNode, String attName) throws I18nServiceError{
        String tagname = attHolderNode.getNodeName();
        String attval = attHolderNode.getAttribute(attName);
        String key = makeAttKey(attHolderNode, tagname, attName, attval);
        String keyDefault = makeAttKeyDefault(tagname, attName, attval);
        if (textDb.attributeMap.containsKey(key)) {
            return (String)(textDb.attributeMap.get(key));
        } else {
            key = makeAttKey(attHolderNode, "#COMMON", attName, attval);
            if (textDb.attributeMap.containsKey(key)) {
                return (String)(textDb.attributeMap.get(key));
            } else {
                key = makeAttKeyDefault("#COMMON", attName, attval);
                if (textDb.attributeMap.containsKey(key)) {
                    return (String)(textDb.attributeMap.get(key));
                } else {
                    return "{ERROR: no mapping for attribute " + attName + ", value '" + attval + "'}";
                }
            }
        }
    }

    public String getTranslatedAttValueForKey(String tagname, String attName, String attval, String langCode) throws I18nServiceError {
    String key = makeAttKey(tagname, attName, attval, langCode);
    String keyDefault = makeAttKeyDefault(tagname, attName, attval);
    if (textDb.attributeMap.containsKey(key)) {
      return (String)(textDb.attributeMap.get(key));
    } else {
      key = makeAttKey("#COMMON", attName, attval, langCode);
      if (textDb.attributeMap.containsKey(key)) {
        return (String)(textDb.attributeMap.get(key));
      } else {
        key = makeAttKeyDefault("#COMMON", attName, attval);
        if (textDb.attributeMap.containsKey(key)) {
          return (String)(textDb.attributeMap.get(key));
        } else {
          return "{ERROR: no mapping for attribute " + attName + ", value '" + attval + "'}";
        }
      }
    }

    }

    /**
     * Returns the translated display text associated with a particular
     * attribute value.
     *
     * @param elemNode The element that exhibits or that has an ancestor that
     * exhibits the attribute to get the translation for. For example, if a
     * <number> element is specified for the attibute "measure.type", the
     * number's ancestors will be checked to see if any have a measure.type
     * attribute.
     *
     * @param attname  The name of the attribute whose value is to be translated.
     *
     */
    public String getTranslatedAttValue(Element elemNode, String attName) throws I18nServiceError {
        Node attHolderNode = I18nUtil.getAttHolder(elemNode, attName);
        if (attHolderNode != null) {
            return lookupAttributeTranslation((Element)attHolderNode, attName);
        } else {
            return "{FOSI code error: no ancestor of " + elemNode.getNodeName() + " takes '" + attName + "' attribute}";
        }
    }

    /**
     * @deprecated Replace this form of the method with the version that uses a URL.
     * @param dbDocPath
     * @throws I18nServiceError
     */
    public void loadStaticTextDatabase(String dbDocPath)
                                       throws I18nServiceError {
    	URL dbDocUrl = null;
    	try {
			 dbDocUrl = new File(dbDocPath).toURL();
		} catch (MalformedURLException e) {
			throw new I18nServiceError(e);
		}
    	textDb.loadStaticTextDatabase(dbDocUrl);
    }

    public void loadStaticTextDatabase(URL dbDocUrl)
    									throws I18nServiceError {
    	textDb.loadStaticTextDatabase(dbDocUrl);
	}

    public String printStaticTextDatabase()
                                       throws I18nServiceError {
            return textDb.printStaticTextDatabase();
    }

    public boolean hasKey(String key, String langCode)
                       throws I18nServiceError {
      return textDb.hasKey(key, langCode);
    }

	/**
	 * @return
	 */
	public ComparatorFactory getComparatorFactory() throws I18nServiceError {
		return this.comparatorFactory;
	}

	/**
	 * @param langCode
	 * @return
	 */
	public Comparator getComparatorForLanguageCode(String langCode) throws Exception {
		Locale locale = I18nUtil.getLocaleFromLangCode(langCode);
		return this.comparatorFactory.getComparatorForLocale(locale);
	}

}