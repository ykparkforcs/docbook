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
package com.isogen.saxoni18n;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Collator;
import java.text.DateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

import com.icl.saxon.style.ExtensionElementFactory;
import com.icl.saxon.expr.*;

import javax.xml.transform.TransformerConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

import com.isogen.i18nsupport.I18nService;
import com.isogen.i18nsupport.I18nServiceError;
import com.isogen.i18nsupport.I18nUtil;
import com.isogen.indexhelper.IndexHelper;
import com.isogen.indexhelper.IndexHelperException;
import com.isogen.util.FileUtil;

/**
 * Binds the relevant methods of the generic I18nService class to the Saxon's
 * extension function API to provide a set of XSLT extension functions.
 * 
 * These functions provide support for both static ("generated") text management
 * and index configuration and collation management. These functions can be
 * used independently.
 * 
 * By default, the index configuration and static text database files are in
 * a directory called "config/" under the directory specified in the 
 * com.innodata.i18n.home Java system variable. The default configuration files
 * are:
 * <ul>
 * <li>config/static_text_database/static_text_database.xml</li>
 * <li>config/botb_index_rules/botb_index_rules.xml</li>
 * </ul>
 * 
 * The locations of these files can be set using the following system properties:
 * 
 * <ul>
 * <li>com.innodata.i18n.indexRulesFilename</li>
 * <li>com.innodata.i18n.staticTextDatabaseFilename</li>
 * </ul>
 * 
 * If either of these is set to the value "#none" then the
 * library will not attempt to load the file. Use this file if
 * you are not using a particular configuration file.
 * 
 * In addition, the class attempts to load the properties in
 * the file "config/properties/isogen_i18n.properties". If this
 * file is present, any properties set in this file will be set
 * as System properties for the JVM.
 * 
 * @version $Revision: 1.3 $
 */
public class Saxoni18nService implements ExtensionElementFactory {
	
    public static I18nService i18nServer = null;
    static final IndexHelper indexHelper;
    static String static_text_fn = "#none"; // By default, no static text database
    static String index_rules_fn = "#none"; // By default, no index rules

    static {
        String homePropertyName = "com.innodata.i18n.home";
		String i18n_home = System.getProperty(homePropertyName);
		
		if (i18n_home == null) {
			System.err.println("Saxoni18nService: ERROR: System property '" + homePropertyName + "' not set. Processing will fail.");
		}

		String config_path = i18n_home + File.separator + "config" + File.separator;
		File configDir = new File(config_path);
	    
		File propertiesDir = new File(configDir, "properties");
		File i18nPropertiesFile = new File(propertiesDir, "isogen_i18n.properties");

		if (i18nPropertiesFile.exists() && i18nPropertiesFile.canRead()) {
			Properties localProps = new Properties();
			try {
				localProps.load(i18nPropertiesFile.toURL().openStream());
				Enumeration propNames = localProps.propertyNames();
				while (propNames.hasMoreElements()) {
					String propname = (String)(propNames.nextElement());
					System.setProperty(propname, localProps.getProperty(propname));
				}
			} catch (Throwable e) {
				System.err.println(" - I18nService(): Warning: Failed to load properties file " + i18nPropertiesFile.getAbsolutePath());
			}
		}

		try {
			i18nServer = new I18nService();
		} catch (Throwable e) {
			System.err.println(" - Saxoni18nService: Failed to construct I18nService instance in class-level static processing. Results will not be good.");
			e.printStackTrace();
		}
        indexHelper = new IndexHelper();
		
        index_rules_fn = System.getProperty("com.innodata.i18n.indexRulesFilename");
        if (index_rules_fn == null) {
    		index_rules_fn = config_path + "botb_index_rules" + File.separator + "botb_index_rules.xml";        
        } 
        
        if (!index_rules_fn.equals("#none")) {
	        try {
	            index_rules_fn = config_path + "botb_index_rules" + File.separator + "botb_index_rules.xml";
				java.net.URL index_rules_Url = null;
				try {
					index_rules_Url = (new File(index_rules_fn)).toURL();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
	            if (index_rules_Url == null) {
	                System.err.println(" - Failed to find index configuration file " + index_rules_fn);
	            } else {
	                // indexHelper.loadIndexRuleSetDoc(index_rules_Url.toString());
	                indexHelper.loadIndexRuleSetDoc(index_rules_fn);
	            }
	        } catch (IndexHelperException e) {
	            String msg = " - Failed to load index rules document " + index_rules_fn + ". " +
				             " - Make sure the com.innodata.i18n.home system variable is set correctly.";
	            System.err.println(msg);
				e.printStackTrace();
	        }
        }
        
        static_text_fn = System.getProperty("com.innodata.i18n.staticTextDatabaseFilename");
        if (static_text_fn == null) {
        	static_text_fn = config_path + "static_text" + File.separator + "static_text_database.xml";
        	File staticTextFile = new File(static_text_fn);
        	if (!staticTextFile.exists()) {
        		System.err.println(" - Warning: com.innodata.i18n.staticTextDatabaseFilename property is not set\n" +
        				           " -          and there is no static text database in the default location\n" + 
								   " -          (config/static_text/static_text_database.xml).\n" +
								   " -          Using empty static text database.");
        		static_text_fn = "#none";
        	}
        } 
        
        if (!static_text_fn.equals("#none")) {
        	File staticTextFile = new File(static_text_fn);
			
	        try {
	        	URL staticTextUrl = staticTextFile.toURL();
				i18nServer.loadStaticTextDatabase(staticTextUrl);
			} catch (Throwable e) {
	            String msg = " - Failed to load static text database document " + static_text_fn + "." +
	            " - Make sure the com.innodata.i18n.home system variable is set correctly.";
	            System.err.println(msg);
	            e.printStackTrace();
			}
        }

    }

    public Saxoni18nService() throws TransformerConfigurationException {
    }

    /**
     * Called by Saxon's extention element mechanism to request classes for
     * extension elements. The element element name is the classname. Names
     * are case sensitive.
     * 
     * This package does not provide any extension elements.
     *
     */
    public java.lang.Class getExtensionClass(java.lang.String localname) {
        return null;
    }

    /**
     * Returns the text-before generated text for the element, if any. Returns
     * "" if there is no generated text or if the input node is not an element.
     *
     * @param sns Singleton Node Set, i.e., as provided by the "." expression.
     */
    public static String getGeneratedTextBefore(SingletonNodeSet sns, String langCode)  throws XPathException {
        Node currentNode = (Node)(sns.convertToJava(Node.class));
        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            try {
                return i18nServer.getGeneratedTextBefore((Element)currentNode, langCode);
            } catch (I18nServiceError exc) {
                return "";
            }
        } else {
            return "";
        }
    }


    /**
     * Returns the text-before generated text for an arbitrary key, if any. Returns
     * "" if there is no generated text.
     *
     * @param key The string key to look up in the static text database.
     */
    public static String getGeneratedTextForKeyBefore(String key, String langCode)  throws XPathException {
        try {
            return i18nServer.getGeneratedTextForKeyBefore(key, langCode);
        } catch (I18nServiceError exc) {
            return "";
        }
    }

	/**
	 * Returns the text-before generated text for an arbitrary key, if any. Returns
	 * "" if there is no generated text.
	 *
	 * @param key The string key to look up in the static text database.
	 */
	public static String getGeneratedTextForKeyAfter(String key, String langCode)  throws XPathException {
		try {
			return i18nServer.getGeneratedTextForKeyAfter(key, langCode);
		} catch (I18nServiceError exc) {
			return "";
		}
	}

    /**
     * Returns the text-before generated text for para elements.
     */
    public static String getParaGeneratedTextBefore(SingletonNodeSet sns)  throws XPathException {
        Node currentNode = (Node)(sns.convertToJava(Node.class));
        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            try {
                return i18nServer.getParaGeneratedTextBefore((Element)currentNode);
            } catch (I18nServiceError exc) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Returns the text-after generated text for the element, if any. Returns
     * "" if there is no generated text or if the input node is not an element.
     *
     * @param sns Singleton Node Set, i.e., as provided by the "." expression.
     */
    public static String getGeneratedTextAfter(SingletonNodeSet sns)  throws XPathException {
        Node currentNode = (Node)(sns.convertToJava(Node.class));
        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            try {
                return i18nServer.getGeneratedTextAfter((Element)currentNode);
            } catch (I18nServiceError exc) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Returns the text-after generated text for para elements.
     */
    public static String getParaGeneratedTextAfter(SingletonNodeSet sns)  throws XPathException {
        Node currentNode = (Node)(sns.convertToJava(Node.class));
        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            try {
                return i18nServer.getParaGeneratedTextAfter((Element)currentNode);
            } catch (I18nServiceError exc) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Returns translated presentation value for the specified attribute of the
     * specified element.
     */
    public static String getAttributeTranslation(SingletonNodeSet sns, String attname)  throws XPathException {
        Node currentNode = (Node)(sns.convertToJava(Node.class));
        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            try {
                return i18nServer.getTranslatedAttValue((Element)currentNode, attname);
            } catch (I18nServiceError exc) {
                return "";
            }
        } else {
            return "";
        }
    }

	/**
	 * Given an element with PCData content, returns the index group key string
	 * it should be grouped with.
	 * @param langCode Language code for the index configuration to use.
	 * @param indexEntry Index entry value to the get the group key for.
	 * @return group key string
	 */
	public static String getIndexGroupKey(String langCode, String indexEntry) throws XPathException {
		// System.err.println("*** Saxoni18nService.getIndexGroupKey(): langCode='" + langCode + "', " +
		//		           "indexEntry='" + I18nUtil.escapeUnicodeString(indexEntry) + "'");
            try {
            	String result = indexHelper.getGroupKey(langCode, indexEntry);
            	
                if (result == null) {
                	System.err.println(" - Warning: empty index entry passed to getIndexGroupKey() extension function.");
					System.err.println(" -          Using key '#NUMERIC'.");
                	return "#NUMERIC";
                }
                return result;
            } catch (IndexHelperException e) {
            	e.printStackTrace();
				throw new XPathException("IndexHelperException: " + e.getMessage());
            }
    }

	public static String getIndexGroupSortKey(String langCode, String groupKey) throws XPathException {
		try {
			return indexHelper.getGroupSortKey(langCode, groupKey);
		} catch (IndexHelperException e) {
			throw new XPathException(e.getMessage());
		}
	}

    /**
     * Given the string key for an index group, returns the display label for
     * that group.
     *
     */
    public static String getIndexGroupLabel(String langCode, String groupKey) throws XPathException {
        if (groupKey == null) {
            groupKey = "";
        }
        try {
            return indexHelper.getGroupLabel(langCode, groupKey);
        } catch (IndexHelperException e) {
            throw new XPathException(e.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new XPathException("NullPointerException: " + e.getMessage());
        }
    }

    public static String printStaticTextDatabase() throws XPathException {
        try {
            return i18nServer.printStaticTextDatabase();
        } catch (I18nServiceError e) {
            throw new XPathException(e.getMessage());
        }
    }

    public static String printIndexConfiguration(String langCode,
                                                 boolean includeCollationRules)
                                                    throws XPathException {
        try {
            return indexHelper.printIndexRuleSet(langCode, includeCollationRules);
        } catch (IndexHelperException e) {
          e.printStackTrace();
            throw new XPathException(e.getMessage());
        }
    }
    public static String printIndexConfiguration(String langCode) throws XPathException {
        return printIndexConfiguration(langCode, false); // Suppress rules by default
    }

    public static String getFileDir(String filePath) {
    	return FileUtil.getFileDir(filePath);
    }

    public static String getUrlPart(String inUrl,
                                    String partName)
                                        throws XPathException {
      java.net.URL url;
      try {
          url = new java.net.URL(inUrl);
      } catch (java.net.MalformedURLException e) {
          try {
              url = new java.net.URL("http://" + inUrl);
          } catch (java.net.MalformedURLException e2) {
              throw new XPathException("getUrlPart: Malformed URL: " +
                                       e2.getMessage());
          }
      }
      if (partName.toLowerCase().equals("protocol")) {
          return url.getProtocol();
      }
      if (partName.toLowerCase().equals("host")) {
          return url.getHost();
      }
      if (partName.toLowerCase().equals("path")) {
          return url.getPath();
      }
      if (partName.toLowerCase().equals("file")) {
          return url.getFile();
      }
      throw new XPathException("getUrlPart: unrecognized part name '" + partName + "'");
    }
    public static boolean urlHasProtocol(String inUrl)
                                        throws XPathException {
      java.net.URL url;
      try {
          // If it's good at this point, it must have a protocol.
          url = new java.net.URL(inUrl);
          return true;
      } catch (java.net.MalformedURLException e) {
          try {
              // If adding a protocol fixes the URL, it must not
              // have had one.
              url = new java.net.URL("http://" + inUrl);
              return false;
          } catch (java.net.MalformedURLException e2) {
              throw new XPathException("getUrlPart: Malformed URL: " +
                                       e2.getMessage());
          }
      }
    }

    public static String escapeUnicodeString(String inStr)
                                        throws XPathException {
         return I18nUtil.escapeUnicodeString(inStr);
    }
    
    public static String constructTargetRelativePath(String origPathStr, String targetBasePathStr)
																		throws XPathException {
        String newPath = "";	
        File origPath = new File(origPathStr);
        File targetBasePath = new File(targetBasePathStr);
		StringTokenizer origTokenizer = null;
        try {
			origTokenizer = new StringTokenizer(origPath.getCanonicalPath().toString(),
			                                    File.separator);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Now eliminate the common part of the two paths:
		while (origTokenizer.hasMoreTokens()) {
			String origToken = (String)(origTokenizer.nextElement());
			String targToken = (String)(origTokenizer.nextElement());
			if (!origToken.equals(targToken)) {
				break;
			}
		}
		// At this point, the remaining tokens reflect the divergent paths
		// of the two paths.
        return newPath;																		
    }
    
    /**
     * Returns the filename part of a file path.
     * @param inPath
     * @return Filename part, including any extension.
     * @throws XPathException
     */
	public static String getFilename(String inPath)
										throws XPathException {
		 return FileUtil.getFileName(inPath);
	}
    
	/**
	 * Returns the filename part of a file path, with no extension
	 * @param inPath
	 * @return Filename part, minus any extension
	 * @throws XPathException
	 */
	public static String getBaseName(String inPath)
										throws XPathException {
         return  FileUtil.getFileBaseName(inPath);
	}
	
	/**
	 * Returns the current system date and time as a string.
	 * @return date and time string.
	 */
	public static String getTimeStamp() {
		String result = "";
		Date date = new Date();
		result = DateFormat.getDateInstance().format(date);
		result = result + " " + DateFormat.getTimeInstance().format(date);
		return result;
	}

	/**
	 * Returns the relative from targetPath to filePath.
	 * @param filePath The path the result is relative to. Must be an absolute path.
	 * @param targetPath The path the result is derived from. Must be an absolute path, 
	 * otherwise it is returned immediately.
	 * @return The relative path, if any. 
	 */
	public static String getRelativePath(String filePath, String targetPath) {
		return FileUtil.getRelativePath(filePath, targetPath);
	}

	public static String getAbsolutePath(String basePath, String relativePath) {
		return FileUtil.getAbsolutePath(basePath, relativePath);
	}

	public static void main(String[] args) {
        System.err.println("This class cannot be run from the command line");
    }

	/**
	 * Given an language code, returns a Comparator for that language.
	 * @param langCode language code, consisting of ISO 639 two-letter language
	 * code and, optionally, an ISO 3166 country code, separated by a hyphen ("-"), 
	 * e.g., "ar", "zh-TW".
	 * @return Comparator object.
	 */
	public static Comparator getComparatorForLanguageCode(String langCode) {
		try {
			return i18nServer.getComparatorForLanguageCode(langCode);
		} catch (Exception e) {
			System.err.println(" - SaxonI18nService.getComparatorForLanguageCode(): " + 
					           e.getClass().getName() + " exception getting comparator for language code '" +
							   langCode + "'. Using default Java Collator."); 
			e.printStackTrace();
			Collator col = Collator.getInstance();
			return (Comparator)col;
		}
	}

    public static String getVersionAndRelease() {
    	return I18nService.getVersionAndRelease();
    }

	/**
	 * Returns the filename used to load the static text database.
	 * @return The filename, as a string
	 */
	public static String getStaticTextDatabaseFileName() {
		return static_text_fn;
	}

	/**
	 * Returns the filename used to load the index rules.
	 * @return The filename, as a string
	 */
	public static String getIndexRulesFileName() {
		return index_rules_fn;
	}
}
