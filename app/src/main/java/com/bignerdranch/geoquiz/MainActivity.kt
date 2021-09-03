package com.bignerdranch.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    //Answer buttons
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    //Navigation buttons
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton

    //Question display
    private lateinit var questionTextView: TextView

    //Questions and answers
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private val answeredQuestions: MutableSet<Int> = mutableSetOf()
    private var currentQuestion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            //direction: true for next question, false for previous question
            changeQuestion(true)
        }

        prevButton.setOnClickListener {
            //direction: true for next question, false for previous question
            changeQuestion(false)
        }

        questionTextView.setOnClickListener {
            //direction: true for next question, false for previous question
            changeQuestion(true)
        }

        toggleNavigationButtons()
        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentQuestion].answer
        val messageResId = if (userAnswer == correctAnswer)
            R.string.correct_toast
        else
            R.string.incorrect_toast

        answeredQuestions.add(currentQuestion)
        toggleAnswerButtons()

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    //direction: true for next question, false for previous question
    private fun changeQuestion(direction: Boolean) {
        val nextFunction = { (currentQuestion + 1) % questionBank.size }
        val prevFunction = { (currentQuestion + questionBank.size - 1) % questionBank.size }

        currentQuestion = if(direction) nextFunction.invoke() else prevFunction.invoke()
        updateQuestion()
        toggleNavigationButtons()
        toggleAnswerButtons()

    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentQuestion].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun toggleNavigationButtons() {
        //If on last question
        if (currentQuestion != questionBank.size - 1) {
            nextButton.isEnabled = true
            nextButton.visibility = View.VISIBLE
        } else {
            nextButton.isEnabled = false
            nextButton.visibility = View.INVISIBLE
        }

        //If on first question
        if (currentQuestion != 0) {
            prevButton.visibility = View.VISIBLE
            prevButton.isEnabled = true
        } else {
            prevButton.visibility = View.INVISIBLE
            prevButton.isEnabled
        }
    }

    private fun toggleAnswerButtons() {
        val isAnswered = !answeredQuestions.contains(currentQuestion)
        trueButton.isEnabled = isAnswered
        falseButton.isEnabled = isAnswered
    }
}