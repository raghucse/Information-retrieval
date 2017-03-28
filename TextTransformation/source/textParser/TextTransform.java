package com.neu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextTransform {
    static String dataStoreDir = System.getProperty("user.dir");
    static String documentDir = dataStoreDir + "/textDocuments";
    static String dirSeperator = "/";
    static  String contentId = "bodyContent";
    static  String contentId1 = "mw-content-text";
    static String fileExt = ".html";
    public static void main(String[] args) {
	// write your code here
        createDatstore();
        TextTransform i = new TextTransform();
        File folder = new File(System.getProperty("user.dir") + "/htmlDocuments");
        File[] listOfFiles = folder.listFiles();
        int count = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
         //       File input = new File("1.html");
                System.out.println("parsing "+file.getName());
                try {
                    Document doc = Jsoup.parse(file, "UTF-8");

                    Element content = doc.getElementById(contentId1);
                    for( Element element : content.select("ol.references") ){
                        element.remove();
                    }

                    storeDoc(i.textTransform(content.text()), file.getName().replaceFirst("[.][^.]+$", ""));
                    System.out.println();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("completed text parsing");

    }

    public static  void storeDoc(String finalDoc, String name) {
      // String finalDoc = doc.text();
        String file_name = documentDir + dirSeperator + name +".txt";
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


    //String dirPath
    //Tranform the text files present in the given directory path
    public static  String textTransform(String nonHtmlString)
    {
        //  String htmlString = "<html> 'hello' ? , - . = \"Raghu\"  \" 9\"9 \"b\",.9999b\"888-90'0\" <html>";
        //  System.out.println(htmlString);

        //removing html tags
        //   String nonHtmlString = Jsoup.parse(htmlString).text();

        //case folding
        String caseFolded = nonHtmlString.toLowerCase();

        //removing punctuations expecpt withing numbers
        if(caseFolded.matches("(.*)\\d[ : , \\- . \\\\ /  ]\\d(.*)")) {
            String split[] = caseFolded.split(" ");
            for (int i = 0; i < split.length; i++) {
                if (split[i].matches("(.*)\\d[ : , \\- . \\\\ /  ]\\d(.*)")) {
                    // System.out.println(split[i]);
                    split[i] = split[i].replaceAll("^(?!-)\\p{P}", " ");
                    // System.out.println(split[i]);
                    split[i] = split[i].replaceAll("(?!-)\\p{P}$", " ");
                    // System.out.println(split[i]);

                    char[] c = split[i].toCharArray();
                    String formatted = "";
                    for (int j = 0; j < c.length; j++) {
                        if (String.valueOf(c[j]).matches("(?!-)\\p{P}")) {

                            if ((j != 0 || j != c.length) && (String.valueOf(c[j - 1]).matches("[^\\d]") || String.valueOf(c[j + 1]).matches("[^\\d]"))) {
                                formatted = formatted + " ";
                            } else {
                                formatted = formatted + String.valueOf(c[j]);
                            }

                        } else {
                            formatted = formatted + String.valueOf(c[j]);
                        }
                    }
                    split[i] = formatted;
                } else {
                    split[i] = split[i].replaceAll("(?!-)\\p{P}", " ");
                }
            }
            String nonalphaPunc = "";
            for (String s : split) {
                //  System.out.println(s);
                if (!s.equals(""))
                    nonalphaPunc = nonalphaPunc.concat(s + " ");
            }
            caseFolded = nonalphaPunc;
        }
        else
        {
            caseFolded = caseFolded.replaceAll("(?!-)\\p{P}"," ");
        }
        return caseFolded.replaceAll("\\s+", " ").trim();
    }

    public static  void createDatstore() {
        File documents = new File(documentDir);
        if (documents.exists()) {
            documents.delete();
            documents.mkdir();
        } else {
            documents.mkdir();
         //   System.out.println(dataStoreDir);
        }
    }
}
