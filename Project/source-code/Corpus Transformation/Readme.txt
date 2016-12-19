-------------------------------------------PRE-REQUISITES--------------------------------------------------------------------------
Required software in system:
java development kit
version preferred: 1.8.0_101

Note : A directory with name cacm should be created in the directory same as the source code to make documents available
      for the program

-----------------------------------------------CODE--------------------------------------------------------------------------------
                        ::::::::::::TEXT TRANSFORMER:::::::::::::::
						
Purpose: To Parse the html documents and covert them to text using specified rules

Description: we need to transform the html documents before into text files having only necessary

Design choice: From HTML documents, only the article content is taken.
               The HTML tags are removed from the document.
               The punctuations except hyphen has been removed from the text.
               The punctuations : , - . \ / are retained between the digits
               The final document is saved in the text file with the name similar to a webpage.
               If there is a clash in name number is appended at the end to differentiate them.

source file name: TextTransform.java

Execution steps:
----------------
step-1: copy all the documents to be parsed into htmlDocuments directory
step-2: launch command promt.
step-3: change current directory to source file directory 
step-4: To compile the code please use following command
		javac -d . -cp jsoup-1.9.2.jar TextTransform.java
step-5: To run the program use following command 
		java -cp ./jsoup-1.9.2.jar;. com.neu.TextTransform

Validation steps:
-----------------
Once the execution is complete,
textDocuments folder will be generated under current directory.
./textDocuments/* contains all the parsed documents
--------------------------------------------------------------------------------------------------------------------------------------