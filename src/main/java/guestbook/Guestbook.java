package guestbook;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import guestbook.DAO.EntryDAO;
import guestbook.Model.Entry;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class Guestbook implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();


        // Send a form if it wasn't submitted yet.
        if(method.equals("GET")){
            String formDisplay = "block";
            EntryDAO eDAO = new EntryDAO();
            List<Entry> comments = eDAO.getAllEntrys();
            Collections.reverse(comments);

            // get a template file
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/example.twig");

            // create a model that will be passed to a template
            JtwigModel model = JtwigModel.newModel();

            // fill the model with values
            model.with("entry", "");
            model.with("users_comments", comments);
            model.with( "formDisplay", formDisplay);

            // render a template to a string
            response = template.render(model);
        }

        // If the form was submitted, retrieve it's content.
        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            String formDisplay = "none";

            Map inputs = parseFormData(formData);

            Entry newEntry = new Entry();
            newEntry.setDate(new Date().toString());
            newEntry.setMessage(inputs.get("message").toString());
            newEntry.setName(inputs.get("name").toString());

            EntryDAO eDAO = new EntryDAO();
            eDAO.addEntry(newEntry);
            List<Entry> comments = eDAO.getAllEntrys();
            Collections.reverse(comments);

            // get a template file
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/example.twig");

            // create a model that will be passed to a template
            JtwigModel model = JtwigModel.newModel();

            // fill the model with values
            model.with("entry", newEntry);
            model.with("users_comments", comments);
            model.with("formDisplay", formDisplay);

            // render a template to a string
            response = template.render(model);

        }
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            // We have to decode the value because it's urlencoded. see: https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }
}
