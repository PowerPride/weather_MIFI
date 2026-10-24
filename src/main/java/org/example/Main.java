package org.example;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String accessKey = System.getenv("API_KEY");

        HttpRequest request = HttpRequest.newBuilder()
                .header("X-Yandex-Weather-Key", accessKey)
                .uri(URI.create("https://api.weather.yandex.ru/v2/forecast?lat=52.37125&lon=4.89388&limit=3"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode weather = mapper.readTree(response.body());

        int currentTemp = weather.get("fact").get("temp").asInt();
        System.out.println("Температура на текущий момент: " + currentTemp + " градусов Цельсия");

        double sum = 0;
        for (int i = 0; i < weather.get("forecasts").size(); i++) {
            double forecast = weather.get("forecasts").get(i).get("parts").get("day").get("temp_avg").asDouble();
            sum += forecast;
        }
        double avg = sum / weather.get("forecasts").size();
        System.out.println("Средняя температура: " + avg + " градусов Цельсия");

    }
}