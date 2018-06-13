package guestbook;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {

        int port = 8000;
        int backlog = 0;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), backlog);

        // set routes
        server.createContext("/guestbook", new Guestbook());
        server.createContext("/guestbookEntrys", new GuestBookEntrys());
        server.createContext("/template", new Template());
        server.createContext("/static", new Static());
        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();
    }
}