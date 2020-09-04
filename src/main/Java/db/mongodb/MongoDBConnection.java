package db.mongodb;

import assist.DateAssist;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ConnectionString;
import db.DBConnection;
import entity.CompanyLevelsItem;
import entity.Item;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongoDBConnection implements DBConnection {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> coll;

    public MongoDBConnection(String database) {
        this(database, "test");
    }

    public MongoDBConnection(String database, String collection) {
        //mongoClient = MongoClients.create("mongodb://localhost:27017/");
        mongoClient = MongoClients.create();
        this.database = mongoClient.getDatabase(database);
        coll = this.database.getCollection(collection);
    }

    @Override
    public void close() {
        mongoClient.close();
    }

    @Override
    public List<Item> searchItems(String title) {
        return null;
    }

    public List<CompanyLevelsItem> searchCompanyLevelsItem(String company) {
        List<CompanyLevelsItem> list = new ArrayList<>();
        for(Document doc : coll.find(eq("company", company))) {
            CompanyLevelsItem clt = new CompanyLevelsItem.CompanyLevelsItemBuilder().setCompany(doc.getString("company"))
                    .setTitles((ArrayList<String>)doc.get("titles"))
                    .setLevels((ArrayList<String>)doc.get("levels"))
                    .setNotes((ArrayList<String>)doc.get("notes"))
                    .setSalaries((ArrayList<String>)doc.get("salaries"))
                    .build();
            list.add(clt);
        }
        return list;
    }

    public List<Document> searchDocument(String key, String value) {
        List<Document> list = new ArrayList<>();
        for(Document doc : coll.find(eq(key, value))) {
            list.add(doc);
        }
        return list;
    }

    @Override
    public void saveItem(Item item) {

    }

    /**
     *
     * @return
     */
    @Override
    public List<Map<String, Integer>> statisticCompanies(int daysRange) {
        List<Map<String, Integer>> result = new ArrayList<>();

        for(int i = 0; i < daysRange; ++i) {
            String date = DateAssist.getDaysAgoDate(i);
            Map<String, Integer> counter = new HashMap<>();
            // transfer date into Integer: 2020-08-02 --> 802
            int d = Integer.parseInt(date.substring(5,7))*100+ Integer.parseInt(date.substring(8));
            counter.put("Date", d);
            collectCompanyOnDate(counter, date);
            result.add(counter);
        }
        return result;
    }

    public List<Map<String, Integer>> statisticByWeek(int daysRange) {
        List<Map<String, Integer>> result = new ArrayList<>();

        // traverse all days
        for (int i = 0; i < daysRange; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -i);
            // check if it's the first day of the week
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
                Map<String, Integer> counter = new HashMap<>();
                // add date info
                counter.put("Date", (calendar.get(Calendar.MONTH)+1)*100 + calendar.get(Calendar.DATE));
                // add companies count in this week
                for (int j = i; j <i+7; j++) {
                    String date = DateAssist.getDaysAgoDate(j);
                    collectCompanyOnDate(counter, date);
                }
                result.add(counter);
            }
        }

        return result;
    }
    public int countPosts(String company, String date) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        int count = 0;
        for (Document doc : coll.find(and(eq("company", company),eq("create_date", date)))) {
                count++;
        }
        return count;
    }

    private void collectCompanyOnDate(Map<String, Integer> counter, String date) {
        for (Document doc : coll.find (eq ("create_date", date))) {
            if (doc.containsKey("company")) {
                String companyName = doc.get("company", String.class);
                if (companyName == null) {
                    continue;
                }
                counter.put(companyName, counter.getOrDefault(companyName, 0) + 1);
            }
        }
    }
}
