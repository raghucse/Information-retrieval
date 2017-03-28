import math
from query_parse import *
import os

I = {}  # Inverted Index
count = 1
Sysname = "TF-IDF-SRA"
corpus = {}
I = {}
# Location of the transformed corpus
p = "C:\\Users\\shashi\\Documents\\GitHub\\IRproject\\Common\\Transformed_Corpus"
dirs = os.listdir(p)

# The program requires 2 things :
# 1. The query file which is transformed
# 2. Location of the corpus (text documents), which are transformed

# The transformed query file is passed through query_parse which removes the
# tags "<DOC>", "</DOC>", "<DOCNO>", "</DOCNO>" and generates a dictionary of
# queries with QueryID as key and the query as value

# The code for tranforming both the query and the text files are provided.
# Make sure to run the query file and corpus through these programs before
# passing them here.


# Parse the directory to generate the corpus
def corpus_parse(dirts):
    for file in dirts:
        f = open(p + "\\" + file, "r")
        s = ''.join(f.readlines())
        text = s.split()
        docid = file
        corpus[docid] = text
    return corpus

# Get query from file after transformation
queries = get_query()

#Obtain corpus
corpus = corpus_parse(dirs)

#Open a file to write the results
f = open("TF-IDF-Output.txt", "w+")

# Generate the unigram index
for each in corpus:
    for term in corpus[each]:
        if term in I:
            if each in I[term]:
                I[term][each] += 1
            else:
                I[term][each] = 1
        else:
            d = {}
            d[each] = 1
            I[term] = d


for qid in range(1, len(queries)):
    query = queries[str(qid)].split()
    wordcount = {}

    for i in query:
        if i not in wordcount:
            wordcount[i] = 1
        else:
            wordcount[i] += 1

    Docs = []
    DocList = {}
    DocScore = {}
    N = len(corpus)

    # Score documents
    for i in wordcount:
        DocList = {}
        if i in I:
            DocList = I[i]
            #calculate idf for each term
            idf = math.log(N, 10) - math.log(len(DocList), 10)
            for j in DocList:
                Dtf = (1.0 + math.log(DocList[j], 10)) * idf
                if j not in DocScore:
                    DocScore[j] = Dtf
                else:
                    DocScore[j] = DocScore[j] + Dtf

    FinalDocScore = DocScore

    # Write the result in a sorted order to a file
    count = 0
    for i in sorted(FinalDocScore, key=FinalDocScore.get, reverse=True)[:100]:
        count += 1
        DocID = i
        Tf_Idf_Score = FinalDocScore[i]
        f.write(str(qid) + " Q0 " + str(DocID)[:-4] + " " + str(count) + " " \
          + str(Tf_Idf_Score) + " " + Sysname + "\n")
f.close()