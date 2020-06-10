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
	public String tweets(@RequestParam(value = "query", defaultValue = "car") String queryIn) {
		//return String.format("Looking for %s", query);

        String results = "";

        try {

        Analyzer analyzer = new StandardAnalyzer();

        String dir = "/Users/cristianfranco/Documents/index";
        Directory indexDir = FSDirectory.open(Paths.get(dir));

        // Now search the index:
        DirectoryReader indexReader = DirectoryReader.open(indexDir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // Tweet newTweet = new Tweet(user,title,date,text,location);
        String[] fields = {"user", "title", "date", "text", "location"};
        Map<String, Float> boosts = new HashMap<>();
        // boosts.put(fields[0], 1.0f);
        // boosts.put(fields[1], 0.5f);
        // weights
        boosts.put(fields[0], 0.2f);
        boosts.put(fields[1], 0.2f);
        boosts.put(fields[2], 0.1f);
        boosts.put(fields[3], 0.3f);
        boosts.put(fields[4], 0.2f);
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
            // System.out.println((rank + 1) + " (score:" + hits[rank].score + ") --> " +
            //                    hitDoc.get("title") + " - " + hitDoc.get("content"));

            // System.out.println((rank + 1) + " (score:" + hits[rank].score + ") --> " +
            //                    hitDoc.get("user") + " - " + hitDoc.get("title") + " - "
            //                    + hitDoc.get("date") + " - " + hitDoc.get("text") + " - "
            //                    + hitDoc.get("location"));
            results += (rank + 1) + " (score:" + hits[rank].score + ") --> " +
                               hitDoc.get("user") + " - " + hitDoc.get("title") + " - "
                               + hitDoc.get("date") + " - " + hitDoc.get("text") + " - "
                               + hitDoc.get("location");

            // System.out.println(indexSearcher.explain(query, hits[rank].doc));
        }
        indexReader.close();
        indexDir.close();

        return String.format(results);
        
        } catch(Exception e){
                System.out.println(e);
            }


    return String.format(results);

	}

}