import json
import sys
import tweepy
import os
import re
import urllib
from bs4 import BeautifulSoup

def main():
    f = open('./tweets/tweets.json')
    ff = open("test.txt", "w")
    tweets = []
    for line in open('./tweets/tweets.json', 'r'):
        tweets.append(json.loads(line))

    #print(tweets)
    for tweet in tweets:
            for url in tweet["entities"]["urls"]:
                x = url["expanded_url"]
                #print(x)
                response = urllib.request.urlopen(x)
                html = response.read()
                soup = BeautifulSoup(html,'html.parser')
                title = soup.html.head.title

                # print(x + "--" + str(title.contents))
                # print(tweet+str(title.contents))
                newTweet = str(tweet) + str(title.contents) + "test111"
                ff.write(newTweet)

                # print (" - found URL: %s" % url["expanded_url"])
    ff.close()
main()
