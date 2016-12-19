import math
from query_parse import *
import os

# The program requires 2 things :
# 1. The query file which is transformed
# 2. Location of the corpus (text documents), which are transformed

# The transformed query file is passed through query_parse which removes the
# tags "<DOC>", "</DOC>", "<DOCNO>", "</DOCNO>" and generates a dictionary of
# queries with QueryID as key and the query as value

# The code for tranforming both the query and the text files are provided.
# Make sure to run the query file and corpus through these programs before
# passing them here.

I = {}  # Inverted Index
count = 1
corpus = {}
# Give the path of the transformed corpus location
p = "C:\\Users\\shashi\\Documents\\GitHub\\IRproject\\Common\\Transformed_Corpus"
dirs = os.listdir(p)

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

# Create a unigram index for the given corpus
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


f = open("CosineSimilarity-Output.txt", "w+")


for qid in range(1, len(queries)):
    query = queries[str(qid)].split()
    wordcount = {}

    # Calculates the frequency of the term in
    # the given query
    for i in query:
        if i not in wordcount:
            wordcount[i] = 1
        else:
            wordcount[i] += 1

    Docs = []
    DocList = {}
    DocScore = {}

    # Calculate the Document weights
    for i in wordcount:
        DocList = {}
        if i in I:
            DocList = I[i]
            for j in corpus:
                if j in DocList:
                    Dtf = 1.0 + math.log(DocList[j], 10)
                    if j not in DocScore:
                        DocScore[j] = {i: Dtf}
                    else:
                        DocScore[j].update({i: Dtf})
                else:
                    if j not in DocScore:
                        DocScore[j] = {i: 0.0}

    for i in DocScore:
        for j in wordcount:
            if j not in DocScore[i]:
                DocScore[i].update({j: 0.0})

    NormDocScore = DocScore
    # Normalise the Doc weights
    for i in DocScore:
        den_sum = 0.0
        for k in wordcount:
            den_sum += math.pow(DocScore[i][k], 2)
        for j in wordcount:
            numerator = DocScore[i][j]
            if numerator > 0.0:
                NormDocScore[i][j] = numerator / math.sqrt(den_sum)
            else:
                NormDocScore[i][j] = 0.0


    Querylist = {}

    # Calculate the Query weights
    for i in wordcount:
        if i in I:
            Qlist = I[i]
            Qidf = math.log(len(corpus), 10) - math.log(len(Qlist), 10)
            Wq = (1.0 + math.log(wordcount[i], 10)) * Qidf
            if i not in Querylist:
                Querylist[i] = Wq

    den_sum = 0.0

    NormQuerylist = Querylist

    for j in Querylist:
        den_sum += math.pow(Querylist[j], 2)
    # Normalise the Doc weights
    for i in Querylist:
        numerator = Querylist[i]
        if numerator > 0.0:
            NormQuerylist[i] = numerator / math.sqrt(den_sum)
        else:
            NormQuerylist[i] = 0.0

    totalscore = 0.0
    FinalDocScore = {}

    # Calculate the final Doc Score
    for i in DocScore:
        for j in NormQuerylist:
            totalscore += NormDocScore[i][j] * NormQuerylist[j]
        FinalDocScore[i] = totalscore
        totalscore = 0.0

    count = 0

    # Sort and display the Top 100 results
    for i in sorted(FinalDocScore, key=FinalDocScore.get, reverse=True)[:100]:
        count += 1
        DocID = i
        CosineScore = FinalDocScore[i]
        f.write(str(qid) + " Q0 " + str(DocID)[:-4] + " " + str(count) + " " \
              + str(CosineScore) + " CosineSimilarity-SRA " + "\n")

f.close()
