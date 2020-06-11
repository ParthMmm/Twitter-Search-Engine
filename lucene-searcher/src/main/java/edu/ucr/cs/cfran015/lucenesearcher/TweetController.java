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

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TweetController {

@GetMapping("/tweets")
    public List<Tweet> searchTweets(@RequestParam(value = "query", defaultValue = "car") String queryIn) {

        List<Tweet> tweets = new ArrayList<Tweet>();

        try {

        Analyzer analyzer = new StandardAnalyzer();

        String dir = "/Users/cristianfranco/Documents/index";
        Directory indexDir = FSDirectory.open(Paths.get(dir));

        // Now search the index:
        DirectoryReader indexReader = DirectoryReader.open(indexDir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        String[] fields = {"user", "title", "date", "body", "location"};
        Map<String, Float> boosts = new HashMap<>();
        // weights
        boosts.put(fields[0], 0.2f);
        boosts.put(fields[1], 0.2f);
        boosts.put(fields[2], 0.1f);
        boosts.put(fields[3], 0.3f);
        boosts.put(fields[4], 0.2f);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
        Query query = parser.parse(queryIn);
        System.out.println(query.toString());
        int topHitCount = 10;
        ScoreDoc[] hits = indexSearcher.search(query, topHitCount).scoreDocs;

        // Iterate through the results:
        for (int rank = 0; rank < hits.length; ++rank) {
            Document hitDoc = indexSearcher.doc(hits[rank].doc);

            Tweet tempTweet = new Tweet( (rank + 1), hitDoc.get("user"), hitDoc.get("title"),
                hitDoc.get("date"), hitDoc.get("text"), hitDoc.get("location") );


            tweets.add(tempTweet);
        }
        indexReader.close();
        indexDir.close();

        return tweets;
        
        } catch(Exception e){
                System.out.println(e);
            }

    System.out.println("EMPTY");
    return tweets;

    }
}
