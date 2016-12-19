-------------------------------------------PRE-REQUISITES--------------------------------------------------------------------------
Required software in system:
java development kit
version preferred: 1.8.0_101

Required libraries:
lucene-analyzers-common-4.7.2.jar
lucene-core-4.7.2.jar
lucene-queryparser-4.7.2.jar

The libraries is provided along with source code.

Note: create a folder for the index file to be placed and provide it while running the program
-----------------------------------------------CODE--------------------------------------------------------------------------------
                                  ::::::::::::Lucene:::::::::::::::
						
Purpose: Indexing and retreival model are implemented using lucene libraries

source file name: HW3.java

Execution steps:
----------------

step-1: launch command promt.
step-2: change current directory to source file directory 
step-3: To compile the code please use following command
		javac -d . -cp ./lucene-analyzers-common-4.7.2.jar;lucene-core-4.7.2.jar;lucene-queryparser-4.7.2.jar;. HW3.java
step-4: To run the program use following command 
		java -cp ./lucene-analyzers-common-4.7.2.jar;lucene-core-4.7.2.jar;lucene-queryparser-4.7.2.jar;. com.neu.HW3
step-5: follow the instructions on the screen and provide requested paths
		
Note: source file and lucene-analyzers-common-4.7.2.jar,lucene-core-4.7.2.jar,lucene-queryparser-4.7.2.jar
      should be present in the same directory.

Validation steps:
-----------------
Once the execution is complete the following file is generated in the same directory as source code,
scoreDocLucene.txt
----------------------------------------------------------------------------------------------------------------------------------