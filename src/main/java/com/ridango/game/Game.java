package com.ridango.game;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;

@Component
@Getter
@Setter
public class Game {

    private final CocktailService cocktailService;
    private final HighScoreService highScoreService;
    private final Random rng;
    private final Scanner scanner;
    private Cocktail currentCocktail;
    private final Set<String> usedCocktailIDs;
    private final List<Integer> shownLetterIndices;
    private final List<Integer> revealedHints;
    private String playerName;
    private int numAttempts;
    private int score;
    private int numLettersRevealed;


    public Game(CocktailService cocktailService, HighScoreService highScoreService) {
        this.cocktailService = cocktailService;
        this.highScoreService = highScoreService;
        this.rng = new Random();
        this.scanner = new Scanner(System.in);
        this.usedCocktailIDs = new HashSet<>();
        this.shownLetterIndices = new ArrayList<>();
        this.revealedHints = new ArrayList<>();
        this.highScoreService.connectToSQLiteDB();
        initGameState();
    }

    public void start() {
        System.out.println("Welcome to the cocktail game!");

        try {
            currentCocktail = cocktailService.getRandomCocktail(usedCocktailIDs);
            runGameLoop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runGameLoop() throws IOException {
        showHighScores();
        promptPlayerName();

        while (true) {
            if (numAttempts == 0) {
                System.out.println("You have run out of attempts. The correct cocktail name was: " + currentCocktail.getName());
                saveHighScoreAndExit();
            }

            showCocktailInfo();
            System.out.println("Enter your guess or type 'skip' to reveal some letters or 'exit' to exit the game: ");
            System.out.print("> ");
            handlePlayerInput(scanner.nextLine().toLowerCase());
        }
    }

    private void showHighScores() {
        System.out.println("High scores:");
        highScoreService.getAllHighScores().forEach(highScore ->
                System.out.println("Player: " + highScore.getPlayerName() + " Score: " + highScore.getScore()));
    }

    private void promptPlayerName() {
        System.out.print("Please enter your name: ");
        playerName = scanner.nextLine();
        if (!isValidInput(playerName)) {
            System.out.println("Invalid input. Exiting game...");
            System.exit(0);
        }
    }

    public void handlePlayerInput(String userInput) throws IOException {
        switch (userInput) {
            case "skip" -> {
                System.out.println("Skipping round...");
                GameUtils.revealRandomLetters(currentCocktail.getName(), shownLetterIndices, rng, GameUtils.getMaxLettersToReveal(currentCocktail.getName()), numLettersRevealed);
            }
            case "exit" -> saveHighScoreAndExit();
            default -> processGuess(userInput);
        }
    }

    private void processGuess(String guess) throws IOException {
        if (guess.equalsIgnoreCase(currentCocktail.getName())) {
            score += numAttempts;
            System.out.println("Congratulations! You guessed the correct cocktail name!");
            usedCocktailIDs.add(currentCocktail.getIdDrink());
            shownLetterIndices.clear();
            revealedHints.clear();
            numAttempts = 5;
            numLettersRevealed = 0;
            currentCocktail = cocktailService.getRandomCocktail(usedCocktailIDs);
        } else {
            System.out.print("Incorrect guess. ");
            GameUtils.revealRandomLetters(currentCocktail.getName(), shownLetterIndices, rng, GameUtils.getMaxLettersToReveal(currentCocktail.getName()), numLettersRevealed);
            numAttempts--;
            if (numAttempts > 0) System.out.println("Try again!");
        }
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

    private void saveHighScoreAndExit() {
        if (score > 0) {
            highScoreService.addHighScore(playerName, score);
        }
        System.out.println("Exiting game...");
        System.exit(0);
    }

    /**
     * Check if the player name contains any SQL injection patterns
     * @param playerName string entered by the player
     * @return true if the input contains common SQL commands masked as player name
     */
    private boolean isValidInput(String playerName) {
        String sqlInjectionPattern = "('.+--)|(--)|(%7C)|([';|])|((?i)or|and|select|insert|update|delete|drop|union|alter|truncate)";
        return !playerName.matches(sqlInjectionPattern);
    }

    public void initGameState() {
        usedCocktailIDs.clear();
        shownLetterIndices.clear();
        revealedHints.clear();
        numAttempts = 5;
        numLettersRevealed = 0;
        score = 0;
    }

    @PreDestroy
    public void onExit() {
        highScoreService.closeConnection();
    }
}
