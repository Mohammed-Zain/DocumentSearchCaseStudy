# DocumentSearchCaseStudy
Simple document search

To run this application, use IntelliJ. Set up a run configuration for an Application with the
base class as DocumentSearch.java. Run the application and a window will popup and a search text
can be entered. After that, pick from the drop down which type of search to be done. Finally
click on Search and the results are printed onto the text area below.


To answer some of the questions on the original case study assignment:

I attempted to run a performance test and to me it seems like the indexed search will take longer
if the processing time to make the Map of values to indices is included. If the text is already
converted into a Map and the only thing left is to look up the text to be viewed, then it is
faster than the string match. String match must go through the entire file and search all
words/phrases, but the indexed search will end as soon as it finds a match.


To handle massive content, we would need to make indices and pre-process the content before any
searches can be attempted on it. A database with a large capacity and a lot of physical memory
will be needed to hold all the processed content. One thing to think about is also that if there
is so much data processed, then a lot of results will be returned so there has to be a way of
filtering out the garbage results or less relevant results. If there is a way to hide or avoid
some content when searching for certain things, that could help performance.

To handle a very large request volume, the server running the requests must be able to run
multiple searches at the same time at the extremely high rate. To do this, it can distribute the
requests over several servers/machines to optimize the time to handle a request and not wait on
other requests when a limit is reached on a machine/server. 

