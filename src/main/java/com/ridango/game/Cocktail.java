package com.ridango.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Cocktail {

    @JsonProperty("idDrink")
    private String idDrink;

    @JsonProperty("strDrink")
    private String name;

    @JsonProperty("strDrinkAlternate")
    private String alternateName;

    @JsonProperty("strTags")
    private String tags;

    @JsonProperty("strVideo")
    private String video;

    @JsonProperty("strCategory")
    private String category;

    @JsonProperty("strIBA")
    private String iba;

    @JsonProperty("strAlcoholic")
    private String alcoholic;

    @JsonProperty("strGlass")
    private String glass;

    @JsonProperty("strInstructions")
    private String instructions;

    @JsonProperty("strInstructionsES")
    private String instructionsES;

    @JsonProperty("strInstructionsDE")
    private String instructionsDE;

    @JsonProperty("strInstructionsFR")
    private String instructionsFR;


    @JsonProperty("strInstructionsIT")
    private String instructionsIT;

    @JsonProperty("strInstructionsZH-HANS")
    private String instructionsZH_HANS;

    @JsonProperty("strInstructionsZH-HANT")
    private String instructionsZH_HANT;

    @JsonProperty("strDrinkThumb")
    private String imageUrl;

    @JsonProperty("strImageSource")
    private String imgUrl;

    @JsonProperty("strImageAttribution")
    private String imgAttribution;

    @JsonProperty("strCreativeCommonsConfirmed")
    private String creativeCommonsConfirmed;

    @JsonProperty("dateModified")
    private String dateModified;

    // 15 ingredients
    @JsonProperty("strIngredient1")
    private String ingredient1;

    @JsonProperty("strIngredient2")
    private String ingredient2;

    @JsonProperty("strIngredient3")
    private String ingredient3;

    @JsonProperty("strIngredient4")
    private String ingredient4;

    @JsonProperty("strIngredient5")
    private String ingredient5;

    @JsonProperty("strIngredient6")
    private String ingredient6;

    @JsonProperty("strIngredient7")
    private String ingredient7;

    @JsonProperty("strIngredient8")
    private String ingredient8;

    @JsonProperty("strIngredient9")
    private String ingredient9;

    @JsonProperty("strIngredient10")
    private String ingredient10;

    @JsonProperty("strIngredient11")
    private String ingredient11;

    @JsonProperty("strIngredient12")
    private String ingredient12;

    @JsonProperty("strIngredient13")
    private String ingredient13;

    @JsonProperty("strIngredient14")
    private String ingredient14;

    @JsonProperty("strIngredient15")
    private String ingredient15;

    // 15 Measures
    @JsonProperty("strMeasure1")
    private String Measure1;

    @JsonProperty("strMeasure2")
    private String Measure2;

    @JsonProperty("strMeasure3")
    private String Measure3;

    @JsonProperty("strMeasure4")
    private String Measure4;

    @JsonProperty("strMeasure5")
    private String Measure5;

    @JsonProperty("strMeasure6")
    private String Measure6;

    @JsonProperty("strMeasure7")
    private String Measure7;

    @JsonProperty("strMeasure8")
    private String Measure8;

    @JsonProperty("strMeasure9")
    private String Measure9;

    @JsonProperty("strMeasure10")
    private String Measure10;

    @JsonProperty("strMeasure11")
    private String Measure11;

    @JsonProperty("strMeasure12")
    private String Measure12;

    @JsonProperty("strMeasure13")
    private String Measure13;

    @JsonProperty("strMeasure14")
    private String Measure14;

    @JsonProperty("strMeasure15")
    private String Measure15;
}
