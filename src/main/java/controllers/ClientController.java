package controllers;

import io.javalin.Javalin;
import persistence.ConnectionPool;
import io.javalin.http.Context;

public class ClientController {

    Javalin app;
    ConnectionPool connectionPool;

    public ClientController(Javalin app, ConnectionPool connectionPool) {
        this.app = app;
        this.connectionPool = connectionPool;
    }

    public void addRoutes(){
        app.get("", ctx -> ctx.render(""));
        app.get("", ctx -> ctx.render(""));
        app.get("", ctx -> ctx.render(""));
        app.get("", ctx -> ctx.render(""));
        app.get("", ctx -> ctx.render(""));
        app.get("", ctx -> ctx.render(""));
        app.get("", ctx -> ctx.render(""));
        app.post("", ctx -> login(ctx));
        app.post("", ctx -> createClient(ctx));
        app.post("h", ctx -> createCupCake(ctx));
        app.post("h", ctx -> setBasket(ctx));
    }

    public void login(Context ctx){
        ctx.render("");
    }

    public void createClient(Context ctx){
        ctx.render("");
    }

    public void createCupCake(Context ctx){
        ctx.render("");
    }

    public void setBasket(Context ctx){
        ctx.render("");
    }


}
