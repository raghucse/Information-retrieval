-------------------------------------------PRE-REQUISITES--------------------------------------------------------------------------
Required software in system:
java development kit
version preferred: 1.8.0_101

Note : Create directory cacm in the folder same as source file and place the cacm.query in it
-----------------------------------------------CODE--------------------------------------------------------------------------------
                        ::::::::::::Query TRANSFORMER:::::::::::::::
						
Purpose: To Parse and text transform the given query file

Description: we need to transform the given query files before using them as the corpus is text transformed

source file name: QueryTextTransform.java

Execution steps:
----------------
step-1: copy cacm.query into folder ./cacm
step-2: launch command promt.
step-3: change current directory to source file directory 
step-4: To compile the code please use following command
		javac -d . QueryTextTransform.java
step-5: To run the program use following command 
		java -cp . com.neu.QueryTextTransform

Validation steps:
-----------------
Once the execution is complete,
textDocuments folder will be generated under current directory.
./textDocuments/* contains all the parsed query file
--------------------------------------------------------------------------------------------------------------------------------------