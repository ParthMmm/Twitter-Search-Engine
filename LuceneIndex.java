
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

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;


class Tweet{
    public String user;
    public String title;
    public String date;
    public String text;
    public String location;

    Tweet(String u, String t, String d, String txt, String l){
        this.user = u;
        this.title = t;
        this.date = d;
        this.text = txt;
        this.location = l;
    }
}

public class LuceneIndex {

    public static void main(String[] args) throws IOException, ParseException {

        Path currentDir = Paths.get(".");
        System.out.println(currentDir.toAbsolutePath());

        File file = new File("/Users/parthmangrola/Documents/School/Spring20/indexer/src/main/java/tweets.json");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        ArrayList<JSONObject> contentsAsJsonObjects = new ArrayList<JSONObject>();

        int count = 0;
        for (String line; (line = reader.readLine()) != null;) {
            count++;
            contentsAsJsonObjects.add(new JSONObject(line));

        }
        String location ="";
        String title = "";

        for (JSONObject json : contentsAsJsonObjects) {
            try{
                String text = json.getString("text");
                String name = json.getJSONObject("user").getString("name");
                String date = json.getString("created_at");
//            if(json.getJSONObject("place").getJSONObject("bounding_box").getString("coordinates[0]") != null){
//                 location = json.getJSONObject("place").getJSONObject("bounding_box").getString("coordinates[0]");
//            }
                if(json.getString("title") != null){
                    title = json.getString("title");
                }
                System.out.println(title);

            }catch(Exception e){
//                System.out.println(e);
            }

        }

    }
}
