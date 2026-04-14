package controllers;

import io.javalin.Javalin;
import persistence.ConnectionPool;
import io.javalin.http.Context;


public class AdminController {

    Javalin app;
    ConnectionPool connectionPool;

    public AdminController(Javalin app, ConnectionPool connectionPool) {
        this.app = app;
        this.connectionPool = connectionPool;
    }

    public void addRoutes(){
        app.get("/adminsite", ctx -> ctx.render("adminSite/Admin.html"));
        app.get("/adminuserinfo", ctx -> ctx.render("adminSite/AdminUserInfo.html"));
        app.get("/adminorders", ctx -> ctx.render("adminSite/AdminOrders.html"));
        app.get("/adminsaldo", ctx -> ctx.render("adminSite/AdminSaldo.html"));
        app.get("/admincreateadmin", ctx -> ctx.render("adminSite/AdminCreateAdmin.html"));
        app.get("f", ctx -> ctx.render(""));
        app.get("g", ctx -> ctx.render(""));
        app.post("i", ctx -> createAdmin(ctx));
        app.post("j", ctx -> getOrders(ctx));
        app.post("h", ctx -> getCustomer(ctx));
        app.post("z", ctx -> setBalance(ctx));
    }

    public void createAdmin(Context ctx){
        ctx.render("");
    }

    public void getOrders(Context ctx){
        ctx.render("");
    }

    public void getCustomer(Context ctx){
        ctx.render("");
    }

    public void setBalance(Context ctx){
        ctx.render("");
    }









}
