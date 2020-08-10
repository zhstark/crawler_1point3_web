package db.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import db.DBConnection;
import entity.Item;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class MongoDBConnection implements DBConnection {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> coll;

    public int getStatisticsRange() {
        return statisticsRange;
    }

    public void setStatisticsRange(int statisticsRange) {
        this.statisticsRange = statisticsRange;
    }

    private int statisticsRange = 180;

    public MongoDBConnection(String collection) {
        mongoClient = MongoClients.create("mongodb://localhost:27017/");
        database = mongoClient.getDatabase("crawler_1point3");
        coll = database.getCollection(collection);
    }

    @Override
    public void close() {
        mongoClient.close();
    }

    @Override
    public List<Item> searchItems(String title) {
        return null;
    }

    @Override
    public void saveItem(Item item) {

    }

    /**
     *
     * @return
     */
    @Override
    public List<Map<String, Integer>> statisticCompanies() {
        List<Map<String, Integer>> result = new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        for(int i = 0; i < statisticsRange; ++i) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -i);
            String date = sdf.format(cal.getTime());
            Map<String, Integer> counter = new HashMap<>();
            // transfer date into Integer: 2020-08-02 --> 802
            int d = Integer.parseInt(date.substring(5,7))*100+ Integer.parseInt(date.substring(8));
            counter.put("Date", d);
            for (Document doc : coll.find (eq ("create_date", date))) {
                if (doc.containsKey("company")) {
                    String companyName = doc.get("company", String.class);
                    if (companyName == null) {
                        continue;
                    }
                    counter.put(companyName, counter.getOrDefault(companyName, 0) + 1);
                }
            }
            result.add(counter);
        }
        return result;
    }
}
