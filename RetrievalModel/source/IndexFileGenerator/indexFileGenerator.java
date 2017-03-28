package com.neu;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class indexFileGenerator {

    static  Map<String, Map<Long, Long>> unigramIndex = new HashMap<String, Map<Long, Long>>();
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

    //Main Function---------------------------------------------------------------------------
    public static void main(String[] args) {
        generateIndex();
    }
//---------------------------------------------------------------------------------------------

    public static void generateIndex() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter full path of director where the files to be mapped are present:");
        String docsDirectory = sc.nextLine();
        File folder = new File(docsDirectory);
        File[] listOfFiles = folder.listFiles();
        long count = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                count++;
                fileMapping.put(count, file.getName());
//----------------------------------------------------------------------------------------------
                String content = readFile(file.toString(), Charset.defaultCharset());
                content = content.replaceAll("[\\r\\n]+", " ");

                System.out.println("Indexing: "+file.getName()+" DocId:"+count);
                indexGenerator( 1, count, content, unigramIndex);

//----------------------------------------------------------------------------------------------
            }
        }
        //writing filename and index mapping into a file
        System.out.println("writing file mapping into file filemap.txt");
        writeFileMapping(fileMapping);

        System.out.println("storing generated indexe as java serializable objects");
        storeIndexes("unigramIndex.ser",unigramIndex);
    }

    //Write document Id mapping into a file
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

    //Purpose: Store the index as java serializable object
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

    //Purpose: Generate Index
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

//Read content of the file
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
