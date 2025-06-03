package server.basic;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import com.google.gson.Gson;

public class Server {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json"))
            .build();
        String response = client.sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .join();
        System.out.println(response);

        Gson gson = new Gson();
        CurrencyResponse responseObj = gson.fromJson(response, CurrencyResponse.class);
        System.out.println(responseObj.eur.get("usd"));
    }
}
