import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class PrivateServer {

    public static void main(String[] args) throws IOException {
        // Specify the private IP address and port
        String privateIpAddress = "192.168.77.28";
        int port = 8000;

        // Create a new HTTP server instance listening on the private IP address and port
        HttpServer server = HttpServer.create(new InetSocketAddress(privateIpAddress, port), 0);

        // Create a context for handling requests
        server.createContext("/", new MyHandler());

        // Start the server
        server.start();
        System.out.println("Server started on " + privateIpAddress + " and port " + port);
    }

    // Define a custom handler to handle incoming HTTP requests
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set the response content type
            exchange.getResponseHeaders().set("Content-Type", "text/plain");

            // Send a simple response
            String response = "Hello from the Java HTTP server running on a private IP!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
    }
}
