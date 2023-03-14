package com.example.cookingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.HorizontalScrollView
import android.widget.ListView
import android.widget.TextView

class OverviewActivity : AppCompatActivity() {

    private lateinit var ingredientListView : ListView
    private var arrayAdapter: ArrayAdapter<String> ?=null
    private var ingredients = mutableListOf<String>("lorem ipsum","dolor sit amet")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        ingredientListView = findViewById(R.id.lvIngredients)
        arrayAdapter = ArrayAdapter(this, R.layout.ingredient_item,R.id.tvIngredient ,ingredients)
        ingredientListView.adapter = arrayAdapter


        //when api response received, add ingredients like this
        ingredients.add("Consectetur adipiscing elit")
        ingredients.add("In vestibulum")


        var appliance1 = findViewById<TextView>(R.id.appliance1)
        var appliance2 = findViewById<TextView>(R.id.appliance2)
        var appliance3 = findViewById<TextView>(R.id.appliance3)
        var appliance4 = findViewById<TextView>(R.id.appliance4)

        //if api response contains appliances, edit text like this
        appliance1.text = "Cast iron"
        appliance2.text = "Rice cooker"


    }
}