package com.example.cookingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.blogspot.atifsoftwares.animatoolib.Animatoo

class OverviewActivity : AppCompatActivity() {

    private lateinit var ingredientListView : ListView
    private var arrayAdapter: ArrayAdapter<String> ?=null
    private var ingredients = mutableListOf<String>("lorem ipsum","dolor sit amet")
    private lateinit var btnOverview : ImageButton
    private lateinit var btnOverviewBack : ImageButton
    private lateinit var btnHeart : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Light)
        setContentView(R.layout.activity_overview)


        var appliance1 = findViewById<TextView>(R.id.appliance1)
        var appliance2 = findViewById<TextView>(R.id.appliance2)
        var appliance3 = findViewById<TextView>(R.id.appliance3)
        var appliance4 = findViewById<TextView>(R.id.appliance4)

        btnOverview = findViewById(R.id.btnOverview)
        btnOverviewBack = findViewById(R.id.btnOverviewBack)
        btnHeart = findViewById(R.id.btnHeart)


        ingredientListView = findViewById(R.id.lvIngredients)
        arrayAdapter = ArrayAdapter(this, R.layout.ingredient_item,R.id.tvIngredient ,ingredients)
        ingredientListView.adapter = arrayAdapter


        //when api response received, add ingredients like this
        ingredients.add("Consectetur adipiscing elit")
        ingredients.add("In vestibulum")

        //if api response contains appliances, edit text like this
        appliance1.text = "Cast iron"
        appliance2.text = "Rice cooker"

        btnOverview.setOnClickListener{
            val intent = Intent(this,PrepActivity::class.java)
            //this is the current activity, and SecondActivity is the one we want to navigate to
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        btnOverviewBack.setOnClickListener{
            finish()
            Animatoo.animateSlideRight(this)
        }

        btnHeart.setOnClickListener{
            btnHeart.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_heart));
            Toast.makeText(this, "Recipe saved to Favorites!", Toast.LENGTH_LONG).show()
            //do something when recipe is favorited
        }
    }
}