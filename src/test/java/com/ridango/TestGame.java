package com.ridango;
import com.ridango.game.Game;
// Import JUnit
import org.junit.jupiter.api.Test;

public class TestGame {

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    @Test
    public void testGetCocktailInfoFromAPI() {
        Game game = new Game();
        try {
            var cocktailInfo = game.getCocktailInfoFromAPI();
            // Check that the API returns a non-null non-empty string for a cocktail name
            assert(cocktailInfo != null);
            assert(cocktailInfo.getName() != null);
            assert(!cocktailInfo.getName().isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
