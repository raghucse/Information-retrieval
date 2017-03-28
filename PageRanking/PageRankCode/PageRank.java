import java.io.*;
import java.util.*;

/**
 * Created by raghu on 10/11/2016.
 */
public class PageRank {
    public static void main(String[] args) {
        Map<String, Set<String>> pageInlink = new HashMap<String, Set<String>>();
        Map<String, Set<String>> pageOutlink = new HashMap<String, Set<String>>();
        Set sinkLinks = new HashSet<String>();
        Set sourceLinks = new HashSet<String>();
        Map<String, Double> pageRankOfLinks = new HashMap<String, Double>();
        Map<String, Integer> pageInlinkCount = new HashMap<String, Integer>();
        PageRank p = new PageRank();

        System.out.println("Importing Inlink graph");
        pageInlink = p.getInLinkMap(args[0]);
        System.out.println("Finished Importing Inlink graph size: "+pageInlink.size());

        System.out.println("Calculating inlink count for each page and sorting");
        p.generateInlinkCount(pageInlink);
        System.out.println("Finished calculating inlink count for each page");



        System.out.println("Calculating pages without inlink");
        sourceLinks = p.getSinkLinkSet(pageInlink);
        System.out.println("Pages without inlink : "+sourceLinks.size());
        double sourceProp = (double)sourceLinks.size()/(double)pageInlink.size();

        System.out.println("sourceLink Proportion : "+sourceProp);

        System.out.println("Started Building outlink map");
        pageOutlink = p.buildOutlink(pageInlink);
        System.out.println("Finished Building outlink map size: "+pageOutlink.size());

        System.out.println("started Building sink link map");
        sinkLinks = p.getSinkLinkSet(pageOutlink);
        System.out.println("Finished Building sink link map size: "+sinkLinks.size());

        double sinkProp = (double)sinkLinks.size()/(double)pageOutlink.size();
        System.out.println("sinkLink Proportion : "+sinkProp);

        System.out.println("Started Computing page rank");
        pageRankOfLinks = p.computePageRank(pageInlink,pageOutlink,sinkLinks);
        System.out.println("Finished Computing page rank size:"+pageRankOfLinks.size());

        //sorting the map values
        List list=new LinkedList(pageRankOfLinks.entrySet());
        Collections.sort(list,new Comparator(){
            public int compare(Object obj1, Object obj2){
                return ((Comparable)((Map.Entry)(obj1)).getValue

                        ()).compareTo(((Map.Entry)(obj2)).getValue());
            }
        });
        //arrange in descending order
        Collections.reverse(list);
        //Out put the page rank into a file
        File file = new File(System.getProperty("user.dir")+"/pageRankSorted.txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Object o : list) {
                bw.write(o.toString()+"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  Map<String, Double>computePageRank(Map<String, Set<String>> pageInlink,
                                               Map<String, Set<String>> pageOutlink, Set<String> sinkLinks)
    {
        List convergeVal = new ArrayList<Double>();
        double N = pageInlink.size();
        double d =0.85;
        Map<String, Double> pageRank = new HashMap<String, Double>();
        pageRank = calIntialRank(pageInlink.size(),pageInlink);

        int i=0;
        while (!converged(pageRank))
        {
            double sinkPR =0;
            /*	calculate	total	sink	PR	*/
            for (String s: sinkLinks) {
                sinkPR = sinkPR + pageRank.get(s);
            }
            Map <String, Double> newPrMap = new HashMap<String, Double>();
            for (Map.Entry<String, Double> entry: pageRank.entrySet()) {
                double newPR = (1.0-d)/N;
                newPR = newPR + (d*sinkPR/N);
                for (String inLink: pageInlink.get(entry.getKey())) {
                    newPR = newPR + (d* pageRank.get(inLink)/(double) pageOutlink.get(inLink).size());
                }
                newPrMap.put(entry.getKey(), newPR);
            }
            pageRank.putAll(newPrMap);
            i++;
        }
        System.out.println("Total Iteration: "+ i);

        return pageRank;
    }

    //Previous iteration perplexity value
    double previous = 0;
    //current iteration perplexity value
    double current = 0;
    //Keep count of consecutive change of perplexity when it is less than 1
    int convergeCount = 0;
    //To skip the calculation of covergence value during the first iteration since there is no previous value
    int flag = 1;

    public Boolean converged(Map<String, Double> pageRank)
    {
        double shannon = 0;

        for (Double rank: pageRank.values()) {
            shannon +=  (rank * (Math.log(rank)/Math.log(2)));
        }
        shannon = -1 * shannon;
        double perplexity = Math.pow(2, shannon);
        System.out.println("perplexity: "+perplexity);
        current = perplexity;
        double diff = Math.abs(current-previous);

        if( diff < 1 && flag!=1)
        {

            if(flag==0) {
                convergeCount++;
            }
        }
        else
            convergeCount = 0;

        flag = 0;
        previous = current;

        if(convergeCount == 4)
            return true;
        else
            return false;
    }


    public  Map<String, Double>calIntialRank(double n, Map<String, Set<String>> pageInlink)
    {
        Map<String, Double> pageRank = new HashMap<String, Double>();

        for (String key: pageInlink.keySet()) {
            double initialPageRank = 1.0 /n;
            pageRank.put(key, Double.valueOf(initialPageRank));
        }
        return pageRank;
    }


    //builds sink link set
    public Set<String>getSinkLinkSet(Map<String, Set<String>> pageOutlink)
    {
        Set sinkLinks = new HashSet<String>();

        for (Map.Entry<String, Set<String>> entry: pageOutlink.entrySet()) {
            if(entry.getValue().size() == 0){
                sinkLinks.add(entry.getKey());
            }

        }
        return sinkLinks;
    }

    //build outLink Map using inlink Map
    public Map<String, Set<String>> buildOutlink(Map<String, Set<String>> pageInlink)
    {
        Map<String, Set<String>> pageOutlink = new HashMap<String, Set<String>>();
        for (Map.Entry<String, Set<String>> entry: pageInlink.entrySet()) {
            if(!pageOutlink.containsKey(entry.getKey()))
            {
                Set<String> links = new HashSet<String>();
                pageOutlink.put(entry.getKey(),links);
            }
            for (String s2 : entry.getValue()) {
                if(!pageOutlink.containsKey(s2))
                {
                    Set<String> links = new HashSet<String>();
                    links.add(entry.getKey());
                    pageOutlink.put(s2,links);
                }
                else
                {
                    pageOutlink.get(s2).add(entry.getKey());
                }
            }
        }
        return  pageOutlink;
    }


    public Map<String, Set<String>>getInLinkMap(String fileName) {
        String filePath = System.getProperty("user.dir") + "\\" + fileName;

        String line = null;
        Map<String , Set<String>> inlinkMap = new HashMap<String, Set<String>>();
        try {

            FileReader fileReader =
                    new FileReader(fileName);

            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                String[] links = line.split(" ");
                Set<String> s = new HashSet<String>();
                int i = 1;
                while (i < links.length){
                    s.add(links[i]);
                    i++;
                }
                inlinkMap.put(links[0], s);
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
        return inlinkMap;
    }

    public void generateInlinkCount(Map<String, Set<String>> pageInlink)
    {
        Map<String, Integer> pageInlinkCount = new HashMap<String, Integer>();

        for (Map.Entry<String, Set<String>> entry : pageInlink.entrySet()) {
            pageInlinkCount.put(entry.getKey(), entry.getValue().size());
        }

        List list=new LinkedList(pageInlinkCount.entrySet());
        Collections.sort(list,new Comparator(){
            public int compare(Object obj1, Object obj2){
                return ((Comparable)((Map.Entry)(obj1)).getValue

                        ()).compareTo(((Map.Entry)(obj2)).getValue());
            }
        });
        //arrange in descending order
        Collections.reverse(list);
        //Out put the page rank into a file
        File file = new File(System.getProperty("user.dir")+"/pageSortedByInlink.txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Object o : list) {
                bw.write(o.toString()+"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
