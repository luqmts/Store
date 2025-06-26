package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInit {
    private static void createTable(){
        String sqlCreateTableProducts = """
            DROP TABLE IF EXISTS products;
            CREATE TABLE products (
                pid SERIAL NOT NULL,
                name CHAR(40) NOT NULL,
                description CHAR(200),
                sku char(15) NOT NULL,
                supplier_id INTEGER NOT NULL
            );
        """;

        String sqlCreateTableSuppliers = """
            DROP TABLE IF EXISTS suppliers;
            CREATE TABLE suppliers (
                sid SERIAL NOT NULL,
                name CHAR(40) NOT NULL,
                mail CHAR(40),
                cnpj CHAR(18),
                phone CHAR(20)
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
                stmt.execute(sqlCreateTableSuppliers);

                System.out.println("Successfully created table(s)");
            }
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createTable();
    }
}
