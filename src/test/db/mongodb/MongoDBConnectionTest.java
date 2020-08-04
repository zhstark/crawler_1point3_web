package db.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;


public class MongoDBConnectionTest {

    @Test
    public void statisticCompanies() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/");
        MongoDatabase database = mongoClient.getDatabase("cralwer_1point3");
        MongoCollection<Document> coll = database.getCollection("posts");

        List<Map<String, Integer>> result = new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        for(int i = 0; i < 7; ++i) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -i);
            String date = sdf.format(cal.getTime());
            Map<String, Integer> counter = new HashMap<>();
            for (Document doc : coll.find (eq ("create_date", date))) {
                if (doc.containsKey("company")) {

                    String obj = doc.get("company", String.class);
                    String companyName = null;
                    try {
                        companyName = new String(obj.getBytes(), "utf-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println(companyName);
                    counter.put(companyName, counter.getOrDefault(companyName, 0) + 1);
                }
            }
            result.add(counter);
        }

        mongoClient.close();
        return;
    }
}
