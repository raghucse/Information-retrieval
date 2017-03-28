INFORMATION RETRIEVAL - COURSE PROJECT

All the tasks in this project are implemented in Python, version 2.7

However, implementation of Lucene along with a few supporting functionalities have been implemented using Java 8.
**********************************************************************************************************************************

Requirements : java development kit, preferred version 1.8.0_101
To Install Java :

Download Java 8 from 
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

**********************************************************************************************************************************
 --  "HW3.java"
 Found in the folder "Task 1"
 
  -> Description
  
    * A Java implementation of lucene retrieval system.
	* Requires the following jar files for successfully run 
	    -- lucene-analyzers-common-4.7.2.jar
		-- lucene-core-4.7.2.jar
		-- lucene-queryparser-4.7.2.jar
		
For detailed steps of execution, refer to the Readme provided along with the source file.

-------------------------------------------------------------------------------------
		
The following are the .java files that are used to process the input for the actual tasks

 --  "QueryTextTransform.java"
 Found in the folder "Query Transformation"
 
 -> Description
 
   * This file is used to parse the contents of the given query file and transform it into a suitable format.
   * Takes a file that contains queries and returns a file with the queries transformed just as the corpus.
 
 For detailed steps of execution, refer to the Readme provided along with the source file.
 
-------------------------------------------------------------------------------------
 --  "FileSeperator.java"
 Found in the folder "Corpus Stemming"
 
 -> Description
 
   * Purpose of this function is to convert the cacm_stem.txt file into a seperate text file with the corresponding text content.
   * The stemmed corpus contains all the files along with their names in a single text file.
	
	Eg:  DocName
		 .....
		 .....
		 .....
		 DocName
		 .....
		 .....
		 
   * Our implementation requires seperate text file for document.
   * This code will generate seperate text files for each document with the DocName as the file name and the
     corresponding text content as the file content.
 
 For detailed steps of execution, refer to the Readme provided along with the source file.
 
-------------------------------------------------------------------------------------										
 -- "TextTransform.java"
 Found in the folder "Corpus Transformation"
 
 -> Description
 
   * This file is used to parse the raw HTML documents provided and convert them to text file of required format.
 
 A detailed explanantion on the rules for text tranformation and execution of the code is provided in a Readme file along with the source file
 
 **************************************************************************************************************************************************

The following tasks have been successfully implemented using Python 2.7

The link to download python 2.7 
https://www.python.org/

  -- "bm25.py"
  Found inside folder called "Task 1"

 -> Description  
	
	* This file contains an implementation of the BM25 ranking algorithm.
	* The file requires 2 files to successfully run, one containing the queries and the other being the corpus.
	* The final score for a document is the summation over all the query terms in the query.
	
	Requirements : "cacm.query.transform" and corpus

 -> Execution 

	Step 1: Open command prompt and go to the location where the source file is located.
	Step 2: Run the code using 
		    python <filename>.py
			
-------------------------------------------------------------------------------------
  -- "CosineSimilarity.py"
  Found inside folder called "Task 1"
  
  -> Description  
	
	* This is an implementation of the vector space model.
	* A similarity measure is computed to rank the documents.
	* Fetches queries from a file and computes cosine score for the corresponding documents and displays
	  top 100 documents for each query.
	  
    Requirements : "cacm.query.transform" and corpus
	
 -> Execution 

	Step 1: Open command prompt and go to the location where the source file is located.
	Step 2: Run the code using 
		    python <filename>.py
			
-------------------------------------------------------------------------------------
  -- "tf-idf.py"
  Found inside folder called "Task 1"
  
  -> Description  
	
	* This is an implementation of the classic Term Frequency - Inverse Document Frequency score computation.
	* Computes score for each document for a particular query. 
    * The top ranking documents are assumed to be most relevant to the query. 
	* Fetches queries from a file and computes cosine score for the corresponding documents and displays
	  top 100 documents for each query.
	  
    Requirements : "cacm.query.transform" and corpus
	
 -> Execution 

	Step 1: Open command prompt and go to the location where the source file is located.
	Step 2: Run the code using 
		    python <filename>.py
			
-------------------------------------------------------------------------------------						
  -- "query_parse.py"
  Found inside folder called "Task 1"
  
  -> Description  
	
	* A helper file that parses the file containing query file.
	* The original query file contains tags i.e "<DOC>" "<DOCNO>" "</DOCNO>" </DOC>"
	* This file removes the above mentioned tags but retains the contents between them.
	* Output of this file will be list of queries used by the search engine.
	  
    Requirements : Original Query file with the tags. "cacm.query"
	
 -> Execution 

	Step 1: Open command prompt and go to the location where the source file is located.
	Step 2: Run the code using 
		    python <filename>.py
			
-------------------------------------------------------------------------------------
  -- "Task-2.py" - For Query Expansion
  Found inside folder called "Task 2"
  
  -> Description  
	
	* Implementation of query expansion using pseudo relevance feedback.
	* We have used BM25 algorithm implemented in the previous task.
	* Ranks the top 100 documents obtained after query expansion for each query.
	  
    Requirements : Query file after tranformation ("cacm.query.transform"), "cacm.rel" and "common_words" 
	
 -> Execution 

	Step 1: Open command prompt and go to the location where the source file is located.
	Step 2: Run the code using 
		    python <filename>.py
			
-------------------------------------------------------------------------------------
  -- "BM25-Stemmed-Version.py" - For Task 3
  Found inside folder called "Task 3"
  
  -> Description  
	
	* Implementation of BM25 ranking algorithm but with different corpus and query set.
	* Both the corpus and the query are stemmed.
	* The stemmed query set and the stemmed version of the corpus is provided in the files. 
	  
    Requirements : Stemmed query file "cacm_stem.query", list of relevant documents "cacm.rel" 
				   and list of stop words "common_words" 
	
 -> Execution 

	Step 1: Open command prompt and go to the location where the source file is located.
	Step 2: Run the code using 
		    python <filename>.py
			
-------------------------------------------------------------------------------------
  -- "BM25-Stopping.py" - For Task 3
  Found inside folder called "Task 3"
  
  -> Description  
	
	* Implementation of BM25 ranking algorithm with use of a stoplist.
	* A list of most common words are provided.
	* This list will be used as the stop list. Thus any word in the query which is a stop word is removed.
	  
    Requirements : list of relevant documents "cacm.rel", list of commonly occuring words "common_words",
				   "query_parse.py" and file with tranformed queries "cacm.query.transform".	
	
 -> Execution 

	Step 1: Open command prompt and go to the location where the source file is located.
	Step 2: Run the code using 
		    python <filename>.py

-------------------------------------------------------------------------------------
-- "eval.py" - For MAP, MRR, Recall, Precision, P@5, 20
  Found inside folder called "Task 3"
  
  -> Description  
	
	* This implementation geneartes MRR, MAP, Recall, Precision, P@5, 20 for given search engine result.
	
 -> Execution 

	Step 1: Open command prompt and go to the location where the source file is located.
	Step 2: Run the code using 
		    python <filename>.py
			
-------------------------------------------------------------------------------------
  -- "tfidf-stoplist.py"
  Found inside folder called "Phase2 A"
  
  -> Description  
	
	* Implementation of tf-idf ranking algorithm with use of a stoplist.
	* A list of most common words are provided.
	* This list will be used as the stop list. Thus any word in the query which is a stop word is removed.
	  
    Requirements : list of commonly occuring words "common_words", "query_parse.py" 
	               and file with tranformed queries "cacm.query.transform".	
	
 -> Execution 

	Step 1: Open command prompt and go to the location where the source file is located.
	Step 2: Run the code using 
		    python <filename>.py

			
-------------------------------------------------------------------------------------
-- "snippet-generation.py"
  Found inside folder called "Snippet Generation"
  
  -> Description  
	
	* This implementation geneartes a snippet for a particular file in the corpus for a given query.
	
 -> Execution 

	Step 1: Open command prompt and go to the location where the source file is located.
	Step 2: Run the code using 
		    python <filename>.py
			
*******************************************************************************************************************************************
FILE LISTS

Source Code Files
	Task 1 Lucene	 	- HW3.java
	Task 1 BM25		 	- bm25.py
	Task 1 TF-IDF	 	- tf-idf.py
	Task 1 CosineSimi 	- CosineSimilarity.py
	Task 2 				- Task-2.py
	Task 3
		Stopping		- BM25-Stopping.py
		Stemming		- BM25-Stemmed-Version.py
	Phase 2				
		TFIDF Stopping	- tfidf-stoplist.py
		MRR,MAP			- eval.py

Tables for 7 runs		- Evaluation.xlsx
		
Top Ranked List for all runs
	Task 1 Lucene	 	- scoreDocLucene.txt
	Task 1 BM25		 	- BM25-Output.txt
	Task 1 TF-IDF	 	- TF-IDF-Output.txt
	Task 1 CosineSimi 	- CosineSimilarity-Output.txt
	Task 2 				- QueryExpansion-BM25.txt
	Task 3
		Stopping		- BM25-Stopping-Output.txt
		Stemming		- BM25-STEM-Output.txt
	Phase 2				
		TFIDF Stopping	- TF-IDF-Stopping-Output.txt
	
For Task-1 and Task-2, query file  - cacm.query.transform
For Task-3, 
	Stopping query file 		 -	cacm.query.transform
	Stemming query file 		 - cacm_stem.query




