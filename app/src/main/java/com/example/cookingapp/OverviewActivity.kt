package com.example.cookingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.cookingapp.Adapters.AppliancesAdapter
import com.example.cookingapp.Listeners.RecipeDetailsListener
import com.example.cookingapp.Models.RecipeDetailsResponse
import org.w3c.dom.Text
import com.squareup.picasso.Picasso;
import com.example.cookingapp.CircleTransform

/**
 * Represents the recipe overview page when a recipe is clicked into
 */
class OverviewActivity : AppCompatActivity() {
    /**
     * Recipe ID Number
     */
    var id: Int = 0
    /**
     * Recipe Title
     */
    lateinit var recipeTitle : TextView
    /**
     * Author of the Recipe
     */
    lateinit var author : TextView
    /**
     * Length of time the recipe takes to make
     */
    lateinit var tvTime : TextView
    /**
     * Amount of servings the recipe is made for
     */
    lateinit var tvServings : TextView
    /**
     * Length of ingredients
     */
    lateinit var tvIngredientCount : TextView
    /**
     * Thumbnail image of the recipe
     */
    lateinit var recipeImage : ImageView
    lateinit var manager : RequestManager

    /**
     * Visual list of the ingredients
     */
    private lateinit var ingredientListView : ListView

    /**
     * Array holding the ingredients
     */
    val ingredients: MutableList<String> = ArrayList()
    private var arrayAdapter: ArrayAdapter<String> ?=null

    private lateinit var btnOverview : ImageButton
    private lateinit var btnOverviewBack : ImageButton
    private lateinit var btnHeart : ImageButton
    private lateinit var btnIngredientsPlus : FrameLayout
    private lateinit var btnIngredientsMinus : FrameLayout

    /**
     * On create lifecycle hook
     *
     * We set all the recipe information into the frontend and
     * initialize each element to listen for on element clicked
     *
     * @param savedInstanceState
     * @see OverviewActivity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Light)
        setContentView(R.layout.activity_overview)
        findViews()

        id = getIntent().getStringExtra("id")?.toIntOrNull() ?: 0
        manager = RequestManager(this)
        manager.getRecipeDetails(recipeDetailsListener, id)

        btnOverview = findViewById(R.id.btnOverview)
        btnOverviewBack = findViewById(R.id.btnOverviewBack)
        btnHeart = findViewById(R.id.btnHeart)
        btnIngredientsPlus = findViewById(R.id.btnIngredientsPlus)
        btnIngredientsMinus = findViewById(R.id.btnIngredientsMinus)
        /*servings = findViewById<TextView>(R.id.tvServings)
        time = findViewById<TextView>(R.id.tvTime)
        numIng = findViewById<TextView>(R.id.tvIngredients)

        ingredientListView = findViewById(R.id.lvIngredients)
        arrayAdapter = ArrayAdapter(this, R.layout.ingredient_item,R.id.tvIngredient ,ingredients)
        ingredientListView.adapter = arrayAdapter


        //when api response received, add ingredients like this
        ingredients.add("Consectetur adipiscing elit")
        ingredients.add("In vestibulum")

        //if api response contains appliances, edit text like this
        /*appliance1.text = "Oven"
        appliance2.text = "Rice cooker"*/

        //when api response received, update serving, time, and num of ingredients like this
        servings.text = "1"
        time.text = "2 min"
        numIng.text = "4"*/

        btnOverview.setOnClickListener{
            val intent = Intent(this,PrepActivity::class.java)
            intent.putExtra("id", id.toString())
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

        //increment servings
        btnIngredientsPlus.setOnClickListener{
            tvServings.text = (Integer.parseInt(tvServings.text.toString()) + 1).toString()
        }
        btnIngredientsMinus.setOnClickListener{
            if (Integer.parseInt(tvServings.text.toString()) > 0) {
                tvServings.text = (Integer.parseInt(tvServings.text.toString()) - 1).toString()
            }
        }
    }

    /**
     * A quick method for assigning frontend objects to their corresponding variables
     *
     * @see findViewById
     */
    private fun findViews() {
        recipeTitle = findViewById(R.id.recipeTitle)
        author = findViewById(R.id.author)
        tvTime = findViewById(R.id.tvTime)
        tvServings = findViewById(R.id.tvServings)
        tvIngredientCount = findViewById(R.id.tvIngredientCount)
        ingredientListView = findViewById(R.id.lvIngredients)
        recipeImage = findViewById(R.id.imageView_food)

        //recycler_recipe_ingredients = findViewById(R.id.recycler_recipe_ingredients)
        //recycler_recipe_appliances = findViewById(R.id.recycler_recipe_appliances)
   }

    /**
     * Fetches the recipe information
     *
     * @see RecipeDetailsListener
     */
   private val recipeDetailsListener = object : RecipeDetailsListener {
        /**
         * Fetches the recipe information
         *
         * @param response The response
         * @param message The response message
         * @see RecipeDetailsListener
         */
       override fun didFetch(response: RecipeDetailsResponse?, message: String?) {
           if (response != null) {
               recipeTitle.text = response.title
               author.text = response.sourceName
               tvTime.text = response.readyInMinutes.toString()
               tvServings.text = response.servings.toString()
               tvIngredientCount.text = response.extendedIngredients.size.toString()
               Log.d("Image URL", response.image);
               Picasso.get().load(response.image)
                   .placeholder(R.drawable.circle)
                   .transform(CircleTransform())
                   .into(recipeImage)

               ingredients.clear()
               for (i in 0 until response.extendedIngredients.size) {
                   ingredients.add(response.extendedIngredients[i].original)
               }
               arrayAdapter = ArrayAdapter(this@OverviewActivity, R.layout.ingredient_item,R.id.tvIngredient ,ingredients)
               ingredientListView.adapter = arrayAdapter

               //recycler_recipe_appliances.setHasFixedSize(true)
               //recycler_recipe_appliances.layoutManager = LinearLayoutManager(this@OverviewActivity, LinearLayoutManager.HORIZONTAL, false)
               //appliancesAdapter = AppliancesAdapter(this@OverviewActivity, response.extendedIngredients)
               //recycler_recipe_appliances.adapter = appliancesAdapter
           }
           else {
               recipeTitle.text = ""
               author.text = ""
               tvTime.text = ""
               tvServings.text = ""
               tvIngredientCount.text = ""
           }
       }
        /**
         * Error in the request
         *
         * @param message The error message
         * @see RecipeDetailsListener
         * @see Toast
         */
       override fun didError(message: String?) {
           Toast.makeText(this@OverviewActivity, message, Toast.LENGTH_SHORT).show()
       }
   }
}