import json
import sys
import tweepy
import os
import re
import urllib
from bs4 import BeautifulSoup
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
        self.num_tweets = 0 # Number tracker of tweets for this run... Just for sanity checking
        self.save_name = ''
        self.tweets = []
        self.file_num = 0
        # Try to pick up where we left off, if we've run this before...
        # Check for tweets dir in the current directory
        if os.path.isdir('./tweets'):
            # Directory exists, find last tweets file
            dir_list = os.listdir('./tweets')
            sorted(dir_list)
            # Change this to a string so we can regex it
            dir_string = "".join([str(i) for i in dir_list])
            # Grab for the LAST match, as this should be the largest numerically
            s = re.search("tweets[0-9]+\.json$", dir_string)
            # If s is empty, we haven't found a match, likely a case of either no file or just the first 'tweets.json'.
            if s :
                # We have a match. Grab the current numerical value of the item, update vars
                self.file_num = int(re.search("[0-9]+", s.group()).group())
                self.save_name = './tweets/tweets' + str(self.file_num) + '.json'
            else:
                # Else default
                self.save_name = './tweets/tweets.json'

        else:
            # Directory does not exist. Create directory and first file.
            os.mkdir('tweets')
            self.save_name = './tweets/tweets.json'

        # Open the file as append so we don't have to re-write the array each time
        # Also so we don't have to load to prevent losing tweets from picking up the script a day later
        self.save_file = open(self.save_name, 'a')

    def verify_save(self):
        # Check to see if file is over 10MB (10,000,000 Bytes)
        if os.stat(self.save_name).st_size >= 10000000:
            # Close current file
            self.save_file.close()
            # Increment file number, create new file name, open new file
            self.file_num += 1
            self.save_name = './tweets/tweets' + str(self.file_num) + '.json'
            self.save_file = open(self.save_name, 'a')
        # Else, continue with current file.

    def on_data(self,tweet):
        # Empty array so we're not appending the same item over and over
        self.tweets = []
        self.verify_save()
        self.num_tweets += 1
        print("Got tweet number " + str(self.num_tweets) + " appending to: " + self.save_name) #Debug statement, just so we can see it's doing something while running...

        decoded = json.loads(tweet)
        for url in decoded["entities"]["urls"]:
            x = url["expanded_url"]
            #print(x)
            response = urllib.request.urlopen(x)
            html = response.read()
            soup = BeautifulSoup(html,'html.parser')
            # title = soup.html.head.title
            title = soup.title.string

            #print(x + "--" + str(title))
            print(title)

        return True


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
    myStream.filter(locations=[-180,-90,180,90])


main()
