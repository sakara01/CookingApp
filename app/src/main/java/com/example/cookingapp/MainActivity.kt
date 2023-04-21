package com.example.cookingapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.cookingapp.Adapters.RandomRecipeAdapter
import com.example.cookingapp.Listeners.RandomRecipeResponseListener
import com.example.cookingapp.Listeners.RecipeClickListener
import com.example.cookingapp.Models.RandomRecipesApiResponse
import com.google.android.material.color.MaterialColors
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var dialog: ProgressDialog
    lateinit var manager: RequestManager
    lateinit var randomRecipeAdapter: RandomRecipeAdapter
    lateinit var recyclerView: RecyclerView

    private lateinit var btnPrep: Button
    private lateinit var btnOverview: Button
    private lateinit var btnTheme: Button
    private var currentTheme: Int =0
    private lateinit var tvFeatured : TextView
    private lateinit var tvPopular : TextView
    private lateinit var tvRecommended : TextView
    private lateinit var searchMic : ImageView
    private lateinit var speechRecognizer : SpeechRecognizer

    val tags: MutableList<String> = ArrayList()
    private lateinit var searchView: SearchView

    private val REQUEST_CODE_SPEECH_INPUT = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Light)
        setContentView(R.layout.activity_main)

        dialog = ProgressDialog(this).apply {
            setTitle("Loading...")
        }

        searchView = findViewById(R.id.searchView_home)
        searchMic = findViewById(R.id.searchMic)


        searchView.setOnCloseListener{
            searchView.queryHint = "Search recipes..."
            true
        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                tags.clear()
                tags.add(query)
                manager.getRandomRecipes(randomRecipeResponseListener, tags)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchView.windowToken, 0)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        manager = RequestManager(this)
        manager.getRandomRecipes(randomRecipeResponseListener)

        //dialog.show()
        //for some reason, the dialog always displays and the user cannot interact with the app

        //initialize speechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        searchMic.setOnClickListener{
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val recognizedText = result?.get(0)
            searchView.setQuery(recognizedText!!.toLowerCase(), true)
        }
    }

    private val randomRecipeResponseListener = object : RandomRecipeResponseListener {
        override fun didFetch(response: RandomRecipesApiResponse, message: String) {
            recyclerView = findViewById(R.id.recycler_random)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 1)
            randomRecipeAdapter = RandomRecipeAdapter(this@MainActivity, response.recipes, recipeClickListener)
            recyclerView.adapter = randomRecipeAdapter
        }

        override fun didError(message: String) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private val recipeClickListener = object : RecipeClickListener {
        override fun onRecipeClicked(id: String) {
            startActivity(Intent(this@MainActivity, OverviewActivity::class.java)
                .putExtra("id", id))
        }
    }


}

//app icon credit to pngtree, minus icon credit to pngimg, prep arrow icon credit to svgrepo
