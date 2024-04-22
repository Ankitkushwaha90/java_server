import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LocalhostForwarder {

    // localhost_ip

    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            System.err.println("Usage: java LocalhostForwarder <localhost_ip> <localhost_port> <private_ip> <private_port>");
            System.exit(1);
        }

        // Get localhost IP address and port from command-line arguments
        String localhostIp = args[0];
        int localhostPort = Integer.parseInt(args[1]);

        // Get private IP address and port from command-line arguments
        String privateIp = args[2];
        int privatePort = Integer.parseInt(args[3]);

        // Create a new HTTP server instance listening on the localhost IP address and port
        HttpServer server = HttpServer.create(new InetSocketAddress(localhostIp, localhostPort), 0);

        // Create a context for handling requests
        server.createContext("/", new MyHandler(privateIp, privatePort));

        // Start the server
        server.start();
        System.out.println("Forwarding server started on " + localhostIp + " and port " + localhostPort);
    }

    // Define a custom handler to forward incoming HTTP requests
    static class MyHandler implements HttpHandler {
        private String privateIp;
        private int privatePort;

        public MyHandler(String privateIp, int privatePort) {
            this.privateIp = privateIp;
            this.privatePort = privatePort;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Forward the request to the private server
            String url = "http://" + privateIp + ":" + privatePort + exchange.getRequestURI();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(exchange.getRequestMethod());

            // Copy request headers
            exchange.getRequestHeaders().forEach((key, value) -> {
                connection.setRequestProperty(key, value.get(0));
            });

            // Get the response from the private server
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();

            // Set the response headers
            exchange.getResponseHeaders().putAll(connection.getHeaderFields());
            exchange.sendResponseHeaders(responseCode, 0);

            // Send the response body
            OutputStream outputStream = exchange.getResponseBody();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                outputStream.write(line.getBytes());
            }
            scanner.close();
            outputStream.close();
        }
    }
}
