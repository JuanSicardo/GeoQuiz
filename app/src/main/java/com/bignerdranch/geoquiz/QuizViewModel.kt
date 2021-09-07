package com.bignerdranch.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    //Questions and answers
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    val numberOfQuestions: Int
        get() = questionBank.size

    //Current question
    var currentQuestionIndex = 0
        private set

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentQuestionIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentQuestionIndex].textResId

    fun changeToNextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionBank.size
    }

    fun changeToPreviousQuestion() {
        currentQuestionIndex = (currentQuestionIndex + questionBank.size - 1) % questionBank.size
    }

    //Scoring
    var score = 0
        private set

    fun incrementScore() {
        score++
    }

    //Answered questions record
    private val answeredQuestions: MutableSet<Int> = mutableSetOf()

    val numberOfAnsweredQuestions
        get() = answeredQuestions.size

    fun isCurrentQuestionAnswered(): Boolean {
        return answeredQuestions.contains(currentQuestionIndex)
    }

    //Mark current question as answered
    fun answerCurrentQuestion() {
        answeredQuestions.add(currentQuestionIndex)
    }
}