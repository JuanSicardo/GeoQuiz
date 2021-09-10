package com.bignerdranch.geoquiz

import androidx.lifecycle.ViewModel

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
        set(value) {
            if (value in 0 until numberOfQuestions) field = value
        }

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
        set(value) {
            if (value in 0 until numberOfAnsweredQuestions) field = value
        }

    fun incrementScore() {
        score++
    }

    //Answered questions record
    private val answeredQuestions: MutableSet<Int> = mutableSetOf()

    val answeredQuestionsList
        get() = answeredQuestions.toTypedArray().toCollection(ArrayList())

    val numberOfAnsweredQuestions
        get() = answeredQuestions.size

    fun isCurrentQuestionAnswered(): Boolean {
        return answeredQuestions.contains(currentQuestionIndex)
    }

    //Mark current question as answered
    fun answerCurrentQuestion() {
        answeredQuestions.add(currentQuestionIndex)
    }

    //Mark question with index as answered
    fun answerQuestion(index: Int) {
        answeredQuestions.add(index)
    }

    //Cheat detection
    var isCheater = false
}