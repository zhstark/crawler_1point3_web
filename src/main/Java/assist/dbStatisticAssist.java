package assist;

import com.kenai.jaffl.annotations.In;
import db.mongodb.MongoDBConnection;
import org.json.JSONArray;

import java.util.List;
import java.util.Map;

public class dbStatisticAssist {

    public static JSONArray collectData(String database, String collection, int daysRange) {
        JSONArray array = new JSONArray();
        MongoDBConnection connection = new MongoDBConnection(database,collection);
        List<Map<String, Integer>> statistics = connection.statisticCompanies(daysRange);
        connection.close();
        for (Map<String, Integer> item : statistics) {
            if (item == null) {
                continue;
            }
            array.put(item);
        }
        return array;
    }

    public static JSONArray collectDataByWeek(String database, String collection, int daysRange) {
        JSONArray array = new JSONArray();
        MongoDBConnection connection = new MongoDBConnection(database,collection);
        List<Map<String, Integer>> statistics = connection.statisticByWeek(daysRange);
        connection.close();
        for (Map<String, Integer> item : statistics) {
            if (item == null) {
                continue;
            }
            array.put(item);
        }
        return array;
    }

}
