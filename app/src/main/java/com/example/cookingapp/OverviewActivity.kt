package com.example.cookingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class OverviewActivity : AppCompatActivity() {

    private lateinit var ingredientListView : ListView
    private var arrayAdapter: ArrayAdapter<String> ?=null
    private var ingredients = arrayOf("lorem ipsum","dolor sit amet")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        ingredientListView = findViewById(R.id.lvIngredients)
        arrayAdapter = ArrayAdapter(this, R.layout.ingredient_item,R.id.tvIngredient ,ingredients)
        ingredientListView.adapter = arrayAdapter

    }
}