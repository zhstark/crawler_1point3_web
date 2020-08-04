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

@WebServlet("/echarts")
public class MyServlet extends HttpServlet {
    private String message;

    @Override
    public void init() throws ServletException {
        message = "Hello world, this message is from servlet!";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置响应内容类型
        JSONArray array = new JSONArray();

        MongoDBConnection connection = new MongoDBConnection();
        List<Map<String, Integer>> statistics = connection.statisticCompanies();
        connection.close();
        for( Map<String, Integer> item : statistics) {
            array.put(item);
        }

        RpcHelper.writeJsonArray(resp, array);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
