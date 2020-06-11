#!/bin/bash

pwd

# Index the tweets...
(cd lucene-indexer && javac -cp \* LuceneIndex.java)
(cd lucene-indexer && java -cp .:* LuceneIndex)

# Prepare the front end...
(cd lucene-searcher-front-end && yarn)

# Serve the back end
(cd lucene-searcher && ./mvnw spring-boot:run)
