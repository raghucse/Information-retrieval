import os
import math
import re

# The code for tranforming both the query and the text files are provided.
# Make sure to run the query file and corpus through these programs before
# passing them here.

relevant = {}
StopList = []
corpus = {}
queries = []
index = {}
doc_len = {}
k1 = 1.2
k2 = 100
b = 0.75
R = 0.0
r = 0.0
uni_index = {}
Documents = {}
count = 0
# Location of the transformed corpus
path = "C:\\Users\\shashi\\Documents\\GitHub\\IRproject\\Common\\Transformed_Corpus"

# Generated the relevant documents list for each query
with open("cacm.rel") as f:
    for line in f:
        words = line.split()
        if words[0] not in relevant:
            relevant[words[0]] = [words[2]]
        else:
            relevant[words[0]].append(words[2])
f.close()


# Calculated the 'r'
def check(q, i):
    c = 0
    if str(i) in relevant:
        for each in relevant[str(i)]:
            f1 = open(path + "\\" + each+".txt", "r")
            x = f1.read().split()
            if q in x:
                c += 1
    else:
        c = 0
    return c


# Generated the snippet for given file and query
def generate_snippet(filename, query):
    f = open(path + '\\' + filename, 'r+')
    s = f.read()
    s_lower = s.lower()
    new_s = s_lower.split("\n")
    snippets = {}
    for sentence in new_s:
        snippets[sentence] = get_sig_factor(sentence, query)
    counter = 1
    summary = ""
    for i in sorted(snippets, key=snippets.get, reverse=True):
        if counter < 6:
            summary += " " + str(i)
            counter += 1
    return summary


# Calculates the significant factor for each sentence
def get_sig_factor(sentence, query):
    L = sentence.split()
    lo = 0
    hi = 0
    counter = 0.0
    for each in L:
        if present_in_query(each, query):
            lo = L.index(each)
            break
    for i in range(len(L) - 1, 0, -1):
        if present_in_query(L[i], query):
            hi = i
            break
    for j in L[lo: (hi + 1)]:
        if present_in_query(j, query):
            counter += 1
    if len(L[lo: (hi + 1)]) == 0:
        sig_factor = 0.0
    else:
        sig_factor = (counter * counter) / len(L[lo: (hi + 1)])

    return sig_factor


# Checks if given word is present in query
def present_in_query(term, query):
    for i in query.split():
        if i == term:
            return True
    return False


# Generates the Stop List for given corpus
def get_stop_list():
    with open("common_words") as f:
        for line in f:
            new_line = line.strip("\n")
            StopList.append(new_line)
    f.close()


# Generates the new Query, Q'
def query_expansion(top_doc_list, query):
    I = {}
    for filename in top_doc_list:
        f = open(path + '\\' + filename, 'r+')
        s = generate_snippet(filename, query)
        new_s = s.split()
        # generate tokens
        for i in new_s:
            if i not in I:
                I[i] = 1
            else:
                I[i] += 1
        f.close()
    # Calculate top 30 terms
    terms = []
    count = 0
    for i in sorted(I, key=I.get, reverse=True):
        if str(i).lower() not in StopList and count < 31:
            terms.append(i)
            count += 1

    return terms


# Generates the HashMap with using the Corpus
def generate_corpus(dirs):
    for filename in dirs:
        f = open(path + "\\" + filename, "r")
        s = ''.join(f.readlines())
        text = s.split()
        doc_id = filename
        corpus[doc_id] = text


# Calculates the score for a term using BM25
def BM25(n, f, qf, N, dl, avdl, r , R):
    K = k1 * ((1 - b) + b * (float(dl) / float(avdl)))
    x = math.log(((r + 0.5) / (R - r + 0.5)) / ((n - r + 0.5) / (N - n - R + r + 0.5)))
    y = (((k1 + 1) * f) / (K + f)) * (((k2 + 1) * qf) / (k2 + qf))
    return x * y


# Calculates the term frequency in a query
def get_qf(term, query):
    qf = 1
    for i in query:
        if i == term:
            qf += 1
    return qf


# Generates the scores for Documents using BM25
def score_docs(query, table, qid):
    query_result = {}
    avdl = average_length(table)
    N = len(table)
    if str(qid) in relevant:
        R = len(relevant[str(qid)])
    else:
        R = 0
    for term in query:
        r = check(term, qid)
        if term in uni_index:
            doc_dict = uni_index[term]  # retrieve index entry
            qf = get_qf(term, query)
            for doc_id, freq in doc_dict.iteritems():
                score = 0.0
                score = BM25(len(doc_dict), freq, qf, N, table[doc_id], avdl, r, R)
                if doc_id in query_result:
                    query_result[doc_id] += score
                else:
                    query_result[doc_id] = score
    return query_result


# Calculates the Average Document length in the Corpus
def average_length(table):
    docs_len = 0.0
    for length in table:
        docs_len += table[length]
    return float(docs_len) / float(len(table))


# Calculates the Top Ranked Documents using BM25
def get_top_docs(q, qid):
    table = {}
    q_list = q.split()
    for each in corpus:
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
    top_doc = {}
    top_doc = score_docs(q_list, table, str(qid))
    return top_doc


# Returns the text between two terms (<DOCNO> ... </DOCNO>)
def find_between(s, first, last):
    try:
        start = s.index(first) + len(first)
        end = s.index(last, start)
        return s[start:end]
    except ValueError:
        return ""


# Generates HashMap for Queries with Qid as Key
def get_queries(filename):
    queries1 = {}
    count = 0
    with open(filename) as f:
        items = f.read().split("</DOC>")
    for i in items:
        if "<DOC>" in i:
            items[count] = i[i.find("<DOC>") + len("<DOC>"):]
        count += 1
    for i in items:
        key = find_between(i, "<DOCNO> ", " </DOCNO>")
        j = re.sub(r'<DOCNO>.*</DOCNO>', "", i)
        val = j.replace("\n", " ")
        queries1[key] = val

    return queries1


# Main Funtion
def main():
    query_list = get_queries("cacm.query.transform")
    dirs = os.listdir(path)
    get_stop_list() # Generates Stop List
    generate_corpus(dirs) # Generates the HashMap for Corpus
    f1 = open('QueryExpansion-BM25.txt', 'w')
    for qid in range(1, len(query_list)):
        doc_list = []
        q1 = ""
        q = str(query_list[str(qid)])
        for i in q.split():
            q1 += " " + str(i)
        doc_list = get_top_docs(q1, str(qid))

        # Top 50 documents for given query, Q
        new_doc_list = sorted(doc_list, key=doc_list.get, reverse=True)[:50]

        # Generates Q' out of Top 50 Documents
        new_query = query_expansion(new_doc_list, q1)
        new_q = ""
        for i in new_query:
            new_q += " " + str(i)

        # Top Ranked Docs for  Q + Q'
        results = get_top_docs(new_q + " " + q1, str(qid))
        count = 0
        # Sort and display the Top 100 results
        for i in sorted(results, key=results.get, reverse=True)[:100]:
            count += 1
            DocID = str(i)
            Score = str(results[i])
            f1.write(str(qid) + " Q0 " + str(DocID)[:-4] + " " + str(count) + " " \
                  + str(Score) + " QueryExpansion" + "\n")
    f1.close()

main()
