package controllers;

import entities.*;
import io.javalin.Javalin;
import persistence.ConnectionPool;
import io.javalin.http.Context;
import persistence.CupcakeMapper;
import persistence.UserMapper;

import java.util.Map;

public class ClientController {

    Javalin app;
    ConnectionPool connectionPool;
    UserMapper usermapper;
    Basket basket;

    public ClientController(Javalin app, ConnectionPool connectionPool) {
        this.app = app;
        this.connectionPool = connectionPool;
        this.usermapper = new UserMapper(connectionPool);
    }

    public void addRoutes() {
        app.get("/login", ctx -> ctx.render("Login.html"));
        app.get("/creatingAccount", ctx -> ctx.render("CreatAccount"));
        app.get("/ordre", ctx -> ctx.render("Odrersite.html"));
        app.get("/gallery", ctx -> ctx.render("Gallery.html"));
        app.get("/index", ctx -> ctx.render("Index.html"));
        app.get("/basket", ctx -> getBasket(ctx));
        app.get("/location", ctx -> ctx.render("info/Location.html"));
        app.get("/contact", ctx -> ctx.render("info/Contact.html"));
        app.get("/about", ctx -> ctx.render("info/About.html"));
        app.get("/logout", ctx -> logout(ctx));

        app.post("/createAccount", ctx -> createClient(ctx));
        app.post("/login", ctx -> login(ctx));
        app.post("/createCupcake", ctx -> createCupCake(ctx));
        app.post("/delete", ctx -> deleteCupcake(ctx));
    }

    public void login(Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        User user = usermapper.login(email.toLowerCase(), password);
        if(user!=null) {
            ctx.sessionAttribute("user", user);
            basket = new Basket(user, connectionPool);
            ctx.render("Index.html");
        }
    }

    public void createClient(Context ctx) {
        String firstname = ctx.formParam("firstname");
        String lastname = ctx.formParam("lastname");
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        Client client = usermapper.createClient(firstname.toLowerCase(), lastname.toLowerCase(), email.toLowerCase(), password1, password2);
        ctx.render("Index.html");
    }

    public void createCupCake(Context ctx) {
        String bottom = ctx.formParam("selectbottom");
        String top = ctx.formParam("selecttop");
        int amount = Integer.parseInt(ctx.formParam("amount"));

        CupcakeMapper cupcakeMapper = new CupcakeMapper(connectionPool);
        CupcakeList cupcakeList = new CupcakeList();
        cupcakeMapper.getAllCupcakes(cupcakeList);

        for(Cupcake cupcake: cupcakeList.getCupcakeList()){
            if (cupcake.getBaseName().equals(bottom) &&
                    cupcake.getToppingName().equals(top)) {

                cupcake.setAmount(amount);
                basket.addCupcakeToBasket(cupcake);
            }
        }
        ctx.render("Odrersite.html");
    }

    public void deleteCupcake(Context ctx){
        int cupcakeId = Integer.parseInt(ctx.formParam("basket.id"));
        basket.getBasketCupcakes().removeIf(c -> c.getId() == (cupcakeId));
        getBasket(ctx);
    }

    public void editCupcake(Context ctx){
    }

    public void getBasket(Context ctx){
        ctx.render("Basket.html", Map.of("basketCupcakes", basket.getBasketCupcakes()));
    }

    public void setBasket(Context ctx) {
        ctx.render("");
    }

    public void logout(Context ctx){
        ctx.sessionAttribute("user", null);
        ctx.render("/Index.html");
    }

}
