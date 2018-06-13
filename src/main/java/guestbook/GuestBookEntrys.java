package guestbook;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class GuestBookEntrys implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // generate a lucky number
        int luckyNumber = new Random().nextInt(100);

        // create a sample hashmap
        Map<String, String> usersEntrys = new HashMap<>();
        usersEntrys.put("user1", "message message message");
        usersEntrys.put("user2", "message message message message message message");
        usersEntrys.put("user3", "message message message message message message message message message");

        // client's address
        String userAgent = httpExchange.getRequestHeaders().getFirst("User-agent");

        // get a template file
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/guest-book.twig");

        // create a model that will be passed to a template
        JtwigModel model = JtwigModel.newModel();

        // fill the model with values
        model.with("client", "Bart");
        model.with("usersEntrys", usersEntrys);

        // render a template to a string
        String response = template.render(model);

        // send the results to a the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
