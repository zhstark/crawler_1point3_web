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

@WebServlet("/interviews")
public class InterviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置响应内容类型
        JSONArray array = new JSONArray();

        MongoDBConnection connection = new MongoDBConnection("interviews");
        List<Map<String, Integer>> statistics = connection.statisticCompanies();
        connection.close();
        for( Map<String, Integer> item : statistics) {
            if (item == null) {
                continue;
            }
            array.put(item);
        }

        RpcHelper.writeJsonArray(resp, array);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
