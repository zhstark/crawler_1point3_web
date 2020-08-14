package service;

import db.mongodb.MongoDBConnection;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/leetcode-interview-questions")
public class LeetcodeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JSONArray array = new JSONArray();

        MongoDBConnection connection = new MongoDBConnection("crawler_leetcode", "interview_questions");
        int daysRange = 180;
        if (req.getParameter("days") != null) {
            daysRange = Integer.parseInt(req.getParameter("days"));
        }
        List<Map<String, Integer>> statistics = connection.statisticCompanies(daysRange);
        connection.close();
        for (Map<String, Integer> item : statistics) {
            if (item == null) {
                continue;
            }
            array.put(item);
        }

        RpcHelper.writeJsonArray(resp, array);
    }
}

