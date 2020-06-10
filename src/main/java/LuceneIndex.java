
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
    public String followers;

    Tweet(String u, String t, String d, String txt, String l, String f){
        this.user = u;
        this.title = t;
        this.date = d;
        this.text = txt;
        this.location = l;
        this.followers = f;
    }
}

public class LuceneIndex {

    public static void main(String[] args) throws IOException, ParseException {

        // FIX dir to work without having to manually change path
        Analyzer analyzer = new StandardAnalyzer();
        //String dir = "/Users/parthmangrola/Documents/index";
        String dir = "/Users/cristianfranco/Documents/index";
        Directory indexDir = FSDirectory.open(Paths.get(dir));
        IndexWriterConfig luceneConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(indexDir, luceneConfig);

        //Folder of tweets
        //File folder = new File("/Users/parthmangrola/documents/tweets");
        File folder = new File("/Users/cristianfranco/Documents/tweets");
        //Path currentDir = Paths.get(".");
        //File folder = new File("/Users/parthmangrola/documents/twe");
        File[] files = folder.listFiles();
        
        int errors = 0;
        int tcount = 0;

        //Iterate through each file
        for (File file: files != null ? files : new File[0]) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            JSONArray arr = new JSONArray();
            try{
                for (String line; (line = reader.readLine()) != null;) {

                    if(!line.trim().equals("")){
                        arr.put(new JSONObject(line.trim()));

                    }
                    
                }
            }catch(Exception e){
                System.out.println("87" + e);
                errors += 1;
            }


            String location ="";
            String title = "";

            for (int j = 0; j < arr.length(); j++) {
                // JSONObject json = arr.getJSONObject(j);
                // tcount++;
                try{
                    JSONObject json = arr.getJSONObject(j);
                    tcount++;
                    System.out.println(tcount);
                    String text = json.getString("text");
                    String user = json.getJSONObject("user").getString("name");
                    String date = json.getString("created_at");
                    String followers = Integer.toString(json.getJSONObject("user").getInt("followers_count"));

//                    JSONArray coords = json.getJSONObject("place").getJSONObject("bounding_box").getJSONArray("coordinates");
                    if(json.getJSONObject("place").getString("full_name") != null){
                        location = json.getJSONObject("place").getString("full_name");
                    }
                    if(json.getString("title") != null){
                        title = json.getString("title");
                    }

                    Tweet newTweet = new Tweet(user,title,date,text,location, followers);
                    Document newDoc = createDocument(newTweet);
                    indexWriter.addDocument(newDoc);


                }catch(Exception e){
//                    System.out.println(e);
                }

            }
        }

        System.out.println(errors);
        System.out.println(tcount);


        indexWriter.close();
    }


    public static Document createDocument(Tweet tweet){
        Document doc = new Document();
        doc.add(new TextField("user", tweet.user, Field.Store.YES));
        doc.add(new TextField("title", tweet.title, Field.Store.YES));
        doc.add(new TextField("date", tweet.date, Field.Store.YES));
        doc.add(new TextField("text", tweet.text, Field.Store.YES));
        doc.add(new TextField("location", tweet.location, Field.Store.YES));
        doc.add(new TextField("followers", tweet.followers, Field.Store.YES));

        return doc;
    }
}

