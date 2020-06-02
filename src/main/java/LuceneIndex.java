
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
        Analyzer analyzer = new StandardAnalyzer();
        String dir = "/Users/parthmangrola/Documents/index";
        Directory indexDir = FSDirectory.open(Paths.get(dir));
        IndexWriterConfig luceneConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(indexDir, luceneConfig);

        Path currentDir = Paths.get(".");
        File folder = new File("/Users/parthmangrola/Documents/tweets/");
        File[] files = folder.listFiles();
        for (File file: files != null ? files : new File[0])
        {
            System.out.println(file);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            ArrayList<JSONObject> contentsAsJsonObjects = new ArrayList<JSONObject>();

            int count = 0;
            try{
                for (String line; (line = reader.readLine()) != null;) {
                    count++;
                    contentsAsJsonObjects.add(new JSONObject(line));
                }
            }catch(Exception e){
                System.out.println(e);
            }

            String location ="";
            String title = "";

            for (JSONObject json : contentsAsJsonObjects) {
                try{
                    String text = json.getString("text");
                    String user = json.getJSONObject("user").getString("name");
                    String date = json.getString("created_at");
//            if(json.getJSONObject("place").getJSONObject("bounding_box").getString("coordinates[0]") != null){
//                 location = json.getJSONObject("place").getJSONObject("bounding_box").getString("coordinates[0]");
//            }
                    if(json.getString("title") != null){
                        title = json.getString("title");
                    }
                    Tweet newTweet = new Tweet(user,title,date,text,location);
                    Document newDoc = createDocument(newTweet);
                    indexWriter.addDocument(newDoc);

                }catch(Exception e){
//                System.out.println(e);
                }

            }
        }
        indexWriter.close();


    }
    public static Document createDocument(Tweet tweet){
        Document doc = new Document();
        doc.add(new TextField("user", tweet.user, Field.Store.YES));
        doc.add(new TextField("title", tweet.title, Field.Store.YES));
        doc.add(new TextField("date", tweet.date, Field.Store.YES));
        doc.add(new TextField("text", tweet.text, Field.Store.YES));
        doc.add(new TextField("location", tweet.location, Field.Store.YES));
        return doc;
    }
}
