package com.neu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class dfsCrawler {
    int flag =0;
    long endTime = 0;
    long startTime = 0;
    long politenessTime = 1000; //expressed in milliseconds
    String url;
    Document doc=null;
    String dataStoreDir =  System.getProperty("user.dir")+"/task2_B";
    String documentDir = dataStoreDir+"/documents";
    Stack<Node> frontierStack = new Stack<Node>();
    List<String> visitedList = new LinkedList<String>();
    String seedUrl = "https://en.wikipedia.org/wiki/Sustainable_energy";
    String contentId = "bodyContent";
    String anchorTag = "a";
    String hrefTag = "href";
    String wikiPrefix = "https://en.wikipedia.org";
    String startWithWiki = "/wiki";
    String adminLink = ":";
    String dirSeperator = "/";
    String fileExt = ".html";
    static String lnkFile = "links.txt";
    int depthLimt = 5;
    Node currentNode;
    int crawlLimit = 1000;
    String focusToken;

    public static void main(String[] args) {
        System.out.println("Given input Seed:"+args[0]+" Focus word: "+args[1]);
        new dfsCrawler().crawler(args[0],args[1]);
    }


    public void crawler(String s,String f){
        seedUrl =s;
        focusToken = f;
        createDatstore();
        Node seedNode = new Node(seedUrl,1);
        frontierStack.push(seedNode);
        while (!frontierStack.isEmpty() && (visitedList.size() != crawlLimit)) {
            currentNode = frontierStack.pop();
            url = currentNode.link;
            if(visitedList.contains(url))
            {
                continue;
            }
            System.out.println("crawling:"+url+" depth:"+currentNode.level);
            getDoc();
            visitedList.add(url);
            storeDoc(doc, url, visitedList.size());
            crawl();
        }
        storeLinksToFile();
    }

    //crawl the links 
    public void crawl()
    {
        if(!(currentNode.level == depthLimt)  && (visitedList.size()!= crawlLimit)) {
            Element content = doc.getElementById(contentId);
            for( Element element : content.select("ol.references") )
            {
                element.remove();
            }
            Elements links = content.getElementsByTag(anchorTag);
            int nextDepth = (currentNode.level)+1;
            Stack<Node> tempStack = new Stack<Node>();
            for (Element link : links) {
                String linkHref = link.attr(hrefTag);
                if (!(linkHref.contains(adminLink)) && (linkHref.startsWith(startWithWiki)) ) {
                    if ((link.ownText().toLowerCase().contains(focusToken) || linkHref.toLowerCase().contains(focusToken))) {
                        String t = wikiPrefix + linkHref;
                        if (t.contains("#")) {
                            String[] ss = t.split("#");
                            t = ss[0];
                        }
                        Node linkNode = new Node(t, nextDepth);
                        if (!(visitedList.contains(t)) && !(tempStack.contains(linkNode) ) ) {
                            tempStack.push(linkNode);
                        }
                    }
                }
            }
            while(!tempStack.isEmpty()) { //store the links int the order they appear in the page
                frontierStack.push(tempStack.pop());
            }
        }
    }


    //Store all the links to a text file
    public void storeLinksToFile() {
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

    //procedure to store crawled documents
    public void storeDoc(Document doc, String url, int fileCount) {
        String finalDoc = url.concat("\n"+doc.html());
        doc.html();
        String file_name = documentDir+dirSeperator+fileCount+fileExt;
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

    //send request to get the document
    public void getDoc() {
        try {
            endTime = System.currentTimeMillis();
            long timeElapsed = endTime-startTime;
            if(startTime != 0 && timeElapsed  < politenessTime) {
                try {
               //     System.out.println(timeElapsed);
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

    //Create data store to store link text and documents
    public void createDatstore() {
        File dataStore = new File(dataStoreDir);
        File documents =  new File(documentDir);
        if(dataStore.exists()) {
            dataStore.delete();
            dataStore.mkdir();
            documents.mkdir();
        }
        else {
            dataStore.mkdir();
            documents.mkdir();
            System.out.println(dataStoreDir);
        }
    }
}

class Node{
    public String link;
    public int level;
    Node(String link, int level) {
        this.level = level;
        this.link = link;
    }

    Node(Node nd){
        level = nd.level;
        link = nd.link;
    }


    public boolean equals(Object o){
        Node n = (Node)(o);
        if (n.link.equals(this.link))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString() {
        return "level:"+level+"  link:"+link;
    }
}
