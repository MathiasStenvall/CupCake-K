package persistence;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class CupcakeMapperTest {

    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";
    private final static String URL = "jdbc:postgresql://localhost:5432/cupcake_shop?currentSchema=test";
    private static final String DB = "cupcake_shop";

    private static ConnectionPool connectionPool;
    private static CupcakeMapper cupcakeMapper;

    @BeforeAll
    public static void databaseSetup() {
        try {
            connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
            // dependency injection
            cupcakeMapper = new CupcakeMapper(connectionPool);
            try (Connection testConnection = connectionPool.getConnection())
            {
                try (Statement stmt = testConnection.createStatement())
                {
                    // The test schema is already created, so we only need to delete/create test tables
                    stmt.execute("DROP TABLE IF EXISTS test.bases CASCADE");
                    stmt.execute("DROP TABLE IF EXISTS test.cupcakes CASCADE");
                    stmt.execute("DROP TABLE IF EXISTS test.orders CASCADE");
                    stmt.execute("DROP TABLE IF EXISTS test.orders_cupcakes CASCADE");
                    stmt.execute("DROP TABLE IF EXISTS test.toppings CASCADE");
                    stmt.execute("DROP TABLE IF EXISTS test.users CASCADE");

                    // Removes sequenceobjects which controls autoincrement of keys
                    stmt.execute("DROP SEQUENCE IF EXISTS test.bases_base_id_seq CASCADE;");
                    stmt.execute("DROP SEQUENCE IF EXISTS test.cupcakes_cupcake_id_seq CASCADE;");
                    stmt.execute("DROP SEQUENCE IF EXISTS test.orders_order_id_seq CASCADE");
                    stmt.execute("DROP SEQUENCE IF EXISTS test.orders_cupcakes_order_cupcake_id_seq CASCADE");
                    stmt.execute("DROP SEQUENCE IF EXISTS test.toppings_topping_id_seq CASCADE");
                    stmt.execute("DROP SEQUENCE IF EXISTS test.users_user_id_seq CASCADE");

                    // Create tables as copy of original public schema structure
                    stmt.execute("CREATE TABLE test.bases AS (SELECT * from public.bases) WITH NO DATA");
                    stmt.execute("CREATE TABLE test.cupcakes AS (SELECT * from public.cupcakes) WITH NO DATA");
                    stmt.execute("CREATE TABLE test.orders AS (SELECT * from public.orders) WITH NO DATA");
                    stmt.execute("CREATE TABLE test.orders_cupcakes AS (SELECT * from public.orders_cupcakes) WITH NO DATA");
                    stmt.execute("CREATE TABLE test.toppings AS (SELECT * from public.toppings) WITH NO DATA");
                    stmt.execute("CREATE TABLE test.users AS (SELECT * from public.users) WITH NO DATA");

                    // Create sequences for auto generating id's
                    stmt.execute("CREATE SEQUENCE test.bases_base_id_seq");
                    stmt.execute("ALTER TABLE test.bases ALTER COLUMN base_id SET DEFAULT nextval('test.bases_base_id_seq')");
                    stmt.execute("CREATE SEQUENCE test.cupcakes_cupcake_id_seq");
                    stmt.execute("ALTER TABLE test.cupcakes ALTER COLUMN cupcake_id SET DEFAULT nextval('test.cupcakes_cupcake_id_seq')");
                    stmt.execute("CREATE SEQUENCE test.orders_order_id_seq");
                    stmt.execute("ALTER TABLE test.orders ALTER COLUMN order_id SET DEFAULT nextval('test.orders_order_id_seq')");
                    stmt.execute("CREATE SEQUENCE test.orders_cupcakes_order_cupcake_id_seq");
                    stmt.execute("ALTER TABLE test.orders_cupcakes ALTER COLUMN order_cupcake_id SET DEFAULT nextval('test.orders_cupcakes_order_cupcake_id_seq')");
                    stmt.execute("CREATE SEQUENCE test.toppings_topping_id_seq");
                    stmt.execute("ALTER TABLE test.toppings ALTER COLUMN topping_id SET DEFAULT nextval('test.toppings_topping_id_seq')");
                    stmt.execute("CREATE SEQUENCE test.users_user_id_seq");
                    stmt.execute("ALTER TABLE test.users ALTER COLUMN user_id SET DEFAULT nextval('test.users_user_id_seq')");

                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            fail("Database connection failed");
        }
    }

    @BeforeEach
    void setUp() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement()) {
                // Delete all rows to start from scratch
                stmt.execute("DELETE FROM test.bases CASCADE");
                stmt.execute("DELETE FROM test.cupcakes CASCADE");
                stmt.execute("DELETE FROM test.orders CASCADE");
                stmt.execute("DELETE FROM test.orders_cupcakes CASCADE");
                stmt.execute("DELETE FROM test.toppings CASCADE");
                stmt.execute("DELETE FROM test.users CASCADE");

                // Set all sequence objects to start autoincrementing from 1
                stmt.execute("SELECT setval('test.bases_base_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.cupcakes_cupcake_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.orders_order_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.orders_cupcakes_order_cupcake_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.toppings_topping_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.users_user_id_seq', 1, false)");

                // Insert test data into bases-tabel
                stmt.execute("INSERT INTO test.bases (base_id, name, price) VALUES " +
                        "(1, 'chocolate', 5.00), " +
                        "(2, 'vanilla', 5.00), " +
                        "(3, 'nutmeg', 5.00), " +
                        "(4, 'pistacio', 6.00), " +
                        "(5, 'almond', 7.00),");

                // Insert test data into toppings-tabel
                stmt.execute("INSERT INTO test.toppings (topping_id, name, price) VALUES " +
                        "(1, 'chocolate', 5.00), " +
                        "(2, 'blueberry', 5.00), " +
                        "(3, 'rasberry', 5.00), " +
                        "(4, 'crisp', 6.00), " +
                        "(5, 'strawberry', 6.00), " +
                        "(6, 'rum/raisin', 7.00), " +
                        "(7, 'orange', 8.00), " +
                        "(8, 'lemon', 8.00), " +
                        "(9, 'blue cheese', 9.00), " +
                        "(10, 'plain', 0), ");

                // More sequence object stuff
                stmt.execute("SELECT setval('test.bases_base_id_seq', COALESCE((SELECT MAX(base_id) FROM test.bases)+1, 1), false)");
                stmt.execute("SELECT setval('test.cupcakes_cupcake_id_seq', COALESCE((SELECT MAX(cupcake_id) FROM test.cupcakes)+1, 1), false)");
                stmt.execute("SELECT setval('test.orders_order_id_seq', COALESCE((SELECT MAX(order_id) FROM test.orders)+1, 1), false)");
                stmt.execute("SELECT setval('test.orders_cupcakes_order_cupcake_id_seq', COALESCE((SELECT MAX(order_cupcake_id) FROM test.orders_cupcakes)+1, 1), false)");
                stmt.execute("SELECT setval('test.toppings_topping_id_seq', COALESCE((SELECT MAX(topping_id) FROM test.toppings)+1, 1), false)");
                stmt.execute("SELECT setval('test.users_user_id_seq', COALESCE((SELECT MAX(user_id) FROM test.users)+1, 1), false)");
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    @Test
    void generateCupcakes() {
    }

    @Test
    void getAllCupcakes() {
    }
}