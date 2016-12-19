package com.neu;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class indexCreator {

    static  Map<String, Map<Long, Long>> unigramIndex = new HashMap<String, Map<Long, Long>>();
    static  Map<String, Map<Long, Long>> bigramIndex = new HashMap<String, Map<Long, Long>>();
    static  Map<String, Map<Long, Long>> trigramIndex = new HashMap<String, Map<Long, Long>>();
    static  TreeMap<Long,String> fileMapping = new TreeMap<Long, String>();

    public static List<String> ngrams(int n, String str) {
        List<String> ngrams = new ArrayList<String>();
        String[] words = str.split("\\s+");
        for (int i = 0; i < words.length - n + 1; i++)
            ngrams.add(concat(words, i, i+n));
        return ngrams;
    }

    public static String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append((i > start ? " " : "") + words[i]);
        return sb.toString();
    }

    public static void main(String[] args) {
        generateIndex();
    }
//---------------------------------------------------------------------------------------------

    public static void generateIndex() {
        File folder = new File(System.getProperty("user.dir") + "/docs");
        File[] listOfFiles = folder.listFiles();
        long count = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                count++;
                fileMapping.put(count, file.getName());
//----------------------------------------------------------------------------------------------
                String content = readFile(file.toString(), Charset.defaultCharset());
                content = content.replaceAll("[\\r\\n]+", " ");

                indexGenerator( 1, count, content, unigramIndex);
                indexGenerator( 2, count, content, bigramIndex);
                indexGenerator( 3, count, content, trigramIndex);
                System.out.println(count);
//----------------------------------------------------------------------------------------------
            }
        }
        //writing filename and index mapping into a file
        System.out.println("writing file mapping into file filemap.xls");
        writeFileMapping(fileMapping);

       System.out.println("generating term and document frequency for all the documents");
        termDocFrequency(unigramIndex, "unigram");
        termDocFrequency(bigramIndex, "bigram");
        termDocFrequency(trigramIndex, "trigram");

        //sorting lexographically before saving index using java serialization
        TreeMap<String, Map<Long, Long>>  unigramtreemap = new TreeMap<String, Map<Long, Long>> (new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });
        unigramtreemap.putAll(unigramIndex);

        TreeMap<String, Map<Long, Long>>  bigramtreemap = new TreeMap<String, Map<Long, Long>> (new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });
        bigramtreemap.putAll(bigramIndex);

        TreeMap<String, Map<Long, Long>>  trigramtreemap = new TreeMap<String, Map<Long, Long>> (new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });
        trigramtreemap.putAll(trigramIndex);
    /*    System.out.println("storing indexes as java serializable objects");
        storeIndexes("unigramIndex.ser",unigramIndex);
        storeIndexes("bigramIndex.ser",bigramIndex);
        storeIndexes("trigramIndex.ser",trigramIndex);*/
    }

    public static void writeFileMapping(TreeMap<Long,String> fileMap)
    {
        File file = new File("filemap.txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Map.Entry<Long,String> e:fileMap.entrySet()) {
                bw.write(e.getKey()+";"+e.getValue()+"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void termDocFrequency(Map<String, Map<Long, Long>> index, String name)
    {
        Map<String , Long> termFrequency = new HashMap<String , Long>();
        Map<String , ArrayList<Long>> docFrequency = new HashMap<String , ArrayList<Long>>();

        for (Map.Entry<String, Map<Long, Long>> e: index.entrySet()) {

            ArrayList<Long> docs = new ArrayList<Long>();
            docs.addAll(e.getValue().keySet());
            docFrequency.put(e.getKey(),docs);
            long tf = 0;
            for (long l : e.getValue().values()) {
                tf = tf + l;
            }
            termFrequency.put(e.getKey(), tf);
        }

        // sorting hash map values in descending order for term frequency
        Set<Map.Entry<String , Long>> set = termFrequency.entrySet();
        List<Map.Entry<String , Long>> list = new ArrayList<Map.Entry<String , Long>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String , Long>>()
        {
            public int compare( Map.Entry<String , Long> o1, Map.Entry<String , Long> o2 )
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        } );
        writetf(list,name);

        TreeMap<String , ArrayList<Long>>   docgramtreemap = new TreeMap<String , ArrayList<Long>>  (new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });
        docgramtreemap.putAll(docFrequency);
        writedf(docgramtreemap,name);
    }

    public static void writetf(List<Map.Entry<String , Long>> t, String name)
    {

        File file = new File(name+"tf.txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Map.Entry<String , Long> e : t) {
                bw.write(e.getKey()+";"+e.getValue()+"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writedf(Map<String , ArrayList<Long>> d, String name)
    {
        File file = new File(name+"df.txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Map.Entry<String , ArrayList<Long>> e : d.entrySet()) {
               String s1 = e.getKey()+";"+e.getValue().size();
               String s2 = "";

                for (Long l : e.getValue())
                {
                   s2 = s2 + ";docId: " + l;
                }
                bw.write(s1+s2+"\n");
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void storeIndexes(String fileName, Map<String, Map<Long, Long>> hmap)
    {
        try
        {
            FileOutputStream fos =
                    new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(hmap);
            oos.close();
            fos.close();
            System.out.println("Serialized HashMap data is saved in "+fileName);
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static void indexGenerator(int n, long count, String content, Map<String, Map<Long, Long>> index)
    {
        List<String> ngram = ngrams(n, content);

        Map<String, Long> counts =
                ngram.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
//-------------------------------------------------------------------------------------------------
        for (Map.Entry e : counts.entrySet()) {

            String key = (String) e.getKey();
            long value = (long) e.getValue();

            if (index.containsKey(key)) {
                index.get(key).put(count, value);
            } else {
                Map<Long, Long> docEntry = new HashMap<Long, Long>();
                docEntry.put(count, value);
                index.put(key, docEntry);
            }
        }
//---------------------------------------------------------------------------------------------------
    }

    static String readFile(String path, Charset encoding)
    {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, encoding);
    }
}
