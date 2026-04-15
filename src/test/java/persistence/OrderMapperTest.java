package persistence;

import entities.Client;
import entities.Cupcake;
import entities.OrderList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";
    private final static String URL = "jdbc:postgresql://localhost:5432/cupcake_shop?currentSchema=test";
    private static final String DB = "cupcake_shop";

    private static ConnectionPool connectionPool;
    private static OrderMapper orderMapper;

    @BeforeAll
    public static void databaseSetup() {
        try {
            connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
            // dependency injection
            orderMapper = new OrderMapper(connectionPool);
            try (Connection testConnection = connectionPool.getConnection()) {
                try (Statement stmt = testConnection.createStatement()) {
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
        } catch (SQLException e) {
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


                // Insert test data into users-tabel
                stmt.execute("INSERT INTO test.users (user_id, first_name, last_name, role, email, password, balance) VALUES " +
                        "(1, 'christian', 'kenter', 'admin', 'ckenter@gmail.com', '1234', 1000), " +
                        "(2, 'mathias', 'stenvall', 'client', 'mstenvall@gmail.com', '1234', 2000)");

                // Insert test data into orders-tabel
                stmt.execute("INSERT INTO test.orders (order_id, user_id, date, price, paid) VALUES " +
                        "(1, 1, '2026-04-14', 40.00, true), " +
                        "(2, 2, '2026-04-14', 60.00, true)");

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

    @org.junit.jupiter.api.Test
    void getAllOrders() {

        OrderList orderList = new OrderList();
        orderMapper.getAllOrders(orderList);

        int expected = 2;
        int actual = orderList.getOrderList().size();

        assertEquals(expected, actual);

    }

    // this also tests uploadOrderDetails()
    @org.junit.jupiter.api.Test
    void uploadOrder() {

        Client mathias = new Client(2,"mathias","stenvall","client", "mstenvall@gmail.com", "1234", 2000);

        Cupcake firstCupcake = new Cupcake(20, 3, "nutmeg", 2, "blueberry",10.00);
        firstCupcake.setAmount(3);
        Cupcake secondCupcake = new Cupcake(21, 3,"nutmeg",3,"rasberry",10.00);
        secondCupcake.setAmount(2);

        List<Cupcake> cupcakeList = new ArrayList<>();
        cupcakeList.add(firstCupcake);
        cupcakeList.add(secondCupcake);

        double price = 50.00;

        orderMapper.uploadOrder(mathias, cupcakeList, price);

        OrderList orderList = new OrderList();
        orderMapper.getAllOrders(orderList);

        int expected = 3;
        int actual = orderList.getOrderList().size();

        assertEquals(expected,actual);

    }

    @org.junit.jupiter.api.Test
    void removeOrder() {

        orderMapper.removeOrder(2);
        OrderList orderList = new OrderList();
        orderMapper.getAllOrders(orderList);

        int expected = 1;
        int actual = orderList.getOrderList().size();

        assertEquals(expected, actual);

    }

    @org.junit.jupiter.api.Test
    void getOrdersByInputs() {
        //testing different version of the same method
        assertEquals(1, orderMapper.getOrderByInputs(null, 1, null).size());

        assertEquals(2, orderMapper.getOrderByInputs("2026-04-14", null, null).size());

        assertEquals(1, orderMapper.getOrderByInputs(null, null, 2).size());

    }

    @org.junit.jupiter.api.Test
    void clientOrders(){

        int expected = 1;
        int actual = orderMapper.clientOrders(1).size();

        assertEquals(expected, actual);

    }

}