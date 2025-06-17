package database;

import java.sql.Connection;
import java.sql.DriverManager;
import io.github.cdimascio.dotenv.Dotenv;

public class PostgresConnection {
    static Dotenv dotenv = Dotenv.configure().load();

    public static Connection connect(){
        String url = "jdbc:postgresql://localhost:5432/store";

        try {
            Connection conn = DriverManager.getConnection(url, dotenv.get("DB_USER"), dotenv.get("DB_PASS"));
            System.out.println("Connected successfully");
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
