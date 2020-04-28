/*
 * Copyright (c) 2005 Innodata-Isogen, Inc.
 */
package com.isogen.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import com.isogen.i18nsupport.I18nService;
import com.isogen.i18nsupport.I18nUtil;

/**
 * Utility for working with the Unicode unihan.txt file.
 */
public class UnihanUtil {
    

    public static void main(String[] args) throws Throwable {
        
        // String zhtwrules = "c:\\temp\\zhtw-collation-rules.txt";
        // UnihanUtil.writeZhTwIcuCollationRules(zhtwrules);
        //String unihanFn = "c:\\temp\\Unihan-sample.txt";
        // String unihanFn = "c:\\temp\\Unihan-half.txt";
        String unihanFn = "D:\\standards\\unicode\\Unihan.txt";
        String outFilename = "c:\\temp\\radical-and-stroke-count.txt";
        TreeSet keySet = new TreeSet();
        //keySet.add("kRSKanWa");
        //keySet.add("kRSKangXi");
        keySet.add("kRSUnicode");

        FileOutputStream fos = new FileOutputStream(outFilename);
        // sliceUnihanDatabase(unihanFn, keySet, fos);
        // sortByStrokeAndRadical(unihanFn, fos);
        sortUsingIcu(unihanFn, fos, "zh-TW");
        
        // UnihanUtil unihanUtil = new UnihanUtil();        
        // UnihanDatabase database = unihanUtil.new UnihanDatabase(unihanFn);

        System.err.println(" + Done.");
    }
    
    /**
     * @param unihanFn
     * @param fos
     * @param string
     */
    private static void sortUsingIcu(String unihanFn, FileOutputStream fos, String langCode) throws Throwable {
        System.err.println("Sorting using ICU collator for locale " + langCode);
        Set keySet = new TreeSet();
        keySet.add("kTotalStrokes");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(unihanFn)));
        Writer out = new OutputStreamWriter(fos, "utf-8");
        String inline = null;
        String currentCodePoint = null;
        StringTokenizer tok = null;
        UnihanEntry dbEntry = null;
        
        UnihanUtil unihanUtil = new UnihanUtil();
        I18nService i18nService = new I18nService();
        Comparator icuComp = i18nService.getComparatorForLanguageCode(langCode); 
        Comparator comp = unihanUtil.new CollationComparator(icuComp); 
        TreeSet entriesByTotalStrokes = new TreeSet(comp);

        inline = br.readLine();
        int ctr = 0;
        while (inline != null) {
            ctr++;
            if (inline.startsWith("#")) {
                inline = br.readLine();
                continue;
            }
            tok = new StringTokenizer(inline, " \t");
            String codePoint = tok.nextToken();
            if (!codePoint.equals(currentCodePoint)) {
                if (dbEntry != null) {
                    if (dbEntry.containsKey("kTotalStrokes")) {
                        entriesByTotalStrokes.add(dbEntry);
                    }
                }
                dbEntry = unihanUtil.new UnihanEntry(codePoint);
                currentCodePoint = codePoint;
            }
            String key = tok.nextToken();
            String value = tok.nextToken();
            if (keySet.contains(key)) {
                if (key.equals("kTotalStrokes")) {
                    // System.err.println("key=" + key + ", value=" + value);
                }
                dbEntry.put(key, value);
            }
            inline = br.readLine();
        }
        // Write the last entry:
        if (dbEntry.containsKey("kTotalStrokes")) {
            entriesByTotalStrokes.add(dbEntry);
        }
        // Now write out the sorted set:
        Collection keys = new Vector();
        keys.add("kTotalStrokes");
        // keys.add("strokeCount");
        // keys.add("radical");
        Iterator iter = entriesByTotalStrokes.iterator();
        while (iter.hasNext()) {
            UnihanUtil.UnihanEntry entry = (UnihanUtil.UnihanEntry)(iter.next());
            out.write(entry.toString(keys));
            out.write("\n");
        }
        out.close();
    }

    /**
     * @param database
     * @param unihanFn
     * @param keySet
     * @param fos
     * @throws Throwable
     */
    private static void sliceUnihanDatabase(String unihanFn, Set keySet, FileOutputStream fos) throws Throwable {
        System.err.println(" + Slicing and dicing Unihan database from " + unihanFn + " to " + unihanFn + "...");
        UnihanUtil unihanUtil = new UnihanUtil();        
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(unihanFn)));
        Writer out = new OutputStreamWriter(fos, "utf-8");
        String inline = null;
        String currentCodePoint = null;
        StringTokenizer tok = null;
        UnihanEntry dbEntry = null; 
        inline = br.readLine();
        int ctr = 0;
        while (inline != null) {
            ctr++;
            if (inline.startsWith("#")) {
                inline = br.readLine();
                continue;
            }
            tok = new StringTokenizer(inline, " \t");
            String codePoint = tok.nextToken();
            if (!codePoint.equals(currentCodePoint)) {
                if (dbEntry != null) {
                    out.write(dbEntry.toString(keySet));
                }
                dbEntry = unihanUtil.new UnihanEntry(codePoint);
                currentCodePoint = codePoint;
            }
            String key = tok.nextToken();
            String value = tok.nextToken();
            if (keySet.contains(key)) {
                dbEntry.put(key, value);
            }
            inline = br.readLine();
        }
        // Write the last entry:
        out.write(dbEntry.toString(keySet));
        out.write("\n");
        out.close();
    }

    private static void sortByStrokeAndRadical(String unihanFn, FileOutputStream fos) throws Throwable {
        System.err.println("Sorting by stroke count and radical");
        Set keySet = new TreeSet();
        keySet.add("kRSUnicode");
        keySet.add("kTotalStrokes");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(unihanFn)));
        Writer out = new OutputStreamWriter(fos, "utf-8");
        String inline = null;
        String currentCodePoint = null;
        StringTokenizer tok = null;
        UnihanEntry dbEntry = null;
        
        UnihanUtil unihanUtil = new UnihanUtil();        
        TreeSet entriesBySandR = new TreeSet(unihanUtil.new StrokeRadicalComparator());

        inline = br.readLine();
        int ctr = 0;
        while (inline != null) {
            ctr++;
            if (inline.startsWith("#")) {
                inline = br.readLine();
                continue;
            }
            tok = new StringTokenizer(inline, " \t");
            String codePoint = tok.nextToken();
            if (!codePoint.equals(currentCodePoint)) {
                if (dbEntry != null) {
                    if (dbEntry.containsKey("kTotalStrokes")) {
                        entriesBySandR.add(dbEntry);
                    }
                }
                dbEntry = unihanUtil.new UnihanEntry(codePoint);
                currentCodePoint = codePoint;
            }
            String key = tok.nextToken();
            String value = tok.nextToken();
            if (key.equals("kRSUnicode")) {
                String[] parts = value.split("\\.");
                String radical = parts[0];
                String count = parts[1];
                // dbEntry.put("radical", radical);
                // dbEntry.put("strokeCount", count);                
            } else {
                if (keySet.contains(key)) {
                    if (key.equals("kTotalStrokes")) {
                        System.err.println("key=" + key + ", value=" + value);
                    }
                    dbEntry.put(key, value);
                }
            }
            inline = br.readLine();
        }
        // Write the last entry:
        if (dbEntry.containsKey("kTotalStrokes")) {
            entriesBySandR.add(dbEntry);
        }
        // Now write out the sorted set:
        Collection keys = new Vector();
        keys.add("kTotalStrokes");
        // keys.add("strokeCount");
        // keys.add("radical");
        Iterator iter = entriesBySandR.iterator();
        while (iter.hasNext()) {
            UnihanUtil.UnihanEntry entry = (UnihanUtil.UnihanEntry)(iter.next());
            out.write(entry.toString(keys));
            out.write("\n");
        }
        out.close();
    }    
    
    public static void writeZhTwIcuCollationRules(String outputFilename) throws Throwable {
        Locale zhTwLocale = Locale.TRADITIONAL_CHINESE;
        I18nUtil.writeIcuCollationRulesForLocale(zhTwLocale, outputFilename);
    }
    
    class UnihanDatabase {
        TreeMap entries = new TreeMap();
        
        public UnihanDatabase(String databaseFilename) throws Throwable {
            File databaseFile = new File(databaseFilename);
            this.loadUnihanDatabase(databaseFile);
        }
        
        public void loadUnihanDatabase(File databaseFile) throws Throwable {
            int ctr = 0;
            System.err.println(" + Loading Unihan database from file '" + databaseFile.getAbsolutePath() + "'");
            UnihanUtil unihanUtil = new UnihanUtil();        
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(databaseFile)));
            String inline = null;
            String currentCodePoint = null;
            UnihanEntry dbEntry = null; 
            inline = br.readLine();
            while (inline != null) {
                if (inline.startsWith("#")) {
                    inline = br.readLine();
                    continue;
                }
                String[] parts = inline.split("[ \t]");
                String codePoint = parts[0];
                
                byte[] tempbytes = I18nUtil.hexToBytes(codePoint.substring(2));
                byte[] charbytes = new byte[tempbytes.length + 2];
                charbytes[0] = (byte)0xFE;
                charbytes[1] = (byte)0xFF;
                for (int i = 0; i < tempbytes.length; i++) {
                    charbytes[i+2] = tempbytes[i];
                }
                ByteArrayInputStream bais = new ByteArrayInputStream(charbytes);
                InputStreamReader isr = new InputStreamReader(bais, "utf-16");
                int c = isr.read();
                String keyChar = Character.toString((char)c);
                
                if (!codePoint.equals(currentCodePoint)) {
                    if (dbEntry != null) {
                        if ((++ctr % 1000) == 0) {
                            System.err.println(ctr);
                        } 
                        entries.put(dbEntry.getCodePoint(), dbEntry);
                    }
                    dbEntry = unihanUtil.new UnihanEntry(keyChar);
                    currentCodePoint = codePoint;
                }
                String key = parts[1];
                String value = parts[2];
                if (parts.length > 3) {
                    for (int i = 3; i < parts.length; i++) {
                        value = value + " " + parts[i];
                    }
                }
                dbEntry.put(key, value);
                inline = br.readLine();
            }            
        }
        
        public String toString() {
            String resultStr = "Unihan database:\n";
            Iterator iter = entries.keySet().iterator();            
            while (iter.hasNext()) {
                String key = (String)(iter.next());
                UnihanEntry entry = (UnihanEntry)(entries.get(key));
                resultStr = resultStr + entry.toString() + "\n";
            }
            return resultStr;
        }

        public void toFile(String outfileName) throws Throwable {
            FileOutputStream fos = new FileOutputStream(outfileName);
            Writer out = new OutputStreamWriter(fos, "utf-8");
            out.write("Unihan database:\n");
            Iterator iter = entries.keySet().iterator();            
            while (iter.hasNext()) {
                String key = (String)(iter.next());
                UnihanEntry entry = (UnihanEntry)(entries.get(key));
                out.write(entry.toString() + "\n");
            }
            out.close();
        }
    }

    class UnihanEntry {
        TreeMap entryFields = new TreeMap();
        private String codePoint;
        
        public UnihanEntry(String codePoint) {
            this.codePoint = codePoint;            
        }
                
        /**
         * @param string
         * @return
         */
        public boolean containsKey(String key) {
            return entryFields.containsKey(key);
        }

        public void put(String key, String value) {
            this.entryFields.put(key, value);
        }
        
        public String getCodePoint() {
            return this.codePoint;
        }
        
        public String get(String key) {
            return (String)(this.entryFields.get(key));
        }
        
        public String toString(Collection keySet) {
            String resultStr = this.codePoint;
            Iterator iter = keySet.iterator();
            while (iter.hasNext()) {
                String key = (String)(iter.next());
                String value = get(key);
                if (key.equals("kRSUnicode")) {
                    String[] parts = value.split("\\.");
                    String radical = parts[0];
                    String count = parts[1];
                    if (count.length() == 1) {
                        count = "0" + count;
                    }
                    value = radical + "." + count;
                }
                resultStr = resultStr + "\t" + key + ": " + value;
            }
            return resultStr;            
        }
        
        public String toString() {
            String resultStr = this.codePoint;
            Iterator iter = entryFields.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String)(iter.next());
                String value = get(key);
                resultStr = resultStr + "\t" + key + ": " + value;
            }
            return resultStr;
        }
    }
    
    class CollationComparator implements Comparator {
        
        Comparator comp = null;
        public CollationComparator(Comparator comp) {
            this.comp = comp; // Character comparator
        }
        /**
         * codePoint1: the string representation of the code point, i.e., "U+5342"
         * codePoint2: the string representation of the code point, i.e., "U+5342"
         */
        public int compare(Object codePoint1, Object codePoint2) {
            
            System.err.println("codePoint1='" + codePoint1 + "', class=" + codePoint1.getClass().getName());
            
            String cp1 = ((UnihanEntry)codePoint1).getCodePoint().substring(2);
            String cp2 = ((UnihanEntry)codePoint2).getCodePoint().substring(2);
            
            char c1 = (char) Integer.parseInt(cp1, 16);
            char c2 = (char) Integer.parseInt(cp2, 16);
            return this.comp.compare(String.valueOf(c1), String.valueOf(c2));
        }
    }
    
    class StrokeRadicalComparator implements Comparator {

        public int compare(Object obj1, Object obj2) {
            Integer radical1;;
            Integer sk1;
            Integer radical2;
            Integer sk2;
            Integer totalStrokes1;
            Integer totalStrokes2;
            
            UnihanUtil.UnihanEntry entry1 = (UnihanUtil.UnihanEntry)obj1; 
            UnihanUtil.UnihanEntry entry2 = (UnihanUtil.UnihanEntry)obj2;
            
            String sc1 = (String)(entry1.get("kTotalStrokes"));
            if (sc1 == null) {
                
                throw new RuntimeException("kTotalStrokes value is null for character " + entry1.getCodePoint());
            }
            String sc2 = (String)(entry2.get("kTotalStrokes"));
            
            totalStrokes1 = Integer.valueOf(sc1);
            totalStrokes2 = Integer.valueOf(sc2);
            
            
            if (totalStrokes1.equals(totalStrokes2)) {
                String rad1 = (String)(entry1.get("radical"));
                if (rad1.endsWith("'")) {
                    rad1 = rad1.substring(0, rad1.length() - 1);
                }
                String rad2 = (String)(entry2.get("radical"));
                if (rad2.endsWith("'")) {
                    rad2 = rad2.substring(0, rad2.length() - 1);
                }
                radical1 = Integer.valueOf(rad1);
                radical2 = Integer.valueOf(rad2);
                if (radical1.equals(radical2)) {
                    sk1 = Integer.valueOf((String)(entry1.get("strokeCount")));
                    sk2 = Integer.valueOf((String)(entry2.get("strokeCount")));
                    return sk1.compareTo(sk2);                        
                } else {
                    return radical1.compareTo(radical2);
                }
            } else {
                return totalStrokes1.compareTo(totalStrokes2);
            }
        }
        
    }
}
