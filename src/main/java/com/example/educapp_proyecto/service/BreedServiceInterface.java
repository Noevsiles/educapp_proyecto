package com.example.educapp_proyecto.service;

import java.util.List;

public interface BreedServiceInterface {
    List<String> getAllBreeds();
    List<String> getBreedImages(String breedName);
}
