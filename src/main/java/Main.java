import config.SessionConfig;
import config.ThymeleafConfig;
import controllers.AdminController;
import controllers.ClientController;
import entities.*;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import persistence.ConnectionPool;
import persistence.CupcakeMapper;
import persistence.OrderMapper;
import persistence.UserMapper;


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
        AdminController adminCOntroller = new AdminController(app, connectionPool);
        ClientController clientController = new ClientController(app, connectionPool);
        clientController.addRoutes();
        adminCOntroller.addRoutes();

        OrderMapper orderMapper = new OrderMapper(connectionPool);
        OrderList orderList = new OrderList();
        orderMapper.getAllOrders(orderList);

        /* test paying for basket - tested and WORKS!
        Client mathias = new Client(2,"mathias","stenvall","client", "mstenvall@gmail.com", "1234", 2000);

        Basket basket = new Basket(mathias,connectionPool);
        Cupcake firstCupcake = new Cupcake(20, 3, "nutmeg", 2, "blueberry",10.00);
        firstCupcake.setAmount(3);
        Cupcake secondCupcake = new Cupcake(21, 3,"nutmeg",3,"rasberry",10.00);
        secondCupcake.setAmount(2);

        basket.addCupcakeToBasket(firstCupcake);
        basket.addCupcakeToBasket(secondCupcake);

        basket.payBasket();
        */

        /* testing getCupcakeId - tested and WORKS!
        System.out.println(cupcakeList.getCupcakeList().size());
        System.out.println(cupcakeList.findCupcakeID("pistacio","orange"));
        */
    }
}
