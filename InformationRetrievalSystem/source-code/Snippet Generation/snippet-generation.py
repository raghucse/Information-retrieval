# This file is used for genrating snippets.
# In the output, query terms are shown as RED and BOLD

# Generates the summary for a file 
def generate_summary(filename, query):
    f = open(filename, 'r+')
    s = f.read()
    s_lower = s.lower()
    new_s = s_lower.split("\n")
    snippets = {}
    for sentence in new_s:
        snippets[sentence] = get_sig_factor(sentence, query)
    count = 1
    summary = []
    for i in sorted(snippets, key=snippets.get, reverse=True):
        if count < 6:
            summary.append(str(i))
            count += 1
    return summary

# Calculates the Significant factor for the sentence
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

# Checks if term is present in query
def present_in_query(term, query):
    for i in query.split():
        if i == term:
            return True
    return False

# Generates the Snippet 
def generate_snippet(file_list, query):
    for filename in file_list:
        snippet = generate_summary(filename, query.lower())
        output = ""
        print "*****************************************************************************************"
        print filename
        for each in snippet:
            output += "..."
            for i in str(each).split():
                if present_in_query(i, query.lower()):
                    output += '\033[1m' + '\033[91m' + i + '\033[0m' + " "
                else:
                    output += i + " "
            output += "..."
            output += '\n'
        print output
        print "========================================================================================="

# Give a List of Files for which you want to generate snippet ad first argument
# Give Query as Second argument
generate_snippet(["CACM-3189.html", "CACM-3188.html", "CACM-3187.html" ], "FORTRAN Program")

