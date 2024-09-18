package com.ridango.game;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.*;


@Service
public class CocktailService {

    private final String API_URL = "https://www.thecocktaildb.com/api/json/v1/1/";
    private final WebClient client;
    private final ObjectMapper objectMapper;
    private final Set<String> usedCocktailIDs = new HashSet<>();

    public CocktailService() {
        this.client = WebClient.create(API_URL);
        this.objectMapper = new ObjectMapper();
    }

    public Cocktail getRandomCocktail(Set<String> usedCocktailIDs) throws IOException {
        System.out.println("Getting cocktail from API...");

        // Get random cocktail
        var bodySpec = client.method(HttpMethod.GET).uri("random.php");
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.body(BodyInserters.fromValue("data"));
        WebClient.ResponseSpec responseSpec = headersSpec.header(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())
                .retrieve();

        Mono<String> response = responseSpec.bodyToMono(String.class);

        JsonNode parentNode = objectMapper.readTree(response.block());
        JsonNode drinkNode = parentNode.get("drinks").get(0);

        var value = objectMapper.treeToValue(drinkNode, Cocktail.class);
        if (usedCocktailIDs.contains(value.getIdDrink())) {
            return getRandomCocktail(usedCocktailIDs); // Recursive call to get a new cocktail if
                                        // the current one has already been used
        }

        return value;
    }
}
