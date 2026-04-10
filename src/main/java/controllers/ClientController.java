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
        app.get("/login", ctx -> ctx.render("Login.html"));
        app.get("/creatingAccount", ctx -> ctx.render("CreatAccount"));
        app.get("/ordre", ctx -> ctx.render("Odrersite.html"));
        app.get("/gallery", ctx -> ctx.render("Gallery.html"));
        app.get("/index", ctx -> ctx.render("Index.html"));
        app.get("/basket", ctx -> ctx.render("Basket.html"));
        app.get("/location", ctx -> ctx.render("info/Location.html"));
        app.get("/contact", ctx -> ctx.render("info/Contact.html"));
        app.get("/about", ctx -> ctx.render("info/About.html"));

        app.post("/creatingAccount", ctx -> createClient(ctx));
        app.post("", ctx -> login(ctx));
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
