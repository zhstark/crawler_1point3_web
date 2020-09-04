package service;

import assist.dbStatisticAssist;
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

@WebServlet("/interviews")
public class InterviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置响应内容类型
        JSONArray array;

        int daysRange = 180;
        if (req.getParameter("days") != null) {
            daysRange = Integer.parseInt(req.getParameter("days"));
        }
        if (req.getParameter("byWeek") != null && req.getParameter("byWeek").equals("true")) {
            array = dbStatisticAssist.collectDataByWeek("crawler_1point3", "interviews", daysRange);
        } else {
            array = dbStatisticAssist.collectData("crawler_1point3", "interviews", daysRange);
        }

        RpcHelper.writeJsonArray(resp, array);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
