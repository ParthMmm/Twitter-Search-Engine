import json
import sys
import tweepy
from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from tweepy import Stream

access_token = "68000072-BRutoxqWNKTynhgHyXOFeMstjcOpr63OxOAIqyC5D"
access_token_secret = "QarYrrGDe4lpbw8DjF6VtnPk6SwBo3MxhzEx4vsUYWNqE"
consumer_key = "Or56hDGO11K90lFNgZ8WqbFee"
consumer_secret = "XEb1ldIMffIDFVALmF1eDUEjb9QzQK2XbnxjXj4jwdfuoHt4OX"

class Crawler(tweepy.StreamListener):

    def __init__(self):
        super(StreamListener, self).__init__()
        self.save_file = open('tweets.json','w')
        self.tweets = []

    def on_data(self,tweet):
        self.tweets.append(json.loads(tweet))
        self.save_file.write(str(tweet))

    # def on_status(self, status):
    #     print(status.text)

    def on_error(self,status):
        if status == 420:
            #returning False in on_error disconnects the stream
            return False

def auth():
    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    api = tweepy.API(auth)
    return auth

def main():
    x = auth()
    myCrawler = Crawler()
    myStream = tweepy.Stream(x,listener = myCrawler)
    myStream.filter(track=['python'])
main()
