package com.example.cookingapp

import android.Manifest.permission.RECORD_AUDIO
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources.Theme
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.*
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.color.MaterialColors
import java.util.*


class PrepActivity : AppCompatActivity() {

    private var speechRecognizer: SpeechRecognizer? = null
    private var voiceInput : TextView? = null
    private var micBtn : ImageButton? = null
    private lateinit var myStepListView : ListView
    private lateinit var btnPrepBack : ImageButton
    private var btnClicked: Boolean =false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Light)
        setContentView(R.layout.activity_prep)

        if (ContextCompat.checkSelfPermission
                (this, RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        )
        {
            checkPermissions()
        }
        myStepListView= findViewById(R.id.stepsList)
        micBtn = findViewById(R.id.buttons)
        voiceInput = findViewById(R.id.tvVoice)
        btnPrepBack = findViewById(R.id.btnPrepBack)

        //create list view and list items
        val values = mutableListOf<String>("step1","step2","step3", "step4")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values)
        myStepListView.adapter= adapter


        micBtn!!.setOnTouchListener { view, motionEvent ->

            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                if (btnClicked == false) {
                    val my_color = MaterialColors.getColor(micBtn!!, R.attr.micOnClick)
                    DrawableCompat.setTint(
                        DrawableCompat.wrap(micBtn!!.getDrawable()),
                        my_color
                    )
                    //create speechrecognizer here
                    startVoice()
                    btnClicked = true
                    //Log.d("myTag","btnClicked set to true")
                } else if (btnClicked == true) {
                    //end speech recognition and reset drawable
                    val my_color = MaterialColors.getColor(micBtn!!, R.attr.redElements)
                    DrawableCompat.setTint(
                        DrawableCompat.wrap(micBtn!!.getDrawable()),
                        my_color
                    )
                    speechRecognizer!!.destroy()
                    voiceInput!!.text="voice commands off"
                    btnClicked = false
                    //Log.d("myTag","btnClicked set to false")
                }
            }

            false
        }

        btnPrepBack.setOnClickListener{
            if (btnClicked == true) {
                speechRecognizer!!.destroy()
            }
            finish()
            Animatoo.animateSlideRight(this)

        }
    }

    private fun checkPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(
                this, arrayOf(RECORD_AUDIO),
                RecordAudioRequestCode
            )
        }
    }

    private fun startVoice(){
        voiceInput = findViewById(R.id.tvVoice)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault())

        speechRecognizer!!.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
            }

            override fun onBeginningOfSpeech() {
                voiceInput!!.text="Listening..."
            }

            override fun onRmsChanged(rmsdB: Float) {
            }

            override fun onBufferReceived(buffer: ByteArray?) {
            }

            override fun onEndOfSpeech() {
                speechRecognizer!!.stopListening()
            }

            override fun onError(error: Int) {
                //Log.d("myTag","my message")
                speechRecognizer!!.startListening(
                    speechRecognizerIntent
                )
            }

            override fun onResults(bundle: Bundle?) {
                //micBtn!!.setImageResource(R.drawable.ic_v_off)
                val data = bundle!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                voiceInput!!.setText(data!![0])
                if (data!![0] == "Next" || data!![0] == "next" || data!![0] == "next up" || data!![0] == "Next Step"){
                    Log.d("myTag", "next recognized")
                    // Add stuff here to move to next instruction or use function
                    nextStep()

                }else if (data!![0] == "Previous" || data!![0] == "previous"){
                    Log.d("myTag", "previous recognized")
                    // Add here to move to next instruction or inside function
                    previousStep()

                }else if (data!![0] == "Exit" || data!![0] == "exit" || data!![0] == "X"){
                    Log.d("myTag", "exit recognized")
                    //Add here to exit recipe or inside function
                    exitRecipe()

                }else {
                    Log.d("myTag", data!![0])
                }

                speechRecognizer!!.startListening(
                    speechRecognizerIntent
                )
            }

            override fun onPartialResults(partialResults: Bundle?) {
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
            }

        })

        speechRecognizer!!.startListening(
            speechRecognizerIntent
        )
    }

    private fun nextStep(){
        /// ADD stuff here to move to next instruction
    }

    private fun previousStep(){
        /// ADD stuff here to move to previous instruction
    }

    private fun exitRecipe(){
        /// ADD stuff here to exit recipe
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RecordAudioRequestCode
            && grantResults.isNotEmpty()
        ){
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        const val RecordAudioRequestCode = 1
    }
}