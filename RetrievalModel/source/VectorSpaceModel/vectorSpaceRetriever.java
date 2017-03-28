package com.neu;

import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.io.*;
import java.util.*;

public class vectorSpaceRetriever {
    static  Map<String, Map<Long, Long>> unigramIndex;
    static  Map<Long, Long> totalTermDoc = new HashMap<Long, Long>();
    static Long N; //holds number of documents in the corpus
    static String systemName = "VectorSpaceModel";
    static String scoreDoc = "scoreDocVSR.txt";
    static File file = new File(scoreDoc);
    static FileWriter fw = null;
    static BufferedWriter bw = null;

    public static void main(String[] args) {
        initialiseBufferOutput();
        Scanner scnr = new Scanner(System.in);

        //---------------loading index into memory-------------------------------
        System.out.println("Enter the complete path of Index file");
        String indexFilePath = scnr.nextLine();
        //read serialised index object
        System.out.println("Started loading the index into the memory");
        readSerialisedIndex(indexFilePath);
        System.out.println("Done loading serialised object into the memory");
        //------------------------------------------------------------------------


        //------------calculate total terms in each document----------------------
        createTotaltermDocMap();
        System.out.println("Done calculating total terms for each document");
        //------------------------------------------------------------------------


        //------------Calculate total terms in the whole corpus-------------------
        N = (long)totalTermDoc.size();
        System.out.println("Total documents in the corpus N = "+N);
        //------------------------------------------------------------------------


        //-------Compute the Score for documnets with respect to given queries----
        System.out.println("Enter the complete path of file with queries");
        String queryFilePath = scnr.nextLine();
        try (BufferedReader br = new BufferedReader(new FileReader(queryFilePath)))
        {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String line[] = sCurrentLine.split(";");
                System.out.println("computing score for documents relevant to query:" +
                        " "+"\""+line[1]+"\"");
                computeCosineScore(Integer.parseInt(line[0]),line[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //--------------------------------------------------------------------------


        //--------------------close buffer writer-----------------------------------
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //--------------------------------------------------------------------------

    }

    public static void computeCosineScore(int qid, String query)
    {
        //--------------parse given query and calculate term frequency------
        String[] eachTerm = query.split(" ");
        Map<String, Long> queryTermMap = generateQueryTermMap(eachTerm);
        //------------------------------------------------------------------


        Map<String, Double> queryTermScore = new HashMap<String, Double>();
        Map<Long, Map<String, Double>> docTermScore = new HashMap<Long, Map<String, Double>>();
        Map<Long, Double> docscore = new LinkedHashMap<Long, Double>();

        /*----calculate tf-idf values for query terms and respective documents
        *     in which they appear --------------------------------------------*/
        for (Map.Entry<String, Long> e : queryTermMap.entrySet()) {

            //----calculate inverse document frequency-----
            Double idf;
            //number of documents the term appears in
            int n = unigramIndex.get(e.getKey()).size();
            // Idf remains same for all the documents
            idf = Math.log10((double)N/(double) n);
            //---------------------------------------------

            //----log term frequency of the current term of the query---
            Double qtf = 1+(Math.log10((double)e.getValue()));
            //----------------------------------------------------------

            //----calculating weight for the query term-----------------
            Double wq = qtf * idf;
            //----------------------------------------------------------

            //store the weight for the query later to be used for normalizing
            queryTermScore.put(e.getKey(),wq);

            Map<Long, Long> docList = unigramIndex.get(e.getKey());

            //--------calculating term weight for tht  document----------
            for (Map.Entry<Long, Long> ed: docList.entrySet()) {
                //term frequecy of respective term in the document
                Double wd = (1+(Math.log10((double)ed.getValue())));

                //store weight for each term for respective document
                if(docTermScore.containsKey(ed.getKey()))
                {
                    docTermScore.get(ed.getKey()).put(e.getKey(),wd);
                }
                else
                {
                    Map<String, Double> termMap = new HashMap<String, Double>();
                    termMap.put(e.getKey(),wd);
                    docTermScore.put(ed.getKey(),termMap);
                }
            }
            //------------------------------------------------------------
        }
        //------------------------------------------------------------------------------

        //------------------normalise query vector--------------------------------------
        Double queryNormalise = 0.0;
        for (Map.Entry<String, Double> e :queryTermScore.entrySet()) {
            queryNormalise = queryNormalise + Math.pow(e.getValue(),2);
        }

        queryNormalise = Math.sqrt(queryNormalise);

        for (Map.Entry<String, Double> e :queryTermScore.entrySet()) {
            queryTermScore.put(e.getKey(), ((double)e.getValue()/(double)queryNormalise));
        }
        //--------------------------------------------------------------------------------

        //-----normalise document vector and calulate dot product for each documnet-------
        for (Map.Entry<Long, Map<String, Double>> e: docTermScore.entrySet()) {
            Double docNormalise = 0.0;

            for (Map.Entry<String, Double> e2 :e.getValue().entrySet()) {
                docNormalise = docNormalise + Math.pow(e2.getValue(),2);
            }
            docNormalise = Math.sqrt(docNormalise);

            Double docScore = 0.0;
            for (Map.Entry<String, Double> e2 :e.getValue().entrySet()) {
                Double normalise = (double)e2.getValue()/(double)docNormalise;

                //calculating dot product since query weight and document weights are already normalised
                Double dotProduct = queryTermScore.get(e2.getKey()) * normalise;

                docScore = docScore + dotProduct;
            }
            docscore.put(e.getKey(),docScore);
        }
        //---------------------------------------------------------------------------------

        //-------------------Sort and write the final score into a document----------------
        saveScore(qid, docscore);
        //----------------------------------------------------------------------------------
    }

    //purpose: Sort and save the calculated score for each document
    public static void saveScore(int qid, Map<Long, Double> map) {
        System.out.println("saving scores in descending order");

        //--------sort the values of the given map------------------------------------------
        Set<Map.Entry<Long, Double>> set = map.entrySet();
        List<Map.Entry<Long, Double>> list = new ArrayList<Map.Entry<Long, Double>>(set);
        Collections.sort( list, new Comparator<Map.Entry<Long, Double>>()
        {
            public int compare( Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        //----------------------------------------------------------------------------------

        //--------writing the top 100 sorted scored documents to a file---------------------
        int top_k = 0;
        if(list.size() >  100)
        {
            top_k = 100;
        }
        else
        {
            top_k = list.size();
        }

        Map<Long,String> filemap= new HashMap<>();

        FileInputStream fis = null;
        try {
            fis = new FileInputStream("filemap.txt");
        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = null;
        while ((line = br.readLine()) != null) {
            String words[] = line.split(";");
            filemap.put(Long.parseLong(words[0]),words[1]);
        }

        br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0 ; i<top_k ;i++){
            Map.Entry<Long, Double> entry = list.get(i);
            try {
                bw.write(qid+" "+"Q0"+" "+filemap.get(entry.getKey())+" "+(i+1)+" "+entry.getValue()+" "+systemName+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //----------------------------------------------------------------------------------
    }

    //Purpose: generates total number of terms for each documents for each document
    //Where: currently in this program only total documnets count generated from this operation
    ///      is used
    public static  Map<String, Long> generateQueryTermMap(String[] eachTerm)
    {
        Map<String, Long> queryTermMap = new HashMap<String, Long>();
        for (String term : eachTerm)
        {
            if(queryTermMap.containsKey(term))
            {
                Long currentValue = queryTermMap.get(term);
                queryTermMap.put(term, currentValue++);
            }
            else
            {
                queryTermMap.put(term, (long)1);
            }
        }
        return queryTermMap;
    }


    //Purpose: from the given creates a Map which has document ID as key and total terms in the
    //         document as value
    public static void createTotaltermDocMap()
    {
        for (Map.Entry<String, Map<Long, Long>> e: unigramIndex.entrySet()) {
            for (Map.Entry<Long, Long> e2: e.getValue().entrySet()) {
                Long key = e2.getKey();
                if(totalTermDoc.containsKey(key))
                {
                    Long currentTermCount = totalTermDoc.get(key);
                    Long newTermCount = currentTermCount + e2.getValue();
                    totalTermDoc.put(key, newTermCount);
                }
                else
                {
                    totalTermDoc.put(key,e2.getValue());
                }
            }
        }
    }


    //PurPose: reads the serialised indexobject from a particular location
    public static void readSerialisedIndex(String indexFilePath)
    {
        try {
            FileInputStream f_in = new FileInputStream(indexFilePath);
            ObjectInputStream obj_in = new ObjectInputStream (f_in);
            unigramIndex  =(HashMap<String, Map<Long, Long>>) obj_in.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    //Purpose: Initalises the Buffer writer used to write documents score to a file
    public static void initialiseBufferOutput()
    {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
