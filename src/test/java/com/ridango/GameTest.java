package com.ridango;

import com.ridango.game.Cocktail;
import com.ridango.game.CocktailService;
import com.ridango.game.Game;
import com.ridango.game.HighScoreService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        CocktailService cocktailService = Mockito.mock(CocktailService.class);
        HighScoreService highScoreService = Mockito.mock(HighScoreService.class);
        game = new Game(cocktailService, highScoreService);
        Set<String> usedCocktailIDs = new HashSet<>();
    }

    @Test
    void testInitGameState() {
        game.initGameState();
        assertEquals(5, game.getNumAttempts());
        assertEquals(0, game.getScore());
    }

    @Test
    void testProcessCorrectGuess() throws IOException {
        Cocktail mockCocktail = new Cocktail();
        mockCocktail.setName("Margarita");
        game.setCurrentCocktail(mockCocktail);
        game.setNumAttempts(5);

        game.handlePlayerInput("Margarita");

        assertEquals(5, game.getScore());  // Score should increase based on attempts left
    }

    @Test
    void testProcessIncorrectGuess() throws IOException {
        Cocktail mockCocktail = new Cocktail();
        mockCocktail.setName("Margarita");
        game.setCurrentCocktail(mockCocktail);
        game.setNumAttempts(5);

        game.handlePlayerInput("wrongguess");

        assertEquals(4, game.getNumAttempts());  // Attempts should decrease
    }

//    @Test
//    void testSkipRound() throws IOException {
//        Cocktail mockCocktail = new Cocktail();
//        mockCocktail.setName("Margarita");
//        game.setCurrentCocktail(mockCocktail);
//
//        List<Integer> shownLetters = new ArrayList<>();
//
//
//        game.handlePlayerInput("skip");
//
//        assertFalse(shownLetters.isEmpty());  // Ensure letters are revealed after skipping
//    }
}
