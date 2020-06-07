
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

        //Folder of tweets
        File folder = new File("/Users/parthmangrola/documents/tweets");
        File[] files = folder.listFiles();

        int errors = 0;
        int tcount = 0;

        //Iterate through each file
        for (File file: files != null ? files : new File[0])
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            JSONArray arr = new JSONArray();

            //Add each line to JSONArray
            try{
                for (String line; (line = reader.readLine()) != null;) {

                    if(!line.trim().equals("")){
                        arr.put(new JSONObject(line.trim()));
                    }
                }
            }catch(Exception e){
                System.out.println(e);
                errors += 1;
            }

            String location ="";
            String title = "";

            //Iterate through the JSONArray where each JSONObject is a tweet. Add document to index
            for (int j = 0; j < arr.length(); j++) {
                JSONObject json = arr.getJSONObject(j);
                tcount++;
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
        System.out.println(errors);
        System.out.println(tcount);

        indexWriter.close();

                // Now search the index:
        DirectoryReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        String[] fields = {"title", "content"};
        Map<String, Float> boosts = new HashMap<>();
        boosts.put(fields[0], 1.0f);
        boosts.put(fields[1], 0.5f);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
        Query query = parser.parse("UCR");
        // Query query = parser.parse("UCR discussion");
        // QueryParser parser = new QueryParser("content", analyzer);
        // Query query = parser.parse("(title:ucr)^1.0 (content:ucr)^0.5");
        System.out.println(query.toString());
        int topHitCount = 100;
        ScoreDoc[] hits = indexSearcher.search(query, topHitCount).scoreDocs;

        // Iterate through the results:
        for (int rank = 0; rank < hits.length; ++rank) {
            Document hitDoc = indexSearcher.doc(hits[rank].doc);
            System.out.println((rank + 1) + " (score:" + hits[rank].score + ") --> " +
                               hitDoc.get("title") + " - " + hitDoc.get("content"));
            // System.out.println(indexSearcher.explain(query, hits[rank].doc));
        }
        indexReader.close();
        directory.close();


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
