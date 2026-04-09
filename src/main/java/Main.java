import config.SessionConfig;
import config.ThymeleafConfig;
import entities.Cupcake;
import entities.CupcakeList;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import persistence.BaseMapper;
import persistence.ConnectionPool;
import persistence.CupcakeMapper;
import persistence.ToppingMapper;

public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake_shop";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        app.get("/", ctx -> ctx.render("Index.html"));

        CupcakeMapper cm = new CupcakeMapper(connectionPool);
        cm.generateCupcakes();
        CupcakeList cupcakeList = new CupcakeList();
        cm.getAllCupcakes(cupcakeList);
        for (Cupcake c: cupcakeList.getCupcakeList()){
            System.out.println(c);
        }


    }
}
