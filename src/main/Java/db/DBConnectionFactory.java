package db;

import db.mongodb.MongoDBConnection;

public class DBConnectionFactory {
    private static final String DEFAULT_DB = "mongodb";

    public static DBConnection getConnection(String db) {
        switch (db) {
            case "mysql":
                return null;
            case "mongodb":
                return new MongoDBConnection();
            default:
                throw new IllegalArgumentException("Invalid db: "+db);
        }
    }

    public static DBConnection getConnextion() {
        return getConnection(DEFAULT_DB);
    }
}
