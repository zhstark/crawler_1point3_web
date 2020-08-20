package db.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import db.DBConnection;
import entity.CompanyLevelsItem;
import entity.Item;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.mongodb.client.model.Filters.*;

public class MongoDBConnection implements DBConnection {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> coll;

    public MongoDBConnection(String database) {
        this(database, "test");
    }

    public MongoDBConnection(String database, String collection) {
        mongoClient = MongoClients.create("mongodb://localhost:27017/");
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
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        for(int i = 0; i < daysRange; ++i) {
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

    public List<Integer> countPosts(String company, String beginDate, String endDate) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        List<Integer> counter = new ArrayList<>();
        Date begin;
        Date end;
        try {
            begin = sdf.parse(beginDate);
            end = sdf.parse(endDate);
        } catch(Exception e) {
            e.printStackTrace();
            return counter;
        }
        Calendar beginCal = Calendar.getInstance();
        beginCal.setTime(begin);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        int count = 0;
        for (Document doc : coll.find(and(eq("company", company),and(gte("create_date", beginDate),lte("create_date",endDate)))).sort(Sorts.ascending("create_date"))) {
            if (doc.get("create_date").equals(sdf.format(beginCal.getTime()))) {
                count++;
            } else {
                counter.add(count);
                count = 0;
                beginCal.add(beginCal.DATE, 1);
            }
        }
        while (beginCal.before(endCal) || beginCal.equals(endCal)) {
            counter.add(0);
            beginCal.add(beginCal.DATE, 1);
        }
        return counter;
    }
}
