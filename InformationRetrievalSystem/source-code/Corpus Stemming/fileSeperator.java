package com.neu;

import java.io.*;

// Reads a file and seperates it into seperate files
public class fileSeperator {
    static String dataStoreDir = System.getProperty("user.dir");
    static String documentDir = dataStoreDir + "/textDocuments";
    static String dirSeperator = "/";
    public static void main(String[] args) {
        // write your code here

        fileSeperator i = new fileSeperator();
        File file = new File(System.getProperty("user.dir") + "\\cacm_stem.txt");
        int count = 0;

                //       File input = new File("1.html");
                System.out.println("parsing "+file.getName());

                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file));
                    String line = null;

                    /////////////////////////////////////////////////////////////////////
                    FileWriter fw = null;
                    BufferedWriter bw =  null;

                    //////////////////////////////////////////////////////////

                    while ((line = br.readLine()) != null) {
                        if(line.contains("#"))
                        {
                            if(null != bw)
                            {
                                bw.close();
                            }
                            String fname =  line.replace("# ", "CACM-") + ".txt";
                            String file_name = documentDir + dirSeperator + fname;
                            File file2 = new File(file_name);
                            if (!file2.exists()) {
                                try {
                                    file2.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                fw = new FileWriter(file2.getAbsoluteFile());
                                bw = new BufferedWriter(fw);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            bw.write(line+"\n");
                        }

                    }

                    br.close();
                    bw.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


        System.out.println("completed text parsing");

    }
}
