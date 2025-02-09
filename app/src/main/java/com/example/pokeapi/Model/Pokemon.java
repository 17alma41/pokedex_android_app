package com.example.pokeapi.Model;

public class Pokemon {
    private String name;
    private int number;
    private String type;
    private String imageUrl;
    private String sound;

    public Pokemon(String name, int number, String type, String imageUrl, String sound) {
        this.name = name;
        this.number = number;
        this.type = type;
        this.imageUrl = imageUrl;
        this.sound = sound;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSound() {
        return sound;
    }
}

