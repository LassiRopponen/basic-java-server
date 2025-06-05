package server.basic;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/currencies", new CurrenciesHandler());
        server.createContext("/value", new ValueHandler());

        server.setExecutor(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        server.start();

        System.out.println("Server is running on port 8000");
    }

    static class CurrenciesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = CurrencyData.getCurrencyList();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class ValueHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String code = exchange.getRequestURI().getQuery();
            Double value = CurrencyData.getValueInEuros(code);
            String response = "";
            if (value == null) {
                response = "No value found for given currency.";
                exchange.sendResponseHeaders(404, response.getBytes().length);
            }
            else {
                String currency = CurrencyData.getCurrencyName(code);
                response = String.format("One euro is worth %.2f units of %s.", value, currency);
                exchange.sendResponseHeaders(200, response.getBytes().length);
            }
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
