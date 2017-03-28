package com.neu;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BfsCrawler{
    long endTime = 0;
    long startTime = 0;
    long politenessTime = 1000; // expresed in milliseconds
    String url;
    Document doc = null;
    String dataStoreDir = System.getProperty("user.dir") + "/task1";
    String documentDir = dataStoreDir + "/documents";
    String seedUrl;
    String contentId = "bodyContent";
    String anchorTag = "a";
    String hrefTag = "href";
    String wikiPrefix = "https://en.wikipedia.org";
    String startWithWiki = "/wiki";
    String adminLink = ":";
    String dirSeperator = "/";
    String fileExt = ".html";
    String lnkFile = "links.txt";
    String section = "#";
    int depthLimt = 5;
    Node currentNode;
    Queue<Node> frontierQueue = new LinkedList<Node>();
    List<String> visitedList = new LinkedList<String>();
    int crawlLimit = 1000;


    public static void main(String[] args) {
        // write your code here
        System.out.println("Given input Seed:"+args[0]);
        new BfsCrawler().crawler(args[0]);
    }

    public void crawler(String s) {
        seedUrl =s;
        createDatstore();

        Node seedNode = new Node(seedUrl, 1); // initialise current depth and seed url
        frontierQueue.add(seedNode);
        while (((frontierQueue.size() + visitedList.size()) != crawlLimit) && !frontierQueue.isEmpty()) {
            currentNode = frontierQueue.remove();
            if(currentNode.level==depthLimt)
                break;
            url = currentNode.link;
            getDoc();
            System.out.println("crawling:"+url+" depth:"+currentNode.level);
            visitedList.add(url);
            storeDoc(doc, url, visitedList.size());

            crawl();
        }
        while(!frontierQueue.isEmpty()) {

            Node n = frontierQueue.remove();
            url = n.link;
            if(!visitedList.contains(url)) {
                System.out.println("crawling:"+url+" depth:"+n.level);
                visitedList.add(url);
                getDoc();
                storeDoc(doc, url, visitedList.size());
            }
        }
        storeLinksToFile();
    }

    //stores links to a file
    public void storeLinksToFile()
    {
        String linkFile = dataStoreDir + dirSeperator + lnkFile;
        System.out.println("Storing links to a text file:"+linkFile);
        System.out.println("Total Links crawled: "+visitedList.size());
        File file = new File(linkFile);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (String link : visitedList) {
                bw.write(link+"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //crawls the current link
    public void crawl()
    {
        Element content = doc.getElementById(contentId);
        //removes the references section from the page
        for( Element element : content.select("ol.references") ){
            element.remove();
        }
        Elements links = content.getElementsByTag(anchorTag);
        int nextDepth = (currentNode.level)+1;
        for (Element link : links) {
            if ((frontierQueue.size() + visitedList.size()) != crawlLimit) {
                String linkHref = link.attr(hrefTag);
                if (!(linkHref.contains(adminLink)) && (linkHref.startsWith(startWithWiki))) {
                    String t = wikiPrefix + linkHref;
                    if (t.contains(section)) {
                        String[] ss = t.split(section);
                        t = ss[0];
                    }
                    Node linkNode = new Node(t, nextDepth);
                    if (!(visitedList.contains(t)) && !(frontierQueue.contains(linkNode))) {
                        frontierQueue.add(linkNode);
                    }
                }
            }
        }
    }

    //procedure to store crawled documents
    public void storeDoc(Document doc, String url, int fileCount) {
        String finalDoc = url.concat("\n" + doc.outerHtml());
        String file_name = documentDir + dirSeperator + fileCount + fileExt;
        File file = new File(file_name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(finalDoc);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //connect and get the url
    public void getDoc() {
        try {
            endTime = System.currentTimeMillis();
            long timeElapsed = endTime - startTime;
            if (startTime != 0 && timeElapsed < politenessTime) {
                try {
                //    System.out.println(timeElapsed);
                    Thread.sleep(politenessTime-timeElapsed);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            doc = Jsoup.connect(url).get();
            startTime = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    //Create directories to store link text and downloaded documents
    public void createDatstore() {
        File dataStore = new File(dataStoreDir);
        File documents = new File(documentDir);
        if (dataStore.exists()) {
            dataStore.delete();
            dataStore.mkdir();
            documents.mkdir();
        } else {
            dataStore.mkdir();
            documents.mkdir();
            System.out.println(dataStoreDir);
        }
    }
}

//Node to store link and depth level
class Node {
    public String link;
    public int level;

    Node(String link, int level) {
        this.level = level;
        this.link = link;
    }

    Node(Node nd) {
        level = nd.level;
        link = nd.link;
    }

    public boolean equals(Object o) {
        Node n = (Node) (o);
        if (n.link.equals(this.link)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "level:" + level + "  link:" + link;
    }
}