package com.example.cookingapp.Listeners;

import com.example.cookingapp.Models.RandomRecipesApiResponse;

public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipesApiResponse response, String message);
    void didError(String message);
}
