package database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInit {
    private static void createTable(){
        String sqlCreateTableProducts = """
            DROP TABLE IF EXISTS products;
            CREATE TABLE products (
                pid SERIAL NOT NULL,
                name CHAR(40) NOT NULL,
                description CHAR(200),
                sku char(15) NOT NULL
            );
        """;

        String sqlCreateTableCustomers = """
            DROP TABLE IF EXISTS customers;
            CREATE TABLE customers (
                cid SERIAL NOT NULL,
                name CHAR(40) NOT NULL,
                mail CHAR(40),
                phone CHAR(20)
            );
        """;

        try (Connection conn = PostgresConnection.connect()) {
            assert conn != null;

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sqlCreateTableProducts);
                stmt.execute(sqlCreateTableCustomers);

                System.out.println("Successfully created table(s)");
            }
        } catch (Exception e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createTable();
    }
}
