package entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CompanyLevelsItem {
    public String getCompany() {
        return company;
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getLevels() {
        return levels;
    }

    public List<String> getNotes() {
        return notes;
    }

    public List<String> getSalaries() {
        return salaries;
    }

    public List<String> getStocks() {
        return stocks;
    }

    private String company;
    private List<String> titles;
    private List<String> levels;
    private List<String> notes;
    private List<String> salaries;
    private List<String> stocks;

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("titles", titles);
            obj.put("company", company);
            obj.put("levels", levels);
            obj.put("notes", notes);
            obj.put("salaries", salaries);
            obj.put("stocks", stocks);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private CompanyLevelsItem(CompanyLevelsItemBuilder builder) {
        this.company = builder.company;
        this.levels = builder.levels;
        this.titles = builder.titles;
        this.notes = builder.notes;
        this.salaries = builder.salaries;
        this.stocks = builder.stocks;
    }

    public static class CompanyLevelsItemBuilder {
        public CompanyLevelsItemBuilder setCompany(String company) {
            this.company = company;
            return this;
        }

        public CompanyLevelsItemBuilder setTitles(List<String> titles) {
            this.titles = titles;
            return this;
        }

        public CompanyLevelsItemBuilder setLevels(List<String> levels) {
            this.levels = levels;
            return this;
        }

        public CompanyLevelsItemBuilder setNotes(List<String> notes) {
            this.notes = notes;
            return this;
        }

        public CompanyLevelsItemBuilder setSalaries(List<String> salaries) {
            this.salaries = salaries;
            return this;
        }

        public CompanyLevelsItemBuilder setStocks(List<String> stocks) {
            this.stocks = stocks;
            return this;
        }

        private String company;
        private List<String> titles;
        private List<String> levels;
        private List<String> notes;
        private List<String> salaries;
        private List<String> stocks;

        public CompanyLevelsItem build() {
            return new CompanyLevelsItem(this);
        }
    }
}
