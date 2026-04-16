package controllers;

import entities.Client;
import entities.Order;
import io.javalin.Javalin;
import persistence.ConnectionPool;
import io.javalin.http.Context;
import persistence.OrderMapper;
import persistence.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AdminController {

    Javalin app;
    ConnectionPool connectionPool;
    UserMapper userMapper;
    OrderMapper orderMapper;

    public AdminController(Javalin app, ConnectionPool connectionPool) {
        this.app = app;
        this.connectionPool = connectionPool;
        this.userMapper = new UserMapper(connectionPool);
        this.orderMapper = new OrderMapper(connectionPool);
    }

    public void addRoutes() {
        app.get("/adminsite", ctx -> ctx.render("adminSite/Admin.html"));
        app.get("/adminuserinfo", ctx -> getCustomer(ctx));
        app.get("/adminorders", ctx -> getOrders(ctx));
        app.get("/adminsaldo", ctx -> ctx.render("adminSite/AdminSaldo.html"));
        app.get("/admincreateadmin", ctx -> ctx.render("adminSite/AdminCreateAdmin.html"));

        app.post("/viewuserinfo", ctx -> getCustomer(ctx));
        app.post("/vieworderinfo", ctx -> getOrders(ctx));


    }

    public void createAdmin(Context ctx) {
        ctx.render("");
    }

    public void getOrders(Context ctx) {
        List<Order> orderList = new ArrayList<>();

        String orderDate = ctx.formParam("ordre_date") == null ? "" : ctx.formParam("ordre_date");
        String userId = ctx.formParam("customer_number") == null ? "" : ctx.formParam("customer_number");
        String orderId = ctx.formParam("ordre_number") == null ? "" : ctx.formParam("ordre_number");

        for (Order order : orderMapper.getAllOrders())
            if (order.getDate().contains(orderDate) &&
                    String.valueOf(order.getUserId()).contains(userId) &&
                    String.valueOf(order.getOrderId()).contains(orderId))

                orderList.add(order);
        ctx.render("adminSite/AdminOrders.html", Map.of("orderList", orderList));
    }

    public void getCustomer(Context ctx) {
        List<Client> clientList = new ArrayList<>();

        String firstName = ctx.formParam("customer_firstname") == null ? "" : ctx.formParam("customer_firstname");
        String firstLastname = ctx.formParam("customer_lastname") == null ? "" : ctx.formParam("customer_lastname");
        String userId = ctx.formParam("customer_number") == null ? "" : ctx.formParam("customer_number");
        String email = ctx.formParam("customer_email") == null ? "" : ctx.formParam("customer_email");

        for (Client client : userMapper.getAllClients())
            if (client.getFirstName().toLowerCase().contains(firstName.toLowerCase()) &&
                    client.getLastName().toLowerCase().contains(firstLastname.toLowerCase()) &&
                    String.valueOf(client.getUserID()).contains(userId) &&
                    client.getEmail().contains(email))

                clientList.add(client);
        ctx.render("adminSite/AdminUserInfo.html", Map.of("customerList", clientList));
    }

    public void setBalance(Context ctx) {
        ctx.render("");
    }
}
