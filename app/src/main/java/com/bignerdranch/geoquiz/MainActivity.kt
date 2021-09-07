package com.bignerdranch.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

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

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

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
        toggleAnswerButtons()
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
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = if (userAnswer == correctAnswer) {
            quizViewModel.incrementScore()
            R.string.correct_toast
        } else
            R.string.incorrect_toast

        //Mark current question as answered
        quizViewModel.answerCurrentQuestion()
        toggleAnswerButtons()

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        if (quizViewModel.numberOfAnsweredQuestions == quizViewModel.numberOfQuestions){
            //Final score: 4/6
            val toastMessage = "${getString(R.string.score_toast)}${quizViewModel.score}/${quizViewModel.numberOfQuestions}"

            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }

    //direction: true for next question, false for previous question
    private fun changeQuestion(direction: Boolean) {
        if (direction) quizViewModel.changeToNextQuestion() else quizViewModel.changeToPreviousQuestion()
        updateQuestion()
        toggleNavigationButtons()
        toggleAnswerButtons()
    }

    //Update the UI with the new question properties
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    //Enable/disable navigation buttons
    private fun toggleNavigationButtons() {
        //If on last question
        if (quizViewModel.currentQuestionIndex != quizViewModel.numberOfQuestions - 1) {
            nextButton.isEnabled = true
            nextButton.visibility = View.VISIBLE
        } else {
            nextButton.isEnabled = false
            nextButton.visibility = View.INVISIBLE
        }

        //If on first question
        if (quizViewModel.currentQuestionIndex != 0) {
            prevButton.visibility = View.VISIBLE
            prevButton.isEnabled = true
        } else {
            prevButton.visibility = View.INVISIBLE
            prevButton.isEnabled
        }
    }

    //Enable/disable answer buttons
    private fun toggleAnswerButtons() {
        trueButton.isEnabled = !quizViewModel.isCurrentQuestionAnswered()
        falseButton.isEnabled = !quizViewModel.isCurrentQuestionAnswered()
    }
}