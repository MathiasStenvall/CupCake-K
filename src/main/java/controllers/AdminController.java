package controllers;

import entities.Client;
import io.javalin.Javalin;
import persistence.ConnectionPool;
import io.javalin.http.Context;
import persistence.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AdminController {

    Javalin app;
    ConnectionPool connectionPool;
    UserMapper usermapper;

    public AdminController(Javalin app, ConnectionPool connectionPool) {
        this.app = app;
        this.connectionPool = connectionPool;
        this.usermapper = new UserMapper(connectionPool);
    }

    public void addRoutes() {
        app.get("/adminsite", ctx -> ctx.render("adminSite/Admin.html"));
        app.get("/adminuserinfo", ctx -> getCustomer(ctx));
        app.get("/adminorders", ctx -> ctx.render("adminSite/AdminOrders.html"));
        app.get("/adminsaldo", ctx -> ctx.render("adminSite/AdminSaldo.html"));
        app.get("/admincreateadmin", ctx -> ctx.render("adminSite/AdminCreateAdmin.html"));

        app.post("/viewuserinfo", ctx -> getCustomer(ctx));
    }

    public void createAdmin(Context ctx) {
        ctx.render("");
    }

    public void getOrders(Context ctx) {
        ctx.render("");
    }

    public void getCustomer(Context ctx) {
        List<Client> searchClient = new ArrayList<>();

        String firstname = ctx.formParam("customer_name") == null ? "" : ctx.formParam("customer_name");
        String userId = ctx.formParam("customer_number") == null ? "" : ctx.formParam("customer_number");
        String email = ctx.formParam("customer_email") == null ? "" : ctx.formParam("customer_email");

        for (Client client : usermapper.getAllClients())
            if (client.getFirstName().toLowerCase().contains(firstname.toLowerCase()) &&
                    String.valueOf(client.getUserID()).contains(userId) &&
                    client.getEmail().contains(email))

                searchClient.add(client);
        ctx.render("adminSite/AdminUserInfo.html", Map.of("customerList", searchClient));
    }

    public void setBalance(Context ctx) {
        ctx.render("");
    }
}
