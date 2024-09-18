package com.ridango;

import com.ridango.game.CocktailService;
import com.ridango.game.Cocktail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CocktailServiceTest {

    private CocktailService cocktailService;

    @BeforeEach
    void setUp() {
        cocktailService = new CocktailService();
        WebClient client = Mockito.mock(WebClient.class);
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
    }

    @Test
    void testGetRandomCocktail() throws IOException {
        Set<String> usedCocktailIDs = new HashSet<>();
        Cocktail result = cocktailService.getRandomCocktail(usedCocktailIDs);
        assertNotNull(result);
    }
}
