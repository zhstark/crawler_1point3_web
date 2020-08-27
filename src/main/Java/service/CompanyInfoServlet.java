package service;

import assist.DateAssist;
import db.mongodb.MongoDBConnection;
import db.mongodb.MongodbUtil;
import entity.CompanyLevelsItem;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/get_company_info")
public class CompanyInfoServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String source = req.getParameter("source");
        String company = req.getParameter("company");
        JSONArray array = new JSONArray();
        if (source.equals("levels")) {
            array = getLevelInfo(company);
        } else if (source.equals("analysis")) {
            String days = req.getParameter("days");
            if ((days == null) || (days.length() == 0)) {
                days = "180";
            }
            array = getAnalysisChart(company, Integer.parseInt(days));
        }
        RpcHelper.writeJsonArray(resp, array);
    }

    private JSONArray getLevelInfo(String company) {
        MongoDBConnection collection = new MongoDBConnection(MongodbUtil.DB_COMPANY_INFO,"levels");
        JSONArray array = new JSONArray();
        for (CompanyLevelsItem clt: collection.searchCompanyLevelsItem(company)) {
            array.put(clt.toJSONObject());
        }
        return array;
    }

    private JSONArray getAnalysisChart(String company, int days) {
        // key = name, value = data
        Map<String, List<Integer>> line= new HashMap<>();
        JSONArray array = new JSONArray();

        MongoDBConnection collection = new MongoDBConnection(MongodbUtil.DB_1POINT3,"interviews");
        List<Integer> counter = new ArrayList<>();

        for (int i = days-1; i >=0; i--) {
            String date = DateAssist.getDaysAgoDate(i);
            counter.add(collection.countPosts(company,date));
        }
        line.put("1point3 面经", new ArrayList<>(counter));
        counter.clear();

        collection = new MongoDBConnection(MongodbUtil.DB_1POINT3,"jobs");
        for (int i = days-1; i >=0; i--) {
            String date = DateAssist.getDaysAgoDate(i);
            counter.add(collection.countPosts(company,date));
        }
        line.put("求职", new ArrayList<>(counter));
        counter.clear();

        collection = new MongoDBConnection(MongodbUtil.DB_LEETCODE,"interview_questions");
        for (int i = days-1; i >=0; i--) {
            String date = DateAssist.getDaysAgoDate(i);
            counter.add(collection.countPosts(company,date));
        }
        line.put("LeetCode 面经", new ArrayList<>(counter));

        array.put(line);
        return array;
    }

}
