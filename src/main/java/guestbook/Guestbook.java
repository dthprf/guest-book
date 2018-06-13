package guestbook;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class Guestbook implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();


        // Send a form if it wasn't submitted yet.
        if(method.equals("GET")){
            response = "<html><body>" +
                    "<form method=\"POST\">\n" +
                    "  Message:<br>\n" +
                    "  <input type=\"text\" name=\"message\" value=\"Type your message...\">\n" +
                    "  <br>\n" +
                    "  Name:<br>\n" +
                    "  <input type=\"text\" name=\"name\" value=\"Enter your name...\">\n" +
                    "  <br><br>\n" +
                    "  <input type=\"submit\" value=\"Submit\">\n" +
                    "</form> " +
                    "</body></html>";
        }

        // If the form was submitted, retrieve it's content.
        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            System.out.println(formData);
            Map inputs = parseFormData(formData);

            Entry newEntry = new Entry();
            newEntry.setDate(new Date().toString());
            newEntry.setMessage(inputs.get("message").toString());
            newEntry.setName(inputs.get("name").toString());

            System.out.println(newEntry.getName());
            System.out.println(newEntry.getMessage());
            System.out.println(newEntry.getDate());

            EntryDAO eDAO = new EntryDAO();
            eDAO.addEntry(newEntry);
            List<Entry> comments = eDAO.getAllEntrys();

            for (Entry entry : comments) {
                System.out.println(entry);
            }

            response = "<html><body><h1>GUEST BOOK</h1>" +
                    "<h2><b>" + inputs.get("message") + "</b><br>Name: " + inputs.get("name") +
                    "</h2>" +
                    "</body><html>";
        }



        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * Form data is sent as a urlencoded string. Thus we have to parse this string to get data that we want.
     * See: https://en.wikipedia.org/wiki/POST_(HTTP)
     */
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
