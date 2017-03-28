import re
queries = {}

myhtmlFile = open('cacm.query.transform')  # Query File to be parsed
myhtml = myhtmlFile.read()
items = myhtml.split("</DOC>")   # Split based on "</DOC>"
queries = {}
count = 0

# Extract content between "<DOC>" and "</DOC>"
for i in items:
    if "<DOC>" in i:
        items[count] =i [i.find("<DOC>")+len("<DOC>") :]
    count = count + 1;
count2 = 0

# Extract content between "<DOCNO>" and "</DOCNO>"
def find_between( s, first, last ):
    try:
        start = s.index( first ) + len( first )
        end = s.index( last, start )
        return s[start:end]
    except ValueError:
        return ""

# Create a dict with QueryID as key and Query as value
for i in items:
    key = find_between(i, "<DOCNO> ", " </DOCNO>")
    j = re.sub( r'<DOCNO>.*</DOCNO>', "", i)
    val = j.replace("\n", " ")
    queries[key] = val

# Return the create Query dictionary
def get_query():
    return queries