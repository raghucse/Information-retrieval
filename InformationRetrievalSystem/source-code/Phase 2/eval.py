import os

path = os.getcwd()

relevant = {}

with open("cacm.rel") as f:
    for line in f:
        words = line.split()
        if words[0] not in relevant:
            relevant[words[0]] = [words[2]]
        else:
            relevant[words[0]].append(words[2])
f.close()

bm25 = {}

# Give the output filename of Rank list
# Example
#  BM25-Output.txt
#  CosineSimilarity-Output.txt
#  TF-IDF-Output.txt
#  scoreDocLucene.txt
#  QueryExpansion-BM25.txt
#  TF-IDF-Stopping-Output.txt
#  BM25-Stopping-Output.txt
with open("BM25-Stopping-Output.txt") as f:
    for line in f:
        words = line.split()
        if words[0] not in bm25:
            bm25[words[0]] = {words[3]: words[2]}
        else:
            bm25[words[0]].update({words[3]: words[2]})

bm25_rel = {}

for q_id in bm25:
    rank_list = bm25[q_id]
    for each in rank_list:
        if q_id in relevant:
            if rank_list[each] in relevant[q_id]:
                if q_id not in bm25_rel:
                    bm25_rel[q_id] = {each: rank_list[each]}
                else:
                    bm25_rel[q_id].update({each: rank_list[each]})

precision = {}
recall = {}
for each in relevant:
    if each in bm25_rel:
        precision[each] = float(len(bm25_rel[each])) / 100.0
        recall[each] = float(len(bm25_rel[each])) / float(len(relevant[each]))


avprecision = {}

for each in relevant:
    if each in bm25_rel:
        i = bm25_rel[each]
        si = i.keys()
        int_lst = []
        for k in si:
            int_lst.append(int(k))
        int_lst.sort()
        count = 0
        avp = 0.0
        for j in int_lst:
            count += 1
            avp += float(count) / float(j)
        avprecision[each] = avp / float(len(i))

reciporcalRank = {}
for each in relevant:
    if each in bm25_rel:
        i = bm25_rel[each]
        si = i.keys()
        int_lst = []
        for k in si:
            int_lst.append(int(k))
        int_lst.sort()
        rankFirstRel = int_lst[0]
        rr = float(1) / float(rankFirstRel)
        reciporcalRank[each] = rr

# precision at k = 5 and k =20
precisionAt5 = {}
precisionAt20 = {}

for each in relevant:
    if each in bm25_rel:
        i = bm25_rel[each]
        si = i.keys()
        int_lst = []
        for k in si:
            int_lst.append(int(k))
        int_lst.sort()
        int_lst2 = int_lst
        count5 = 0
        count20 = 0

        for i in int_lst:
            if i <= 5:
                count5 += 1
            if i <= 20:
                count20 += 1
            else:
                break

        pAt5 = float(count5) / 5.0
        pAt20 = float(count20) / 20.0

        precisionAt5[each] = pAt5
        precisionAt20[each] = pAt20


def print_result(dict):
    for i in range(1, 65):
        if str(i) in dict:
            print str(i).ljust(10, ' ') + " " + str(dict[str(i)])
        else:
            print str(i).ljust(10, ' ') + " " + "0.0"
print "==============================================="
print "Reciprocal Rank"
print "==============================================="
print "Query-ID".ljust(10, ' ') + "RR"
print_result(reciporcalRank)
print "==============================================="
print "Precision"
print "==============================================="
print "Query-ID".ljust(10, ' ') + "Precision"
print_result(precision)
print "==============================================="
print "Recall"
print "==============================================="
print "Query-ID".ljust(10, ' ') + "Recall"
print_result(recall)
print "==============================================="
print "Average Precision"
print "==============================================="
print "Query-ID".ljust(10, ' ') + "Avg Precision"
print_result(avprecision)
print "==============================================="
print "Precision at 5"
print "==============================================="
print_result(precisionAt5)
print "==============================================="
print "Precision at 20"
print "==============================================="
print_result(precisionAt20)
print "==============================================="
# Mean average precision calculation
# ----------------------------------
mvp = 0.0
for i in avprecision:
    mvp = mvp + avprecision[i]

mvp /= float(len(avprecision))
print "Mean Average Precision : " +  str(mvp)
# -----------------------------------
print "==============================================="
# Mean reciprocal rank
mrr = 0.0
for i in reciporcalRank:
    mrr += reciporcalRank[i]

mrr /= float(len(reciporcalRank))
print "Mean Reciprocal Rank : " + str(mrr)
print "==============================================="

# ---------------------------------------
