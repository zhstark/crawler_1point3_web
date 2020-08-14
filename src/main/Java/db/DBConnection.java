package db;

import entity.Item;

import java.util.List;
import java.util.Map;

public interface DBConnection {

    /**
     * Close the connection.
     */
    public void close();

    /**
     * Search items near a geolocation and a term (optional).
     *
     * @param title
     *            (Nullable)
     * @return list of items
     */
    public List<Item> searchItems(String title);

    /**
     * Save item into db.
     *
     * @param item
     */
    public void saveItem(Item item);

    /**
     * Calculate the frequency of companies
     * @return a list of maps
     * index represent the date: today - index
     * map key = company, value = count
     */
    public List<Map<String, Integer>> statisticCompanies(int n);

}
