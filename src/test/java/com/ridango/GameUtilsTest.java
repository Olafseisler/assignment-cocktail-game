package com.ridango;

import com.ridango.game.GameUtils;
import com.ridango.game.Cocktail;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameUtilsTest {

    @Test
    void testGetMaxLettersToReveal() {
        String cocktailName = "Margarita";
        int maxLetters = GameUtils.getMaxLettersToReveal(cocktailName);

        assertEquals(4, maxLetters);  // Half the length of "Margarita" is 4
    }

    @Test
    void testRevealRandomLetters() {
        String cocktailName = "Margarita";
        List<Integer> shownLetters = new ArrayList<>();
        Random random = new Random();
        int maxLetters = GameUtils.getMaxLettersToReveal(cocktailName);

        int numLettersRevealed = 0;
        for (int i = 0; i < 4; i++) {
            GameUtils.revealRandomLetters(cocktailName, shownLetters, random, maxLetters, numLettersRevealed);
            numLettersRevealed += shownLetters.size();
        }

        assertFalse(shownLetters.isEmpty());
        assertTrue(shownLetters.size() <= maxLetters);
    }

    @Test
    void testRevealCocktailTrivia() {
        Cocktail mockCocktail = new Cocktail();
        mockCocktail.setCategory("Ordinary Drink");
        mockCocktail.setInstructions("Mix ingredients.");
        mockCocktail.setGlass("Highball glass");

        List<Integer> shownHints = new ArrayList<>();
        Random random = new Random();

        GameUtils.revealCocktailTrivia(mockCocktail, shownHints, random);

        assertFalse(shownHints.isEmpty());  // Ensure at least one hint is shown

        // Ensure that the hint is one of the three possible hints
        assertTrue(shownHints.contains(0) || shownHints.contains(1) || shownHints.contains(2));
    }

}
