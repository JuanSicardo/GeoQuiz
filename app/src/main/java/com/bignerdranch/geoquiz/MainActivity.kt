package com.bignerdranch.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val KEY_CURRENT_QUESTION_INDEX = "current_question_index"
private const val KEY_SCORE = "score"
private const val KEY_ANSWERED_QUESTIONS = "answered_questions"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    //Answer buttons
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    //Navigation buttons
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton

    //Cheat button
    private lateinit var cheatButton: Button

    //Question display
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get savedInstanceState
        quizViewModel.currentQuestionIndex = savedInstanceState?.getInt(KEY_CURRENT_QUESTION_INDEX, 0) ?: 0
        quizViewModel.score = savedInstanceState?.getInt(KEY_SCORE, 0) ?: 0
        val listOfAnsweredQuestions =
            savedInstanceState?.getIntegerArrayList(KEY_ANSWERED_QUESTIONS) ?: ArrayList<Int>()
        for (index in listOfAnsweredQuestions)
            quizViewModel.answerQuestion(index)

        //Get UI elements
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
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

        cheatButton.setOnClickListener {
            //Start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        questionTextView.setOnClickListener {
            //direction: true for next question, false for previous question
            changeQuestion(true)
        }

        toggleNavigationButtons()
        toggleAnswerButtons()
        updateQuestion()
    }

    //Get if the user cheated
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> {
                quizViewModel.incrementScore()
                R.string.correct_toast
            }
            else -> R.string.incorrect_toast
        }
        //Mark current question as answered
        quizViewModel.answerCurrentQuestion()
        toggleAnswerButtons()

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        if (quizViewModel.numberOfAnsweredQuestions == quizViewModel.numberOfQuestions) {
            //Final score: 4/6
            val toastMessage =
                "${getString(R.string.score_toast)}${quizViewModel.score}/${quizViewModel.numberOfQuestions}"

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_QUESTION_INDEX, quizViewModel.currentQuestionIndex)
        outState.putInt(KEY_SCORE, quizViewModel.score)
        outState.putIntegerArrayList(KEY_ANSWERED_QUESTIONS, quizViewModel.answeredQuestionsList)
    }
}