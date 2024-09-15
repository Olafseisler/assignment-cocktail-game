package com.ridango.game;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

public class Game {

    final String API_URL = "https://www.thecocktaildb.com/api/json/v1/1/";
    WebClient client;
    ObjectMapper objectMapper;
    JsonParser jsonParser;

    String currentCocktailName;

    public Game() {
        try {
            client = WebClient.create(API_URL);
            objectMapper = new ObjectMapper();
            jsonParser = objectMapper.getFactory().createParser("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Welcome to the cocktail game!");
        System.out.println("You will be presented with a cocktail recipe and you will have to guess the name of the cocktail.");
        System.out.println("You can guess the name of the cocktail by typing the letters into the console.");
        System.out.println("Good luck!");
        try {
            var result = getCocktailInfoFromAPI();
            System.out.println(result.getName());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public Cocktail getCocktailInfoFromAPI() throws JsonProcessingException, IllegalArgumentException {
        System.out.println("Getting cocktail from API...");

        // Get random cocktail
        var bodySpec = client.method(HttpMethod.GET).uri("random.php");
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.body(BodyInserters.fromValue("data"));
        WebClient.ResponseSpec responseSpec = headersSpec.header(
            HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())
        .retrieve();

        Mono<String> response = responseSpec.bodyToMono(String.class);

        JsonNode parentNode = objectMapper.readTree(response.block());
        JsonNode drinkNode = parentNode.get("drinks").get(0);

        return objectMapper.treeToValue(drinkNode, Cocktail.class);
    }

}
