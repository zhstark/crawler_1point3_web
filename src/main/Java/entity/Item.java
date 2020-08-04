package entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getLastReplyDate() {
        return lastReplyDate;
    }

    public String getAuthor() {
        return author;
    }

    private String title;
    private String company;
    private String author;
    private String createDate;
    private String lastReplyDate;

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("title", title);
            obj.put("company", company);
            obj.put("author", author);
            obj.put("create_date", createDate);
            obj.put("last_reply_date", lastReplyDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static class ItemBuilder {
        public void setTitle(String title) {
            this.title = title;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public void setLastReplyDate(String lastReplyDate) {
            this.lastReplyDate = lastReplyDate;
        }

        private String title;
        private String company;
        private String author;
        private String createDate;
        private String lastReplyDate;

        public Item build() {
            return new Item(this);
        }
    }

    /**
     * this is a builder pattern in java
     * @param builder
     */
    private Item(ItemBuilder builder) {
        this.title = builder.title;
        this.company = builder.company;
        this.author = builder.author;
        this.createDate = builder.createDate;
        this.lastReplyDate = builder.lastReplyDate;
    }
}


