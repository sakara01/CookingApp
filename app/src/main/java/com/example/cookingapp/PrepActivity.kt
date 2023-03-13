package com.example.cookingapp

import android.Manifest.permission.RECORD_AUDIO
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.MotionEvent
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class PrepActivity : AppCompatActivity() {

    private var speechRecognizer: SpeechRecognizer? = null
    private var editText : EditText? = null
    private var micBtn : ImageButton? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prep)

        if (ContextCompat.checkSelfPermission
                (this, RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        )
        {
            checkPermissions()
        }
        editText = findViewById(R.id.texts)
        micBtn = findViewById(R.id.buttons)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        //create list view and list items
        val values = mutableListOf<String>("step1","step2","step3", "step4")
        val myListView= findViewById<ListView>(R.id.stepList)
        val adapter = ArrayAdapter<String>(this, R.layout.list_item, values)
        myListView.adapter= adapter

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
                editText!!.setText("")
                editText!!.setHint("Listening...")
            }

            override fun onRmsChanged(rmsdB: Float) {
            }

            override fun onBufferReceived(buffer: ByteArray?) {
            }

            override fun onEndOfSpeech() {
                speechRecognizer!!.stopListening()
            }

            override fun onError(error: Int) {
                Log.d("myTag","my message")
                speechRecognizer!!.startListening(
                    speechRecognizerIntent
                )
            }

            override fun onResults(bundle: Bundle?) {
                //micBtn!!.setImageResource(R.drawable.ic_v_off)
                val data = bundle!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                editText!!.setText(data!![0])
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

        micBtn!!.setOnTouchListener { view, motionEvent ->

            if (motionEvent.action == MotionEvent.ACTION_DOWN){
                micBtn!!.setImageResource(R.drawable.ic_v_on)
                speechRecognizer!!.startListening(
                    speechRecognizerIntent
                )
            }

            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer!!.destroy()
    }

    private fun checkPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(
                this, arrayOf(RECORD_AUDIO),
                RecordAudioRequestCode
            )
        }
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