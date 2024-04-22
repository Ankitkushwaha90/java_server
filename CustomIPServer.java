import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class CustomIPServer {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: java CustomIPServer <ip_address> <port>");
            System.exit(1);
        }

        // Get IP address and port from command-line arguments
        String ipAddress = args[0];
        int port = Integer.parseInt(args[1]);

        // Create a new HTTP server instance listening on the specified IP address and port
        HttpServer server = HttpServer.create(new InetSocketAddress(ipAddress, port), 0);

        // Create a context for handling requests
        server.createContext("/", new MyHandler());

        // Start the server
        server.start();
        System.out.println("Server started on " + ipAddress + " and port " + port);
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
