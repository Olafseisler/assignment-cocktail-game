package com.ridango.game;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HighScore {

    private Long id;
    private String playerName;
    private int score;

    public HighScore() {
        // Default constructor
    }

    public HighScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }
}