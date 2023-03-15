package com.example.cookingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.blogspot.atifsoftwares.animatoolib.Animatoo

class MainActivity : AppCompatActivity() {
    private lateinit var btnPrep: Button
    private lateinit var btnOverview: Button
    private lateinit var btnTheme: Button
    private var currentTheme: Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Light)
        setContentView(R.layout.activity_main)

        main()
    }

    private fun main(){
        btnPrep = findViewById(R.id.btnPrep)
        btnOverview = findViewById(R.id.btnOverview)
        btnTheme = findViewById(R.id.btnTheme)

        btnPrep.setOnClickListener{
            val intent = Intent(this,PrepActivity::class.java)
            //this is the current activity, and SecondActivity is the one we want to navigate to
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        btnOverview.setOnClickListener{
            val intent = Intent(this,OverviewActivity::class.java)
            //this is the current activity, and SecondActivity is the one we want to navigate to
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        btnTheme.setOnClickListener{
            if (currentTheme == 0){
                setTheme(R.style.Dark)
                setContentView(R.layout.activity_main)
                main()
                currentTheme = 1
            }
            else {
                setTheme(R.style.Light)
                setContentView(R.layout.activity_main)
                main()
                currentTheme = 0
            }
        }
    }
}

//app icon credit to pngtree, minus icon credit to pngimg