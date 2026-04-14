package controllers;

import io.javalin.Javalin;
import persistence.ConnectionPool;
import io.javalin.http.Context;
import persistence.UserMapper;

public class ClientController {

    Javalin app;
    ConnectionPool connectionPool;
    UserMapper usermapper = new UserMapper(connectionPool);

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

        app.post("/createAccount", ctx -> createClient(ctx));
        app.post("", ctx -> login(ctx));
        app.post("l", ctx -> createCupCake(ctx));
        app.post("m", ctx -> setBasket(ctx));
    }

    public void login(Context ctx){
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        usermapper.login(email,password);
        ctx.render("Gallery.html");
    }

    public void createClient(Context ctx){
        String firstname = ctx.formParam("firstname");
        String lastname = ctx.formParam("lastname");
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        usermapper.createClieant(firstname, lastname, email, password1, password2);
        ctx.render("Index.html");
    }

    public void createCupCake(Context ctx){
        ctx.render("");
    }

    public void setBasket(Context ctx){
        ctx.render("");
    }


}
