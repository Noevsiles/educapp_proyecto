package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.service.BreedServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BreedService implements BreedServiceInterface {

    private final RestTemplate restTemplate;

    @Autowired
    public BreedService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    // Obtener todas las razas de la api dog.ceo
    @Override
    public List<String> getAllBreeds() {
        String url = "https://dog.ceo/api/breeds/list/all";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, List<String>> breeds = (Map<String, List<String>>) response.get("message");

        List<String> allBreeds = new ArrayList<>();
        for (String breed : breeds.keySet()) {
            List<String> subBreeds = breeds.get(breed);
            if (subBreeds.isEmpty()) {
                allBreeds.add(breed);
            } else {
                for (String sub : subBreeds) {
                    allBreeds.add(sub + " " + breed);
                }
            }
        }
        return allBreeds;
    }

    // Obtener todas las imagenes asociadas a las razas de dog.ceo
    @Override
    public List<String> getBreedImages(String breedName) {
        // manejar subrazas si vienen en formato "sub breed"
        String[] parts = breedName.split(" ");
        String url;
        if (parts.length == 2) {
            url = String.format("https://dog.ceo/api/breed/%s/%s/images", parts[1], parts[0]);
        } else {
            url = String.format("https://dog.ceo/api/breed/%s/images", breedName);
        }

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<String>) response.get("message");
    }
}
