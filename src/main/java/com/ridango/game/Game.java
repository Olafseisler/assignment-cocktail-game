package com.ridango.game;


import com.fasterxml.jackson.core.JsonParser;
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
import java.util.*;

public class Game {

    final String API_URL = "https://www.thecocktaildb.com/api/json/v1/1/";
    WebClient client;
    ObjectMapper objectMapper;
    JsonParser jsonParser;
    Cocktail currentCocktail;
    Set<String> usedCocktailIDs;
    Random rng;
    Scanner scanner;
    int numAttempts;
    int numLettersRevealed;
    int score;

    List<Integer> shownLetterIndices;

    public Game() {
        try {
            client = WebClient.create(API_URL);
            objectMapper = new ObjectMapper();
            usedCocktailIDs = new HashSet<>();
            rng = new Random();
            scanner = new Scanner(System.in);
            shownLetterIndices = new ArrayList<>();
            jsonParser = objectMapper.getFactory().createParser("");
            initGameState();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initGameState() {
        usedCocktailIDs.clear();
        shownLetterIndices.clear();
        numAttempts = 5;
        numLettersRevealed = 0;
    }

    public void start() {
        System.out.println("Welcome to the cocktail game!");
        System.out.println("You will be presented with a cocktail recipe and you will have to guess the name of the cocktail.");
        System.out.println("You can guess the name of the cocktail by typing the letters into the console.");
        System.out.println("Alternatively, you can skip the round by typing 'skip' if you feel like getting a hint," + " or 'exit' if you don't wish to play anymore.");
        System.out.println("Good luck!");
        try {
            currentCocktail = getCocktailInfoFromAPI();
            runGameLoop();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void runGameLoop() throws IOException {
        while (true) {
            if (numAttempts == 0) {
                System.out.println("You have run out of attempts. The correct cocktail name was: " + currentCocktail.getName());
                System.out.println("Your score is: " + score);
                System.out.println("Exiting game...");
                System.exit(0);
            }
            showCocktailInfo();
            System.out.println("Attempts left: " + numAttempts);
            System.out.print("Please enter your guess or command: ");
            String userInput = scanner.nextLine().toLowerCase();

            switch (userInput) {
                case "skip" -> {
                    System.out.println("Skipping round...");
                    revealRandomLetters();
                }
                case "exit" -> {
                    System.out.println("Exiting game...");
                    System.exit(0);
                }
                default -> {
                    if (userInput.equals(currentCocktail.getName().toLowerCase())) {
                        score += numAttempts;
                        System.out.println("Congratulations! You guessed the correct cocktail name!");
                        System.out.println("Your score is: " + score);
                        usedCocktailIDs.add(currentCocktail.getIdDrink());
                        initGameState();
                        var newCocktail = getCocktailInfoFromAPI();
                        if (usedCocktailIDs.contains(newCocktail.getIdDrink())) {
                            continue; // Skip this cocktail if it has already been used
                        }
                    } else {
                        System.out.print("Incorrect guess. ");
                        revealRandomLetters();
                        numAttempts--;

                        if (numAttempts > 0) {
                            System.out.println("Try again!");
                        }
                    }
                }
            }
        }
    }

    public Cocktail getCocktailInfoFromAPI() throws IOException {
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

        return objectMapper.treeToValue(drinkNode, Cocktail.class);
    }

    public void showCocktailInfo() {
        System.out.print("Cocktail: ");
        for (int i = 0; i < currentCocktail.getName().length(); i++) {
            if (shownLetterIndices.contains(i)) {
                System.out.print(currentCocktail.getName().charAt(i));
            } else if (currentCocktail.getName().charAt(i) == ' ') {
                System.out.print(" ");
            } else {
                System.out.print("_");
            }
        }
        System.out.println();
    }

    int getMaxLettersToReveal() {
        return Math.max(1, currentCocktail.getName().length() / 2 + 1);
    }

    void revealRandomLetters() {
        int maxRevealed = getMaxLettersToReveal();
        // Generate random int from 1 to 3
        int numToReveal = rng.nextInt(3) + 1;
        int i = 0;
        while (i < numToReveal) {
            if (numLettersRevealed >= maxRevealed) {
                return;
            }
            int randomIndex = rng.nextInt(currentCocktail.getName().length());
            if (!shownLetterIndices.contains(randomIndex)) {
                shownLetterIndices.add(randomIndex);
                numLettersRevealed++;
            }
            i++;
        }
    }

    void revealCocktailTrivia() {
        System.out.print("Here's some hints: ");
        int randomIndex = rng.nextInt(3);
        switch (randomIndex) {
            case 0 -> {
                if (currentCocktail.getCategory() != null) {
                    System.out.println("Category: " + currentCocktail.getCategory());
                }
            }
            case 1 -> {
                if (currentCocktail.getInstructions() != null) {
                    System.out.println("Instructions: " + currentCocktail.getInstructions());
                }
            }
            case 2 -> {
                if (currentCocktail.getGlass() != null) {
                    System.out.println("Glass: " + currentCocktail.getGlass());
                }
            }
        }
    }



}
