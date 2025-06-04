package server.basic;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

public class CurrencyData {
    private final static String listUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json"; 
    private final static String valueUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json"; 

    public static String getCurrencyList() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(listUrl))
            .build();
        return client.sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .join();
    }

    public static Double getValueInEuros(String currency) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(valueUrl))
            .build();
        String response = client.sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .join();

        Gson gson = new Gson();
        CurrencyResponse responseObj = gson.fromJson(response, CurrencyResponse.class);
        return responseObj.eur.get(currency);
    } 
}
