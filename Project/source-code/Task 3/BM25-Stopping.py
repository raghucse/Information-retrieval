import os
import math
from query_parse import *

# The program requires 2 things :
# 1. The query file which is transformed
# 2. Location of the corpus (text documents), which is transformed

# The transformed query file is passed through query_parse which removes the
# tags "<DOC>", "</DOC>", "<DOCNO>", "</DOCNO>" and generates a dictionary of
# queries with QueryID as key and the query as value

# The code for tranforming both the query and the text files are provided.
# Make sure to run the query file and corpus through these programs before
# passing them here.

SysName = "BM25-Stopping-SRA"
# Location of the transformed corpus
p = "C:\\Users\\shashi\\Documents\\GitHub\\IRproject\\Common\\Transformed_Corpus"
dirs = os.listdir(p)
corpus = {}
stoplist = []
relevant = {}
queries = []
index = {}
doc_len = {}
uni_index = {}
Documents = {}
k1 = 1.2
k2 = 100
b = 0.75
count = 0

# Writes the result obtained into a file after sorting
def print_results(dict):
    top_docs = {}
    f = open("BM25-Stopping-Output.txt", "w+")
    for each in dict:
        ID = each
        top_docs = dict[each]
        rank = 1
        for i in sorted(top_docs, key=top_docs.get, reverse=True)[:100]:
            term = i
            val = top_docs[i]
            f.write(str(ID) + " " + " Q0 " + str(term)[:-4] + " " + str(rank) + " " + str(val) + " " + SysName+ "\n")
            rank += 1
    f.close()

# Parses the corpus in the given directory and generates a dictionary with filename as the key
# and the textual content as the value
def corpus_parse(dirts):
    for file in dirts:
        f = open(p + "\\" + file, "r")
        s = ''.join(f.readlines())
        text = s.split()
        docid = file
        corpus[docid] = text
    return corpus

# BM25 Score computation
# Accepts few parameters and few other values are declared as constants such as k1, k2 , R and r
def BM25(n, f, qf, r, R, N, dl, avdl):
    K = k1 * ((1 - b) + b * (float(dl) / float(avdl)))
    x = math.log(((r + 0.5) / (R - r + 0.5)) / ((n - r + 0.5) / (N - n - R + r + 0.5)))
    y = (((k1 + 1) * f) / (K + f)) * (((k2 + 1) * qf) / (k2 + qf))
    return x * y

# Provides the frequency of the given term in the given query
def get_qf(term, query):
    qf = 1
    for i in query:
        if i == term:
            qf += 1
    return qf

# Creates a dict with the BM25 score for each document in the corpus
def score_doc(query, table, qid):
    query_result = {}
    avdl = average_length(table)
    N = len(table)
    # R value is the total number of relevant documents for a query
    if str(qid) in relevant:
        R = len(relevant[str(qid)])
    else:
        R = 0
    for term in query:
        # r is the number of documents in which the particular term of the query appears
        r = check(term, qid)
        if term in uni_index:
            doc_dict = uni_index[term]
            qf = get_qf(term, query)
            for docid, freq in doc_dict.iteritems():
                score = BM25(n=len(doc_dict), f=freq, qf=qf, r= r, R=R, N=N,
                                   dl=table[docid], avdl=avdl)
                if docid in query_result:
                    query_result[docid] += score
                else:
                    query_result[docid] = score
    return query_result

# Generates the average length for the given corpus
def average_length(table):
    sum = 0.0
    for length in table:
        sum += table[length]
    return float(sum) / float(len(table))

# Generate a dict with Query ID and its corresponding list of relevant documents
with open("cacm.rel") as f:
    for line in f:
        words = line.split()
        if words[0] not in relevant:
            relevant[words[0]] = [words[2]]
        else:
            relevant[words[0]].append(words[2])
f.close()

# Check if the given term is present in the list of relevant documents for that query
def check(q, i):
    c = 0
    if str(i) in relevant:
        for each in relevant[str(i)]:
            f1 = open(p + "\\" + each+".txt", "r")
            x = f1.read().split()
            if q in x:
                c += 1
    else:
        c = 0
    return c


# Create stoplist from the given file
f1 = open("common_words", "r")
for i in f1.readlines():
    stoplist.append(i.strip("\n"))

# Main Function
# Starts execution here
def main():
    table = {}
    corpus = corpus_parse(dirs) # Create the corpus
    queries = get_query()       # Obtain the queries
    for each in corpus:         # Generate the unigram index
        for term in corpus[each]:
            if term in uni_index:
                if each in uni_index[term]:
                    uni_index[term][each] += 1
                else:
                    uni_index[term][each] = 1
            else:
                d = {}
                d[each] = 1
                uni_index[term] = d
        length = len(corpus[str(each)])
        table[each] = length
    temp = {}

    # Remove stop words from queries before processing
    for qid in range(1, len(queries)): # BM25 scoring for each of the queries
        q = queries[str(qid)].split()
        query = []
        for i in q:
            if i not in stoplist:
                query.append(i)
        # Score the documents based on the queries
        temp[qid] = (score_doc(query, table, qid))
    print_results(temp)

main()