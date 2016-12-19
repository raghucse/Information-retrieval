package com.neu;

import java.io.*;

public class TextTransform {
    static String dataStoreDir = System.getProperty("user.dir");
    static String documentDir = dataStoreDir + "/textDocuments";
    static String dirSeperator = "/";
    public static void main(String[] args) {
	// write your code here
        createDatstore();
        TextTransform i = new TextTransform();
        File folder = new File(System.getProperty("user.dir") + "/cacm");
        File[] listOfFiles = folder.listFiles();
        int count = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
         //       File input = new File("1.html");
                System.out.println("parsing "+file.getName());

                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file));
                    String line = null;

                    /////////////////////////////////////////////////////////////////////
                    String s[] = file.getName().split("-");
                    String num = s[1].replace(".html","");
                    int inNum = Integer.parseInt(num);
                    String fname = s[0]+"-"+inNum+".txt";
                    String file_name = documentDir + dirSeperator + fname;
                    File file2 = new File(file_name);
                    if (!file2.exists()) {
                        try {
                            file2.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileWriter fw = null;
                    BufferedWriter bw =  null;
                    try {
                        fw = new FileWriter(file2.getAbsoluteFile());
                        bw = new BufferedWriter(fw);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //////////////////////////////////////////////////////////

                while ((line = br.readLine()) != null) {
                    String nonhtmlLine = line.replaceAll("\\<[^>]*>","");
                    String nonPuncline = i.textTransform(nonhtmlLine);
                    bw.write(nonPuncline+"\n");
                }

                br.close();
                    bw.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("completed text parsing");

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
