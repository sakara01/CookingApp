package com.example.cookingapp;

import android.content.Context;

import com.example.cookingapp.Listeners.RandomRecipeResponseListener;
import com.example.cookingapp.Models.RandomRecipesApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getRandomRecipes(RandomRecipeResponseListener listener){
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipesApiResponse> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key), "10");
        call.enqueue(new Callback<RandomRecipesApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipesApiResponse> call, Response<RandomRecipesApiResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipesApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());

            }
        });
    }

    private interface CallRandomRecipes{
        @GET("recipes/random")
        Call<RandomRecipesApiResponse> callRandomRecipe (
                @Query("apiKey") String apiKey,
                @Query("number") String number
        );

    }
}
