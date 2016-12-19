-------------------------------------------PRE-REQUISITES--------------------------------------------------------------------------
Required software in system:
java development kit
version preferred: 1.8.0_101

Note: Create a folder textDocuments in the same directory as source file where results are stored
	  Place cacm_stem.txt in the same directory as source file
-----------------------------------------------CODE--------------------------------------------------------------------------------
                                  ::::::::::::FileSeperator:::::::::::::::
						
Purpose: Indexing and retreival model are implemented using lucene libraries

source file name: fileSeperator.java

Execution steps:
----------------
step-1: copy all the documents to be parsed into htmlDocuments directory
step-2: launch command promt.
step-3: change current directory to source file directory 
step-4: To compile the code please use following command
		javac -d . fileSeperator.java
step-5: To run the program use following command 
		java -cp . com.neu.fileSeperator
		
Validation steps:
-----------------
Once the execution is complete,
textDocuments folder will have seperated files.
-----------------------------------------------------------------------------------------------------------------------------------