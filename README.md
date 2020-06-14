# Twitter-Search-Engine

Using tweepy, a Python library for Twitter's api, we collect tweets based in the US. The tweets are collected in a JSON file that are 10mb each and we only collect 2GB of data. Additionally, if a user posts a URL in the tweet the crawler gets the title of the URL. Then, using Lucene the tweets are indexed. 
The fields that are indexed include user, title, date, text, and location. With spring-boot running as a back-end and Angular for the front-end, we can then search with certain queries and the appropriate tweets will be returned.
