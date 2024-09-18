package com.ridango.game;

import java.util.List;
import java.util.Random;

public class GameUtils {

    public static int getMaxLettersToReveal(String cocktailName) {
        return Math.max(1, cocktailName.length() / 2);
    }

    public static void revealRandomLetters(String cocktailName, List<Integer> shownLetterIndices, Random rng,
                                           int maxLettersToReveal, int numLettersRevealed) {
        int numToReveal = rng.nextInt(3) + 1;
        int i = 0;
        while (i < numToReveal) {
            if (numLettersRevealed >= maxLettersToReveal) {
                return;  // Stop revealing letters if limit is reached
            }
            int randomIndex = rng.nextInt(cocktailName.length());
            if (!shownLetterIndices.contains(randomIndex)) {
                shownLetterIndices.add(randomIndex);
                numLettersRevealed++;
            }
            i++;
        }
    }

    public static void revealCocktailTrivia(Cocktail currentCocktail, List<Integer> revealedHints, Random rng) {
        System.out.print("Here's some hints: ");
        int randomIndex = rng.nextInt(3);

        if (revealedHints.contains(randomIndex) && revealedHints.size() < 3) {
            revealCocktailTrivia(currentCocktail, revealedHints, rng);
            return;
        }

        switch (randomIndex) {
            case 0 -> {
                if (currentCocktail.getCategory() != null) {
                    System.out.println("Category: " + currentCocktail.getCategory());
                    revealedHints.add(randomIndex);
                }
            }
            case 1 -> {
                if (currentCocktail.getInstructions() != null) {
                    System.out.println("Instructions: " + currentCocktail.getInstructions());
                    revealedHints.add(randomIndex);
                }
            }
            case 2 -> {
                if (currentCocktail.getGlass() != null) {
                    System.out.println("Glass: " + currentCocktail.getGlass());
                    revealedHints.add(randomIndex);
                }
            }
        }
    }
}
