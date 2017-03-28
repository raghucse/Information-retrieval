
-------------PRE-REQUISITES-------------------------------------------------------------------------------------------------------------------------
Required software in system:
java development kit
version preferred: 1.8.0_101

Required libraries:
jsoup-1.9.2.jar

The library is provided along with source code.

It can also be downloaded from the following URL:
https://jsoup.org/packages/jsoup-1.9.2.jar

Note: Downloaded document saving is enabled for the tree tasks.
If you wish to disable it comment the following line:

Before:
storeDoc(doc, url, visitedList.size());

After:
storeDoc(doc, url, visitedList.size());

--------------TASK1---------------------------------------------------------------------------------------------------------------------------------
Purpose: To crawl the documents using seed url

Description: The crawler crawls until 1000 unique links are crawled using the given seed url as starting point.
Maximum depth crawler can crawl is 5. The crawler has politeness policy of one second.

source file name: BfsCrawler.java

Execution steps:
----------------
step-1: launch command promt.
step-2: change current directory to source file directory 
step-3: To compile the code please use following command
		javac -d . -cp jsoup-1.9.2.jar BfsCrawler.java
step-4: To run the program use following command 
		java -cp ./jsoup-1.9.2.jar;. com.neu.BfsCrawler https://en.wikipedia.org/wiki/Sustainable_energy

Note:
Argument to the com.neu.BfsCrawler is seed url
source file and jsoup-1.9.2.jar should be present in the same directory.

Validation steps:
-----------------
Once the execution is complete,
task1 folder will be generated under current directory.
./task1/links.txt contains the links crawled
./task1/documents contains the downloaded documents


--------------TASK2A---------------------------------------------------------------------------------------------------------------------------------
Purpose : To focus crawl the documents using BFS algorithm and focus word "solar" 

Description: The crawler crawls at most 1000 unique links are crawled using the given seed url as starting point.
Maximum depth crawler can crawl is 5. The crawler has politeness policy of one second.

source file name: BfsCrawlerFocused.java

Execution steps:
----------------
step-1: launch command promt.
step-2: change current directory to source file directory 
step-3: To compile the code please use following command
		javac -d . -cp ./jsoup-1.9.2.jar BfsCrawlerFocused.java
step-4: To run the program use following command 
		java -cp ./jsoup-1.9.2.jar;. BfsCrawlerFocused https://en.wikipedia.org/wiki/Sustainable_energy solar

Note:
Argument to the BfsCrawlerFocused is seed url and focused word
source file and jsoup-1.9.2.jar should be present in the same directory.

Validation steps:
-----------------
Once the execution is complete,
task2_A folder will be generated under current directory.
./task2_A/links.txt contains the links crawled
./task2_A/documents contains the downloaded documents


--------------TASK2B---------------------------------------------------------------------------------------------------------------------------------
Purpose : To focus crawl the documents using DFS algorithm and focus word "solar" 

Description: The crawler crawls at most 1000 unique links are crawled using the given seed url as starting point.
Maximum depth crawler can crawl is 5. The crawler has politeness policy of one second.

source file name: dfsCrawler.java

Execution steps:
----------------
step-1: launch command promt.
step-2: change current directory to source file directory 
step-3: To compile the code please use following command
		javac -d . -cp ./jsoup-1.9.2.jar dfsCrawler.java
step-4: To run the program use following command 
		java -cp ./jsoup-1.9.2.jar;. com.neu.dfsCrawler https://en.wikipedia.org/wiki/Sustainable_energy solar

Note:
Argument to the dfsCrawler is seed url and focused word
source file and jsoup-1.9.2.jar should be present in the same directory.

Validation steps:
-----------------
Once the execution is complete,
task2_A folder will be generated under current directory.
./task2_A/links.txt contains the links crawled
./task2_A/documents contains the downloaded documents


--------------TASK3---------------------------------------------------------------------------------------------------------------------------------
Purpose : To crawl the documents

Description: The crawler crawls until 1000 unique links are crawled using the given seed url as starting point.
Maximum depth crawler can crawl is 5. The crawler has politeness policy of one second.

source file name: BfsCrawler.java

Execution steps:
----------------
step-1: launch command promt.
step-2: change current directory to source file directory 
step-3: To compile the code please use following command
		javac -d . -cp jsoup-1.9.2.jar BfsCrawler.java
step-4: To run the program use following command 
		java -cp ./jsoup-1.9.2.jar;. com.neu.BfsCrawler https://en.wikipedia.org/wiki/Solar_power

Note:
Argument to the com.neu.BfsCrawler is seed url
source file and jsoup-1.9.2.jar should be present in the same directory.

Validation steps:
-----------------
Once the execution is complete,
task1 folder will be generated under current directory.
./task1/links.txt contains the links crawled
./task1/documents contains the downloaded documents
 
