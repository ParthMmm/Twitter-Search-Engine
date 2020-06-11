// package edu.ucr.cs.cfran015.lucenesearcher;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
// public class LuceneSearcherApplication {

// 	public static void main(String[] args) {
// 		SpringApplication.run(LuceneSearcherApplication.class, args);
// 	}

// }

package edu.ucr.cs.cfran015.lucenesearcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// mine
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

class Tweet{
    public int id;
    public String user;
    public String title;
    public String date;
    public String body;
    public String location;

    Tweet(int id, String u, String t, String d, String txt){
        this.id = id;
        this.user = u;
        this.title = t;
        this.date = d;
        this.body = txt;
        //this.location = l;
    }

    @Override
    public String toString() {
        return String.format("Tweet[id=%d, title=%s, body=%s, user=%s, date=%s, location=%s]", id, title, body, user, date, location);
    }
}
              

@SpringBootApplication
@RestController
public class LuceneSearcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(LuceneSearcherApplication.class, args);
	}

	// // Original
	// @GetMapping("/hello")
	// public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
	// 	return String.format("Hello %s!", name);
	// }

	@GetMapping("/tweets")
	public List<Tweet> tweets(@RequestParam(value = "query", defaultValue = "car") String queryIn) {
		//return String.format("Looking for %s", query);

        //String results = "";

        List<Tweet> tweets = new ArrayList<Tweet>();

        try {

        Analyzer analyzer = new StandardAnalyzer();

        String dir = "/Users/cristianfranco/Documents/index";
        Directory indexDir = FSDirectory.open(Paths.get(dir));

        // Now search the index:
        DirectoryReader indexReader = DirectoryReader.open(indexDir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // Tweet newTweet = new Tweet(user,title,date,text,location);
        String[] fields = {"user", "title", "date", "text"};
        // , "location"
        Map<String, Float> boosts = new HashMap<>();
        // boosts.put(fields[0], 1.0f);
        // boosts.put(fields[1], 0.5f);
        // weights
        boosts.put(fields[0], 0.25f);
        boosts.put(fields[1], 0.25f);
        boosts.put(fields[2], 0.1f);
        boosts.put(fields[3], 0.4f);
        //boosts.put(fields[4], 0.2f);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
        Query query = parser.parse(queryIn);
        //Query query = parser.parse("UCR");
        // Query query = parser.parse("UCR discussion");
        // QueryParser parser = new QueryParser("content", analyzer);
        // Query query = parser.parse("(title:ucr)^1.0 (content:ucr)^0.5");
        System.out.println(query.toString());
        int topHitCount = 10;
        ScoreDoc[] hits = indexSearcher.search(query, topHitCount).scoreDocs;

        // Iterate through the results:
        for (int rank = 0; rank < hits.length; ++rank) {
            Document hitDoc = indexSearcher.doc(hits[rank].doc);

            Tweet tempTweet = new Tweet( (rank + 1), hitDoc.get("user"), hitDoc.get("title"),
                hitDoc.get("date"), hitDoc.get("text") );

            
            
            //hitDoc.get("location")

            tweets.add(tempTweet);



            //tweets += "Tweet[id=" + tempTweet.id + ", title=" + tempTweet.title + ", body=" + tempTweet.body + ", user=" + tempTweet.user + ", date=" + tempTweet.date + "]";
            // + ", location=" + tempTweet.location + 
            //System.out.println(tweets);
            // results += (rank + 1) + " (score:" + hits[rank].score + ") --> " +
            //                    hitDoc.get("user") + " - " + hitDoc.get("title") + " - "
            //                    + hitDoc.get("date") + " - " + hitDoc.get("text") + " - "
            //                    + hitDoc.get("location");
        }
        indexReader.close();
        indexDir.close();

        //return String.format(results);

        return tweets;
        
        } catch(Exception e){
                System.out.println(e);
            }


    //return String.format(results);
    return tweets;

	}

}