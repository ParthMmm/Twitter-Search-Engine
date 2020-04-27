import json
import sys
import tweepy
from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from tweepy import Stream

access_token = "68000072-craaf71WltY1B5HiE0qdYVtEeVEWUlrh3qlt4nJZM"
access_token_secret = "AzktEKG0OuhckEM5XFxjMvsqeRgLMX7V33hJNQASoVS32"
consumer_key = "3BGeBF0z0SxRFuio1lotM2jtW"
consumer_secret = "nTJQKWcxhM9S4QRyPDsRk8y0UAxYv0oRYXu3Nc4rCXnutsfY1K"

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

    api = tweepy.API(auth,wait_on_rate_limit=True)
    return auth

def main():
    x = auth()
    myCrawler = Crawler()
    myStream = tweepy.Stream(x,listener = myCrawler)
    myStream.filter(track=['python'])
main()
