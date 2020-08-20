package service;

import assist.Date;
import db.mongodb.MongoDBConnection;
import entity.CompanyLevelsItem;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
            if (days == null || days.length() == 0) {
                days = "180";
            }
            array = getAnalysisChart(company, Integer.parseInt(days));
        }
        RpcHelper.writeJsonArray(resp, array);
    }

    private JSONArray getLevelInfo(String company) {
        MongoDBConnection collection = new MongoDBConnection("company_info","levels");
        JSONArray array = new JSONArray();
        for (CompanyLevelsItem clt: collection.searchCompanyLevelsItem(company)) {
            array.put(clt.toJSONObject());
        }
        return array;
    }

    private JSONArray getAnalysisChart(String company, int days) {
        MongoDBConnection collection = new MongoDBConnection("company_info","levels");
        JSONArray array = new JSONArray();
        String today = Date.getCurrentDate();
        String beginDate = Date.getDaysAgoDate(days);
        List<Integer> counter = collection.countPosts(company,beginDate,today);
        array.put(counter);
        return array;
    }
}
