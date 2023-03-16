package com.example.cookingapp

import android.Manifest.permission.RECORD_AUDIO
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
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
    private lateinit var stepListView : ListView
    private lateinit var btnPrepBack : ImageButton
    private var btnClicked: Boolean =false
    private lateinit var btnTest : Button
    private lateinit var btnTest2 : Button
    private var position : Int = 0
    private var maxPosition : Int = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Light)
        setContentView(R.layout.activity_prep)

        //use num of instructions for max position and previous position
        position = 0
        maxPosition = 9

        if (ContextCompat.checkSelfPermission
                (this, RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        )
        {
            checkPermissions()
        }

        micBtn = findViewById(R.id.buttons)
        voiceInput = findViewById(R.id.tvVoice)
        btnPrepBack = findViewById(R.id.btnPrepBack)
        btnTest = findViewById(R.id.btnTest)
        btnTest2 = findViewById(R.id.btnTest2)

        createSteps()

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


        btnTest.setOnClickListener{
            nextStep()
            position +=1
        }

        btnTest2.setOnClickListener{
            previousStep()
            position -=1
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
                    position +=1

                }else if (data!![0] == "Previous" || data!![0] == "previous"){
                    Log.d("myTag", "previous recognized")
                    // Add here to move to next instruction or inside function
                    previousStep()
                    position -=1

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

    private fun createSteps() {
        stepListView= findViewById(R.id.stepsList)
        //create list view and list items
        val values = mutableListOf<String>(
            "Add the garlic and cook, stirring, until fragrant, 1 to 2 minutes.",
            "Add the chopped tomatoes, red chile flakes, Italian seasoning and salt and pepper to taste.",
            "Bring to a boil and cook for 5 minutes.",
            "Remove from the heat and add the chopped basil.",
            "Drain the pasta and add it to the sauce.",
            "Garnish with Parmigiano-Reggiano flakes and more basil and serve warm.",
            "placeholder(important dont delete)",
            "placeholder(important dont delete)",
            "placeholder(important dont delete)",
            "placeholder(important dont delete)")
        val adapter = ArrayAdapter<String>(this, R.layout.step_item, R.id.tvInstruction, values)
        stepListView.adapter= adapter
    }

    private fun nextStep(){
        //current position plus one (to move forward)

        if (position ==0 ){
            var myCard: CardView = stepListView.getChildAt(0).findViewById<CardView>(R.id.stepCardView)
            myCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.cardButtonBack))
            focusCard(myCard)

            //var myText: TextView = stepListView.getChildAt(0).findViewById<TextView>(R.id.tvInstruction)
            //myText.text = position.toString()

            var myNextCard: CardView = stepListView.getChildAt(1).findViewById<CardView>(R.id.stepCardView)
            myNextCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            defocusCard(myNextCard)

            var myNextCard2: CardView = stepListView.getChildAt(2).findViewById<CardView>(R.id.stepCardView)
            myNextCard2.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            defocusCard(myNextCard2)

        }
        else if (position == 1){
            stepListView.smoothScrollToPositionFromTop(position, 326)
            var myCard: CardView = stepListView.getChildAt(position).findViewById<CardView>(R.id.stepCardView)
            myCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.cardButtonBack))
            focusCard(myCard)

            //var myText: TextView = stepListView.getChildAt(position).findViewById<TextView>(R.id.tvInstruction)
            //myText.text = position.toString()
            var oldCard: CardView = stepListView.getChildAt(position-1).findViewById<CardView>(R.id.stepCardView)
            oldCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            defocusCard(oldCard)

            var myNextCard: CardView = stepListView.getChildAt(position+1).findViewById<CardView>(R.id.stepCardView)
            myNextCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            defocusCard(myNextCard)

        }
        else if (position == 2){
            stepListView.smoothScrollToPositionFromTop(position, 326)
            var myCard: CardView = stepListView.getChildAt(position).findViewById<CardView>(R.id.stepCardView)
            myCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.cardButtonBack))
            focusCard(myCard)

            //var myText: TextView = stepListView.getChildAt(position).findViewById<TextView>(R.id.tvInstruction)
            //myText.text = position.toString()
            var oldCard: CardView = stepListView.getChildAt(position-1).findViewById<CardView>(R.id.stepCardView)
            oldCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            defocusCard(oldCard)

            var myNextCard: CardView = stepListView.getChildAt(position+1).findViewById<CardView>(R.id.stepCardView)
            myNextCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            defocusCard(myNextCard)

        }
        else if (position <= maxPosition){

            stepListView.smoothScrollToPositionFromTop(position, 326)
            var myCard: CardView = stepListView.getChildAt(2).findViewById<CardView>(R.id.stepCardView)
            myCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.cardButtonBack))
            focusCard(myCard)

            //var myText: TextView = stepListView.getChildAt(2).findViewById<TextView>(R.id.tvInstruction)
            //myText.text = position.toString()
            var oldCard: CardView = stepListView.getChildAt(1).findViewById<CardView>(R.id.stepCardView)
            oldCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            defocusCard(oldCard)

            if (position < maxPosition){
                var myNextCard: CardView = stepListView.getChildAt(3).findViewById<CardView>(R.id.stepCardView)
                myNextCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
                defocusCard(myNextCard)
            }

        }
    }

    private fun focusCard(myCard : CardView) {
        val params: ViewGroup.LayoutParams = myCard.getLayoutParams()
        params.width = 900
        params.height = 400
        myCard.layoutParams = params

        var layoutCard = myCard.findViewById<LinearLayout>(R.id.layoutCard)
        val linParams: ViewGroup.LayoutParams = layoutCard.getLayoutParams()
        linParams.width = 800
        linParams.height = 280
        layoutCard.layoutParams = linParams

        var instCard = myCard.findViewById<TextView>(R.id.tvInstruction)
        instCard.setTextSize(17F)

        var instText = myCard.findViewById<TextView>(R.id.tvInstructions)
        instText.setTextSize(17F)
    }

    private fun defocusCard(myCard: CardView){
        val params: ViewGroup.LayoutParams = myCard.getLayoutParams()
        params.width = 740
        params.height = 340
        myCard.layoutParams = params

        var layoutCard = myCard.findViewById<LinearLayout>(R.id.layoutCard)
        val linParams: ViewGroup.LayoutParams = layoutCard.getLayoutParams()
        linParams.width = 720
        linParams.height = 200
        layoutCard.layoutParams = linParams

        var instCard = myCard.findViewById<TextView>(R.id.tvInstruction)
        instCard.setTextSize(14F)

        var instText = myCard.findViewById<TextView>(R.id.tvInstructions)
        instText.setTextSize(14F)
    }

    private fun previousStep(){

        if (position ==0 ){
            var myCard: CardView = stepListView.getChildAt(0).findViewById<CardView>(R.id.stepCardView)
            myCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.cardButtonBack))
            focusCard(myCard)

            //var myText: TextView = stepListView.getChildAt(0).findViewById<TextView>(R.id.tvInstruction)
            //myText.text = position.toString()

            var myNextCard: CardView = stepListView.getChildAt(1).findViewById<CardView>(R.id.stepCardView)
            myNextCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            defocusCard(myNextCard)

            var myNextCard2: CardView = stepListView.getChildAt(2).findViewById<CardView>(R.id.stepCardView)
            myNextCard2.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            defocusCard(myNextCard2)
        }

        else if (position > maxPosition){
            var myCard: CardView = stepListView.getChildAt(1).findViewById<CardView>(R.id.stepCardView)
            myCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.cardButtonBack))
            focusCard(myCard)

            //var myText: TextView = stepListView.getChildAt(2).findViewById<TextView>(R.id.tvInstruction)
            //myText.text = position.toString()
            var oldCard: CardView = stepListView.getChildAt(0).findViewById<CardView>(R.id.stepCardView)
            oldCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            defocusCard(oldCard)

            var myNextCard: CardView = stepListView.getChildAt(2).findViewById<CardView>(R.id.stepCardView)
            myNextCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            defocusCard(myNextCard)
        }

        else if (position >= 0){
            //stepListView.smoothScrollToPosition(position -1)
            stepListView.smoothScrollBy(-450, 300)
            var myCard: CardView = stepListView.getChildAt(0).findViewById<CardView>(R.id.stepCardView)
            myCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.cardButtonBack))
            focusCard(myCard)

            //var myText: TextView = stepListView.getChildAt(2).findViewById<TextView>(R.id.tvInstruction)
            //myText.text = position.toString()
            //var oldCard: CardView = stepListView.getChildAt(-1).findViewById<CardView>(R.id.stepCardView)
            //oldCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            //defocusCard(oldCard)

            var myNextCard: CardView = stepListView.getChildAt(1).findViewById<CardView>(R.id.stepCardView)
            myNextCard.setCardBackgroundColor(MaterialColors.getColor(stepListView!!, R.attr.prepBackground))
            defocusCard(myNextCard)
        }


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