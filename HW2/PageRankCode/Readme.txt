-------------PRE-REQUISITES-------------------------------------------------------------------------------------------------------------------------
Required software in system:
java development kit
version preferred: 1.8.0_101

--------------TASK2---------------------------------------------------------------------------------------------------------------------------------
Purpose: To page rank the pages given as graph

Description: The program implements the page rank algorithm.

source file name: PageRank.java

Execution steps:
----------------
step-1: launch command prompt.
step-2: change current directory to source file directory 
step-3: To compile the code please use following command
		javac -d . PageRank.java
step-4: To run the program use following command 
		java -cp . PageRank <graphfile>

Note:
Argument to the PageRank is a grpah file and should be placed in same directory as the java file.
Ignore the java unchecked operation warning.

Validation steps:
-----------------
Once the execution is complete,
PageRankCode folder will be generated under current directory.
./pageRankSorted.txt contains page rank sorted 
./pageSortedByInlink.txt contains inlink count of pages
------------------------------------------------------------------------------------------------------------------------------------------------------